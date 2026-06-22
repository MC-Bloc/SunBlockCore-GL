# SunBlockCore Architecture & Features

Version covered: `0.8.7-1.20.1` (branch `main`)

SunBlockCore is a Forge mod for the [SunBlock](https://github.com/MC-Bloc/SunBlock) project — a solar-powered Minecraft server. A physical solar/battery rig (NUC + sensors) streams live telemetry over Socket.IO; this mod consumes that stream server-side, broadcasts it to clients, and ties real-world solar/battery state into in-game mechanics (HUD, tool power, loot).

> Note: an older doc, [`DisplayingServerStats.md`](./DisplayingServerStats.md), describes a previous polling-based design (`DataQueryProcess.java` reading a JSON file once per second). That design has been replaced by the socket-push model described below — `DataQueryProcess.java` and `ServerSetup.java` no longer exist in this codebase. Treat that doc as historical only.

## Data flow

```
SunBlock-LL API (Socket.IO server)
        │  "solar_data" event (JSON)
        ▼
SolarSocketClient.connect()           [services/SolarSocketClient.java]
        │  parses JSON → SolarSnapshot record
        ▼
ServerManager.ServerEvents.onSolarData()   [services/setup/ServerManager.java]
        │
        ├─► updates static caches (cachedPvPower, cachedPowerProfile, ...)
        │       read directly by: solar tools, PowerButton, break-speed handler
        │
        └─► broadcasts ServerDataS2CPacket to all connected clients
                │
                ▼
        SolarServerData (client-side static cache)  [application/client/SolarServerData.java]
                │
                ▼
        HUD render (ClientEventHandler → HUD.java), cycled by F8 / configured in HUDSettings
```

The connection is opened in `ServerEvents.onServerStarting()` and explicitly closed in `onServerStopping()` — this ordering matters because the socket callback checks `ModConfig.isLoaded()` and must not fire after config unload during shutdown (`SolarSocketClient.java`, the `"solar_data"` handler).

## Package map

```
ca.milieux.sunblock.core/
├── SunBlockCore.java                  Mod entry point (@Mod), registers everything below
├── application/
│   ├── block/        PowerButton, SolarSwitchBlock, SolarLightSwitchBlock, ModBlocks
│   ├── item/          SolarSword/Pickaxe/Axe/Shovel/Hoe, ModItems
│   ├── client/        HUD, HUDType, HUDSettings, ClientEventHandler, ClientForgeHandler,
│   │                   ClientModHandler, KeyBindings, OverlaySide, SolarServerData
│   ├── config/         ConfigHandler (client), ConfigHandlerServer (server)
│   ├── network/        ModPackets, packets/ServerDataS2CPacket
│   ├── loot/           ModLootModifiers, AddItemModifier
│   ├── util/           RenderUtils, PlayerUtils
│   └── datagen/        DataGenerators + 3 providers (blockstates, item models, loot modifiers)
├── services/
│   ├── SolarSocketClient.java   Socket.IO client
│   ├── SolarSnapshot.java       immutable telemetry record
│   └── setup/
│       ├── CommonSetup.java     registers the network channel
│       ├── ClientSetup.java     registers item "power" properties + HUD render hook
│       └── ServerManager.java   server lifecycle, caches, power-profile API calls, tick handlers
└── registry/
    └── ModSounds.java          POWER_ON / POWER_OFF sound events
```

## Mod entry point (`SunBlockCore.java`)

The `@Mod("sunblockcore")` constructor wires up, in order: item registry, block registry, sound registry, loot modifier registry, common setup (network), client setup (Dist.CLIENT only, via `DistExecutor`), creative tab population, and two config files (`SunBlockCore-ClientConfig.toml`, `SunBlockCore-server.toml`).

## Blocks

| Block | File | Notes |
|---|---|---|
| **Power Button** | `PowerButton.java` | Indestructible button (hardness -1). Right-click toggles the server's power profile (Performance ↔ Power Saver) via `ServerManager.PowerProfileSwitch()`. Plays POWER_ON/POWER_OFF sound + spawns yellow dust particles. Has a cooldown (`ServerManager.LAST_PROFILE_SWITCH`) to stop rapid re-clicking. |
| **Solar Switch** | `SolarSwitchBlock.java` | Indestructible, light-emitting (level 14 when ON) toggle with a 10-tick press animation (OFF→PRESSED→ON cycle). Completing the ON transition also calls `ServerManager.PowerProfileSwitch()` — functionally a second power-profile switch. Cannot be placed facing down (ceiling). |
| **Solar Light Switch** | `SolarLightSwitchBlock.java` | Same state machine and animation as Solar Switch, but breakable (hardness 1.5) and does **not** call `PowerProfileSwitch()` — it's light-only. |

None of these have a block entity / tile entity — all state lives in blockstate properties.

## Items — Solar Tools

All five tools (`SolarSword`, `SolarPickaxe`, `SolarAxe`, `SolarShovel`, `SolarHoe`) share Iron tier stats (+3 attack damage, -2.4 attack speed, 600 durability) and react to live PV power (`ServerManager.cachedPvPower`):

- **Sword / Axe**: extra `hurtEnemy()` damage = `cachedPvPower / 10`.
- **Pickaxe / Hoe**: faster mining — `ServerManager.onBreakSpeed()` boosts break speed to `max(pvPower, originalSpeed)`.
- **All five**: while held with shift pressed, repair durability by `cachedPvPower / 20` per tick (`inventoryTick()`).
- **All five**: client-registered item property `"power"` (`ClientSetup.java`) returns `1.0`/`0.5`/`0.0` based on whether PV power is ≥20W / ≥10W / below — drives texture variants via item-model predicates.

## Configuration

**Client** (`ConfigHandler.java` → `SunBlockCore-ClientConfig.toml`):

| Key | Default | Notes |
|---|---|---|
| `displayWithChatOpen` | `true` | Keep HUD visible with chat open |
| `overlayLineOffset` | `1` (0–50) | Text-HUD line spacing |
| `overlaySide` | `LEFT` | `LEFT`/`RIGHT` |
| `hud.scale` | `0.85` (0.3–1.5) | Graphical HUD scale |
| `hud.xPos` / `hud.yPos` | `2` / `2` | Graphical HUD position |
| `hud.opacity` | `1.0` (0.0–1.0) | Graphical HUD alpha |

**Server** (`ConfigHandlerServer.java` → `SunBlockCore-server.toml`):

| Key | Default | Notes |
|---|---|---|
| CPU Temp Path | `/sys/class/thermal/thermal_zone1/temp` | sysfs path, **fragile** — see Known Issues |
| API URL | Defined by linux server admin | Socket.IO endpoint |
| PerformanceEndpoint / PowerSaverEndpoint | `.../performance-mode` / `.../power-saver-mode` | HTTP POST targets for profile switches |
| TOKEN | Generated by SunBlockCore-LL | Bearer token sent with profile-switch requests |
| Battery Capacity | `1200.0` Wh | Used for time-remaining estimate, will vary based on physical layer config |
| Cooldown | `60` s | Minimum gap between power-profile switches |

## Networking

A single Forge SimpleChannel (`sunblockcore:sunblockcore`, version-agnostic) carries one packet: `ServerDataS2CPacket` — the full telemetry snapshot plus computed time-remaining, sent to all clients on every `solar_data` event and individually to a player on join (`ServerManager.onPlayerJoinWorld`).

## HUD

Three renderable HUD types (`HUDType`), cycled with **F8**:

1. **TextV0** — rotates between time-remaining / load power / solar V+A every 5s.
2. **TextV1** — always-on 6-line detailed text block.
3. **GraphicalV0** (default) — textured panel with day/dusk/night background (chosen by hour), colored status icons (solar/battery/load/time/CPU, color-coded green→yellow→red by threshold), and a hardcoded **"Montreal, QC"** location label (see Known Issues). Positionable/scalable via the in-game settings screen (`HUDSettings.java`, opened with **U**).

## Loot modifiers

A small `IGlobalLootModifier` system (`AddItemModifier` + `ModGlobalLootModifiersProvider`) adds a chance for the Solar Hoe to drop a sapling when breaking vanilla leaves (20% chance, vanilla saplings only — Spruce, Acacia, Birch, Cherry, Oak, Dark Oak, Mangrove). See Known Issues for the Jungle entry, which is currently broken.


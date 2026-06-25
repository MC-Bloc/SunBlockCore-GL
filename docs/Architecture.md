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

A small `IGlobalLootModifier` system (`AddItemModifier` + `ModGlobalLootModifiersProvider`) gives a 37% chance for the Solar Hoe to drop a bonus sapling when breaking any registered leaf block, vanilla or modded — 79 entries total (8 vanilla + 71 from the FTT modpack's third-party mods: Quark, Nature's Spirit, Nature's Aura, Regions Unexplored, Primal Magick, Integrated Dynamics, Natural Decor, Enchanted). All 79 are generated by `ModGlobalLootModifiersProvider`; nothing is hand-maintained anymore.

The block-match condition is `HasBlockIdCondition` (`application/loot/HasBlockIdCondition.java`), a custom `LootItemCondition` matching by registry id (`ResourceLocation`) rather than a resolved `Block` instance — this is what makes generating the third-party entries possible without those 9 mods being a build dependency: datagen never needs to resolve `quark:blue_blossom_leaves` etc., only the runtime registry (populated by the actual modpack) does, at `test()` time. `AddItemModifier` was changed the same way — it stores the dropped item as a `ResourceLocation` and resolves it from `ForgeRegistries.ITEMS` inside `doApply()`, not at construction. Both `AddItemModifier`'s loot-modifier codec and `HasBlockIdCondition`'s condition-type serializer are registered in `ModLootModifiers.java` (the latter as a `Registries.LOOT_CONDITION_TYPE` deferred register — Forge 1.20.1 doesn't wrap loot condition types in a Forge registry, so this goes straight to the vanilla one).

---

## Points of interest

1. ~~**Duplicate loot-modifier key drops Dark Oak, mislabels Jungle**~~ — **Fixed.** The jungle `add()` call now has its own key (`jungle_sapling_chance_with_solar_hoe`); `dark_oak_sapling_chance_with_solar_hoe.json` correctly contains Dark Oak Leaves→Sapling again, and a new `jungle_sapling_chance_with_solar_hoe.json` carries the Jungle drop. Both are listed in `global_loot_modifiers.json`.

2. ~~**Generated loot-modifier JSONs for FTT Modpack are hand/out-of-band-maintained**~~ — **Fixed.** `ModGlobalLootModifiersProvider.java` now generates all 79 entries (8 vanilla + 71 from Quark, Nature's Spirit, Nature's Aura, Regions Unexplored, Primal Magick, Integrated Dynamics, Natural Decor, and Enchanted), including the `global_loot_modifiers.json` index — `./gradlew runData` regenerates the entire set with `removed stale: 0`, nothing left to lose. This was made possible by `HasBlockIdCondition` (see "Loot modifiers" above), which matches blocks by registry id instead of a resolved `Block`, so the third-party mods never need to be a build dependency. The previous caution about backing up third-party files before running `runData` no longer applies.

3. **Hardcoded "Montreal, QC" in the Graphical HUD** (`HUD.java`) — not configurable, not timezone-aware. Will be wrong for any server not physically located there.

4. **Fragile CPU temperature path** — `/sys/class/thermal/thermal_zone1/temp` is configurable, but the index of `thermal_zoneN` is not stable across reboots/kernel updates on the physical host. This already caused a crash after cloning the host drive (documented in `docs/DisplayingServerStats.md`). There's no fallback/auto-detection if the configured path stops existing.

5. **`docs/DisplayingServerStats.md` is stale** — describes the pre-Socket.IO architecture (`DataQueryProcess.java`, `ServerSetup.java`), neither of which exists anymore. Should be rewritten or removed in favor of this document.

6. **Identical hardcoded power thresholds repeated 5×** — every solar tool's `"power"` item property in `ClientSetup.java` independently hardcodes the same 20W/10W thresholds. Not a bug, but any future threshold tuning requires touching 5 call sites.

7. ~~**Two of three blocks have no blockstate datagen**~~ — **Fixed.** `ModBlockStateProvider` now generates blockstates for `SOLAR_SWITCH` and `SOLAR_LIGHT_SWITCH` via `forAllStates(...)`, covering every `FACING` value including `DOWN` (unreachable in-game since placement always rejects ceiling mounting, but the variant validator still requires a model for it). The hand-authored blockstate JSONs that previously caused the `missing model for variant 'facing=down'` warnings were removed in favor of the generated ones.

8. ~~**Item models for 4 of 5 solar tools are orphaned generated output**~~ — **Fixed, then simplified further.** The 5 solar tools' item models (the power-level predicate overrides driven by the "sunblockcore:power" item property registered in `ClientSetup`, plus their low/med texture variants) are pure hand-authored content — nothing in `ModItemModelProvider` ever generated their actual contents, only a one-line `withExistingParent(...)` stub delegating to a separate `_base.json`. That stub added nothing: it's been removed, and `_base.json`'s content was merged directly into `solar_<tool>.json` (the file Minecraft actually loads by an item's registry path), with `_low`/`_med` re-pointing their `"parent"` from `solar_<tool>_base` to `solar_<tool>` accordingly. All 15 of these files (5 tools × main/low/med) live in `src/main/resources/assets/sunblockcore/models/item/`, alongside the switch block item models — `ModItemModelProvider` no longer references the tools at all, only the power button. Leaving any of these files under `src/generated/resources` is a trap: Forge's `HashCache` deletes anything no provider claims to produce the next time `runData` runs — this happened twice during this fix and had to be restored from git history both times.

9. ~~**Recipes are entirely unmanaged by code**~~ — **Fixed.** Added `ModRecipeProvider.java`, registered in `DataGenerators.java`, generating all 5 solar tool recipes (shaped, using `RecipeCategory.COMBAT` for the sword and `RecipeCategory.TOOLS` for the rest — the original hand-authored JSONs used `"category": "equipment"`, which isn't a valid vanilla `RecipeCategory` and confirms these were never actually produced by a `RecipeProvider` before). This also generates recipe-unlock advancements (`unlockedBy` a copper block), which didn't exist previously.

10. ~~**Two dead private methods in the datagen providers**~~ — **Fixed**, removed during the rewrites above. Still outstanding: `solar_sword_old.png` is an unreferenced, dead texture asset.

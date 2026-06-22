package ca.milieux.sunblock.core;

import ca.milieux.sunblock.core.application.block.ModBlocks;
import ca.milieux.sunblock.core.application.config.ConfigHandler;
import ca.milieux.sunblock.core.application.config.ConfigHandlerServer;
import ca.milieux.sunblock.core.application.item.ModItems;
import ca.milieux.sunblock.core.application.loot.ModLootModifiers;
import ca.milieux.sunblock.core.registry.ModSounds;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SunBlockCore.MODID)
public class SunBlockCore {
	public static final String MODID = "sunblockcore";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public SunBlockCore(IEventBus modEventBus, ModContainer modContainer) {
		ModItems.register(modEventBus);
		ModBlocks.register(modEventBus);
		ModSounds.init(modEventBus);
		ModLootModifiers.register(modEventBus);

		modEventBus.addListener(this::addCreative);

		// Client-only setup (ClientSetup::init) is registered by the Dist.CLIENT-gated
		// ClientModHandler class instead of here. A direct method reference to
		// ClientSetup::init in this constructor would force the JVM to resolve the
		// ClientSetup class (which touches client-only rendering classes) even on a
		// dedicated server, crashing with NoClassDefFoundError. NeoForge removed
		// DistExecutor, which used to guard against exactly this; class-level dist
		// gating via @Mod.EventBusSubscriber(value = Dist.CLIENT) is the replacement.

		modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC, "SunBlockCore-ClientConfig.toml");
		modContainer.registerConfig(ModConfig.Type.SERVER, ConfigHandlerServer.SPEC, "SunBlockCore-server.toml");
	}

	private void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.COMBAT) {
			event.accept(ModItems.SOLAR_SWORD);
			event.accept(ModItems.SOLAR_AXE);
		}

		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(ModItems.SOLAR_PICKAXE);
			event.accept(ModItems.SOLAR_SHOVEL);
			event.accept(ModItems.SOLAR_HOE);
			event.accept(ModItems.SOLAR_AXE);

		}

		if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
			event.accept(ModBlocks.POWER_BUTTON);
		}

		if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			event.accept(ModBlocks.SOLAR_LIGHT_SWITCH);
			event.accept(ModBlocks.SOLAR_SWITCH);
		}

	}
}

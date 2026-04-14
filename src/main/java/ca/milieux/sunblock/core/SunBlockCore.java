package ca.milieux.sunblock.core;

import ca.milieux.sunblock.core.application.block.ModBlocks;
import ca.milieux.sunblock.core.application.config.ConfigHandler;
import ca.milieux.sunblock.core.application.config.ConfigHandlerServer;
import ca.milieux.sunblock.core.application.item.ModItems;
import ca.milieux.sunblock.core.registry.ModSounds;
import ca.milieux.sunblock.core.services.setup.ClientSetup;
import ca.milieux.sunblock.core.services.setup.CommonSetup;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SunBlockCore.MODID)
public class SunBlockCore {
	public static final String MODID = "sunblockcore";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static ModConfig CLIENT_MOD_CONFIG;

	public SunBlockCore() {
		//Get the mod‑event bus Forge creates for us
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		//Register blocks, items, sounds, etc.
		ModItems.register(modEventBus);
		ModBlocks.register(modEventBus);
		ModSounds.init(modEventBus);           // <-- NEW: sound events

		//Common + client setup hooks
		modEventBus.addListener(CommonSetup::init);
		modEventBus.addListener(this::addCreative);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () ->
				() -> modEventBus.addListener(ClientSetup::init));

		//Config files
		ModLoadingContext.get().registerConfig(
				ModConfig.Type.CLIENT,
				ConfigHandler.CLIENT_SPEC,
				"SunBlockCore-ClientConfig.toml");

		ModLoadingContext.get().registerConfig(
				ModConfig.Type.SERVER,
				ConfigHandlerServer.SPEC,
				"SunBlockCore-server.toml");
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

	}
}

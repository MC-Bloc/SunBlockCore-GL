package ca.milieux.sunblock.sunblockcore;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.milieux.sunblock.sunblockcore.application.config.ConfigHandler;
import ca.milieux.sunblock.sunblockcore.application.item.ModItems;
import ca.milieux.sunblock.sunblockcore.services.setup.ClientSetup;
import ca.milieux.sunblock.sunblockcore.services.setup.CommonSetup;

@Mod(SunBlockCore.MODID)
public class SunBlockCore {
	public static final String MODID = "sunblockcore";

	public static final Logger LOGGER = LogManager.getLogger(MODID);


	public SunBlockCore() {

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModItems.register(modEventBus);

		modEventBus.addListener(CommonSetup::init);
		modEventBus.addListener(this::addCreative);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
		});

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
	}

	private void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.COMBAT) {
			event.accept(ModItems.SOLAR_SWORD);
		}
	}
}

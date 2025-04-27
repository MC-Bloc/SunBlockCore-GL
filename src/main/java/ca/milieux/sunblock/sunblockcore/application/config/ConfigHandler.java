package ca.milieux.sunblock.sunblockcore.application.config;

import ca.milieux.sunblock.sunblockcore.SunBlockCore;
import ca.milieux.sunblock.sunblockcore.application.client.OverlaySide;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ConfigHandler {

	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

	public static final Client CLIENT = new Client(CLIENT_BUILDER);

	public static final ForgeConfigSpec CLIENT_SPEC = CLIENT_BUILDER.build();

	public static class Client {
		public final ForgeConfigSpec.BooleanValue displayWithChatOpen;
		public final ForgeConfigSpec.EnumValue<OverlaySide> overlaySide;
		public final ForgeConfigSpec.IntValue overlayLineOffset;

		public final ForgeConfigSpec.DoubleValue HUD_SCALE;
		public final ForgeConfigSpec.IntValue HUD_X_POSITION;
		public final ForgeConfigSpec.IntValue HUD_Y_POSITION;
		public final ForgeConfigSpec.DoubleValue HUD_OPACITY;

		Client(ForgeConfigSpec.Builder builder) {
			String desc;
			builder.push("Client");

			desc = "Displays Solar Server information on the HUD even while chat is open.";
			displayWithChatOpen = builder.comment(desc).define("displayWithChatOpen", true);

			desc = "The line offset for information rendered on the HUD.";
			overlayLineOffset = builder.comment(desc).defineInRange("overlayLineOffset", 1, 0, 50);

			desc = "The side for information rendered on the HUD. Ex: LEFT, RIGHT";
			overlaySide = builder.comment(desc).defineEnum("overlaySide", OverlaySide.LEFT);


			builder.comment("Graphic HUD Settings").push("hud");
			HUD_SCALE = builder.comment("HUD scale factor")
					.defineInRange("scale", 0.85, 0.3, 1.5);

			HUD_X_POSITION = builder.comment("HUD X position")
					.defineInRange("xPos", 2, 0, Integer.MAX_VALUE);

			HUD_Y_POSITION = builder.comment("HUD Y position")
					.defineInRange("yPos", 2, 0, Integer.MAX_VALUE);

			HUD_OPACITY = builder.comment("HUD opacity")
					.defineInRange("opacity", 1.0, 0.0, 1.0);

			builder.pop();
		}
	}

	@SubscribeEvent
	public static void onLoadConfig(final ModConfigEvent.Loading event) {
		ModConfig config = event.getConfig();
		if (config.getModId().equals(SunBlockCore.MODID) && config.getType() == ModConfig.Type.CLIENT) {
			SunBlockCore.CLIENT_MOD_CONFIG = config;
		}
	}

	@SubscribeEvent
	public static void onReloadConfig(final ModConfigEvent.Reloading event) {
		ModConfig config = event.getConfig();
		if (config.getModId().equals(SunBlockCore.MODID) && config.getType() == ModConfig.Type.CLIENT) {
			SunBlockCore.CLIENT_MOD_CONFIG = config;
		}
	}

}

// All the config code is from Matt Czyr's Explorer's Compass. Thanks Matt! :)
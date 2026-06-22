package ca.milieux.sunblock.core.application.config;

import ca.milieux.sunblock.core.application.client.OverlaySide;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigHandler {

	private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();

	public static final Client CLIENT = new Client(CLIENT_BUILDER);

	public static final ModConfigSpec CLIENT_SPEC = CLIENT_BUILDER.build();

	public static class Client {
		public final ModConfigSpec.BooleanValue displayWithChatOpen;
		public final ModConfigSpec.EnumValue<OverlaySide> overlaySide;
		public final ModConfigSpec.IntValue overlayLineOffset;

		public final ModConfigSpec.DoubleValue HUD_SCALE;
		public final ModConfigSpec.IntValue HUD_X_POSITION;
		public final ModConfigSpec.IntValue HUD_Y_POSITION;
		public final ModConfigSpec.DoubleValue HUD_OPACITY;

		Client(ModConfigSpec.Builder builder) {
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

}

// All the config code is from Matt Czyr's Explorer's Compass. Thanks Matt! :)
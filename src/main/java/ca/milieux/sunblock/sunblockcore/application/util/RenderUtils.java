package ca.milieux.sunblock.sunblockcore.application.util;

import ca.milieux.sunblock.sunblockcore.application.client.OverlaySide;
import ca.milieux.sunblock.sunblockcore.application.config.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderUtils {

	private static final Minecraft mc = Minecraft.getInstance();
	private static final Font font = mc.font;

	static int margin = 5;

	public static void drawStringLeft(GuiGraphics guiGraphics, String string, Font font, int x, int y, int color) {
		guiGraphics.drawString(font, string, x, y, color, true);
	}

	public static void drawStringRight(GuiGraphics guiGraphics, String string, Font font, int x, int y, int color) {
		guiGraphics.drawString(font, string, x - font.width(string), y, color, true);
	}

	public static void drawStringCenter(GuiGraphics guiGraphics, String str, int color, int line) {
		int xOffset = (mc.getWindow().getGuiScaledWidth() / 2) - (font.width(str) / 2);
		int yOffset = margin + (mc.getWindow().getGuiScaledHeight() / (font.lineHeight)) * line ;
		guiGraphics.drawString(font, str, xOffset, yOffset, color);
	}

	public static void drawStringBottomLeft(GuiGraphics guiGraphics, String str, int color, int line) {
		int xOffset = margin;
		int yOffset = mc.getWindow().getGuiScaledHeight() - ((font.lineHeight) * line + margin);
		guiGraphics.drawString(font, str, xOffset, yOffset, color);
	}

	public static void drawStringTopLeft(GuiGraphics guiGraphics, String str, int color, int line) {
		int xOffset = margin;
		int yOffset = ((font.lineHeight) * line) + margin;
		guiGraphics.drawString(font, str, xOffset, yOffset, color);
	}

	public static void drawConfiguredStringOnHUD(GuiGraphics guiGraphics, String string, int xOffset, int yOffset, int color, int relLineOffset) {
		yOffset += (relLineOffset + ConfigHandler.CLIENT.overlayLineOffset.get()) * font.lineHeight;
		if (ConfigHandler.CLIENT.overlaySide.get() == OverlaySide.LEFT) {
			drawStringLeft(guiGraphics, string, font, xOffset + 2, yOffset + 2, color);
		} else {
			drawStringRight(guiGraphics, string, font, mc.getWindow().getGuiScaledWidth() - xOffset - 2, yOffset + 2, color);
		}
	}

}
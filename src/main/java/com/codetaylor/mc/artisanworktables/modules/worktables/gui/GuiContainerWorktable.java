package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiContainerWorktable
    extends GuiContainer {

  private final ResourceLocation backgroundTexture;
  private final String titleKey;
  private final int textShadowColor;

  public GuiContainerWorktable(
      ContainerWorktable container,
      ResourceLocation backgroundTexture,
      String titleKey,
      int textShadowColor
  ) {

    super(container);
    this.backgroundTexture = backgroundTexture;
    this.titleKey = titleKey;
    this.textShadowColor = textShadowColor;
    this.xSize = 176;
    this.ySize = 166;
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(this.backgroundTexture);
    int i = this.guiLeft;
    int j = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    this.drawString(this.titleKey, 8, 5);
    this.drawString("container.inventory", 8, this.ySize - 96 + 2);
  }

  private void drawString(String translateKey, int x, int y) {

    String displayText = I18n.format(translateKey);

    this.fontRenderer.drawString(displayText, x + 0, y + 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x + 1, y + 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x + 1, y - 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x + 1, y + 0, this.textShadowColor);

    this.fontRenderer.drawString(displayText, x - 0, y - 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x - 1, y - 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x - 1, y + 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x - 1, y - 0, this.textShadowColor);

    this.fontRenderer.drawString(displayText, x, y, Color.BLACK.getRGB());
  }

}

package com.codetaylor.mc.artisanworktables.modules.toolbox.gui;

import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiContainerToolbox
    extends GuiContainer {

  private static final int TEXT_SHADOW_COLOR = new Color(89, 57, 27).getRGB();
  private static final ResourceLocation TEXTURE = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      "textures/gui/toolbox.png"
  );

  public GuiContainerToolbox(ContainerToolbox container) {

    super(container);
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
    this.mc.getTextureManager().bindTexture(TEXTURE);
    int x = this.guiLeft;
    int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    this.drawString(ModuleToolbox.Lang.TOOLBOX_TITLE, 8, 6);
    this.drawString("container.inventory", 8, this.ySize - 96 + 3);
  }

  private void drawString(String translateKey, int x, int y) {

    String displayText = I18n.format(translateKey);
    FontRenderer fontRenderer = this.fontRenderer;

    fontRenderer.drawString(displayText, x + 0, y + 1, TEXT_SHADOW_COLOR);
    fontRenderer.drawString(displayText, x + 1, y + 1, TEXT_SHADOW_COLOR);
    fontRenderer.drawString(displayText, x + 1, y - 1, TEXT_SHADOW_COLOR);
    fontRenderer.drawString(displayText, x + 1, y + 0, TEXT_SHADOW_COLOR);

    fontRenderer.drawString(displayText, x - 0, y - 1, TEXT_SHADOW_COLOR);
    fontRenderer.drawString(displayText, x - 1, y - 1, TEXT_SHADOW_COLOR);
    fontRenderer.drawString(displayText, x - 1, y + 1, TEXT_SHADOW_COLOR);
    fontRenderer.drawString(displayText, x - 1, y - 0, TEXT_SHADOW_COLOR);

    fontRenderer.drawString(displayText, x, y, Color.BLACK.getRGB());
  }
}

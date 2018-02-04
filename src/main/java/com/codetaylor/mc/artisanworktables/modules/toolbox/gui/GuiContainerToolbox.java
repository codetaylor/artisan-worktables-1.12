package com.codetaylor.mc.artisanworktables.modules.toolbox.gui;

import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.gui.Texture;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

import java.awt.*;

public class GuiContainerToolbox
    extends GuiContainerBase {

  private static final int TEXT_SHADOW_COLOR = new Color(103, 69, 29).getRGB();
  private final String guiContainerTitleKey;

  public GuiContainerToolbox(
      ContainerToolbox container,
      String guiContainerTitleKey,
      Texture texture
  ) {

    super(container, 176, 166);
    this.guiContainerTitleKey = guiContainerTitleKey;

    this.guiContainerElementAdd(new GuiElementTextureRectangle(
        this,
        texture,
        0,
        0,
        176,
        166
    ));
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    this.drawString(this.guiContainerTitleKey, 8, 6);
    this.drawString("container.inventory", 8, this.ySize - 96 + 3);

    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }

  @Override
  public void drawString(String translateKey, int x, int y) {

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

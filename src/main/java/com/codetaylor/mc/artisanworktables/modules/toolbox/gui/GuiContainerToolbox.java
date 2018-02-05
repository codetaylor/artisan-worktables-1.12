package com.codetaylor.mc.artisanworktables.modules.toolbox.gui;

import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTitle;

import java.awt.*;

public class GuiContainerToolbox
    extends GuiContainerBase {

  private static final int TEXT_SHADOW_COLOR = new Color(103, 69, 29).getRGB();

  public GuiContainerToolbox(
      ContainerToolbox container,
      String titleKey,
      Texture texture
  ) {

    super(container, 176, 166);

    // toolbox title
    this.guiContainerElementAdd(new GuiElementTitle(
        this,
        titleKey,
        8,
        6
    ));

    // inventory title
    this.guiContainerElementAdd(new GuiElementTitle(
        this,
        "container.inventory",
        8,
        this.ySize - 93
    ));

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
  public void drawString(String translateKey, int x, int y) {

    GuiHelper.drawStringOutlined(translateKey, x, y, this.fontRenderer, TEXT_SHADOW_COLOR);
  }
}

package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;

public class GuiElementAutomatorPanel
    extends GuiElementTextureRectangle {

  private final AutomatorGuiContainer guiContainer;

  public GuiElementAutomatorPanel(
      AutomatorGuiContainer guiBase,
      Texture[] textures,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight
  ) {

    super(guiBase, textures, elementX, elementY, elementWidth, elementHeight);
    this.guiContainer = guiBase;
  }

  @Override
  protected int textureIndexGet(int mouseX, int mouseY) {

    AutomatorContainer.EnumState containerState = this.guiContainer.getContainerState();
    return containerState.getIndex();
  }
}

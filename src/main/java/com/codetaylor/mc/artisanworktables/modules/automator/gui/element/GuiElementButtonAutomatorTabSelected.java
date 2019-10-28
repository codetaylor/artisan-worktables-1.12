package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureButtonBase;

public class GuiElementButtonAutomatorTabSelected
    extends GuiElementTextureButtonBase {

  private final AutomatorContainer.State state;
  private final AutomatorGuiContainer guiContainer;

  public GuiElementButtonAutomatorTabSelected(
      AutomatorContainer.State state,
      AutomatorGuiContainer guiBase,
      Texture texture,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight
  ) {

    super(guiBase, new Texture[]{texture}, elementX, elementY, elementWidth, elementHeight);
    this.state = state;
    this.guiContainer = guiBase;
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    return (this.guiContainer.getContainerState() == this.state);
  }

  @Override
  protected int textureIndexGet(int mouseX, int mouseY) {

    return 0;
  }
}

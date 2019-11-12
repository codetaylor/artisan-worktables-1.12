package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureButtonBase;

import java.util.function.Supplier;

public abstract class GuiElementPanelButtonBase
    extends GuiElementTextureButtonBase {

  private final Supplier<AutomatorContainer.EnumState> currentState;
  private final AutomatorContainer.EnumState state;

  @SuppressWarnings("WeakerAccess")
  public GuiElementPanelButtonBase(
      Supplier<AutomatorContainer.EnumState> currentState,
      AutomatorContainer.EnumState state,
      GuiContainerBase guiBase,
      Texture[] textures,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight
  ) {

    super(guiBase, textures, elementX, elementY, elementWidth, elementHeight);
    this.currentState = currentState;
    this.state = state;
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    return (this.currentState.get() == this.state);
  }
}

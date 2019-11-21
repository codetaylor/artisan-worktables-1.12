package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementFluidTankHorizontal;
import net.minecraftforge.fluids.FluidTank;

public class GuiElementFluidTank
    extends GuiElementFluidTankHorizontal {

  private final AutomatorGuiContainer guiContainer;

  public GuiElementFluidTank(
      AutomatorGuiContainer guiBase,
      FluidTank fluidTank,
      int elementX, int elementY,
      int elementWidth, int elementHeight
  ) {

    super(guiBase, fluidTank, elementX, elementY, elementWidth, elementHeight);
    this.guiContainer = guiBase;
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    return (this.guiContainer.getContainerState() == AutomatorContainer.EnumState.Fluid);
  }
}

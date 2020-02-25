package com.codetaylor.mc.artisanworktables.modules.automator.tile;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class FluidHandlerDelegate
    implements IFluidHandler {

  protected IFluidHandler fluidHandler;

  @Override
  public IFluidTankProperties[] getTankProperties() {

    if (this.fluidHandler == null) {
      return new IFluidTankProperties[0];
    }

    return this.fluidHandler.getTankProperties();
  }

  @Override
  public int fill(FluidStack resource, boolean doFill) {

    return (this.fluidHandler == null) ? 0 : this.fluidHandler.fill(resource, doFill);
  }

  @Nullable
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {

    return (this.fluidHandler == null) ? null : this.fluidHandler.drain(resource, doDrain);
  }

  @Nullable
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {

    return (this.fluidHandler == null) ? null : this.fluidHandler.drain(maxDrain, doDrain);
  }
}

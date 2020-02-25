package com.codetaylor.mc.artisanworktables.modules.automator.tile;

import com.codetaylor.mc.athenaeum.spi.TileEntityBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileAutomatorPowerSupplierRF
    extends TileEntityBase
    implements ITileAutomatorPowerSupplier {

  private final EnergyCapabilityDelegate energyCapabilityDelegate;
  private final ItemCapabilityDelegate itemCapabilityDelegate;
  private final FluidCapabilityDelegate fluidCapabilityDelegate;

  public TileAutomatorPowerSupplierRF() {

    this.energyCapabilityDelegate = new EnergyCapabilityDelegate();
    this.itemCapabilityDelegate = new ItemCapabilityDelegate();
    this.fluidCapabilityDelegate = new FluidCapabilityDelegate();
  }

  // --------------------------------------------------------------------------
  // - Accessors
  // --------------------------------------------------------------------------

  public boolean isPowered() {

    TileEntity tileEntity = this.world.getTileEntity(this.pos.up());

    if (tileEntity instanceof ITileAutomatorPowerConsumer) {
      return ((ITileAutomatorPowerConsumer) tileEntity).isPowered();
    }

    return false;
  }

  // --------------------------------------------------------------------------
  // - Update
  // --------------------------------------------------------------------------

  public void neighborChanged() {

    this.updateCapabilityDelegates();
  }

  private void updateCapabilityDelegates() {

    TileEntity tileEntity = this.world.getTileEntity(this.pos.up());

    if (tileEntity instanceof ITileAutomatorPowerConsumer) {
      this.energyCapabilityDelegate.setEnergyStorage(
          tileEntity.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN)
      );
      this.itemCapabilityDelegate.setItemHandler(
          tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)
      );
      this.fluidCapabilityDelegate.setFluidHandler(
          tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)
      );

    } else {
      this.energyCapabilityDelegate.setEnergyStorage(null);
      this.itemCapabilityDelegate.setItemHandler(null);
      this.fluidCapabilityDelegate.setFluidHandler(null);
    }
  }

  // --------------------------------------------------------------------------
  // - Capabilities
  // --------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (capability == CapabilityEnergy.ENERGY
        || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
        || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityEnergy.ENERGY) {
      this.updateCapabilityDelegates();
      //noinspection unchecked
      return (T) this.energyCapabilityDelegate;

    } else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      this.updateCapabilityDelegates();
      //noinspection unchecked
      return (T) this.itemCapabilityDelegate;

    } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      this.updateCapabilityDelegates();
      //noinspection unchecked
      return (T) this.fluidCapabilityDelegate;
    }

    return null;
  }

  public static class EnergyCapabilityDelegate
      extends EnergyStorageAdapter {

    private IEnergyStorage energyStorage;

    public EnergyCapabilityDelegate setEnergyStorage(@Nullable IEnergyStorage energyStorage) {

      this.energyStorage = energyStorage;
      return this;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {

      if (this.energyStorage == null) {
        return 0;
      }

      return this.energyStorage.receiveEnergy(
          maxReceive,
          simulate
      );
    }

    @Override
    public int getEnergyStored() {

      if (this.energyStorage == null) {
        return 0;
      }

      return this.energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {

      if (this.energyStorage == null) {
        return 0;
      }

      return this.energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canReceive() {

      return (this.energyStorage != null);
    }
  }

  public static class ItemCapabilityDelegate
      extends ItemHandlerDelegate {

    public void setItemHandler(@Nullable IItemHandler iItemHandler) {

      this.itemHandler = iItemHandler;
    }
  }

  public static class FluidCapabilityDelegate
      extends FluidHandlerDelegate {

    public void setFluidHandler(@Nullable IFluidHandler iFluidHandler) {

      this.fluidHandler = iFluidHandler;
    }
  }
}

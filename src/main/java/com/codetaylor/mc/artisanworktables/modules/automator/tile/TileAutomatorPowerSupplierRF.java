package com.codetaylor.mc.artisanworktables.modules.automator.tile;

import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomatorConfig;
import com.codetaylor.mc.athenaeum.spi.TileEntityBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileAutomatorPowerSupplierRF
    extends TileEntityBase
    implements ITileAutomatorPowerSupplier {

  private final EnergyConverter energyConverter;

  public TileAutomatorPowerSupplierRF() {

    this.energyConverter = new EnergyConverter();
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

    this.updateCapabilityWrapper();
  }

  private void updateCapabilityWrapper() {

    TileEntity tileEntity = this.world.getTileEntity(this.pos.up());

    if (tileEntity instanceof ITileAutomatorPowerConsumer) {
      this.energyConverter.setEnergyStorage(
          tileEntity.getCapability(CapabilityEnergy.ENERGY, EnumFacing.DOWN)
      );
    }
  }

  // --------------------------------------------------------------------------
  // - Capabilities
  // --------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (capability == CapabilityEnergy.ENERGY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityEnergy.ENERGY) {
      this.updateCapabilityWrapper();
    }

    //noinspection unchecked
    return (T) this.energyConverter;
  }

  public static class EnergyConverter
      extends EnergyStorageAdapter {

    private IEnergyStorage energyStorage;

    public EnergyConverter setEnergyStorage(IEnergyStorage energyStorage) {

      this.energyStorage = energyStorage;
      return this;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {

      if (this.energyStorage == null) {
        return 0;
      }

      return this.energyStorage.receiveEnergy(
          maxReceive * ModuleAutomatorConfig.RF_CONVERTER.AP_PER_RF,
          simulate
      );
    }

    @Override
    public int getEnergyStored() {

      if (this.energyStorage == null) {
        return 0;
      }

      return this.energyStorage.getEnergyStored() / ModuleAutomatorConfig.RF_CONVERTER.AP_PER_RF;
    }

    @Override
    public int getMaxEnergyStored() {

      if (this.energyStorage == null) {
        return 0;
      }

      return this.energyStorage.getMaxEnergyStored() / ModuleAutomatorConfig.RF_CONVERTER.AP_PER_RF;
    }

    @Override
    public boolean canReceive() {

      return (this.energyStorage != null);
    }
  }
}

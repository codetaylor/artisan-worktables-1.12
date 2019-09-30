package com.codetaylor.mc.artisanworktables.modules.automator.tile;

import com.codetaylor.mc.athenaeum.util.BlockHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileAutomatorPowerRF
    extends TileEntity
    implements ITileAutomatorPower {

  private static final int CAPACITY = 5000; // TODO: config
  private static final int MAX_TRANSFER = 500; // TODO: config

  private EnergyTank energyTank;

  public TileAutomatorPowerRF() {

    this.energyTank = new EnergyTank(this, CAPACITY, MAX_TRANSFER);
  }

  public boolean isPowered() {

    return (this.energyTank.getEnergyStored() > 0);
  }

  // --------------------------------------------------------------------------
  // - Capabilities
  // --------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityEnergy.ENERGY) {
      return true;
    }

    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityEnergy.ENERGY) {
      //noinspection unchecked
      return (T) this.energyTank;
    }

    return super.getCapability(capability, facing);
  }

  // --------------------------------------------------------------------------
  // - Serialization
  // --------------------------------------------------------------------------

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.energyTank.deserializeNBT((NBTTagInt) compound.getTag("energyTank"));
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("energyTank", this.energyTank.serializeNBT());
    return compound;
  }

  // --------------------------------------------------------------------------
  // - Chunk Load Sync
  // --------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound getUpdateTag() {

    return this.writeToNBT(new NBTTagCompound());
  }

  @Override
  public void handleUpdateTag(@Nonnull NBTTagCompound tag) {

    super.handleUpdateTag(tag);
  }

  // --------------------------------------------------------------------------
  // - Update Sync
  // --------------------------------------------------------------------------

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    NBTTagCompound tag = new NBTTagCompound();
    this.writeToNBT(tag);
    return new SPacketUpdateTileEntity(getPos(), 1, tag);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {

    NBTTagCompound tag = pkt.getNbtCompound();
    this.readFromNBT(tag);
  }

  // --------------------------------------------------------------------------
  // - Energy Tank
  // --------------------------------------------------------------------------

  public static class EnergyTank
      extends EnergyStorage
      implements INBTSerializable<NBTTagInt> {

    private final TileAutomatorPowerRF tile;

    public EnergyTank(TileAutomatorPowerRF tile, int capacity, int maxTransfer) {

      super(capacity, maxTransfer);
      this.tile = tile;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {

      int previous = this.energy;
      int result = super.receiveEnergy(maxReceive, simulate);

      if (!simulate && previous != this.energy) {
        BlockHelper.notifyBlockUpdate(this.tile.getWorld(), this.tile.getPos());
      }

      return result;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {

      int previous = this.energy;
      int result = super.extractEnergy(maxExtract, simulate);

      if (!simulate && previous != this.energy) {
        BlockHelper.notifyBlockUpdate(this.tile.getWorld(), this.tile.getPos());
      }

      return result;
    }

    @Override
    public NBTTagInt serializeNBT() {

      return new NBTTagInt(this.energy);
    }

    @Override
    public void deserializeNBT(NBTTagInt nbt) {

      this.energy = Math.min(this.capacity, nbt.getInt());
    }
  }
}

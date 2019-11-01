package com.codetaylor.mc.artisanworktables.modules.automator.tile;

import com.codetaylor.mc.artisanworktables.lib.TileNetBase;
import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomatorConfig;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockBase;
import com.codetaylor.mc.athenaeum.inventory.ObservableEnergyStorage;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataEnergyStorage;
import com.codetaylor.mc.athenaeum.network.tile.data.TileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataEnergyStorage;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.tile.IContainerProvider;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileAutomator
    extends TileNetBase
    implements IContainerProvider<AutomatorContainer, AutomatorGuiContainer>,
    ITileAutomatorPowerConsumer {

  private final TileDataEnergyStorage<EnergyTank> energyStorageData;
  private final EnergyTank energyStorage;
  private final TableItemStackHandler tableItemStackHandler;

  @SideOnly(Side.CLIENT)
  private int previousEnergy;

  public TileAutomator() {

    super(ModuleAutomator.TILE_DATA_SERVICE);

    this.energyStorage = new EnergyTank(
        ModuleAutomatorConfig.MECHANICAL_ARTISAN.RF_CAPACITY,
        ModuleAutomatorConfig.MECHANICAL_ARTISAN.RF_PER_TICK,
        Integer.MAX_VALUE
    );

    this.tableItemStackHandler = new TableItemStackHandler();

    this.energyStorageData = new TileDataEnergyStorage<>(this.energyStorage);

    this.registerTileDataForNetwork(new ITileData[]{
        this.energyStorageData,
        new TileDataItemStackHandler<>(this.tableItemStackHandler)
    });
  }

  // ---------------------------------------------------------------------------
  // - Accessors
  // ---------------------------------------------------------------------------

  public int getEnergyAmount() {

    return this.energyStorage.getEnergyStored();
  }

  public int getEnergyCapacity() {

    return this.energyStorage.getMaxEnergyStored();
  }

  public TableItemStackHandler getTableItemStackHandler() {

    return this.tableItemStackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (capability == CapabilityEnergy.ENERGY && facing == EnumFacing.DOWN);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityEnergy.ENERGY && facing == EnumFacing.DOWN) {
      //noinspection unchecked
      return (T) this.energyStorage;
    }

    return null;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("energyStorage", this.energyStorage.serializeNBT());
    compound.setTag("tableItemStackHandler", this.tableItemStackHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.energyStorage.deserializeNBT(compound.getCompoundTag("energyStorage"));
    this.tableItemStackHandler.deserializeNBT(compound.getCompoundTag("tableItemStackHandler"));
  }

  // ---------------------------------------------------------------------------
  // - IContainerProvider
  // ---------------------------------------------------------------------------

  @Override
  public AutomatorContainer getContainer(InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos) {

    return new AutomatorContainer(inventoryPlayer, world, this);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public AutomatorGuiContainer getGuiContainer(InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos) {

    return new AutomatorGuiContainer(this, this.getContainer(inventoryPlayer, world, state, pos), 176, 190);
  }

  // ---------------------------------------------------------------------------
  // - Client Data Update
  // ---------------------------------------------------------------------------

  @SideOnly(Side.CLIENT)
  @Override
  public void onTileDataUpdate() {

    if (this.energyStorageData.isDirty()) {
      int currentEnergy = this.getEnergyAmount();

      if ((this.previousEnergy == 0 && currentEnergy > 0)
          || (this.previousEnergy > 0 && currentEnergy == 0)) {
        BlockHelper.notifyBlockUpdate(this.world, this.pos.down());
      }

      this.previousEnergy = currentEnergy;
    }
  }

  // ---------------------------------------------------------------------------
  // - ITileAutomatorPowerConsumer
  // ---------------------------------------------------------------------------

  @Override
  public boolean isPowered() {

    return (this.energyStorage.getEnergyStored() > 0);
  }

  // ---------------------------------------------------------------------------
  // - Energy Tank
  // ---------------------------------------------------------------------------

  public static class EnergyTank
      extends ObservableEnergyStorage
      implements ITileDataEnergyStorage {

    /* package */ EnergyTank(int capacity, int maxReceive, int maxExtract) {

      super(capacity, maxReceive, maxExtract);
    }
  }

  // ---------------------------------------------------------------------------
  // - Table Stack Handler
  // ---------------------------------------------------------------------------

  public static class TableItemStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ TableItemStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

      if (!(stack.getItem() instanceof ItemBlock)) {
        return false;
      }

      Block block = ((ItemBlock) stack.getItem()).getBlock();
      return (block instanceof BlockBase);
    }
  }
}

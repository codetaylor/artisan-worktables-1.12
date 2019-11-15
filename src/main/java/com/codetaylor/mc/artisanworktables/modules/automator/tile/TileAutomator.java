package com.codetaylor.mc.artisanworktables.modules.automator.tile;

import com.codetaylor.mc.artisanworktables.lib.IBooleanSupplier;
import com.codetaylor.mc.artisanworktables.lib.TileNetBase;
import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomatorConfig;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.item.ItemDesignPattern;
import com.codetaylor.mc.athenaeum.inventory.ObservableEnergyStorage;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.*;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataEnergyStorage;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataItemStackHandler;
import com.codetaylor.mc.athenaeum.tile.IContainerProvider;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class TileAutomator
    extends TileNetBase
    implements IContainerProvider<AutomatorContainer, AutomatorGuiContainer>,
    ITileAutomatorPowerConsumer,
    ITickable {

  // ---------------------------------------------------------------------------
  // - Panel: Power
  // ---------------------------------------------------------------------------

  private final TileDataEnergyStorage<EnergyTank> energyStorageData;
  private final EnergyTank energyStorage;
  private final TableItemStackHandler tableItemStackHandler;
  private final TileDataFloat progress;
  private int temporaryTickCounter;

  @SideOnly(Side.CLIENT)
  private int previousEnergy;

  // ---------------------------------------------------------------------------
  // Panel: Pattern
  // ---------------------------------------------------------------------------

  public enum EnumOutputMode {
    Keep(0), Manual(1), Inventory(2), Export(3);

    private static final EnumOutputMode[] INDEX_LOOKUP = Stream.of(EnumOutputMode.values())
        .sorted(Comparator.comparing(EnumOutputMode::getIndex))
        .toArray(EnumOutputMode[]::new);

    private final int index;

    EnumOutputMode(int index) {

      this.index = index;
    }

    public int getIndex() {

      return this.index;
    }

    public static EnumOutputMode fromIndex(int index) {

      return INDEX_LOOKUP[index];
    }
  }

  private final PatternItemStackHandler patternItemStackHandler;
  private final OutputItemStackHandler[] outputItemStackHandler;
  private final boolean[] outputDirty;
  private final List<TileDataEnum<EnumOutputMode>> outputMode;

  // ---------------------------------------------------------------------------
  // Panel: Inventory
  // ---------------------------------------------------------------------------

  private final InventoryItemStackHandler inventoryItemStackHandler;
  private final InventoryGhostItemStackHandler inventoryGhostItemStackHandler;
  private final TileDataBoolean inventoryLocked;

  // ---------------------------------------------------------------------------
  // Constructor
  // ---------------------------------------------------------------------------

  public TileAutomator() {

    super(ModuleAutomator.TILE_DATA_SERVICE);

    // power panel

    this.energyStorage = new EnergyTank(
        ModuleAutomatorConfig.MECHANICAL_ARTISAN.RF_CAPACITY,
        ModuleAutomatorConfig.MECHANICAL_ARTISAN.RF_PER_TICK,
        Integer.MAX_VALUE
    );
    this.energyStorage.addObserver((energyStorage, amount) -> this.markDirty());

    this.tableItemStackHandler = new TableItemStackHandler();
    this.tableItemStackHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());

    this.energyStorageData = new TileDataEnergyStorage<>(this.energyStorage);

    this.progress = new TileDataFloat(0);

    // pattern panel

    this.outputDirty = new boolean[9];
    Arrays.fill(this.outputDirty, true);

    this.patternItemStackHandler = new PatternItemStackHandler();
    this.patternItemStackHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());

    this.outputItemStackHandler = new OutputItemStackHandler[9];

    for (int i = 0; i < this.outputItemStackHandler.length; i++) {
      int handlerIndex = i;
      this.outputItemStackHandler[i] = new OutputItemStackHandler();
      this.outputItemStackHandler[i].addObserver((stackHandler, slotIndex) -> {
        this.markDirty();
        this.outputDirty[handlerIndex] = true;
      });
    }

    this.outputMode = new ArrayList<>(9);

    for (int i = 0; i < 9; i++) {
      this.outputMode.add(new TileDataEnum<>(
          EnumOutputMode::fromIndex,
          EnumOutputMode::getIndex,
          EnumOutputMode.Keep
      ));
    }

    // inventory panel

    this.inventoryGhostItemStackHandler = new InventoryGhostItemStackHandler();
    this.inventoryGhostItemStackHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());
    this.inventoryItemStackHandler = new InventoryItemStackHandler(
        this::isInventoryLocked,
        this.inventoryGhostItemStackHandler
    );
    this.inventoryItemStackHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());
    this.inventoryLocked = new TileDataBoolean(false);

    // network

    List<ITileData> tileDataList = new ArrayList<>(Arrays.asList(
        this.energyStorageData,
        new TileDataItemStackHandler<>(this.tableItemStackHandler),
        this.progress,
        new TileDataItemStackHandler<>(this.patternItemStackHandler)
    ));

    for (OutputItemStackHandler itemStackHandler : this.outputItemStackHandler) {
      tileDataList.add(new TileDataItemStackHandler<>(itemStackHandler));
    }

    tileDataList.addAll(this.outputMode);

    tileDataList.add(new TileDataItemStackHandler<>(this.inventoryItemStackHandler));
    tileDataList.add(new TileDataItemStackHandler<>(this.inventoryGhostItemStackHandler));
    tileDataList.add(this.inventoryLocked);

    this.registerTileDataForNetwork(tileDataList.toArray(new ITileData[0]));
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

  public float getProgress() {

    return this.progress.get();
  }

  public PatternItemStackHandler getPatternItemStackHandler() {

    return this.patternItemStackHandler;
  }

  public OutputItemStackHandler getOutputItemStackHandler(int index) {

    return this.outputItemStackHandler[index];
  }

  public EnumOutputMode getOutputMode(int slotIndex) {

    TileDataEnum<EnumOutputMode> tileData = this.outputMode.get(slotIndex);
    return tileData.get();
  }

  public void cycleOutputMode(int slotIndex) {

    TileDataEnum<EnumOutputMode> tileData = this.outputMode.get(slotIndex);
    EnumOutputMode enumOutputMode = tileData.get();

    int nextIndex = enumOutputMode.getIndex() + 1;

    if (nextIndex == EnumOutputMode.values().length) {
      nextIndex = 0;
    }

    EnumOutputMode newMode = EnumOutputMode.fromIndex(nextIndex);
    this.setOutputMode(slotIndex, newMode);
    this.markDirty();
  }

  private void setOutputMode(int slotIndex, EnumOutputMode mode) {

    this.outputMode.get(slotIndex).set(mode);
  }

  public InventoryItemStackHandler getInventoryItemStackHandler() {

    return this.inventoryItemStackHandler;
  }

  public InventoryGhostItemStackHandler getInventoryGhostItemStackHandler() {

    return this.inventoryGhostItemStackHandler;
  }

  public void setInventoryLocked(boolean locked) {

    this.inventoryLocked.set(locked);
    this.markDirty();
  }

  public boolean isInventoryLocked() {

    return this.inventoryLocked.get();
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
    compound.setTag("patternItemStackHandler", this.patternItemStackHandler.serializeNBT());

    for (int i = 0; i < this.outputItemStackHandler.length; i++) {
      compound.setTag("outputItemStackHandler" + i, this.outputItemStackHandler[i].serializeNBT());
    }

    for (int i = 0; i < this.outputMode.size(); i++) {
      TileDataEnum<EnumOutputMode> tileData = this.outputMode.get(i);
      EnumOutputMode enumOutputMode = tileData.get();
      compound.setInteger("outputMode" + i, enumOutputMode.getIndex());
    }

    compound.setTag("inventoryItemStackHandler", this.inventoryItemStackHandler.serializeNBT());
    compound.setTag("inventoryGhostItemStackHandler", this.inventoryGhostItemStackHandler.serializeNBT());
    compound.setBoolean("inventoryLocked", this.inventoryLocked.get());

    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.energyStorage.deserializeNBT(compound.getCompoundTag("energyStorage"));
    this.tableItemStackHandler.deserializeNBT(compound.getCompoundTag("tableItemStackHandler"));
    this.patternItemStackHandler.deserializeNBT(compound.getCompoundTag("patternItemStackHandler"));

    for (int i = 0; i < this.outputItemStackHandler.length; i++) {
      this.outputItemStackHandler[i].deserializeNBT(compound.getCompoundTag("outputItemStackHandler" + i));
    }

    for (int i = 0; i < this.outputMode.size(); i++) {
      int index = compound.getInteger("outputMode" + i);
      EnumOutputMode value = EnumOutputMode.fromIndex(index);
      TileDataEnum<EnumOutputMode> tileData = this.outputMode.get(i);
      tileData.set(value);
    }

    this.inventoryItemStackHandler.deserializeNBT(compound.getCompoundTag("inventoryItemStackHandler"));
    this.inventoryGhostItemStackHandler.deserializeNBT(compound.getCompoundTag("inventoryGhostItemStackHandler"));
    this.inventoryLocked.set(compound.getBoolean("inventoryLocked"));
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
  // - Update
  // ---------------------------------------------------------------------------

  @Override
  public void update() {

    if (this.world.isRemote) {
      return;
    }

    this.temporaryTickCounter += 1;

    if (this.temporaryTickCounter >= 100) {
      this.temporaryTickCounter = 0;
      //this.energyStorage.extractEnergy(10000, false);

      /*ItemStack[] output = new ItemStack[]{
          new ItemStack(Blocks.DIRT),
          new ItemStack(Blocks.GRAVEL),
          new ItemStack(Blocks.STONE)
      };

      for (OutputItemStackHandler itemStackHandler : this.outputItemStackHandler) {
        for (ItemStack stack : output) {
          ItemStack itemStack = stack.copy();
          itemStackHandler.insert(itemStack, false);
        }
      }*/
    }

    for (int i = 0; i < 9; i++) {

      if (this.outputDirty[i]) {
        this.outputItemStackHandler[i].settleStacks();
        this.outputDirty[i] = false;
      }
    }

    //this.progress.set(this.temporaryTickCounter / (float) 99);
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

  // ---------------------------------------------------------------------------
  // - Pattern Stack Handler
  // ---------------------------------------------------------------------------

  public static class PatternItemStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ PatternItemStackHandler() {

      super(9);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

      Item item = stack.getItem();

      return (item instanceof ItemDesignPattern)
          && (((ItemDesignPattern) item).hasRecipe(stack));
    }
  }

  // ---------------------------------------------------------------------------
  // - Output Stack Handler
  // ---------------------------------------------------------------------------

  public static class OutputItemStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ OutputItemStackHandler() {

      super(9);
    }

    /**
     * Attempt to insert the given item stack into all slots in this handler
     * starting with slot 0.
     *
     * @param itemStack the stack to insert
     * @param simulate  simulate
     * @return the items that couldn't be inserted
     */
    private ItemStack insert(ItemStack itemStack, boolean simulate) {

      for (int i = 0; i < this.getSlots(); i++) {
        itemStack = this.insertItem(i, itemStack, simulate);

        if (itemStack.isEmpty()) {
          break;
        }
      }

      return itemStack;
    }

    /**
     * Loop through the handler's slots starting with the second slot. If
     * the slot isn't empty, remove the slot's stack and try to place the
     * removed stack into all slots up to and including the current slot
     * that was just emptied.
     */
    private void settleStacks() {

      for (int j = 1; j < this.getSlots(); j++) {
        ItemStack stackInSlot = this.getStackInSlot(j);

        if (!stackInSlot.isEmpty()) {
          int count = stackInSlot.getCount();
          stackInSlot = this.extractItem(j, count, false);

          for (int k = 0; k <= j; k++) {
            stackInSlot = this.insertItem(k, stackInSlot, false);

            if (stackInSlot.isEmpty()) {
              break;
            }
          }
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // - Inventory Stack Handler
  // ---------------------------------------------------------------------------

  public static class InventoryItemStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    private final IBooleanSupplier isLocked;
    private final InventoryGhostItemStackHandler ghostItemStackHandler;

    /* package */ InventoryItemStackHandler(
        IBooleanSupplier isLocked,
        InventoryGhostItemStackHandler ghostItemStackHandler
    ) {

      super(26);
      this.isLocked = isLocked;
      this.ghostItemStackHandler = ghostItemStackHandler;
    }

    @Override
    protected void onContentsChanged(int slot) {

      if (this.isLocked.get()) {
        ItemStack stackInSlot = this.getStackInSlot(slot);

        if (!stackInSlot.isEmpty()) {
          ItemStack copy = stackInSlot.copy();
          copy.setCount(1);
          this.ghostItemStackHandler.setStackInSlot(slot, copy);
        }
      }
      super.onContentsChanged(slot);
    }
  }

  // ---------------------------------------------------------------------------
  // - Inventory Ghost Stack Handler
  // ---------------------------------------------------------------------------

  public static class InventoryGhostItemStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ InventoryGhostItemStackHandler() {

      super(26);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }
  }
}

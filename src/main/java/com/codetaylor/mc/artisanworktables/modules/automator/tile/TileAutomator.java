package com.codetaylor.mc.artisanworktables.modules.automator.tile;

import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.ArtisanConfig;
import com.codetaylor.mc.artisanworktables.api.ArtisanToolHandlers;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.OutputWeightPair;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.IToolHandler;
import com.codetaylor.mc.artisanworktables.lib.IBooleanSupplier;
import com.codetaylor.mc.artisanworktables.lib.TileNetBase;
import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomatorConfig;
import com.codetaylor.mc.artisanworktables.modules.automator.Util;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolboxConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorkshop;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorkstation;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.item.ItemDesignPattern;
import com.codetaylor.mc.athenaeum.inventory.ObservableEnergyStorage;
import com.codetaylor.mc.athenaeum.inventory.ObservableFluidTank;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.network.tile.data.*;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileData;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataEnergyStorage;
import com.codetaylor.mc.athenaeum.network.tile.spi.ITileDataFluidTank;
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
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
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
  private int tickCounter;

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
  // Panel: Fluid
  // ---------------------------------------------------------------------------

  public enum EnumFluidMode {
    Fill(0), Drain(1);

    private static final EnumFluidMode[] INDEX_LOOKUP = Stream.of(EnumFluidMode.values())
        .sorted(Comparator.comparing(EnumFluidMode::getIndex))
        .toArray(EnumFluidMode[]::new);

    private final int index;

    EnumFluidMode(int index) {

      this.index = index;
    }

    public int getIndex() {

      return this.index;
    }

    public static EnumFluidMode fromIndex(int index) {

      return INDEX_LOOKUP[index];
    }
  }

  private final FluidHandler[] fluidHandler;
  private final List<TileDataFluidTank<FluidHandler>> fluidHandlerTileData;
  private final BucketItemStackHandler bucketItemStackHandler;
  private final List<TileDataEnum<EnumFluidMode>> fluidMode;
  private final List<TileDataBoolean> fluidLocked;
  private final boolean[] bucketUpdateRequired;

  // ---------------------------------------------------------------------------
  // Panel: Tools
  // ---------------------------------------------------------------------------

  private final ToolStackHandler toolStackHandler;
  private final ToolboxStackHandler toolboxStackHandler;

  // ---------------------------------------------------------------------------
  // Internal
  // ---------------------------------------------------------------------------

  private final ItemCapabilityWrapper itemCapabilityWrapper;
  private final FluidCapabilityWrapper fluidCapabilityWrapper;

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

    // fluid panel

    this.fluidHandler = new FluidHandler[3];
    for (int i = 0; i < this.fluidHandler.length; i++) {
      int index = i;
      this.fluidHandler[index] = new FluidHandler(
          ModuleAutomatorConfig.MECHANICAL_ARTISAN.FLUID_CAPACITY,
          () -> this.isFluidLocked(index),
          () -> this.getFluidMode(index)
      );
      this.fluidHandler[index].addObserver((fluidTank, amount) -> {
        this.markDirty();
        TileAutomator.this.bucketUpdateRequired[index] = true;
      });
    }

    this.bucketItemStackHandler = new BucketItemStackHandler();
    this.bucketItemStackHandler.addObserver((stackHandler, slotIndex) -> {
      this.markDirty();
      TileAutomator.this.bucketUpdateRequired[slotIndex] = true;
    });

    this.fluidMode = new ArrayList<>(3);
    for (int i = 0; i < 3; i++) {
      this.fluidMode.add(new TileDataEnum<>(
          EnumFluidMode::fromIndex,
          EnumFluidMode::getIndex,
          EnumFluidMode.Fill
      ));
    }

    this.fluidLocked = new ArrayList<>(3);
    for (int i = 0; i < 3; i++) {
      this.fluidLocked.add(new TileDataBoolean(false));
    }

    this.bucketUpdateRequired = new boolean[3];

    // tool panel

    this.toolStackHandler = new ToolStackHandler();
    this.toolStackHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());

    this.toolboxStackHandler = new ToolboxStackHandler();
    this.toolboxStackHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());

    // internal

    this.itemCapabilityWrapper = new ItemCapabilityWrapper(
        this.inventoryItemStackHandler,
        this.inventoryGhostItemStackHandler,
        this.outputItemStackHandler,
        this.outputMode,
        this::isInventoryLocked
    );

    this.fluidCapabilityWrapper = new FluidCapabilityWrapper(
        this.fluidHandler
    );

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

    this.fluidHandlerTileData = new ArrayList<>(this.fluidHandler.length);

    for (FluidHandler handler : this.fluidHandler) {
      TileDataFluidTank<FluidHandler> tileData = new TileDataFluidTank<>(handler);
      this.fluidHandlerTileData.add(tileData);
      tileDataList.add(tileData);
    }

    tileDataList.add(new TileDataItemStackHandler<>(this.bucketItemStackHandler));
    tileDataList.addAll(this.fluidMode);
    tileDataList.addAll(this.fluidLocked);

    tileDataList.add(new TileDataItemStackHandler<>(this.toolStackHandler));
    tileDataList.add(new TileDataItemStackHandler<>(this.toolboxStackHandler));

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
  }

  private void setOutputMode(int slotIndex, EnumOutputMode mode) {

    this.outputMode.get(slotIndex).set(mode);
    this.markDirty();
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

  public FluidHandler getFluidHandler(int index) {

    return this.fluidHandler[index];
  }

  public BucketItemStackHandler getBucketItemStackHandler() {

    return this.bucketItemStackHandler;
  }

  public void setFluidLocked(int index, boolean locked) {

    this.fluidLocked.get(index).set(locked);

    if (this.fluidHandler[index].updateMemory()) {
      this.fluidHandlerTileData.get(index).setDirty(true);
    }
    this.markDirty();
    this.bucketUpdateRequired[index] = true;
  }

  public boolean isFluidLocked(int index) {

    return this.fluidLocked.get(index).get();
  }

  public EnumFluidMode getFluidMode(int slotIndex) {

    TileDataEnum<EnumFluidMode> tileData = this.fluidMode.get(slotIndex);
    return tileData.get();
  }

  public void cycleFluidMode(int slotIndex) {

    TileDataEnum<EnumFluidMode> tileData = this.fluidMode.get(slotIndex);
    EnumFluidMode enumOutputMode = tileData.get();

    int nextIndex = enumOutputMode.getIndex() + 1;

    if (nextIndex == EnumFluidMode.values().length) {
      nextIndex = 0;
    }

    EnumFluidMode newMode = EnumFluidMode.fromIndex(nextIndex);
    this.setFluidMode(slotIndex, newMode);
  }

  private void setFluidMode(int slotIndex, EnumFluidMode mode) {

    this.fluidMode.get(slotIndex).set(mode);
    this.markDirty();
    this.bucketUpdateRequired[slotIndex] = true;
  }

  public void destroyFluid(int index) {

    if (this.fluidHandler[index].clearAll()) {
      this.fluidHandlerTileData.get(index).setDirty(true);
      this.bucketUpdateRequired[index] = true;
    }
  }

  public ToolStackHandler getToolStackHandler() {

    return this.toolStackHandler;
  }

  public ToolboxStackHandler getToolboxStackHandler() {

    return this.toolboxStackHandler;
  }

  // ---------------------------------------------------------------------------
  // - Capabilities
  // ---------------------------------------------------------------------------

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

    return (facing == EnumFacing.DOWN && capability == CapabilityEnergy.ENERGY)
        || (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        || (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityEnergy.ENERGY
        && facing == EnumFacing.DOWN) {
      //noinspection unchecked
      return (T) this.energyStorage;

    } else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      //noinspection unchecked
      return (T) this.itemCapabilityWrapper;

    } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {

      //noinspection unchecked
      return (T) this.fluidCapabilityWrapper;
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

    for (int i = 0; i < this.fluidHandler.length; i++) {
      compound.setTag("fluidHandler" + i, this.fluidHandler[i].writeToNBT(new NBTTagCompound()));
    }

    compound.setTag("bucketItemStackHandler", this.bucketItemStackHandler.serializeNBT());

    for (int i = 0; i < this.fluidMode.size(); i++) {
      TileDataEnum<EnumFluidMode> tileData = this.fluidMode.get(i);
      EnumFluidMode mode = tileData.get();
      compound.setInteger("fluidMode" + i, mode.getIndex());
    }

    for (int i = 0; i < this.fluidLocked.size(); i++) {
      TileDataBoolean tileData = this.fluidLocked.get(i);
      compound.setBoolean("fluidLocked" + i, tileData.get());
    }

    compound.setTag("toolStackHandler", this.toolStackHandler.serializeNBT());

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

    for (int i = 0; i < this.fluidHandler.length; i++) {
      this.fluidHandler[i].readFromNBT(compound.getCompoundTag("fluidHandler" + i));
    }

    this.bucketItemStackHandler.deserializeNBT(compound.getCompoundTag("bucketItemStackHandler"));

    for (int i = 0; i < this.fluidMode.size(); i++) {
      int index = compound.getInteger("fluidMode" + i);
      EnumFluidMode mode = EnumFluidMode.fromIndex(index);
      TileDataEnum<EnumFluidMode> tileData = this.fluidMode.get(i);
      tileData.set(mode);
    }

    for (int i = 0; i < this.fluidLocked.size(); i++) {
      boolean locked = compound.getBoolean("fluidLocked" + i);
      TileDataBoolean tileData = this.fluidLocked.get(i);
      tileData.set(locked);
    }

    this.toolStackHandler.deserializeNBT(compound.getCompoundTag("toolStackHandler"));
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

    this.tickCounter += 1;

    if (this.tickCounter >= ModuleAutomatorConfig.MECHANICAL_ARTISAN.TICKS_PER_CRAFT) {
      this.tickCounter = 0;
      this.doCrafting();
    }

    this.updateOutputStacks();
    this.updateBuckets();

    this.progress.set(this.tickCounter / (float) ModuleAutomatorConfig.MECHANICAL_ARTISAN.TICKS_PER_CRAFT);
  }

  private void doCrafting() {

    // get recipe
    ItemStack tableStack = this.tableItemStackHandler.getStackInSlot(0);

    if (tableStack.isEmpty()) {
      return;
    }

    Block blockFromItem = Block.getBlockFromItem(tableStack.getItem());

    if (!(blockFromItem instanceof BlockBase)) {
      return;
    }

    EnumTier tableTier = this.getTableTier(blockFromItem);

    if (tableTier == null) {
      return;
    }

    // get the table's type name
    IBlockState blockState = blockFromItem.getStateFromMeta(tableStack.getMetadata());
    EnumType type = blockState.getValue(BlockBase.VARIANT);
    String typeName = type.getName();

    // bake tools
    ItemStack[] tools = this.bakeTools(this.toolStackHandler);
    IToolHandler[] toolHandlers = this.getToolHandlers(tools);

    for (int i = 0; i < this.patternItemStackHandler.getSlots(); i++) {
      ItemStack patternStack = this.patternItemStackHandler.getStackInSlot(i);
      Item item = patternStack.getItem();

      if (!(item instanceof ItemDesignPattern)) {
        continue;
      }

      String recipeName = ((ItemDesignPattern) item).getRecipeName(patternStack);

      if (recipeName == null) {
        continue;
      }

      if (!recipeName.split(":")[0].equals(typeName)) {
        continue;
      }

      IArtisanRecipe recipe = ArtisanAPI.getRecipe(recipeName);

      // check recipe exists
      // check recipe tier
      if (recipe == null
          || !recipe.matchTier(tableTier)) {
        continue;
      }

      if (!ArtisanConfig.MODULE_WORKTABLES_CONFIG.enablePatternCreationForRecipesWithRequirements()) {

        // check recipe has no requirements
        // check recipe requires no experience
        if (!recipe.getRequirements().isEmpty()
            || recipe.getExperienceRequired() > 0) {
          continue;
        }
      }

      // check ingredients
      // check secondary ingredients
      if (!Util.hasIngredientsFor(
          recipe.getIngredientList(),
          recipe.getSecondaryIngredients(),
          this.inventoryItemStackHandler
      )) {
        continue;
      }

      // check fluids
      if (recipe.getFluidIngredient() != null
          && !Util.hasFluidsFor(recipe.getFluidIngredient(), this.fluidHandler)) {
        continue;
      }

      // check tools
      if (!recipe.matchesTools(tools, toolHandlers)) {
        continue;
      }

      // check output space
      if (!this.hasOutputSpaceFor(i, recipe)) {
        continue;
      }

      // if we've made it this far, eat the input, place the output and damage the tools

      Util.consumeIngredientsFor(
          recipe.getIngredientList(),
          recipe.getSecondaryIngredients(),
          this.inventoryItemStackHandler
      );

      Util.consumeFluidsFor(recipe.getFluidIngredient(), this.fluidHandler);

      recipe.damageTools(this.toolStackHandler, this.world, null, this.pos, false, null);

      // TODO:
      // - handle remaining items, functions / actions
      // - insert output / additional outputs

      /*
      I think we're going to need a fake context with a fake player to pass into
      the recipe's method doCraft.
       */
    }
  }

  private boolean hasOutputSpaceFor(int recipeSlotIndex, IArtisanRecipe recipe) {

    List<OutputWeightPair> outputWeightPairList = recipe.getOutputWeightPairList();
    ItemStack secondaryOutput = recipe.getSecondaryOutput().toItemStack();
    ItemStack tertiaryOutput = recipe.getTertiaryOutput().toItemStack();
    ItemStack quaternaryOutput = recipe.getQuaternaryOutput().toItemStack();

    for (OutputWeightPair pair : outputWeightPairList) {

      ItemStack output = pair.getOutput().toItemStack();
      EnumOutputMode outputMode = this.getOutputMode(recipeSlotIndex);
      IItemHandler handler;

      if (outputMode == EnumOutputMode.Inventory) {
        handler = new InventoryItemStackHandler(this.getInventoryItemStackHandler());

      } else {
        handler = new OutputItemStackHandler(this.getOutputItemStackHandler(recipeSlotIndex));
      }

      if (!this.hasOutputSpaceFor(handler, output, secondaryOutput, tertiaryOutput, quaternaryOutput)) {
        return false;
      }
    }

    return true;
  }

  private boolean hasOutputSpaceFor(IItemHandler handler, ItemStack output, ItemStack secondaryOutput, ItemStack tertiaryOutput, ItemStack quaternaryOutput) {

    if (handler instanceof OutputItemStackHandler) {

      ItemStack copy = output.copy();
      ItemStack insert = ((OutputItemStackHandler) handler).insert(copy, false);

      if (!insert.isEmpty()) {
        return false;
      }

    } else if (!this.testInsert(handler, output).isEmpty()) {
      return false;
    }

    if (!secondaryOutput.isEmpty()
        && !this.testInsert(handler, secondaryOutput).isEmpty()) {
      return false;
    }

    if (!tertiaryOutput.isEmpty()
        && !this.testInsert(handler, tertiaryOutput).isEmpty()) {
      return false;
    }

    if (!quaternaryOutput.isEmpty()
        && !this.testInsert(handler, quaternaryOutput).isEmpty()) {
      return false;
    }

    return true;
  }

  private ItemStack testInsert(IItemHandler handler, ItemStack toInsert) {

    ItemStack remainingItems = toInsert.copy();

    for (int i = 0; i < handler.getSlots(); i++) {
      remainingItems = handler.insertItem(i, remainingItems, false);

      if (remainingItems.isEmpty()) {
        break;
      }
    }

    return remainingItems;
  }

  private ItemStack[] bakeTools(TileAutomator.ToolStackHandler toolStackHandler) {

    int slotCount = toolStackHandler.getSlots();
    List<ItemStack> tools = new ArrayList<>(slotCount);

    for (int i = 0; i < slotCount; i++) {
      ItemStack stackInSlot = toolStackHandler.getStackInSlot(i);

      if (!stackInSlot.isEmpty()) {
        tools.add(stackInSlot);
      }
    }

    return tools.toArray(new ItemStack[0]);
  }

  private IToolHandler[] getToolHandlers(ItemStack[] tools) {

    IToolHandler[] toolHandlers = new IToolHandler[tools.length];

    for (int i = 0; i < tools.length; i++) {
      toolHandlers[i] = ArtisanToolHandlers.get(tools[i]);
    }

    return toolHandlers;
  }

  @Nullable
  private EnumTier getTableTier(Block block) {

    if (block instanceof BlockWorktable) {
      return EnumTier.WORKTABLE;

    } else if (block instanceof BlockWorkstation) {
      return EnumTier.WORKSTATION;

    } else if (block instanceof BlockWorkshop) {
      return EnumTier.WORKSHOP;
    }

    return null;
  }

  private void updateOutputStacks() {

    for (int i = 0; i < 9; i++) {

      if (this.outputDirty[i]) {
        this.outputItemStackHandler[i].settleStacks();
        this.outputDirty[i] = false;
      }
    }
  }

  private void updateBuckets() {

    for (int i = 0; i < this.bucketUpdateRequired.length; i++) {

      if (this.bucketUpdateRequired[i]) {
        this.bucketUpdateRequired[i] = false;
        ItemStack container = this.bucketItemStackHandler.getStackInSlot(i);

        if (container.isEmpty()) {
          // early out for empty slots
          continue;
        }

        IFluidHandlerItem capability = container.getCapability(
            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

        if (capability == null) {
          continue;
        }

        if (this.fluidMode.get(i).get() == EnumFluidMode.Drain
            && this.fluidHandler[i].getFluidAmount() > 0) {
          // fill bucket
          int containerCapacity = capability.getTankProperties()[0].getCapacity();
          FluidActionResult fluidActionResult = FluidUtil.tryFillContainer(
              container, this.fluidHandler[i], containerCapacity, null, true);

          if (fluidActionResult.success) {
            this.bucketItemStackHandler.setStackInSlot(i, fluidActionResult.result);
          }

        } else if (this.fluidMode.get(i).get() == EnumFluidMode.Fill
            && this.fluidHandler[i].getFluidAmount() < this.fluidHandler[i].getCapacity()) {
          // drain bucket
          int containerCapacity = capability.getTankProperties()[0].getCapacity();
          FluidActionResult fluidActionResult = FluidUtil.tryEmptyContainer(
              container, this.fluidHandler[i], containerCapacity, null, true);

          if (fluidActionResult.success) {
            this.bucketItemStackHandler.setStackInSlot(i, fluidActionResult.result);
          }
        }
      }
    }
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

    /* package */ OutputItemStackHandler(OutputItemStackHandler toCopy) {

      super(toCopy.getSlots());

      for (int i = 0; i < toCopy.getSlots(); i++) {
        this.setStackInSlot(i, toCopy.getStackInSlot(i));
      }
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

    /* package */ InventoryItemStackHandler(InventoryItemStackHandler toCopy) {

      super(toCopy.getSlots());
      this.isLocked = toCopy.isLocked;
      this.ghostItemStackHandler = toCopy.ghostItemStackHandler;

      for (int i = 0; i < toCopy.getSlots(); i++) {
        this.setStackInSlot(i, toCopy.getStackInSlot(i));
      }
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

  // ---------------------------------------------------------------------------
  // - Item Capability Wrapper
  // ---------------------------------------------------------------------------

  public static class ItemCapabilityWrapper
      implements IItemHandler {

    private final InventoryItemStackHandler inventoryItemStackHandler;
    private final InventoryGhostItemStackHandler inventoryGhostItemStackHandler;
    private final OutputItemStackHandler[] outputItemStackHandlers;
    private final List<TileDataEnum<EnumOutputMode>> outputModes;
    private final IBooleanSupplier inventoryLocked;

    /* package */ ItemCapabilityWrapper(
        InventoryItemStackHandler inventoryItemStackHandler,
        InventoryGhostItemStackHandler inventoryGhostItemStackHandler,
        OutputItemStackHandler[] outputItemStackHandlers,
        List<TileDataEnum<EnumOutputMode>> outputModes,
        IBooleanSupplier inventoryLocked
    ) {

      this.inventoryItemStackHandler = inventoryItemStackHandler;
      this.inventoryGhostItemStackHandler = inventoryGhostItemStackHandler;
      this.outputItemStackHandlers = outputItemStackHandlers;
      this.outputModes = outputModes;
      this.inventoryLocked = inventoryLocked;
    }

    @Override
    public int getSlots() {

      return this.inventoryItemStackHandler.getSlots() + this.outputItemStackHandlers.length;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {

      if (slot < this.inventoryItemStackHandler.getSlots()) {
        return this.inventoryItemStackHandler.getStackInSlot(slot);
      }

      return this.outputItemStackHandlers[slot - this.inventoryItemStackHandler.getSlots()].getStackInSlot(0);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

      if (slot >= this.inventoryItemStackHandler.getSlots()) {
        return stack;
      }

      if (this.inventoryLocked.get()) {
        ItemStack ghostStack = this.inventoryGhostItemStackHandler.getStackInSlot(slot);

        if (ghostStack.getItem() != stack.getItem()) {
          // items aren't equal
          return stack;

        } else if (ghostStack.getMetadata() != OreDictionary.WILDCARD_VALUE
            && ghostStack.getMetadata() != stack.getMetadata()) {
          // ghost stack doesn't have wildcard and metas don't match
          return stack;

        } else if (ghostStack.hasTagCompound()
            && !ItemStack.areItemStackTagsEqual(ghostStack, stack)) {
          // ghost stack has tag, tags aren't equal
          return stack;
        }
      }

      return this.inventoryItemStackHandler.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {

      if (slot < this.inventoryItemStackHandler.getSlots()) {
        return ItemStack.EMPTY;
      }

      int actualSlot = slot - this.inventoryItemStackHandler.getSlots();

      if (this.outputModes.get(actualSlot).get() != EnumOutputMode.Manual) {
        return ItemStack.EMPTY;
      }

      return this.outputItemStackHandlers[actualSlot].extractItem(0, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {

      if (slot < this.inventoryItemStackHandler.getSlots()) {
        return this.inventoryItemStackHandler.getSlotLimit(slot);
      }

      return this.outputItemStackHandlers[slot - this.inventoryItemStackHandler.getSlots()].getSlotLimit(0);
    }
  }

  // ---------------------------------------------------------------------------
  // - Fluid Tank
  // ---------------------------------------------------------------------------

  public static class FluidHandler
      extends ObservableFluidTank
      implements ITileDataFluidTank {

    private FluidStack memoryStack;

    private final IBooleanSupplier locked;
    private final Supplier<EnumFluidMode> mode;

    /* package */ FluidHandler(
        int capacity,
        IBooleanSupplier locked,
        Supplier<EnumFluidMode> mode
    ) {

      super(capacity);
      this.locked = locked;
      this.mode = mode;
    }

    public FluidStack getMemoryStack() {

      return this.memoryStack;
    }

    public boolean isLocked() {

      return this.locked.get();
    }

    private boolean updateMemory() {

      if (this.locked.get()) {
        // update the handler's memory
        FluidStack fluid = this.getFluid();

        if (fluid != null) {
          this.memoryStack = fluid.copy();
          return true;
        }
      }

      // clear the handler's memory
      return this.clearMemory();
    }

    private boolean clearMemory() {

      if (this.memoryStack != null) {
        this.memoryStack = null;
        return true;
      }

      return false;
    }

    public boolean clearAll() {

      FluidStack drained = super.drainInternal(this.getFluidAmount(), true);
      boolean memoryCleared = this.clearMemory();
      return (drained != null && drained.amount > 0) || memoryCleared;
    }

    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {

      super.readFromNBT(nbt);

      if (nbt.hasKey("memoryStack")) {
        NBTTagCompound memoryStackTag = nbt.getCompoundTag("memoryStack");
        this.memoryStack = FluidStack.loadFluidStackFromNBT(memoryStackTag);

      } else {
        this.memoryStack = null;
      }
      return this;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

      super.writeToNBT(nbt);

      if (this.memoryStack != null) {
        nbt.setTag("memoryStack", this.memoryStack.writeToNBT(new NBTTagCompound()));
      }
      return nbt;
    }

    @Override
    public int fillInternal(FluidStack resource, boolean doFill) {

      /*
      Do nothing if the tank's mode is not set to fill.
      If the tank is locked and the input fluid does not match the remembered
      fluid, do nothing.
      If the tank is unlocked and it was actually filled, set the remembered
      fluid to the input fluid.
       */

      if (this.mode.get() != EnumFluidMode.Fill) {
        return 0;
      }

      if (this.locked.get()
          && !resource.isFluidEqual(this.memoryStack)) {
        return 0;
      }

      int filled = super.fillInternal(resource, doFill);

      if (doFill
          && !this.locked.get()
          && filled > 0) {
        this.memoryStack = resource.copy();
      }

      return filled;
    }

    @Nullable
    @Override
    public FluidStack drainInternal(int maxDrain, boolean doDrain) {

      /*
      Do nothing if the tank's mode is not set to drain.
      If the tank is unlocked and it was actually emptied by this drain,
      clear the remembered fluid.
       */

      if (this.mode.get() != EnumFluidMode.Drain) {
        return null;
      }

      FluidStack fluidStack = super.drainInternal(maxDrain, doDrain);

      if (doDrain
          && !this.locked.get()
          && fluidStack != null
          && fluidStack.amount > 0
          && this.getFluidAmount() == 0) {
        this.memoryStack = null;
      }

      return fluidStack;
    }
  }

  // ---------------------------------------------------------------------------
  // - Bucket Stack Handler
  // ---------------------------------------------------------------------------

  public static class BucketItemStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    /* package */ BucketItemStackHandler() {

      super(3);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

      return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
    }
  }

  // ---------------------------------------------------------------------------
  // - Fluid Capability Wrapper
  // ---------------------------------------------------------------------------

  public static class FluidCapabilityWrapper
      implements IFluidHandler {

    private final FluidHandler[] fluidHandler;

    private IFluidTankProperties[] tankProperties;

    public FluidCapabilityWrapper(
        FluidHandler[] fluidHandler
    ) {

      this.fluidHandler = fluidHandler;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {

      if (this.tankProperties == null) {
        List<IFluidTankProperties> list = new ArrayList<>();

        for (FluidHandler handler : this.fluidHandler) {
          list.addAll(Arrays.asList(handler.getTankProperties()));
        }
        this.tankProperties = list.toArray(new IFluidTankProperties[0]);
      }

      return this.tankProperties;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {

      FluidStack copy = resource.copy();
      int total = copy.amount;

      for (int i = 0; i < this.fluidHandler.length; i++) {
        int filled = this.fluidHandler[i].fill(copy, doFill);
        copy.amount -= filled;

        if (copy.amount <= 0) {
          return total;
        }
      }

      return total - copy.amount;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {

      FluidStack toDrain = resource.copy();
      int totalAmountDrained = 0;

      for (int i = 0; i < this.fluidHandler.length; i++) {
        FluidStack drained = this.fluidHandler[i].drain(toDrain, doDrain);
        totalAmountDrained += (drained != null) ? drained.amount : 0;
        toDrain.amount -= (drained != null) ? drained.amount : 0;

        if (toDrain.amount <= 0) {
          break;
        }
      }

      return new FluidStack(resource, totalAmountDrained);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {

      if (maxDrain <= 0) {
        return null;
      }

      FluidStack result = null;
      int remainingDrain = maxDrain;

      for (int i = 0; i < this.fluidHandler.length; i++) {
        FluidStack drained = this.fluidHandler[i].drain(remainingDrain, false);

        if (drained == null) {
          continue;
        }

        remainingDrain -= drained.amount;

        if (result == null) {
          result = this.fluidHandler[i].drain(drained.amount, doDrain);

        } else {

          if (result.isFluidEqual(drained)) {
            this.fluidHandler[i].drain(drained.amount, doDrain);
            result.amount += drained.amount;
          }
        }

        if (remainingDrain <= 0) {
          break;
        }
      }

      return result;
    }
  }

  // ---------------------------------------------------------------------------
  // - Tool Stack Handler
  // ---------------------------------------------------------------------------

  public static class ToolStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public ToolStackHandler() {

      super(12);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

      return ArtisanAPI.containsRecipeWithTool(stack);
    }
  }

  // ---------------------------------------------------------------------------
  // - Toolbox Stack Handler
  // ---------------------------------------------------------------------------

  public static class ToolboxStackHandler
      extends ObservableStackHandler
      implements ITileDataItemStackHandler {

    public ToolboxStackHandler() {

      super(1);
    }

    @Override
    public int getSlotLimit(int slot) {

      return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

      return ModuleToolboxConfig.ENABLE_MODULE
          && ModuleToolboxConfig.MECHANICAL_TOOLBOX.ENABLED
          && stack.getItem() == Item.getItemFromBlock(ModuleToolbox.Blocks.MECHANICAL_TOOLBOX);
    }
  }

}

package com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi;

import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.ArtisanRegistries;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingContext;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ISecondaryIngredientMatcher;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementContext;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementContextSupplier;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.Container;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.network.CPacketWorktableFluidUpdate;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.CraftingContextFactory;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.tile.IContainer;
import com.codetaylor.mc.athenaeum.tile.IContainerProvider;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.EnchantmentHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class TileEntityBase
    extends TileEntity
    implements IContainer,
    IContainerProvider<Container, GuiContainerBase> {

  private String uuid;
  private EnumType type;
  private ObservableStackHandler toolHandler;
  private CraftingMatrixStackHandler craftingMatrixHandler;
  private ObservableStackHandler secondaryOutputHandler;
  private FluidTank tank;

  private boolean initialized;

  private List<Container> containerList = new ArrayList<>();

  protected TileEntityBase() {
    // serialization
  }

  public TileEntityBase(EnumType type) {

    this.type = type;
    this.initializeInternal(type);
  }

  private void initializeInternal(EnumType type) {

    if (!this.initialized) {
      this.initialize(type);
      this.initialized = true;
    }
  }

  protected void initialize(EnumType type) {

    this.uuid = type.getName() + "." + this.getTier().getName();

    this.craftingMatrixHandler = this.createCraftingMatrixHandler();
    this.toolHandler = this.createToolHandler();
    this.secondaryOutputHandler = this.createSecondaryOutputHandler();
    this.tank = this.createFluidTank(type);

    ObservableStackHandler.IContentsChangedEventHandler contentsChangedEventHandler;
    contentsChangedEventHandler = (stackHandler, slotIndex) -> {
      this.markDirty();
      this.triggerContainerRecipeUpdate();
    };
    this.craftingMatrixHandler.addObserver(contentsChangedEventHandler);
    this.toolHandler.addObserver(contentsChangedEventHandler);
    this.secondaryOutputHandler.addObserver(contentsChangedEventHandler);
  }

  protected FluidTank createFluidTank(EnumType type) {

    return new FluidTank(this.getFluidTankCapacity(type)) {

      @Override
      protected void onContentsChanged() {

        TileEntityBase.this.onFluidTankContentsChanged();
      }
    };
  }

  protected void onFluidTankContentsChanged() {

    this.triggerContainerRecipeUpdate();

    if (!this.world.isRemote) {
      ModuleWorktables.PACKET_SERVICE.sendToAllAround(
          new CPacketWorktableFluidUpdate(this.getPos(), this.tank),
          this
      );
    }
  }

  public void addContainer(Container container) {

    this.containerList.add(container);
  }

  public void removeContainer(Container container) {

    this.containerList.remove(container);
  }

  public void triggerContainerRecipeUpdate() {

    for (Container container : this.containerList) {
      container.updateRecipeOutput();
    }
  }

  public FluidTank getTank() {

    return this.tank;
  }

  public ItemStackHandler getToolHandler() {

    return this.toolHandler;
  }

  public ICraftingMatrixStackHandler getCraftingMatrixHandler() {

    return this.craftingMatrixHandler;
  }

  public ItemStackHandler getSecondaryOutputHandler() {

    return this.secondaryOutputHandler;
  }

  protected String getWorktableName() {

    return this.type.getName();
  }

  protected int getGuiTextShadowColor() {

    return ModuleWorktablesConfig.CLIENT.getTextHighlightColor(this.type.getName());
  }

  public boolean canHandleRecipeTransferJEI(String name, EnumTier tier) {

    return this.type.getName().equals(name) && tier.getId() <= this.getTier().getId();
  }

  public int getWorktableGuiTabTextureYOffset() {

    return this.type.getGuiTabTextureOffsetY();
  }

  public EnumType getType() {

    return this.type;
  }

  @Override
  public boolean hasCapability(
      @Nonnull Capability<?> capability, @Nullable EnumFacing facing
  ) {

    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      //noinspection unchecked
      return (T) this.tank;
    }

    return super.getCapability(capability, facing);
  }

  @Override
  public List<ItemStack> getBlockBreakDrops() {

    List<ItemStack> result = new ArrayList<>();

    for (int i = 0; i < this.toolHandler.getSlots(); i++) {
      ItemStack itemStack = this.toolHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        result.add(itemStack);
      }
    }

    int slotCount = this.craftingMatrixHandler.getSlots();

    for (int i = 0; i < slotCount; i++) {
      ItemStack itemStack = this.craftingMatrixHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        result.add(itemStack);
      }
    }

    if (this.secondaryOutputHandler != null) {

      for (int i = 0; i < this.secondaryOutputHandler.getSlots(); i++) {
        result.add(this.secondaryOutputHandler.getStackInSlot(i));
      }
    }

    return result;
  }

  public boolean canPlayerUse(EntityPlayer player) {

    return this.getWorld().getTileEntity(this.getPos()) == this
        && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
  }

  @Override
  public boolean shouldRefresh(
      World world, BlockPos pos, IBlockState oldState, IBlockState newSate
  ) {

    return oldState.getBlock() != newSate.getBlock();
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {

    tag = super.writeToNBT(tag);
    tag.setInteger("type", this.type.getMeta());
    tag.setTag("craftingMatrixHandler", this.craftingMatrixHandler.serializeNBT());
    tag.setTag("toolHandler", this.toolHandler.serializeNBT());
    tag.setTag("secondaryOutputHandler", this.secondaryOutputHandler.serializeNBT());
    tag.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {

    super.readFromNBT(tag);
    this.type = EnumType.fromMeta(tag.getInteger("type"));
    this.initializeInternal(this.type);
    this.craftingMatrixHandler.deserializeNBT(tag.getCompoundTag("craftingMatrixHandler"));
    this.toolHandler.deserializeNBT(tag.getCompoundTag("toolHandler"));
    this.secondaryOutputHandler.deserializeNBT(tag.getCompoundTag("secondaryOutputHandler"));
    this.tank.readFromNBT(tag.getCompoundTag("tank"));
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {

    this.readFromNBT(packet.getNbtCompound());
  }

  @Override
  public final NBTTagCompound getUpdateTag() {

    return this.writeToNBT(new NBTTagCompound());
  }

  public void onTakeResult(EntityPlayer player) {

    IArtisanRecipe recipe = this.getRecipe(player);

    if (recipe == null) {
      return;
    }

    recipe.doCraft(this.getCraftingContext(player));

    this.markDirty();

    if (!this.world.isRemote) {
      this.notifyBlockUpdate();
    }
  }

  public ICraftingContext getCraftingContext(EntityPlayer player) {

    return CraftingContextFactory.createContext(this, player, null);
  }

  public void notifyBlockUpdate() {

    this.markDirty();
    BlockHelper.notifyBlockUpdate(this.getWorld(), this.getPos());
  }

  public IArtisanRecipe getRecipe(@Nonnull EntityPlayer player) {

    FluidStack fluidStack = this.getTank().getFluid();

    if (fluidStack != null) {
      fluidStack = fluidStack.copy();
    }

    int playerExperience = EnchantmentHelper.getPlayerExperienceTotal(player);
    int playerLevels = player.experienceLevel;
    boolean isPlayerCreative = player.isCreative();

    IForgeRegistry<RequirementContextSupplier> contextSupplierRegistry = ArtisanRegistries.REQUIREMENT_CONTEXT_SUPPLIER;
    Map<ResourceLocation, IRequirementContext> contextMap = new HashMap<>();
    ICraftingContext craftingContext = this.getCraftingContext(player);

    for (Map.Entry<ResourceLocation, RequirementContextSupplier> entry : contextSupplierRegistry.getEntries()) {
      RequirementContextSupplier contextSupplier = entry.getValue();
      IRequirementContext context = contextSupplier.get();
      context.initialize(craftingContext);
      contextMap.put(entry.getKey(), context);
    }

    return this.getWorktableRecipeRegistry().findRecipe(
        playerExperience,
        playerLevels,
        isPlayerCreative,
        this.getTools(),
        this.craftingMatrixHandler,
        fluidStack,
        this.getSecondaryIngredientMatcher(),
        this.getTier(),
        contextMap
    );
  }

  public ISecondaryIngredientMatcher getSecondaryIngredientMatcher() {

    return ISecondaryIngredientMatcher.FALSE;
  }

  public ItemStack[] getTools() {

    int slotCount = this.toolHandler.getSlots();
    ItemStack[] tools = new ItemStack[slotCount];

    for (int i = 0; i < slotCount; i++) {
      tools[i] = this.toolHandler.getStackInSlot(i);
    }

    return tools;
  }

  public boolean hasTool() {

    ItemStack[] tools = this.getTools();
    boolean hasTool = false;

    for (ItemStack tool : tools) {

      if (!tool.isEmpty()) {
        hasTool = true;
        break;
      }
    }

    return hasTool;
  }

  public List<TileEntityBase> getJoinedTables(List<TileEntityBase> result) {

    return this.getJoinedTables(result, null);
  }

  /**
   * Uses a flood fill to find all tables adjacent to this one. An empty list is returned
   * if any of the tables found are of the same type and tier. If a player is provided,
   * any tables outside of the player's reach will not be returned in the list.
   *
   * @param result a list to store the result
   * @param player the player, can be null
   * @return result list
   */
  public List<TileEntityBase> getJoinedTables(List<TileEntityBase> result, @Nullable EntityPlayer player) {

    Map<String, TileEntityBase> joinedTableMap = new TreeMap<>();
    joinedTableMap.put(this.uuid, this);

    Set<BlockPos> searchedPositionSet = new HashSet<>();
    searchedPositionSet.add(this.pos);

    Queue<BlockPos> toSearchQueue = new ArrayDeque<>();
    toSearchQueue.offer(this.pos.offset(EnumFacing.NORTH));
    toSearchQueue.offer(this.pos.offset(EnumFacing.EAST));
    toSearchQueue.offer(this.pos.offset(EnumFacing.SOUTH));
    toSearchQueue.offer(this.pos.offset(EnumFacing.WEST));

    BlockPos searchPosition;

    while ((searchPosition = toSearchQueue.poll()) != null) {

      if (searchedPositionSet.contains(searchPosition)) {
        // we've already looked here, skip
        continue;
      }

      // record that we've looked here
      searchedPositionSet.add(searchPosition);

      TileEntity tileEntity = this.world.getTileEntity(searchPosition);

      if (tileEntity instanceof TileEntityBase) {
        String key = ((TileEntityBase) tileEntity).uuid;

        if (joinedTableMap.containsKey(key)) {
          // this indicates two tables of the same type joined in the pseudo-multiblock
          // and we need to invalidate the structure by returning nothing
          return Collections.emptyList();
        }

        // found a table!
        if (player == null || ((TileEntityBase) tileEntity).canPlayerUse(player)) {
          joinedTableMap.put(key, (TileEntityBase) tileEntity);
        }

        // check around this newly discovered table
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.NORTH));
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.EAST));
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.SOUTH));
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.WEST));
      }
    }

    result.addAll(joinedTableMap.values());
    //return result.size() < 2 ? Collections.emptyList() : result;
    return result;
  }

  /**
   * Searches cardinal directions around all joined tables and returns an adjacent toolbox.
   * <p>
   * If more than one toolbox is found, the first toolbox found is returned.
   * <p>
   * If no toolbox is found, null is returned.
   *
   * @return adjacent toolbox or null
   */
  @Nullable
  public TileEntityToolbox getAdjacentToolbox() {

    List<TileEntityBase> joinedTables = this.getJoinedTables(new ArrayList<>());

    for (TileEntityBase joinedTable : joinedTables) {
      BlockPos pos = joinedTable.getPos();
      TileEntity tileEntity;

      for (EnumFacing facing : EnumFacing.HORIZONTALS) {

        if ((tileEntity = this.world.getTileEntity(pos.offset(facing))) != null) {

          if (tileEntity instanceof TileEntityToolbox) {

            return (TileEntityToolbox) tileEntity;
          }
        }
      }
    }

    return null;
  }

  @Nullable
  public ITileEntityDesigner getAdjacentDesignersTable() {

    List<TileEntityBase> joinedTables = this.getJoinedTables(new ArrayList<>());

    for (TileEntityBase joinedTable : joinedTables) {
      BlockPos pos = joinedTable.getPos();
      TileEntity tileEntity;

      for (EnumFacing facing : EnumFacing.HORIZONTALS) {

        if ((tileEntity = this.world.getTileEntity(pos.offset(facing))) != null) {

          if (tileEntity instanceof ITileEntityDesigner) {
            return (ITileEntityDesigner) tileEntity;
          }
        }
      }
    }

    return null;
  }

  public RecipeRegistry getWorktableRecipeRegistry() {

    return ArtisanAPI.getWorktableRecipeRegistry(this.getWorktableName());
  }

  public ItemStack getItemStackForTabDisplay(IBlockState state) {

    Block block = state.getBlock();
    Item item = Item.getItemFromBlock(block);
    return new ItemStack(item, 1, block.getMetaFromState(state));
  }

  @Override
  public Container getContainer(
      InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos
  ) {

    return new Container(inventoryPlayer, world, this);
  }

  public int getMaximumDisplayedTabCount() {

    return 6;
  }

  protected abstract String getTableTitleKey();

  protected abstract ResourceLocation getGuiBackgroundTexture();

  protected abstract ObservableStackHandler createToolHandler();

  protected abstract CraftingMatrixStackHandler createCraftingMatrixHandler();

  protected abstract ObservableStackHandler createSecondaryOutputHandler();

  protected abstract int getFluidTankCapacity(EnumType type);

  public abstract EnumTier getTier();
}

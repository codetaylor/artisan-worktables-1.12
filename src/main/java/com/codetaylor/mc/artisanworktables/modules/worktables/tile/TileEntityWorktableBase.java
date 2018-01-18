package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.ContainerWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.CraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipeWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import com.codetaylor.mc.athenaeum.helper.StackHelper;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.tile.IContainer;
import com.codetaylor.mc.athenaeum.tile.IContainerProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.*;

public abstract class TileEntityWorktableBase
    extends TileEntity
    implements IContainer,
    IContainerProvider<ContainerWorktable, GuiContainerWorktable> {

  private Random random = new Random();

  private ObservableStackHandler toolHandler;
  private CraftingMatrixStackHandler craftingMatrixHandler;
  private ObservableStackHandler secondaryOutputHandler;

  private static int GUI_TAB_OFFSET;

  public TileEntityWorktableBase(int width, int height) {

    this.craftingMatrixHandler = new CraftingMatrixStackHandler(width, height);
    this.toolHandler = new ObservableStackHandler(1);
    this.secondaryOutputHandler = new ObservableStackHandler(3);

    ObservableStackHandler.IContentsChangedEventHandler contentsChangedEventHandler;
    contentsChangedEventHandler = (stackHandler, slotIndex) -> this.markDirty();
    this.craftingMatrixHandler.addObserver(contentsChangedEventHandler);
    this.toolHandler.addObserver(contentsChangedEventHandler);
    this.secondaryOutputHandler.addObserver(contentsChangedEventHandler);
  }

  public ItemStackHandler getToolHandler() {

    return this.toolHandler;
  }

  public CraftingMatrixStackHandler getCraftingMatrixHandler() {

    return this.craftingMatrixHandler;
  }

  public ItemStackHandler getSecondaryOutputHandler() {

    return this.secondaryOutputHandler;
  }

  @Override
  public List<ItemStack> getBlockBreakDrops() {

    List<ItemStack> result = new ArrayList<>();

    result.add(this.toolHandler.getStackInSlot(0));

    int slotCount = this.craftingMatrixHandler.getSlots();

    for (int i = 0; i < slotCount; i++) {
      ItemStack itemStack = this.craftingMatrixHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        result.add(itemStack);
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
    tag.setTag("craftingMatrixHandler", this.craftingMatrixHandler.serializeNBT());
    tag.setTag("toolHandler", this.toolHandler.serializeNBT());
    tag.setTag("secondaryOutputHandler", this.secondaryOutputHandler.serializeNBT());
    return tag;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {

    super.readFromNBT(tag);
    this.craftingMatrixHandler.deserializeNBT(tag.getCompoundTag("craftingMatrixHandler"));
    this.toolHandler.deserializeNBT(tag.getCompoundTag("toolHandler"));
    this.secondaryOutputHandler.deserializeNBT(tag.getCompoundTag("secondaryOutputHandler"));
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

    // Find the recipe

    IRecipeWorktable recipe = this.getRecipe(player);

    if (recipe == null) {
      return;
    }

    // Decrease stacks in crafting matrix

    int slotCount = this.craftingMatrixHandler.getSlots();

    for (int i = 0; i < slotCount; i++) {
      ItemStack itemStack = this.craftingMatrixHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {

        if (itemStack.getItem().hasContainerItem(itemStack)
            && itemStack.getCount() == 1) {
          this.craftingMatrixHandler.setStackInSlot(i, itemStack.getItem().getContainerItem(itemStack));

        } else {
          this.craftingMatrixHandler.setStackInSlot(i, StackHelper.decrease(itemStack.copy(), 1, false));
        }
      }
    }

    // Check if the recipe has multiple, weighted outputs and swap outputs accordingly.

    if (!this.world.isRemote && !player.inventory.getItemStack().isEmpty()) {

      if (recipe.hasMultipleWeightedOutputs()) {
        ItemStack itemStack = recipe.selectOutput(this.random);
        player.inventory.setItemStack(itemStack);
        ((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(-1, -1, itemStack));
      }
    }

    // Check for and populate secondary, tertiary and quaternary outputs

    if (!this.world.isRemote) {
      ItemStack extraOutput = recipe.getSecondaryOutput();

      if (!extraOutput.isEmpty()) {

        if (this.random.nextFloat() < recipe.getSecondaryOutputChance()) {
          this.generateExtraOutput(extraOutput);
        }
      }

      extraOutput = recipe.getTertiaryOutput();

      if (!extraOutput.isEmpty()) {

        if (this.random.nextFloat() < recipe.getTertiaryOutputChance()) {
          this.generateExtraOutput(extraOutput);
        }
      }

      extraOutput = recipe.getQuaternaryOutput();

      if (!extraOutput.isEmpty()) {

        if (this.random.nextFloat() < recipe.getQuaternaryOutputChance()) {
          this.generateExtraOutput(extraOutput);
        }
      }
    }

    // Damage or destroy tool

    ItemStack itemStack = this.toolHandler.getStackInSlot(0);

    if (!itemStack.isEmpty()) {
      int itemDamage = itemStack.getMetadata() + recipe.getToolDamage();

      if (itemDamage >= itemStack.getItem().getMaxDamage(itemStack)) {
        this.toolHandler.setStackInSlot(0, ItemStack.EMPTY);

        if (!this.world.isRemote) {
          this.world.playSound(
              player,
              player.posX,
              player.posY,
              player.posZ,
              SoundEvents.ENTITY_ITEM_BREAK,
              SoundCategory.PLAYERS,
              1.0f,
              1.0f
          );
        }

      } else {
        ItemStack copy = itemStack.copy();
        copy.setItemDamage(itemDamage);
        this.toolHandler.setStackInSlot(0, copy);
      }
    }

    this.markDirty();
  }

  public IRecipeWorktable getRecipe(EntityPlayer player) {

    RegistryRecipeWorktable registry = this.getWorktableRecipeRegistry();
    return registry.findRecipe(
        player,
        this.toolHandler.getStackInSlot(0),
        this.craftingMatrixHandler
    );
  }

  private void generateExtraOutput(ItemStack extraOutput) {

    ItemStack result = extraOutput;

    for (int i = 0; i < 3; i++) {
      result = this.secondaryOutputHandler.insertItem(i, result, false);

      if (result.isEmpty()) {
        break;
      }
    }

    if (!result.isEmpty() && !this.world.isRemote) {
      StackHelper.spawnStackOnTop(this.world, result, this.pos.add(0, 1, 0));
    }
  }

  public List<TileEntityWorktableBase> getJoinedTables(List<TileEntityWorktableBase> result) {

    Map<String, TileEntityWorktableBase> joinedTableMap = new TreeMap<>();
    joinedTableMap.put(this.getClass().getName(), this);

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

      if (tileEntity instanceof TileEntityWorktableBase) {
        String name = tileEntity.getClass().getName();

        if (joinedTableMap.containsKey(name)) {
          // this indicates two tables of the same type joined in the pseudo-multiblock
          // and we need to invalidate the structure by returning nothing
          return Collections.emptyList();
        }

        // found a table!
        joinedTableMap.put(name, (TileEntityWorktableBase) tileEntity);

        // check around this newly discovered table
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.NORTH));
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.EAST));
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.SOUTH));
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.WEST));
      }
    }

    result.addAll(joinedTableMap.values());
    return result.size() < 2 ? Collections.emptyList() : result;
  }

  /**
   * Searches cardinal directions around all joined tables and returns an adjacent inventory.
   * <p>
   * If more than one inventory is found, null is returned.
   *
   * @return adjacent inventory or null
   */
  @Nullable
  public IItemHandler getAdjacentInventory() {

    List<TileEntityWorktableBase> joinedTables = this.getJoinedTables(new ArrayList<>());
    IItemHandler result = null;

    for (TileEntityWorktableBase joinedTable : joinedTables) {
      BlockPos pos = joinedTable.getPos();
      TileEntity tileEntity;

      for (EnumFacing facing : EnumFacing.HORIZONTALS) {

        if ((tileEntity = this.world.getTileEntity(pos.offset(facing))) != null) {

          if (!(tileEntity instanceof TileEntityWorktableBase)) {

            IItemHandler capability = tileEntity.getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                facing.getOpposite()
            );

            if (capability != null) {

              if (result != null) {
                return null;
              }

              result = capability;
            }
          }
        }
      }
    }

    return result;
  }

  public RegistryRecipeWorktable getWorktableRecipeRegistry() {

    return WorktableAPI.getWorktableRecipeRegistry(this.getWorktableName());
  }

  public ItemStack getItemStackForTabDisplay(IBlockState state) {

    Block block = state.getBlock();
    Item item = Item.getItemFromBlock(block);
    return new ItemStack(item, 1, block.getMetaFromState(state));
  }

  public void setGuiTabOffset(int offset) {
    GUI_TAB_OFFSET = offset;
  }

  public int getGuiTabOffset() {

    return GUI_TAB_OFFSET;
  }

  @Override
  public ContainerWorktable getContainer(
      InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos
  ) {

    return new ContainerWorktable(inventoryPlayer, world, this);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public GuiContainerWorktable getGuiContainer(
      InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos
  ) {

    return new GuiContainerWorktable(
        this.getContainer(inventoryPlayer, world, state, pos),
        this.getWorktableGuiBackgroundTexture(),
        this.getTableTitleKey(),
        this.getWorktableGuiTextShadowColor(),
        this
    );
  }

  private String getTableTitleKey() {

    return String.format(ModuleWorktables.Lang.WORKTABLE_TITLE, ModuleWorktables.MOD_ID, this.getWorktableName());
  }

  public abstract int getWorktableGuiTabTextureYOffset();

  public abstract boolean canHandleJEIRecipeTransfer(String name);

  protected abstract int getWorktableGuiTextShadowColor();

  protected abstract ResourceLocation getWorktableGuiBackgroundTexture();

  protected abstract String getWorktableName();

}

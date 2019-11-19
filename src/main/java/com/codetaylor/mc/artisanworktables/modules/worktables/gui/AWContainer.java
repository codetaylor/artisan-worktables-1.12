package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.api.ArtisanToolHandlers;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingContext;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.util.Util;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.lib.RoundRobinHelper;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot.*;
import com.codetaylor.mc.artisanworktables.modules.worktables.network.SCPacketWorktableContainerJoinedBlockBreak;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.ITileEntityDesigner;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntitySecondaryInputBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workshop.TileEntityWorkshop;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workstation.TileEntityWorkstation;
import com.codetaylor.mc.athenaeum.gui.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class AWContainer
    extends ContainerBase {

  private final CraftingResultSlot craftingResultSlot;
  private World world;
  private TileEntityBase tile;
  private TileEntityToolbox toolbox;
  private ITileEntityDesigner designersTable;
  private final ItemStackHandler resultHandler;
  private final ItemStackHandler patternResultHandler;
  private FluidStack lastFluidStack;
  private final EntityPlayer player;
  private final List<ToolboxSideSlot> toolboxSideSlotList;

  public final int slotIndexResult;
  public final int slotIndexCraftingMatrixStart;
  public final int slotIndexCraftingMatrixEnd;
  public final int slotIndexInventoryStart;
  public final int slotIndexInventoryEnd;
  public final int slotIndexHotbarStart;
  public final int slotIndexHotbarEnd;
  public final int slotIndexToolsStart;
  public final int slotIndexToolsEnd;
  public final int slotIndexSecondaryOutputStart;
  public final int slotIndexSecondaryOutputEnd;
  public final int slotIndexToolboxStart;
  public final int slotIndexToolboxEnd;
  public final int slotIndexSecondaryInputStart;
  public final int slotIndexSecondaryInputEnd;
  public final int slotIndexPattern;
  public final int slotIndexPatternResult;
  private final ItemStackHandler patternHandler;

  public AWContainer(
      InventoryPlayer playerInventory,
      World world,
      TileEntityBase tile
  ) {

    super(playerInventory);

    this.world = world;
    this.tile = tile;
    this.toolbox = this.getToolbox(this.tile);
    this.designersTable = this.getAdjacentDesignersTable(this.tile);

    this.tile.addContainer(this);

    this.player = playerInventory.player;
    Runnable slotChangeListener = () -> AWContainer.this.tile.setRequiresRecipeUpdate();

    // ------------------------------------------------------------------------
    // Result
    this.slotIndexResult = this.nextSlotIndex;
    this.resultHandler = this.tile.getResultHandler();
    this.craftingResultSlot = new CraftingResultSlot(
        this::updateRecipeOutput,
        this.tile,
        this.resultHandler,
        0,
        this.containerResultPositionGetX(),
        this.containerResultPositionGetY()
    );
    this.containerSlotAdd(this.craftingResultSlot);

    // ------------------------------------------------------------------------
    // Crafting Matrix
    ICraftingMatrixStackHandler craftingMatrixHandler = this.tile.getCraftingMatrixHandler();
    this.slotIndexCraftingMatrixStart = this.nextSlotIndex;

    for (int y = 0; y < craftingMatrixHandler.getHeight(); ++y) {
      for (int x = 0; x < craftingMatrixHandler.getWidth(); ++x) {
        this.containerSlotAdd(new CraftingIngredientSlot(
            this,
            this.tile,
            slotChangeListener,
            craftingMatrixHandler,
            x + y * craftingMatrixHandler.getWidth(),
            20 + x * 18,
            17 + y * 18
        ));
      }
    }
    this.slotIndexCraftingMatrixEnd = this.nextSlotIndex - 1;

    // ------------------------------------------------------------------------
    // Player Inventory
    this.slotIndexInventoryStart = this.nextSlotIndex;
    this.containerPlayerInventoryAdd();
    this.slotIndexInventoryEnd = this.nextSlotIndex - 1;

    // ------------------------------------------------------------------------
    // Player HotBar
    this.slotIndexHotbarStart = this.nextSlotIndex;
    this.containerPlayerHotbarAdd();
    this.slotIndexHotbarEnd = this.nextSlotIndex - 1;

    // ------------------------------------------------------------------------
    // Tool Slots
    {
      this.slotIndexToolsStart = this.nextSlotIndex;
      ItemStackHandler toolHandler = this.tile.getToolHandler();

      for (int i = 0; i < toolHandler.getSlots(); i++) {
        this.containerSlotAdd(new CraftingToolSlot(
            this,
            this.tile,
            slotChangeListener,
            itemStack -> {
              RecipeRegistry worktableRecipeRegistry = this.tile.getWorktableRecipeRegistry();
              return this.tile.isCreative()
                  || worktableRecipeRegistry.containsRecipeWithTool(ArtisanToolHandlers.get(itemStack), itemStack);
            },
            toolHandler,
            i,
            78 + this.containerToolOffsetGetX(),
            35 + 22 * i + this.containerToolOffsetGetY()
        ));
      }
      this.slotIndexToolsEnd = this.nextSlotIndex - 1;
    }

    // ------------------------------------------------------------------------
    // Secondary output
    this.slotIndexSecondaryOutputStart = this.nextSlotIndex;

    if (this.tile instanceof TileEntityWorkshop) {

      for (int i = 0; i < 3; i++) {
        this.containerSlotAdd(new CraftingExtraResultSlot(
            this.tile,
            this.tile.getSecondaryOutputHandler(),
            i,
            116 + i * 18,
            17
        ));
      }

    } else {

      for (int i = 0; i < 3; i++) {
        this.containerSlotAdd(new CraftingExtraResultSlot(
            this.tile,
            this.tile.getSecondaryOutputHandler(),
            i,
            152,
            17 + i * 18
        ));
      }
    }
    this.slotIndexSecondaryOutputEnd = this.nextSlotIndex - 1;

    // ------------------------------------------------------------------------
    // Secondary input
    if (this.tile instanceof TileEntitySecondaryInputBase) {
      this.slotIndexSecondaryInputStart = this.nextSlotIndex;
      IItemHandler handler = ((TileEntitySecondaryInputBase) this.tile).getSecondaryIngredientHandler();
      int slotCount = handler.getSlots();

      for (int i = 0; i < slotCount; i++) {
        this.containerSlotAdd(new CraftingSecondarySlot(
            this,
            this.tile,
            slotChangeListener,
            handler,
            i,
            8 + i * 18,
            75 + this.containerSecondaryInputOffsetGetY()
        ));
      }
      this.slotIndexSecondaryInputEnd = this.nextSlotIndex - 1;

    } else {
      this.slotIndexSecondaryInputStart = -1;
      this.slotIndexSecondaryInputEnd = -1;
    }
    // ------------------------------------------------------------------------
    // Designer's Table
    if (this.canPlayerUsePatternSlots()) {
      // TODO: pattern input
      this.slotIndexPattern = this.nextSlotIndex;
      this.patternHandler = this.designersTable.getPatternStackHandler();
      this.containerSlotAdd(new PatternSlot(
          this::canPlayerUsePatternSlots,
          this::updateRecipeOutput,
          this.patternHandler,
          0,
          2 * -18 + this.containerToolboxOffsetGetX(),
          8
      ));

      // TODO: pattern output
      this.slotIndexPatternResult = this.nextSlotIndex;
      this.patternResultHandler = new ItemStackHandler(1);
      this.containerSlotAdd(new PatternResultSlot(
          this::canPlayerUsePatternSlots,
          () -> {
            ItemStack stackInSlot = this.patternHandler.getStackInSlot(0);
            this.patternHandler.setStackInSlot(0, Util.decrease(stackInSlot, 1, false));
            this.updateRecipeOutput();
          },
          this.patternResultHandler,
          0,
          this.containerToolboxOffsetGetX(),
          8
      ));

    } else {
      this.patternHandler = null;
      this.patternResultHandler = null;
      this.slotIndexPattern = -1;
      this.slotIndexPatternResult = -1;
    }

    // ------------------------------------------------------------------------
    // Side Toolbox
    this.slotIndexToolboxStart = this.nextSlotIndex;
    if (this.canPlayerUseToolbox()) {
      this.toolboxSideSlotList = new ArrayList<>(27);
      ItemStackHandler itemHandler = this.toolbox.getItemHandler();
      int offsetY = (this.designersTable != null) ? 33 : 0;

      for (int x = 0; x < 3; x++) {

        for (int y = 0; y < 9; y++) {
          ToolboxSideSlot toolboxSideSlot = new ToolboxSideSlot(
              this::canPlayerUseToolbox,
              itemHandler,
              y + x * 9,
              x * -18 + this.containerToolboxOffsetGetX(),
              y * 18 + 8
          );
          this.toolboxSideSlotList.add(toolboxSideSlot);
          this.containerSlotAdd(toolboxSideSlot);
          toolboxSideSlot.move(toolboxSideSlot.xPos, toolboxSideSlot.yPos + offsetY);
        }
      }

    } else {
      this.toolboxSideSlotList = Collections.emptyList();
    }
    this.slotIndexToolboxEnd = this.nextSlotIndex - 1;

    this.updateRecipeOutput();
  }

  private int containerToolboxOffsetGetX() {

    return -26;
  }

  private int containerSecondaryInputOffsetGetY() {

    if (this.tile instanceof TileEntityWorkshop) {
      return 36;
    }

    return 0;
  }

  private int containerToolOffsetGetX() {

    if (this.tile instanceof TileEntityWorkshop) {
      return 36;
    }

    return 0;
  }

  private int containerToolOffsetGetY() {

    if (this.tile instanceof TileEntityWorkstation) {
      return -11;

    }
    if (this.tile instanceof TileEntityWorkshop) {
      return 5;
    }

    return 0;
  }

  private int containerResultPositionGetY() {

    if (this.tile instanceof TileEntityWorkshop) {
      return 62;
    }

    return 35;
  }

  private int containerResultPositionGetX() {

    if (this.tile instanceof TileEntityWorkshop) {
      return 143;
    }

    return 115;
  }

  @Override
  protected int containerInventoryPositionGetY() {

    if (this.tile instanceof TileEntityWorkstation) {
      return 107;

    } else if (this.tile instanceof TileEntityWorkshop) {
      return 143;
    }

    return super.containerInventoryPositionGetY();
  }

  @Override
  protected int containerInventoryPositionGetX() {

    return super.containerInventoryPositionGetX();
  }

  @Override
  protected int containerHotbarPositionGetY() {

    if (this.tile instanceof TileEntityWorkstation) {
      return 165;

    } else if (this.tile instanceof TileEntityWorkshop) {
      return 201;
    }

    return super.containerHotbarPositionGetY();
  }

  @Override
  protected int containerHotbarPositionGetX() {

    return super.containerHotbarPositionGetX();
  }

  private TileEntityToolbox getToolbox(TileEntityBase tile) {

    return tile.getAdjacentToolbox();
  }

  private ITileEntityDesigner getAdjacentDesignersTable(TileEntityBase tile) {

    return tile.getAdjacentDesignersTable();
  }

  public void onJoinedBlockBreak(World world, BlockPos pos) {

    if (!world.isRemote) {
      ModuleWorktables.PACKET_SERVICE.sendTo(
          new SCPacketWorktableContainerJoinedBlockBreak(pos),
          (EntityPlayerMP) this.player
      );
    }

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity != null && tileEntity == this.designersTable) {

      for (ToolboxSideSlot slot : this.toolboxSideSlotList) {
        slot.moveToOrigin();
      }
    }
  }

  /**
   * @return the adjacent toolbox, or null
   */
  @Nullable
  public TileEntityToolbox getToolbox() {

    if (this.hasValidToolbox()) {

      return this.toolbox;
    }

    return null;
  }

  public boolean hasValidToolbox() {

    return this.toolbox != null && !this.toolbox.isInvalid();
  }

  public boolean canPlayerUseToolbox() {

    return this.hasValidToolbox() && this.toolbox.canPlayerUse(this.player);
  }

  @Nullable
  public ITileEntityDesigner getDesignersTable() {

    if (this.hasValidPatternSlots()) {

      return this.designersTable;
    }

    return null;
  }

  public boolean hasValidPatternSlots() {

    return this.designersTable != null
        && !this.designersTable.getTileEntity().isInvalid();
  }

  public boolean canPlayerUsePatternSlots() {

    return this.hasValidPatternSlots()
        && this.designersTable.canPlayerUse(this.player)
        && ModuleWorktablesConfig.patternSlotsEnabledForTier(this.tile.getTier());
  }

  public void updateRecipeOutput() {

    if (this.tile == null) {
      return;
    }

    if (this.tile.isCreative()) {
      return;
    }

    IArtisanRecipe recipe = this.tile.getRecipe(this.player);

    if (recipe != null) {
      ICraftingContext context = this.tile.getCraftingContext(this.player);
      this.resultHandler.setStackInSlot(0, recipe.getBaseOutput(context).toItemStack());

      if (this.canPlayerUsePatternSlots()) {

        if (this.patternHandler.getStackInSlot(0).isEmpty()) {
          this.patternResultHandler.setStackInSlot(0, ItemStack.EMPTY);

        } else if (recipe.getName() != null) {
          // 2019-09-23 added the null check because vanilla recipes have a null name
          ItemStack stack = new ItemStack(ModuleWorktables.Items.DESIGN_PATTERN);
          NBTTagCompound tag = new NBTTagCompound();
          tag.setString("recipe", recipe.getName());
          stack.setTagCompound(tag);
          this.patternResultHandler.setStackInSlot(0, stack);
        }
      }

    } else {
      this.resultHandler.setStackInSlot(0, ItemStack.EMPTY);

      if (this.canPlayerUsePatternSlots()) {
        this.patternResultHandler.setStackInSlot(0, ItemStack.EMPTY);
      }
    }
  }

  @Override
  public void onCraftMatrixChanged(IInventory inventoryIn) {

    //
  }

  @Override
  public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {

    return this.tile.canPlayerUse(playerIn);
  }

  private boolean swapItemStack(int originSlotIndex, int targetSlotIndex, boolean swapOnlyEmtpyTarget) {

    Slot originSlot = this.inventorySlots.get(originSlotIndex);
    Slot targetSlot = this.inventorySlots.get(targetSlotIndex);

    ItemStack originStack = originSlot.getStack();
    ItemStack targetStack = targetSlot.getStack();

    if (swapOnlyEmtpyTarget) {

      if (!targetStack.isEmpty()) {
        return false;
      }
    }

    if (originStack.isItemEqual(targetStack)) {
      return true;
    }

    if (!originStack.isEmpty()
        && targetSlot.isItemValid(originStack)) {

      if (targetStack.isEmpty()) {
        targetSlot.putStack(originStack);
        originSlot.putStack(ItemStack.EMPTY);

      } else {
        targetSlot.putStack(originStack);
        originSlot.putStack(targetStack);
      }

      return true;
    }

    return false;
  }

  @Override
  public boolean canMergeSlot(ItemStack stack, Slot slotIn) {

    return slotIn != this.craftingResultSlot && super.canMergeSlot(stack, slotIn);
  }

  private boolean isSlotSecondaryInput(int slotIndex) {

    return slotIndex >= this.slotIndexSecondaryInputStart && slotIndex <= this.slotIndexSecondaryInputEnd;
  }

  private boolean isSlotIndexResult(int slotIndex) {

    return slotIndex == this.slotIndexResult;
  }

  private boolean isSlotIndexInventory(int slotIndex) {

    return slotIndex >= this.slotIndexInventoryStart && slotIndex <= this.slotIndexInventoryEnd;
  }

  private boolean isSlotIndexHotbar(int slotIndex) {

    return slotIndex >= this.slotIndexHotbarStart && slotIndex <= this.slotIndexHotbarEnd;
  }

  private boolean isSlotIndexToolbox(int slotIndex) {

    return slotIndex >= this.slotIndexToolboxStart && slotIndex <= this.slotIndexToolboxEnd;
  }

  private boolean isSlotIndexTool(int slotIndex) {

    return slotIndex >= this.slotIndexToolsStart && slotIndex <= this.slotIndexToolsEnd;
  }

  private boolean mergeInventory(ItemStack itemStack, boolean reverse) {

    return this.mergeItemStack(itemStack, this.slotIndexInventoryStart, this.slotIndexInventoryEnd + 1, reverse);
  }

  private boolean mergeHotbar(ItemStack itemStack, boolean reverse) {

    return this.mergeItemStack(itemStack, this.slotIndexHotbarStart, this.slotIndexHotbarEnd + 1, reverse);
  }

  private boolean mergeCraftingMatrix(ItemStack itemStack, boolean reverse) {

    return this.mergeItemStack(
        itemStack,
        this.slotIndexCraftingMatrixStart,
        this.slotIndexCraftingMatrixEnd + 1,
        reverse
    );
  }

  private boolean mergeBlankPattern(ItemStack itemStack, boolean reverse) {

    if (this.slotIndexPattern == -1) {
      return false;
    }

    return this.mergeItemStack(
        itemStack,
        this.slotIndexPattern,
        this.slotIndexPattern + 1,
        reverse
    );
  }

  private boolean mergeToolbox(ItemStack itemStack, boolean reverse) {

    return this.mergeItemStack(itemStack, this.slotIndexToolboxStart, this.slotIndexToolboxEnd + 1, reverse);
  }

  private boolean mergeSecondaryInput(ItemStack itemStack, boolean reverse) {

    if (this.slotIndexSecondaryInputStart == -1) {
      return false;
    }

    return this.mergeItemStack(
        itemStack,
        this.slotIndexSecondaryInputStart,
        this.slotIndexSecondaryInputEnd + 1,
        reverse
    );
  }

  private boolean swapTools(int slotIndex) {

    for (int i = this.slotIndexToolsStart; i <= this.slotIndexToolsEnd; i++) {

      // try to swap tool into empty slot first
      if (this.swapItemStack(slotIndex, i, true)) {
        return true; // Swapped tools
      }
    }

    for (int i = this.slotIndexToolsStart; i <= this.slotIndexToolsEnd; i++) {

      // swap tools into any valid slot
      if (this.swapItemStack(slotIndex, i, false)) {
        return true; // Swapped tools
      }
    }

    return false;
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex) {

    ItemStack itemStackCopy = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(slotIndex);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemStack = slot.getStack();
      itemStackCopy = itemStack.copy();

      if (this.tile.isLocked()
          && (this.isSlotIndexInventory(slotIndex) || this.isSlotIndexHotbar(slotIndex))) {

        // If the table is locked and the player has shift-clicked something in
        // the inventory or hotbar...

        ICraftingMatrixStackHandler craftingMatrixHandler = this.tile.getCraftingMatrixHandler();
        ICraftingMatrixStackHandler craftingMatrixHandlerGhost = this.tile.getCraftingMatrixHandlerGhost();

        int count = itemStack.getCount();

        for (int i = 0; i < count; i++) {
          List<Tuple> list = RoundRobinHelper.getSortedIndices(itemStack, craftingMatrixHandler, craftingMatrixHandlerGhost);

          if (list.isEmpty()) {
            return ItemStack.EMPTY;
          }

          ItemStack copy = itemStack.copy();
          copy.setCount(1);
          int index = (int) list.get(0).getFirst();
          ItemStack result = craftingMatrixHandler.insertItem(index, copy, false);

          if (result.isEmpty()) {
            itemStack.shrink(1);

          } else {
            return ItemStack.EMPTY;
          }
        }

        return ItemStack.EMPTY;
      }

      if (this.isSlotIndexResult(slotIndex)) {
        // Result

        // This is executed on both the client and server for each craft. If the crafting
        // grid has multiple, complete recipes, this will be executed for each complete
        // recipe.

        IArtisanRecipe recipe = this.tile.getRecipe(playerIn);

        if (recipe == null) {
          return ItemStack.EMPTY;
        }

        if (recipe.hasMultipleWeightedOutputs()) {
          // Restrict the player from shift-clicking items out of the result slot when the recipe has
          // multiple weighted outputs. This allows us to replace the item the player picks up after
          // it has been retrieved.
          return ItemStack.EMPTY;
        }

        if (!this.mergeInventory(itemStack, false)
            && !this.mergeHotbar(itemStack, false)) {
          return ItemStack.EMPTY;
        }

        itemStack.getItem().onCreated(itemStack, this.world, playerIn);
        slot.onSlotChange(itemStack, itemStackCopy);

      } else if (this.isSlotIndexInventory(slotIndex)) {
        // Inventory clicked, try to move to tool slot first, then blank pattern slot,
        // then crafting matrix, then secondary, then hotbar

        if (this.swapTools(slotIndex)) {
          return ItemStack.EMPTY; // swapped tools
        }

        if (!this.mergeBlankPattern(itemStack, false)
            && !this.mergeCraftingMatrix(itemStack, false)
            && !this.mergeSecondaryInput(itemStack, false)
            && !this.mergeHotbar(itemStack, false)) {
          return ItemStack.EMPTY;
        }

      } else if (this.isSlotIndexHotbar(slotIndex)) {
        // HotBar clicked, try to move to tool slot first, then blank pattern slot,
        // then crafting matrix, then secondary, then inventory

        if (this.swapTools(slotIndex)) {
          return ItemStack.EMPTY; // swapped tools
        }

        if (!this.mergeBlankPattern(itemStack, false)
            && !this.mergeCraftingMatrix(itemStack, false)
            && !this.mergeSecondaryInput(itemStack, false)
            && !this.mergeInventory(itemStack, false)) {
          return ItemStack.EMPTY;
        }

      } else if (this.isSlotIndexToolbox(slotIndex)) {
        // Toolbox clicked, try to move to tool slot first, then crafting matrix, then secondary, then inventory, then hotbar

        if (this.swapTools(slotIndex)) {
          return ItemStack.EMPTY; // swapped tools
        }

        if (!this.mergeCraftingMatrix(itemStack, false)
            && !this.mergeSecondaryInput(itemStack, false)
            && !this.mergeInventory(itemStack, false)
            && !this.mergeHotbar(itemStack, false)) {
          return ItemStack.EMPTY;
        }

      } else if (this.isSlotIndexTool(slotIndex)) {
        // Tool slot clicked, try to move to toolbox first, then inventory, then hotbar

        if (this.canPlayerUseToolbox()) {

          if (!this.mergeToolbox(itemStack, false)
              && !this.mergeInventory(itemStack, false)
              && !this.mergeHotbar(itemStack, false)) {
            return ItemStack.EMPTY;
          }

        } else if (!this.mergeInventory(itemStack, false)
            && !this.mergeHotbar(itemStack, false)) {
          return ItemStack.EMPTY;
        }

      } else if (!this.mergeInventory(itemStack, false)
          && !this.mergeHotbar(itemStack, false)) {
        // All others: crafting matrix, secondary output
        return ItemStack.EMPTY;
      }

      if (itemStack.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);

      } else {
        slot.onSlotChanged();
      }

      if (itemStack.getCount() == itemStackCopy.getCount()) {
        return ItemStack.EMPTY;
      }

      ItemStack itemStack2 = slot.onTake(playerIn, itemStack);

      if (slotIndex == 0) {
        playerIn.dropItem(itemStack2, false);
      }
    }

    return itemStackCopy;
  }

  public List<Slot> getRecipeSlotsJEI(List<Slot> result, int transferGridSize) {

    // grid

    /*
    2019-10-15 - Issue #196
    A smaller grid size is returned if the transfer handler making the request
    passes a smaller transferGridSize parameter. This prevents the smaller
    transfer handlers from making a mess in the larger tables.
     */

    int rowLength = (this.tile.getTier() == EnumTier.WORKSHOP) ? 5 : 3;

    for (int row = 0; row < transferGridSize; row++) {
      for (int col = 0; col < transferGridSize; col++) {
        result.add(this.inventorySlots.get(this.slotIndexCraftingMatrixStart + col + (row * rowLength)));
      }
    }

    // tool
    for (int i = this.slotIndexToolsStart; i <= this.slotIndexToolsEnd; i++) {
      result.add(this.inventorySlots.get(i));
    }

    // Intentionally left out the secondary ingredient slots to prevent JEI from
    // transferring items to these slots when the transfer button is clicked.

    return result;
  }

  public List<Slot> getInventorySlotsJEI(List<Slot> result) {

    for (int i = this.slotIndexInventoryStart; i <= this.slotIndexInventoryEnd; i++) {
      result.add(this.inventorySlots.get(i));
    }

    for (int i = this.slotIndexHotbarStart; i <= this.slotIndexHotbarEnd; i++) {
      result.add(this.inventorySlots.get(i));
    }

    if (this.canPlayerUseToolbox()) {

      for (int i = this.slotIndexToolboxStart; i <= this.slotIndexToolboxEnd; i++) {
        result.add(this.inventorySlots.get(i));
      }
    }

    for (int i = this.slotIndexSecondaryInputStart; i < this.slotIndexSecondaryInputEnd; i++) {
      result.add(this.inventorySlots.get(i));
    }

    return result;
  }

  public TileEntityBase getTile() {

    return this.tile;
  }

  public boolean canHandleRecipeTransferJEI(
      String name,
      EnumTier tier
  ) {

    return !this.tile.isCreative()
        && this.tile.canHandleRecipeTransferJEI(name, tier);
  }

  @Override
  public void detectAndSendChanges() {

    super.detectAndSendChanges();

    //System.out.println("Player: " + this.player + ", Stack:" + this.craftingResultSlot.getStack());

    if (this.tile == null
        || this.tile.getWorld().isRemote) {
      return;
    }

    // Send fluid changes to the client.

    FluidTank tank = this.tile.getTank();
    FluidStack fluidStack = tank.getFluid();

    if (this.lastFluidStack != null
        && fluidStack == null) {
      this.lastFluidStack = null;
      this.updateRecipeOutput();

    } else if (this.lastFluidStack == null
        && fluidStack != null) {
      this.lastFluidStack = fluidStack.copy();
      this.updateRecipeOutput();

    } else if (this.lastFluidStack != null) {

      if (!this.lastFluidStack.isFluidStackIdentical(fluidStack)) {
        this.lastFluidStack = fluidStack.copy();
        this.updateRecipeOutput();
      }
    }
  }

  @Override
  public void onContainerClosed(EntityPlayer playerIn) {

    super.onContainerClosed(playerIn);
    this.tile.removeContainer(this);
  }

  @Nonnull
  @Override
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {

    ItemStack playerStack = player.inventory.getItemStack().copy();

    if (this.tile.isCreative()) {

      if (slotId > -1) {
        Slot slot = this.inventorySlots.get(slotId);

        if (slot instanceof ICreativeSlotClick) {
          return ((ICreativeSlotClick) slot).creativeSlotClick(slotId, dragType, clickTypeIn, player);
        }
      }
    }

    if (slotId == this.slotIndexResult) {
      IArtisanRecipe recipe = this.tile.getRecipe(player);

      if (recipe != null
          && recipe.hasMultipleWeightedOutputs()) {

        if (!playerStack.isEmpty()
            || clickTypeIn == ClickType.THROW) {
          return ItemStack.EMPTY;

        } else if (clickTypeIn == ClickType.PICKUP
            && dragType == 1) {
          return super.slotClick(slotId, 0, clickTypeIn, player);
        }
      }
    }

    if (slotId == this.slotIndexResult
        || (slotId >= this.slotIndexSecondaryOutputStart && slotId <= this.slotIndexSecondaryOutputEnd)) {
      // prevent deleting half of the stack
      return super.slotClick(slotId, 0, clickTypeIn, player);
    }

    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}

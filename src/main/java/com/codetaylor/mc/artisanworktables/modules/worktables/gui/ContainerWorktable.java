package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipeWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityWorktableBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityWorktableFluidBase;
import com.codetaylor.mc.athenaeum.gui.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class ContainerWorktable
    extends ContainerBase {

  private final CraftingResultSlot craftingResultSlot;
  private World world;
  private TileEntityWorktableBase tile;
  private TileEntityToolbox toolbox;
  private final ItemStackHandler resultHandler;
  private FluidStack lastFluidStack;
  private final EntityPlayer player;

  public ContainerWorktable(
      InventoryPlayer playerInventory,
      World world,
      TileEntityWorktableBase tile
  ) {

    super(playerInventory);

    this.world = world;
    this.tile = tile;
    this.toolbox = this.getToolbox(this.tile);

    this.player = playerInventory.player;
    Runnable slotChangeListener = () -> this.updateRecipeOutput(this.player);

    // Result Slot 0
    this.resultHandler = new ItemStackHandler(1);
    this.craftingResultSlot = new CraftingResultSlot(
        slotChangeListener,
        this.tile,
        resultHandler,
        0,
        115,
        35
    );
    this.containerSlotAdd(this.craftingResultSlot);

    // Crafting Matrix 1 - 9, inclusive
    CraftingMatrixStackHandler craftingMatrixHandler = this.tile.getCraftingMatrixHandler();

    for (int y = 0; y < craftingMatrixHandler.getHeight(); ++y) {
      for (int x = 0; x < craftingMatrixHandler.getWidth(); ++x) {
        this.containerSlotAdd(new CraftingIngredientSlot(
            slotChangeListener,
            craftingMatrixHandler,
            x + y * 3,
            20 + x * 18,
            17 + y * 18
        ));
      }
    }

    // Player Inventory 10 - 36, inclusive
    this.containerPlayerInventoryAdd();
    /*for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 9; ++x) {
        this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
      }
    }*/

    // Player HotBar 37 - 46, inclusive
    this.containerPlayerHotbarAdd();
    /*for (int x = 0; x < 9; ++x) {
      this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
    }*/

    // Tool Slot 46
    this.containerSlotAdd(new CraftingToolSlot(
        slotChangeListener,
        itemStack -> this.tile.getWorktableRecipeRegistry().containsRecipeWithToolInSlot(itemStack, 0),
        this.tile.getToolHandler(),
        0,
        78,
        35
    ));

    // Secondary output 47 - 49, inclusive
    for (int i = 0; i < 3; i++) {
      this.containerSlotAdd(new ResultSlot(this.tile.getSecondaryOutputHandler(), i, 152, 17 + i * 18));
    }

    if (this.toolbox != null && !this.toolbox.isInvalid()) {
      ItemStackHandler itemHandler = this.toolbox.getItemHandler();

      // Side Toolbox 50 - 76, inclusive
      for (int x = 0; x < 3; x++) {

        for (int y = 0; y < 9; y++) {
          this.containerSlotAdd(new ToolboxSlot(
              this.toolbox,
              itemHandler,
              y + x * 9,
              x * -18 - 26,
              y * 18 + 8
          ));
        }
      }
    }

    this.updateRecipeOutput(this.player);
  }

  @Override
  protected int containerInventoryPositionGetX() {

    return super.containerInventoryPositionGetX();
  }

  @Override
  protected int containerHotbarPositionGetX() {

    return super.containerHotbarPositionGetX();
  }

  private TileEntityToolbox getToolbox(TileEntityWorktableBase tile) {

    return tile.getAdjacentToolbox();
  }

  public TileEntityToolbox getToolbox() {

    return this.toolbox;
  }

  private void updateRecipeOutput(EntityPlayer player) {

    FluidStack fluidStack = null;

    if (this.tile instanceof TileEntityWorktableFluidBase) {
      fluidStack = ((TileEntityWorktableFluidBase) this.tile).getTank().getFluid();

      if (fluidStack != null) {
        fluidStack = fluidStack.copy();
      }
    }

    RegistryRecipeWorktable registry = this.tile.getWorktableRecipeRegistry();
    IRecipeWorktable recipe = registry.findRecipe(
        player,
        this.tile.getToolHandler().getStackInSlot(0),
        this.tile.getCraftingMatrixHandler(),
        fluidStack
    );

    if (recipe != null) {
      this.resultHandler.setStackInSlot(0, recipe.getBaseOutput());

    } else {
      this.resultHandler.setStackInSlot(0, ItemStack.EMPTY);
    }
  }

  @Override
  public void onCraftMatrixChanged(IInventory inventoryIn) {

    //
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {

    return this.tile.canPlayerUse(playerIn);
  }

  private boolean swapItemStack(int originSlotIndex, int targetSlotIndex) {

    Slot originSlot = this.inventorySlots.get(originSlotIndex);
    Slot targetSlot = this.inventorySlots.get(targetSlotIndex);

    ItemStack originStack = originSlot.getStack();
    ItemStack targetStack = targetSlot.getStack();

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

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex) {

    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(slotIndex);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (slotIndex == 0) {
        // Result

        // This is executed on both the client and server for each craft. If the crafting
        // grid has multiple, complete recipes, this will be executed for each complete
        // recipe.

        IRecipeWorktable recipe = this.tile.getRecipe(playerIn);

        if (recipe == null) {
          return ItemStack.EMPTY;
        }

        if (recipe.hasMultipleWeightedOutputs()) {
          return ItemStack.EMPTY;
        }

        if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
          // Can't merge the craft result into any inventory slot.
          return ItemStack.EMPTY;
        }

        itemstack1.getItem().onCreated(itemstack1, this.world, playerIn);
        slot.onSlotChange(itemstack1, itemstack);

      } else if (slotIndex >= 10 && slotIndex <= 36) {
        // Inventory clicked, try to move to tool slot first, then crafting matrix, then hotbar

        if (this.swapItemStack(slotIndex, 46)) {
          return ItemStack.EMPTY; // Swapped tools
        }

        if (!this.mergeItemStack(itemstack1, 1, 10, false)
            && !this.mergeItemStack(itemstack1, 37, 46, false)) {
          return ItemStack.EMPTY;
        }

      } else if (slotIndex >= 37 && slotIndex <= 45) {
        // HotBar clicked, try to move to tool slot first, then crafting matrix, then inventory

        if (this.swapItemStack(slotIndex, 46)) {
          return ItemStack.EMPTY; // Swapped tools
        }

        if (!this.mergeItemStack(itemstack1, 1, 10, false)
            && !this.mergeItemStack(itemstack1, 10, 37, false)) {
          return ItemStack.EMPTY;
        }

      } else if (slotIndex >= 50 && slotIndex <= 76) {
        // Toolbox clicked, try to move to tool slot first, then crafting matrix, then inventory

        if (this.swapItemStack(slotIndex, 46)) {
          //return this.inventorySlots.get(slotIndex).getStack();
          return ItemStack.EMPTY; // Swapped tools
        }

        if (!this.mergeItemStack(itemstack1, 1, 10, false)
            && !this.mergeItemStack(itemstack1, 10, 37, false)) {
          return ItemStack.EMPTY;
        }

      } else if (slotIndex == 46) {
        // Tool slot clicked, try to move to toolbox first, then player inventory

        if (this.toolbox != null) {

          if (!this.mergeItemStack(itemstack1, 50, 77, false)
              && !this.mergeItemStack(itemstack1, 10, 46, false)) {
            return ItemStack.EMPTY;
          }

        } else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
          return ItemStack.EMPTY;
        }

      } else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
        // All others: crafting matrix
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);

      } else {
        slot.onSlotChanged();
      }

      if (itemstack1.getCount() == itemstack.getCount()) {
        return ItemStack.EMPTY;
      }

      ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

      if (slotIndex == 0) {
        playerIn.dropItem(itemstack2, false);
      }
    }

    return itemstack;
  }

  public List<Slot> getRecipeSlots(List<Slot> result) {

    // grid
    for (int i = 1; i < 10; i++) {
      result.add(this.inventorySlots.get(i));
    }

    // tool
    result.add(this.inventorySlots.get(46));

    return result;
  }

  public List<Slot> getInventorySlots(List<Slot> result) {

    for (int i = 10; i < 46; i++) {
      result.add(this.inventorySlots.get(i));
    }

    if (this.toolbox != null) {

      for (int i = 50; i < 77; i++) {
        result.add(this.inventorySlots.get(i));
      }
    }

    return result;
  }

  public TileEntityWorktableBase getTile() {

    return this.tile;
  }

  public boolean canHandleJEIRecipeTransfer(String name) {

    return this.tile.canHandleJEIRecipeTransfer(name);
  }

  @Override
  public void detectAndSendChanges() {

    super.detectAndSendChanges();

    if (!(this.tile instanceof TileEntityWorktableFluidBase)
        || this.tile.getWorld().isRemote) {
      return;
    }

    FluidTank tank = ((TileEntityWorktableFluidBase) this.tile).getTank();
    FluidStack fluidStack = tank.getFluid();

    if (this.lastFluidStack != null
        && fluidStack == null) {
      this.lastFluidStack = null;
      this.updateRecipeOutput(this.player);

    } else if (this.lastFluidStack == null
        && fluidStack != null) {
      this.lastFluidStack = fluidStack.copy();
      this.updateRecipeOutput(this.player);

    } else if (this.lastFluidStack != null) {

      if (!this.lastFluidStack.isFluidStackIdentical(fluidStack)) {
        this.lastFluidStack = fluidStack.copy();
        this.updateRecipeOutput(this.player);
      }
    }
  }
}

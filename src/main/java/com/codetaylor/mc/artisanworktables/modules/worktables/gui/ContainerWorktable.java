package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipeWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityWorktableBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class ContainerWorktable
    extends Container {

  private World world;
  private TileEntityWorktableBase tile;
  private final ItemStackHandler resultHandler;

  public ContainerWorktable(
      InventoryPlayer playerInventory,
      World world,
      TileEntityWorktableBase tile
  ) {

    this.world = world;
    this.tile = tile;

    Runnable slotChangeListener = () -> this.updateRecipeOutput(playerInventory.player);

    // Result Slot 0
    this.resultHandler = new ItemStackHandler(1);
    this.addSlotToContainer(new CraftingResultSlot(
        slotChangeListener,
        this.tile,
        resultHandler,
        0,
        124,
        35
    ));

    // Crafting Matrix 1 - 9
    CraftingMatrixStackHandler craftingMatrixHandler = this.tile.getCraftingMatrixHandler();

    for (int y = 0; y < craftingMatrixHandler.getHeight(); ++y) {
      for (int x = 0; x < craftingMatrixHandler.getWidth(); ++x) {
        this.addSlotToContainer(new CraftingIngredientSlot(
            slotChangeListener,
            craftingMatrixHandler,
            x + y * 3,
            29 + x * 18,
            17 + y * 18
        ));
      }
    }

    // Player Inventory 10 - 37
    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 9; ++x) {
        this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
      }
    }

    // Player HotBar 38 - 46
    for (int x = 0; x < 9; ++x) {
      this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));
    }

    // Tool Slot 47
    this.addSlotToContainer(new CraftingToolSlot(
        slotChangeListener,
        itemStack -> this.tile.getRecipeRegistry().containsRecipeWithTool(itemStack),
        this.tile.getToolHandler(),
        0,
        87,
        35
    ));

    this.updateRecipeOutput(playerInventory.player);
  }

  private void updateRecipeOutput(EntityPlayer player) {

    RegistryRecipeWorktable registry = this.tile.getRecipeRegistry();
    IRecipeWorktable recipe = registry.findRecipe(
        player,
        this.tile.getToolHandler().getStackInSlot(0),
        this.tile.getCraftingMatrixHandler()
    );

    if (recipe != null) {
      this.resultHandler.setStackInSlot(0, recipe.getOutput());

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
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex) {

    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(slotIndex);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (slotIndex == 0) {
        // Result

        itemstack1.getItem().onCreated(itemstack1, this.world, playerIn);

        if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
          return ItemStack.EMPTY;
        }

        slot.onSlotChange(itemstack1, itemstack);

      } else if (slotIndex >= 10 && slotIndex < 37) {
        // Inventory clicked, try to move to tool slot first, then crafting matrix, then hotbar

        if (this.swapItemStack(slotIndex, 46)) {
          return ItemStack.EMPTY; // Swapped tools
        }

        if (!this.mergeItemStack(itemstack1, 46, 47, false)
            && !this.mergeItemStack(itemstack1, 1, 10, false)
            && !this.mergeItemStack(itemstack1, 37, 46, false)) {
          return ItemStack.EMPTY;
        }

      } else if (slotIndex >= 37 && slotIndex < 46) {
        // HotBar clicked, try to move to tool slot first, then crafting matrix, then inventory

        if (this.swapItemStack(slotIndex, 46)) {
          return ItemStack.EMPTY; // Swapped tools
        }

        if (!this.mergeItemStack(itemstack1, 46, 47, false)
            && !this.mergeItemStack(itemstack1, 1, 10, false)
            && !this.mergeItemStack(itemstack1, 10, 37, false)) {
          return ItemStack.EMPTY;
        }

      } else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
        // All others: tool slot and crafting matrix
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
    result.add(this.inventorySlots.get(this.inventorySlots.size() - 1));

    return result;
  }

  public List<Slot> getInventorySlots(List<Slot> result) {

    for (int i = 10; i < 46; i++) {
      result.add(this.inventorySlots.get(i));
    }

    return result;
  }

  public TileEntityWorktableBase getTile() {

    return this.tile;
  }
}

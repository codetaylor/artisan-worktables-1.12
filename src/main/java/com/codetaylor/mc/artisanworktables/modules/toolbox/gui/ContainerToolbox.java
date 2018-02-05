package com.codetaylor.mc.artisanworktables.modules.toolbox.gui;

import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.athenaeum.gui.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerToolbox
    extends ContainerBase {

  private static final int NUM_ROWS = 3;

  private final TileEntityToolbox tile;

  public ContainerToolbox(
      InventoryPlayer playerInventory,
      TileEntityToolbox tile
  ) {

    super(playerInventory);

    this.tile = tile;

    for (int j = 0; j < NUM_ROWS; ++j) {
      for (int k = 0; k < 9; ++k) {
        this.containerSlotAdd(new SlotItemHandler(this.tile.getItemHandler(), k + j * 9, 8 + k * 18, 17 + j * 18));
      }
    }

    this.containerPlayerInventoryAdd();
    this.containerPlayerHotbarAdd();
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {

    return this.tile.isUsableByPlayer(playerIn);
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (index < NUM_ROWS * 9) {
        if (!this.mergeItemStack(itemstack1, NUM_ROWS * 9, this.inventorySlots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.mergeItemStack(itemstack1, 0, NUM_ROWS * 9, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }
    }

    return itemstack;
  }

}

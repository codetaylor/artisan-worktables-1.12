package com.codetaylor.mc.artisanworktables.modules.toolbox.gui;

import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerToolbox
    extends Container {

  private static final int NUM_ROWS = 3;
  private final TileEntityToolbox tile;

  public ContainerToolbox(
      InventoryPlayer playerInventory,
      TileEntityToolbox tile
  ) {

    this.tile = tile;
    this.tile.openInventory(playerInventory.player);

    int i = (NUM_ROWS - 4) * 18;

    for (int j = 0; j < NUM_ROWS; ++j) {
      for (int k = 0; k < 9; ++k) {
        this.addSlotToContainer(new SlotItemHandler(this.tile.getItemHandler(), k + j * 9, 8 + k * 18, 17 + j * 18));
      }
    }

    for (int l = 0; l < 3; ++l) {
      for (int j1 = 0; j1 < 9; ++j1) {
        this.addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 102 + l * 18 + i));
      }
    }

    for (int i1 = 0; i1 < 9; ++i1) {
      this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 160 + i));
    }
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

  @Override
  public void onContainerClosed(EntityPlayer playerIn) {

    super.onContainerClosed(playerIn);
    this.tile.closeInventory(playerIn);
  }

}

package com.codetaylor.mc.artisanworktables.modules.automator.tile;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ItemHandlerDelegate
    implements IItemHandler {

  protected IItemHandler itemHandler;

  @Override
  public int getSlots() {

    if (this.itemHandler == null) {
      return 0;
    }

    return this.itemHandler.getSlots();
  }

  @Nonnull
  @Override
  public ItemStack getStackInSlot(int slot) {

    if (this.itemHandler == null) {
      return ItemStack.EMPTY;
    }

    return this.itemHandler.getStackInSlot(slot);
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

    if (this.itemHandler == null) {
      return stack;
    }

    return this.itemHandler.insertItem(slot, stack, simulate);
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {

    if (this.itemHandler == null) {
      return ItemStack.EMPTY;
    }

    return this.itemHandler.extractItem(slot, amount, simulate);
  }

  @Override
  public int getSlotLimit(int slot) {

    if (this.itemHandler == null) {
      return 0;
    }

    return this.itemHandler.getSlotLimit(slot);
  }
}

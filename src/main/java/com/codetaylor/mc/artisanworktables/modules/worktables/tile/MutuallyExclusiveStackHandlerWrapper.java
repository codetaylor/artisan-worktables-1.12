package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class MutuallyExclusiveStackHandlerWrapper
    implements IItemHandler {

  private final ItemStackHandler handler;
  private BiMap<Item, Integer> indexMap;

  public MutuallyExclusiveStackHandlerWrapper(ItemStackHandler handler) {

    this.handler = handler;
    this.indexMap = HashBiMap.create(this.handler.getSlots());
  }

  @Override
  public int getSlots() {

    return this.handler.getSlots();
  }

  @Nonnull
  @Override
  public ItemStack getStackInSlot(int slot) {

    return this.handler.getStackInSlot(slot);
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

    slot = this.indexMap.getOrDefault(stack.getItem(), slot);
    ItemStack existingItemStack = this.handler.getStackInSlot(slot);

    if (this.matchStacks(stack, existingItemStack)) {
      return this.handler.insertItem(slot, stack, simulate);
    }

    for (int i = 0; i < this.handler.getSlots(); i++) {
      existingItemStack = this.handler.getStackInSlot(i);

      if (this.matchStacks(stack, existingItemStack)) {
        this.indexMap.forcePut(stack.getItem(), i);
        return this.handler.insertItem(i, stack, simulate);
      }
    }

    return this.handler.insertItem(slot, stack, simulate);
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {

    return this.handler.extractItem(slot, amount, simulate);
  }

  @Override
  public int getSlotLimit(int slot) {

    return this.handler.getSlotLimit(slot);
  }

  private boolean matchStacks(@Nonnull ItemStack stackToInsert, ItemStack existingItemStack) {

    return stackToInsert.isItemEqual(existingItemStack)
        && ItemStack.areItemStackTagsEqual(existingItemStack, stackToInsert);
  }
}

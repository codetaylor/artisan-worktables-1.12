package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Limits the underlying stack handler to one slot per item type.
 */
public class MutuallyExclusiveStackHandler
    extends ObservableStackHandler {

  private BiMap<Item, Integer> indexMap;

  public MutuallyExclusiveStackHandler(int size) {

    super(size);
    this.indexMap = HashBiMap.create(size);
  }

  @Nonnull
  @Override
  public ItemStack insertItem(
      int slot, @Nonnull ItemStack stack, boolean simulate
  ) {

    slot = this.indexMap.getOrDefault(stack.getItem(), slot);
    ItemStack existingItemStack = this.stacks.get(slot);

    if (this.matchStacks(stack, existingItemStack)) {
      return super.insertItem(slot, stack, simulate);
    }

    for (int i = 0; i < this.stacks.size(); i++) {
      existingItemStack = this.stacks.get(i);

      if (this.matchStacks(stack, existingItemStack)) {
        this.indexMap.put(stack.getItem(), i);
        return super.insertItem(i, stack, simulate);
      }
    }

    return super.insertItem(slot, stack, simulate);
  }

  private boolean matchStacks(@Nonnull ItemStack stackToInsert, ItemStack existingItemStack) {

    return stackToInsert.isItemEqual(existingItemStack)
        && ItemStack.areItemStackTagsEqual(existingItemStack, stackToInsert);
  }
}

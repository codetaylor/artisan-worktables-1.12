package com.codetaylor.mc.artisanworktables.modules.toolbox.tile;

import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ToolboxItemStackHandler
    extends ObservableStackHandler {

  private Predicate<ItemStack> predicate;

  public ToolboxItemStackHandler(Predicate<ItemStack> predicate, int size) {

    super(size);
    this.predicate = predicate;
  }

  @Override
  public void setStackInSlot(int slot, @Nonnull ItemStack stack) {

    if (this.predicate.test(stack)) {
      super.setStackInSlot(slot, stack);
    }
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

    if (this.predicate.test(stack)) {
      return super.insertItem(slot, stack, simulate);
    }

    return stack;
  }
}

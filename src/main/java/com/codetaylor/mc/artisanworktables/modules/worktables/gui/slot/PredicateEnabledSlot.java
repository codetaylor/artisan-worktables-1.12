package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class PredicateEnabledSlot
    extends SlotItemHandler {

  public interface Predicate {

    boolean isEnabled();
  }

  private final Predicate predicate;

  public PredicateEnabledSlot(
      Predicate predicate,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.predicate = predicate;
  }

  @Nonnull
  @Override
  public ItemStack getStack() {

    if (this.isEnabled()) {
      return super.getStack();
    }

    return ItemStack.EMPTY;
  }

  @Override
  public void putStack(@Nonnull ItemStack stack) {

    if (this.isEnabled()) {
      super.putStack(stack);
    }
  }

  @Override
  public void onSlotChange(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {

    if (this.isEnabled()) {
      super.onSlotChange(p_75220_1_, p_75220_2_);
    }
  }

  @Override
  public int getSlotStackLimit() {

    if (this.isEnabled()) {
      return super.getSlotStackLimit();
    }

    return 0;
  }

  @Override
  public int getItemStackLimit(@Nonnull ItemStack stack) {

    if (this.isEnabled()) {
      return super.getItemStackLimit(stack);
    }

    return 0;
  }

  @Override
  public boolean canTakeStack(EntityPlayer playerIn) {

    if (this.isEnabled()) {
      return super.canTakeStack(playerIn);
    }

    return false;
  }

  @Nonnull
  @Override
  public ItemStack decrStackSize(int amount) {

    if (this.isEnabled()) {
      return super.decrStackSize(amount);
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean isSameInventory(Slot other) {

    if (this.isEnabled()) {
      return super.isSameInventory(other);
    }

    return false;
  }

  @Override
  public boolean isEnabled() {

    return this.predicate.isEnabled();
  }
}

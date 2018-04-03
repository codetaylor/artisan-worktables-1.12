package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class PatternResultSlot
    extends PredicateEnabledSlot {

  private final Runnable slotChangeListener;

  public PatternResultSlot(
      Predicate predicate,
      Runnable slotChangeListener,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(predicate, itemHandler, index, xPosition, yPosition);
    this.slotChangeListener = slotChangeListener;
  }

  @Override
  public ItemStack onTake(
      EntityPlayer player, ItemStack stack
  ) {

    this.slotChangeListener.run();
    return stack;
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return false;
  }
}

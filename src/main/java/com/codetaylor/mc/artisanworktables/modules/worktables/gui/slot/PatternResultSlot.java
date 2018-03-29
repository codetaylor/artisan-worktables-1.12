package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class PatternResultSlot
    extends ResultSlot {

  private final Runnable slotChangeListener;

  public PatternResultSlot(
      Runnable slotChangeListener,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.slotChangeListener = slotChangeListener;
  }

  @Override
  public ItemStack onTake(
      EntityPlayer player, ItemStack stack
  ) {

    this.slotChangeListener.run();
    return stack;
  }
}

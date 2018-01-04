package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CraftingIngredientSlot
    extends SlotItemHandler {

  private final Runnable slotChangeListener;

  public CraftingIngredientSlot(
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
  public void onSlotChanged() {

    super.onSlotChanged();

    this.slotChangeListener.run();
  }
}

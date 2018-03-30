package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class PatternSlot
    extends SlotItemHandler {

  private final Runnable slotChangeListener;

  public PatternSlot(
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
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return (stack.getItem() == ModuleWorktables.Items.DESIGN_PATTERN)
        && (!stack.hasTagCompound());
  }

  @Override
  public void onSlotChanged() {

    super.onSlotChanged();
    this.slotChangeListener.run();
  }
}

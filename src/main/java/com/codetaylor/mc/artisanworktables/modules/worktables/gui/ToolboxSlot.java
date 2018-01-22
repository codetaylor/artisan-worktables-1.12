package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ToolboxSlot
    extends SlotItemHandler {

  private final TileEntityToolbox tile;
  private final Runnable slotChangeListener;

  public ToolboxSlot(
      TileEntityToolbox tile,
      Runnable slotChangeListener,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.tile = tile;
    this.slotChangeListener = slotChangeListener;
  }

  @Nonnull
  @Override
  public ItemStack getStack() {

    if (!this.tile.isInvalid()) {
      return super.getStack();
    }

    return ItemStack.EMPTY;
  }

  @Override
  public void putStack(@Nonnull ItemStack stack) {

    if (!this.tile.isInvalid()) {
      super.putStack(stack);
    }
  }

  @Override
  public void onSlotChange(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {

    if (!this.tile.isInvalid()) {
      super.onSlotChange(p_75220_1_, p_75220_2_);
    }
  }

  @Override
  public int getSlotStackLimit() {

    if (!this.tile.isInvalid()) {
      return super.getSlotStackLimit();
    }

    return 0;
  }

  @Override
  public int getItemStackLimit(@Nonnull ItemStack stack) {

    if (!this.tile.isInvalid()) {
      return super.getItemStackLimit(stack);
    }

    return 0;
  }

  @Override
  public boolean canTakeStack(EntityPlayer playerIn) {

    if (!this.tile.isInvalid()) {
      return super.canTakeStack(playerIn);
    }

    return false;
  }

  @Nonnull
  @Override
  public ItemStack decrStackSize(int amount) {

    if (!this.tile.isInvalid()) {
      return super.decrStackSize(amount);
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean isSameInventory(Slot other) {

    if (!this.tile.isInvalid()) {
      return super.isSameInventory(other);
    }

    return false;
  }

  @Override
  public boolean isEnabled() {

    return !this.tile.isInvalid();
  }

  @Override
  public void onSlotChanged() {

    super.onSlotChanged();

    this.slotChangeListener.run();
  }
}

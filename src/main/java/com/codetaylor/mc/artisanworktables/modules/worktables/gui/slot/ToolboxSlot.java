package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ToolboxSlot
    extends SlotItemHandler {

  private final Container container;

  public ToolboxSlot(
      Container container,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.container = container;
  }

  @Nonnull
  @Override
  public ItemStack getStack() {

    if (this.container.canPlayerUseToolbox()) {
      return super.getStack();
    }

    return ItemStack.EMPTY;
  }

  @Override
  public void putStack(@Nonnull ItemStack stack) {

    if (this.container.canPlayerUseToolbox()) {
      super.putStack(stack);
    }
  }

  @Override
  public void onSlotChange(@Nonnull ItemStack p_75220_1_, @Nonnull ItemStack p_75220_2_) {

    if (this.container.canPlayerUseToolbox()) {
      super.onSlotChange(p_75220_1_, p_75220_2_);
    }
  }

  @Override
  public int getSlotStackLimit() {

    if (this.container.canPlayerUseToolbox()) {
      return super.getSlotStackLimit();
    }

    return 0;
  }

  @Override
  public int getItemStackLimit(@Nonnull ItemStack stack) {

    if (this.container.canPlayerUseToolbox()) {
      return super.getItemStackLimit(stack);
    }

    return 0;
  }

  @Override
  public boolean canTakeStack(EntityPlayer playerIn) {

    if (this.container.canPlayerUseToolbox()) {
      return super.canTakeStack(playerIn);
    }

    return false;
  }

  @Nonnull
  @Override
  public ItemStack decrStackSize(int amount) {

    if (this.container.canPlayerUseToolbox()) {
      return super.decrStackSize(amount);
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean isSameInventory(Slot other) {

    if (this.container.canPlayerUseToolbox()) {
      return super.isSameInventory(other);
    }

    return false;
  }

  @Override
  public boolean isEnabled() {

    return this.container.canPlayerUseToolbox();
  }
}

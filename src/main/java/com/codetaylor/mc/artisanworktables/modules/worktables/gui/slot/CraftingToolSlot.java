package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.inventory.PredicateSlotItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class CraftingToolSlot
    extends PredicateSlotItemHandler
    implements ICreativeSlotClick {

  private final TileEntityBase tile;
  private final Runnable slotChangeListener;

  public CraftingToolSlot(
      TileEntityBase tile,
      Runnable slotChangeListener,
      Predicate<ItemStack> predicate,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(predicate, itemHandler, index, xPosition, yPosition);
    this.tile = tile;
    this.slotChangeListener = slotChangeListener;
  }

  @Override
  public void onSlotChanged() {

    super.onSlotChanged();

    this.slotChangeListener.run();
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return !this.tile.isCreative()
        && super.isItemValid(stack);
  }

  @Override
  public boolean canTakeStack(EntityPlayer player) {

    return !this.tile.isCreative()
        && super.canTakeStack(player);
  }

  @Override
  public ItemStack creativeSlotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {

    ItemStack copy = player.inventory.getItemStack().copy();
    copy.setCount(1);
    this.putStack(copy);
    return ItemStack.EMPTY;
  }
}

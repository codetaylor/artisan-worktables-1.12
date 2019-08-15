package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.lib.RoundRobinHelper;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class RoundRobinGhostStackHandler
    implements IItemHandler {

  private final TileEntityBase tile;
  private final IItemHandler stackHandler;
  private final IItemHandler ghostStackHandler;

  public RoundRobinGhostStackHandler(TileEntityBase tile, IItemHandler stackHandler, IItemHandler ghostStackHandler) {

    this.tile = tile;
    this.stackHandler = stackHandler;
    this.ghostStackHandler = ghostStackHandler;
  }

  @Override
  public int getSlots() {

    return this.stackHandler.getSlots();
  }

  @Nonnull
  @Override
  public ItemStack getStackInSlot(int slot) {

    return this.stackHandler.getStackInSlot(slot);
  }

  @Nonnull
  @Override
  public ItemStack insertItem(int slot, @Nonnull ItemStack itemStack, boolean simulate) {

    IItemHandler stackHandler;
    IItemHandler ghostStackHandler;

    if (simulate) {
      stackHandler = RoundRobinHelper.copyItemHandler(this.stackHandler);

    } else {
      stackHandler = this.stackHandler;
    }

    ghostStackHandler = this.ghostStackHandler;

    ItemStack safeItemStack = itemStack.copy();
    int count = safeItemStack.getCount();

    for (int i = 0; i < count; i++) {
      List<Tuple> list = RoundRobinHelper.getSortedIndices(safeItemStack, stackHandler, ghostStackHandler);

      if (list.isEmpty()) {
        return safeItemStack;
      }

      ItemStack copy = safeItemStack.copy();
      copy.setCount(1);
      int index = (int) list.get(0).getFirst();
      ItemStack result = stackHandler.insertItem(index, copy, simulate);

      if (result.isEmpty()) {
        safeItemStack.shrink(1);

      } else {
        return safeItemStack;
      }
    }

    return safeItemStack;
  }

  @Nonnull
  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {

    return ItemStack.EMPTY;
  }

  @Override
  public int getSlotLimit(int slot) {

    return this.stackHandler.getSlotLimit(slot);
  }
}

package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityWorktableBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CraftingResultSlot
    extends SlotItemHandler {

  private final TileEntityWorktableBase tile;

  public CraftingResultSlot(
      TileEntityWorktableBase tile,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.tile = tile;
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return false;
  }

  @Override
  public ItemStack onTake(
      EntityPlayer player, ItemStack stack
  ) {

    this.tile.onTakeResult(player);
    return stack;
  }
}

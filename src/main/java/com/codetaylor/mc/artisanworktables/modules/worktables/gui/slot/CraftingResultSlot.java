package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CraftingResultSlot
    extends SlotItemHandler
    implements ICreativeSlotClick {

  private final Runnable slotChangeListener;
  private final TileEntityBase tile;

  public CraftingResultSlot(
      Runnable slotChangeListener,
      TileEntityBase tile,
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
  public ItemStack onTake(
      EntityPlayer player, @Nonnull ItemStack stack
  ) {

    this.tile.onTakeResult(player);
    this.slotChangeListener.run();
    return stack;
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return this.tile.isCreative();
  }

  @Override
  public boolean canTakeStack(EntityPlayer player) {

    return !this.tile.isCreative()
        && super.canTakeStack(player);
  }

  @Override
  public ItemStack creativeSlotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {

    this.putStack(player.inventory.getItemStack().copy());
    return ItemStack.EMPTY;
  }
}

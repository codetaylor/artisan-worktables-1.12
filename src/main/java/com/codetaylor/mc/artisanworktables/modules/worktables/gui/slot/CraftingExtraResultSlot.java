package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class CraftingExtraResultSlot
    extends ResultSlot
    implements ICreativeSlotClick {

  private final TileEntityBase tile;

  public CraftingExtraResultSlot(
      TileEntityBase tile,
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

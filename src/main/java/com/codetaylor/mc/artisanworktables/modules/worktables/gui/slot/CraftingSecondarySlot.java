package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.AWContainer;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class CraftingSecondarySlot
    extends SlotItemHandler
    implements ICreativeSlotClick {

  private final AWContainer container;
  private final TileEntityBase tile;
  private final Runnable slotChangeListener;

  public CraftingSecondarySlot(
      AWContainer container,
      TileEntityBase tile,
      Runnable slotChangeListener,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.container = container;
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
  public ItemStack creativeSlotClick(int slotId, int dragType, ClickType clickType, EntityPlayer player) {

    ItemStack stack = this.getStack();

    if (!OreDictSlotClickDelegate.slotClick(this.container.inventorySlots, slotId, stack, this.tile.oreDictMap, clickType, player.world.isRemote, this.tile.isOreDictLinked())) {
      this.putStack(player.inventory.getItemStack().copy());
      this.tile.oreDictMap.removeObject(slotId);
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean allowOredict() {

    return true;
  }
}

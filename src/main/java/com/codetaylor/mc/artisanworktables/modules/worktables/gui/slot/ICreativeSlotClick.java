package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public interface ICreativeSlotClick {

  ItemStack creativeSlotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player);
}

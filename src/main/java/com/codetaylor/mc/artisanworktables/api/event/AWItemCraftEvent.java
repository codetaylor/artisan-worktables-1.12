package com.codetaylor.mc.artisanworktables.api.event;

import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * This event is fired immediately after a player successfully crafts an item
 * in an Artisan Worktable. All values are immutable; making changes to any of
 * the values provided by the event will have no effect on the outcome of the
 * crafting process.
 * <p>
 * This event is only fired on the server.
 */
public class AWItemCraftedEvent
    extends PlayerEvent {

  private final EnumType tableType;
  private final EnumTier tableTier;
  private final ItemStack craftedItem;

  public AWItemCraftedEvent(EntityPlayer player, EnumType tableType, EnumTier tableTier, ItemStack craftedItem) {

    super(player);
    this.tableType = tableType;
    this.tableTier = tableTier;
    this.craftedItem = craftedItem;
  }

  @Override
  public boolean isCancelable() {

    return false;
  }

  /**
   * @return the tier of the table used to craft
   */
  public EnumTier getTableTier() {

    return this.tableTier;
  }

  /**
   * @return the type of the table used to craft
   */
  public EnumType getTableType() {

    return this.tableType;
  }

  /**
   * @return a copy of the item crafted
   */
  public ItemStack getCraftedItem() {

    return this.craftedItem;
  }
}

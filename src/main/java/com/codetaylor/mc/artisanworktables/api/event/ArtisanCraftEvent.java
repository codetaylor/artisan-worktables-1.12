package com.codetaylor.mc.artisanworktables.api.event;

import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public abstract class ArtisanCraftEvent
    extends PlayerEvent {

  private final EnumType tableType;
  private final EnumTier tableTier;

  public ArtisanCraftEvent(EntityPlayer player, EnumType tableType, EnumTier tableTier) {

    super(player);
    this.tableType = tableType;
    this.tableTier = tableTier;
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
   * This event is fired immediately after a player successfully crafts an item
   * in an Artisan Worktable. All values are immutable; making changes to any of
   * the values provided by the event will have no effect on the outcome of the
   * crafting process.
   * <p>
   * This event is only fired on the server.
   */
  public static class Post
      extends ArtisanCraftEvent {

    private final ItemStack craftedItem;
    private final List<ItemStack> secondaryOutputItemList;

    @ParametersAreNonnullByDefault
    public Post(
        EntityPlayer player,
        EnumType tableType,
        EnumTier tableTier,
        ItemStack craftedItem,
        List<ItemStack> secondaryOutputItemList
    ) {

      super(player, tableType, tableTier);
      this.craftedItem = craftedItem;
      this.secondaryOutputItemList = secondaryOutputItemList;
    }

    /**
     * @return a copy of the item crafted
     */
    @Nonnull
    public ItemStack getCraftedItem() {

      return this.craftedItem;
    }

    /**
     * @return a list containing copies of the secondary output items
     */
    @Nonnull
    public List<ItemStack> getSecondaryOutputItemList() {

      return this.secondaryOutputItemList;
    }
  }
}

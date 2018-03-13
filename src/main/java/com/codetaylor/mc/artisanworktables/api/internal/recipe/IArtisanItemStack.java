package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IArtisanItemStack {

  int getAmount();

  Item getItem();

  boolean isEmpty();

  /**
   * Returns a copy of the internal item stack object.
   * <p>
   * Modifications to the returned {@link ItemStack} should not modify the internal object.
   *
   * @return a copy of the internal item stack object
   */
  ItemStack toItemStack();

  IArtisanItemStack copy();

}

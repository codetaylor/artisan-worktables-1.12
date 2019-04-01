package com.codetaylor.mc.artisanworktables.api.recipe;

import net.minecraft.item.ItemStack;

import java.util.Random;

public class DefaultToolHandler
    implements IToolHandler {

  @Override
  public boolean matches(ItemStack itemStack) {

    return itemStack.isItemStackDamageable();
  }

  @Override
  public boolean canAcceptAllDamage(ItemStack itemStack, int damage) {

    return (itemStack.getItemDamage() + damage <= itemStack.getMaxDamage());
  }

  @Override
  public boolean applyDamage(ItemStack itemStack, int damage, boolean simulate) {

    if (simulate) {
      return (itemStack.getItemDamage() + damage > itemStack.getMaxDamage());

    } else {
      boolean broken = itemStack.attemptDamageItem(damage, new Random(), null)
          || itemStack.getItemDamage() == itemStack.getMaxDamage();

      if (broken) {
        itemStack.shrink(1);
      }

      return broken;
    }
  }
}

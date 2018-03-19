package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public interface IArtisanIngredient {

  boolean isEmpty();

  int getAmount();

  boolean matches(IArtisanItemStack itemStack);

  boolean matches(ItemStack itemStack);

  boolean matchesIgnoreAmount(IArtisanItemStack itemStack);

  boolean matchesIgnoreAmount(ItemStack itemStack);

  IArtisanItemStack[] getMatchingStacks();

  Ingredient toIngredient();

}

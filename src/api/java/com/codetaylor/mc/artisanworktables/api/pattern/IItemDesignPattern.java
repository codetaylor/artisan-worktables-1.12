package com.codetaylor.mc.artisanworktables.api.pattern;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IItemDesignPattern {

  boolean hasRecipe(ItemStack itemStack);

  @Nullable
  String getRecipeName(ItemStack itemStack);

  void setRecipeName(ItemStack itemStack, String recipeName);
}

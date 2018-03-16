package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArtisanIngredient
    implements IArtisanIngredient {

  public static final IArtisanIngredient EMPTY = new ArtisanIngredient(Ingredient.EMPTY);

  public static IArtisanIngredient from(@Nullable Ingredient ingredient) {

    if (ingredient == null) {
      return ArtisanIngredient.EMPTY;
    }

    return new ArtisanIngredient(ingredient);
  }

  private final Ingredient ingredient;

  private ArtisanIngredient(@Nonnull Ingredient ingredient) {

    this.ingredient = ingredient;
  }

  @Override
  public int getAmount() {

    return 1;
  }

  @Override
  public boolean matches(IArtisanItemStack itemStack) {

    return this.matches(itemStack.toItemStack());
  }

  @Override
  public boolean matches(ItemStack itemStack) {

    return this.ingredient.apply(itemStack);
  }

  @Override
  public boolean matchesIgnoreAmount(IArtisanItemStack itemStack) {

    return this.matchesIgnoreAmount(itemStack.toItemStack());
  }

  @Override
  public boolean matchesIgnoreAmount(ItemStack itemStack) {

    return this.ingredient.apply(itemStack);
  }

  @Override
  public IArtisanItemStack[] getMatchingStacks() {

    ItemStack[] matchingStacks = this.ingredient.getMatchingStacks();
    IArtisanItemStack[] result = new IArtisanItemStack[matchingStacks.length];

    for (int i = 0; i < matchingStacks.length; i++) {
      result[i] = ArtisanItemStack.from(matchingStacks[i]);
    }

    return result;
  }

  @Override
  public Ingredient toIngredient() {

    return this.ingredient;
  }
}

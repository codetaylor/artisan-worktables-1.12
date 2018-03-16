package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class InputReplacementEntry {

  private final IArtisanIngredient toReplace;
  private final IArtisanIngredient replacement;

  public InputReplacementEntry(@Nullable IArtisanIngredient toReplace, @Nullable IArtisanIngredient replacement) {

    this.toReplace = toReplace;
    this.replacement = replacement;
  }

  public boolean matches(IArtisanIngredient toMatch) {

    if (this.toReplace == null) {

      return (toMatch == null || toMatch == ArtisanIngredient.EMPTY || toMatch.toIngredient() == Ingredient.EMPTY);
    }

    for (IArtisanItemStack itemStack : toMatch.getMatchingStacks()) {

      if (this.toReplace.matches(itemStack)) {
        return true;
      }
    }

    return false;
  }

  public IArtisanIngredient getReplacement() {

    return this.replacement;
  }
}

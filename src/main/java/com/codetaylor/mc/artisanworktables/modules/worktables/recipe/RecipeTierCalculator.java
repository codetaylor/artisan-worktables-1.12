package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;

import javax.annotation.Nullable;

public class RecipeTierCalculator {

  @Nullable
  public static EnumTier calculateTier(
      int width,
      int height,
      int toolCount,
      int secondaryIngredientCount
  ) {

    // test for tier one requirements
    if (width <= 3
        && height <= 3
        && toolCount <= 1
        && secondaryIngredientCount == 0) {
      return EnumTier.WORKTABLE;
    }

    // test for tier two requirements
    if (width <= 3
        && height <= 3
        && toolCount <= 2) {
      return EnumTier.WORKSTATION;
    }

    // test for tier three requirements
    if (width <= 5
        && height <= 5
        && toolCount <= 3) {
      return EnumTier.WORKSHOP;
    }

    return null;
  }

  private RecipeTierCalculator() {
    //
  }

}

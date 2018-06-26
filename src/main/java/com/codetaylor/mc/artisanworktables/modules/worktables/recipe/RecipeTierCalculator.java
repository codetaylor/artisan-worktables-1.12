package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class RecipeTierCalculator {

  @Nullable
  public static EnumTier calculateTier(
      String tableName,
      int width,
      int height,
      int toolCount,
      int secondaryIngredientCount,
      FluidStack fluidIngredient
  ) {

    // test for tier one requirements
    if (width <= 3
        && height <= 3
        && toolCount <= 1
        && secondaryIngredientCount == 0) {

      if (fluidIngredient == null
          || fluidIngredient.amount <= ModuleWorktablesConfig.FLUID_CAPACITY_WORKTABLE.get(tableName)) {
        return EnumTier.WORKTABLE;
      }
    }

    // test for tier two requirements
    if (width <= 3
        && height <= 3
        && toolCount <= 2) {

      if (fluidIngredient == null
          || fluidIngredient.amount <= ModuleWorktablesConfig.FLUID_CAPACITY_WORKSTATION.get(tableName)) {
        return EnumTier.WORKSTATION;
      }
    }

    // test for tier three requirements
    if (width <= 5
        && height <= 5
        && toolCount <= 3) {

      if (fluidIngredient == null
          || fluidIngredient.amount <= ModuleWorktablesConfig.FLUID_CAPACITY_WORKSHOP.get(tableName)) {
        return EnumTier.WORKSHOP;
      }
    }

    return null;
  }

  private RecipeTierCalculator() {
    //
  }

}

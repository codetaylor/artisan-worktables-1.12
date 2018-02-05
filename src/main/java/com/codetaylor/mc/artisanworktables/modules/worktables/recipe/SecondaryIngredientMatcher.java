package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;

import java.util.Collection;
import java.util.List;

public class SecondaryIngredientMatcher
    implements ISecondaryIngredientMatcher {

  private final int[] availableAmounts;
  private final List<IItemStack> inputs;

  public SecondaryIngredientMatcher(List<IItemStack> inputs) {

    this.inputs = inputs;
    this.availableAmounts = new int[this.inputs.size()];
  }

  @Override
  public boolean matches(Collection<IIngredient> requiredIngredients) {

    for (int i = 0; i < this.inputs.size(); i++) {
      this.availableAmounts[i] = this.inputs.get(i).getAmount();
    }

    for (IIngredient recipeInput : requiredIngredients) {
      int amountRequired = recipeInput.getAmount();

      for (int i = 0; i < this.inputs.size(); i++) {
        IItemStack input = this.inputs.get(i);

        if (recipeInput.matches(input)) {

          if (this.availableAmounts[i] >= amountRequired) {
            // more ingredients are available in this stack than are required
            // adjust and break
            this.availableAmounts[i] -= amountRequired;
            amountRequired = 0;
            break;

          } else {
            // there aren't enough ingredients available to satisfy the entire requirement
            // adjust and keep looking
            amountRequired -= this.availableAmounts[i];
            this.availableAmounts[i] = 0;
          }
        }
      }

      if (amountRequired > 0) {
        // the requirements were not met
        return false;
      }
    }

    return true;
  }
}

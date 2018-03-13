package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanIngredient;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanItemStack;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ISecondaryIngredientMatcher;

import java.util.Collection;
import java.util.List;

public class SecondaryIngredientMatcher
    implements ISecondaryIngredientMatcher {

  private final int[] availableAmounts;
  private final List<IArtisanItemStack> inputs;

  public SecondaryIngredientMatcher(List<IArtisanItemStack> inputs) {

    this.inputs = inputs;
    this.availableAmounts = new int[this.inputs.size()];
  }

  @Override
  public boolean matches(Collection<IArtisanIngredient> requiredIngredients) {

    for (int i = 0; i < this.inputs.size(); i++) {
      IArtisanItemStack iItemStack = this.inputs.get(i);
      this.availableAmounts[i] = (iItemStack != null) ? iItemStack.getAmount() : 0;
    }

    for (IArtisanIngredient recipeInput : requiredIngredients) {
      int amountRequired = recipeInput.getAmount();

      // Set the amount to 1 to avoid quantity discrepancies when matching
      //IIngredient toMatch = recipeInput.amount(1);

      for (int i = 0; i < this.inputs.size(); i++) {
        IArtisanItemStack input = this.inputs.get(i);

        if (input == null) {
          continue;
        }

        // Set the amount to 1 to avoid quantity discrepancies when matching
        //input = input.amount(1);

        if (recipeInput.matchesIgnoreAmount(input)) {

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

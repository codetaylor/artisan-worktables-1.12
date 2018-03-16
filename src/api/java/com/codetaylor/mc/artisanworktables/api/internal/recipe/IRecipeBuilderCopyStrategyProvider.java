package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import crafttweaker.api.recipes.ICraftingRecipe;

public interface IRecipeBuilderCopyStrategyProvider {

  IRecipeBuilderCopyStrategyProvider NO_OP = new IRecipeBuilderCopyStrategyProvider() {

    @Override
    public IRecipeBuilderCopyStrategy byName(String recipeName) {

      return RecipeBuilderCopyStrategyNoOp.INSTANCE;
    }

    @Override
    public IRecipeBuilderCopyStrategy byRecipe(ICraftingRecipe recipe) {

      return RecipeBuilderCopyStrategyNoOp.INSTANCE;
    }

    @Override
    public IRecipeBuilderCopyStrategy byOutput(IArtisanIngredient[] outputs) {

      return RecipeBuilderCopyStrategyNoOp.INSTANCE;
    }
  };

  IRecipeBuilderCopyStrategy byName(String recipeName);

  IRecipeBuilderCopyStrategy byRecipe(ICraftingRecipe recipe);

  IRecipeBuilderCopyStrategy byOutput(IArtisanIngredient[] outputs);

}

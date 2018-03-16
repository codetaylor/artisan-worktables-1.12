package com.codetaylor.mc.artisanworktables.api.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import crafttweaker.api.recipes.ICraftingRecipe;

public class RecipeBuilder {

  private static final IRecipeBuilderProvider RECIPE_BUILDER_PROVIDER = IRecipeBuilderProvider.NO_OP;

  public static IRecipeBuilder get(String tableName) throws RecipeBuilderException {

    return RECIPE_BUILDER_PROVIDER.get(tableName, IRecipeFactory.DEFAULT);
  }

  public static IRecipeBuilder get(String tableName, IRecipeFactory recipeFactory) throws RecipeBuilderException {

    return RECIPE_BUILDER_PROVIDER.get(tableName, recipeFactory);
  }

  public static class Copy {

    private static final IRecipeBuilderCopyStrategyProvider RECIPE_BUILDER_COPY_STRATEGY_PROVIDER = IRecipeBuilderCopyStrategyProvider.NO_OP;

    public static IRecipeBuilderCopyStrategy byName(String recipeName) {

      return RECIPE_BUILDER_COPY_STRATEGY_PROVIDER.byName(recipeName);
    }

    public static IRecipeBuilderCopyStrategy byRecipe(ICraftingRecipe recipe) {

      return RECIPE_BUILDER_COPY_STRATEGY_PROVIDER.byRecipe(recipe);
    }

    public static IRecipeBuilderCopyStrategy byOutput(IArtisanIngredient[] outputs) {

      return RECIPE_BUILDER_COPY_STRATEGY_PROVIDER.byOutput(outputs);
    }

    private Copy() {
      //
    }

  }

  private RecipeBuilder() {
    //
  }

}

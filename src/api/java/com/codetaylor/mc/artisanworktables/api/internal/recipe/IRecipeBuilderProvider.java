package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.recipe.IRecipeFactory;

public interface IRecipeBuilderProvider {

  IRecipeBuilderProvider NO_OP = (tableName, recipeFactory) -> RecipeBuilderNoOp.INSTANCE;

  IRecipeBuilder get(String tableName, IRecipeFactory recipeFactory) throws RecipeBuilderException;

}

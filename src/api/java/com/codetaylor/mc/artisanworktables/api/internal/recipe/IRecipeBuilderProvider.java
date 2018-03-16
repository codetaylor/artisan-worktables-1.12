package com.codetaylor.mc.artisanworktables.api.internal.recipe;

public interface IRecipeBuilderProvider {

  IRecipeBuilderProvider NO_OP = tableName -> RecipeBuilderNoOp.INSTANCE;

  IRecipeBuilder get(String tableName) throws RecipeBuilderException;

}

package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilder;

public interface IRecipeBuilderCopyStrategy
    extends IZenRecipeBuilderCopyStrategy {

  boolean isExcludeInput();

  boolean isExcludeOutput();

  boolean isValid();

  void onCreate(String tableName, RecipeBuilder builder);

}

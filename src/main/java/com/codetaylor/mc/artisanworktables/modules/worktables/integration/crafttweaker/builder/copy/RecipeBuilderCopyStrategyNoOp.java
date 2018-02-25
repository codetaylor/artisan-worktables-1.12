package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.IZenRecipeBuilderCopyStrategy;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilder;
import crafttweaker.api.item.IItemStack;

public class RecipeBuilderCopyStrategyNoOp
    implements IRecipeBuilderCopyStrategy {

  public static final IZenRecipeBuilderCopyStrategy INSTANCE = new RecipeBuilderCopyStrategyNoOp();

  @Override
  public IZenRecipeBuilderCopyStrategy excludeInput() {

    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy excludeOutput() {

    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceOutput(IItemStack replacement) {

    return this;
  }

  @Override
  public boolean isExcludeInput() {

    return true;
  }

  @Override
  public boolean isExcludeOutput() {

    return true;
  }

  @Override
  public boolean isValid() {

    return false;
  }

  @Override
  public void onCreate(
      String tableName,
      RecipeBuilder builder
  ) {
    //
  }
}

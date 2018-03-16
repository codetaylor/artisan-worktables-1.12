package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import javax.annotation.Nullable;

public class RecipeBuilderCopyStrategyNoOp
    implements IRecipeBuilderCopyStrategy {

  public static final IRecipeBuilderCopyStrategy INSTANCE = new RecipeBuilderCopyStrategyNoOp();

  @Override
  public IRecipeBuilderCopyStrategy noInput() {

    return this;
  }

  @Override
  public IRecipeBuilderCopyStrategy noOutput() {

    return this;
  }

  @Override
  public IRecipeBuilderCopyStrategy replaceInput(
      @Nullable IArtisanIngredient toReplace, @Nullable IArtisanIngredient replacement
  ) {

    return this;
  }

  @Override
  public IRecipeBuilderCopyStrategy replaceShapedInput(
      int col, int row, @Nullable IArtisanIngredient replacement
  ) {

    return this;
  }

  @Override
  public IRecipeBuilderCopyStrategy replaceOutput(IArtisanItemStack replacement) {

    return this;
  }
}

package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;

import javax.annotation.Nullable;

public class ZenRecipeBuilderCopyStrategyNoOp
    implements IZenRecipeBuilderCopyStrategy {

  public static final IZenRecipeBuilderCopyStrategy INSTANCE = new ZenRecipeBuilderCopyStrategyNoOp();

  @Override
  public IZenRecipeBuilderCopyStrategy noInput() {

    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy noOutput() {

    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceInput(
      @Nullable IIngredient toReplace,
      @Nullable IIngredient replacement
  ) {

    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceShapedInput(int col, int row, @Nullable IIngredient replacement) {

    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceOutput(IItemStack replacement) {

    return this;
  }

}

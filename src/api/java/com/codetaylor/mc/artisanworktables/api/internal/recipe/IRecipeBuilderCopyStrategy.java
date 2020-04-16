package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import javax.annotation.Nullable;

@SuppressWarnings("UnusedReturnValue")
public interface IRecipeBuilderCopyStrategy {

  IRecipeBuilderCopyStrategy runAfter();

  IRecipeBuilderCopyStrategy noInput();

  IRecipeBuilderCopyStrategy noOutput();

  IRecipeBuilderCopyStrategy replaceInput(
      @Nullable IArtisanIngredient toReplace,
      @Nullable IArtisanIngredient replacement
  );

  IRecipeBuilderCopyStrategy replaceShapedInput(int col, int row, @Nullable IArtisanIngredient replacement);

  IRecipeBuilderCopyStrategy replaceOutput(IArtisanItemStack replacement);

}

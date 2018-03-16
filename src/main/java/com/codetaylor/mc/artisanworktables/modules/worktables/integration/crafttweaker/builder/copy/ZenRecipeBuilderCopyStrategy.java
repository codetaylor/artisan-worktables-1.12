package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IRecipeBuilderCopyStrategy;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTArtisanIngredient;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTArtisanItemStack;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;

import javax.annotation.Nullable;

public class ZenRecipeBuilderCopyStrategy
    implements IZenRecipeBuilderCopyStrategy {

  private final IRecipeBuilderCopyStrategy strategy;

  public ZenRecipeBuilderCopyStrategy(IRecipeBuilderCopyStrategy strategy) {

    this.strategy = strategy;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy noInput() {

    this.strategy.noInput();
    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy noOutput() {

    this.strategy.noOutput();
    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceInput(
      @Nullable IIngredient toReplace, @Nullable IIngredient replacement
  ) {

    this.strategy.replaceInput(
        CTArtisanIngredient.from(toReplace),
        CTArtisanIngredient.from(replacement)
    );
    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceShapedInput(
      int col, int row, @Nullable IIngredient replacement
  ) {

    this.strategy.replaceShapedInput(
        col,
        row,
        CTArtisanIngredient.from(replacement)
    );
    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceOutput(IItemStack replacement) {

    this.strategy.replaceOutput(CTArtisanItemStack.from(replacement));
    return this;
  }

  public IRecipeBuilderCopyStrategy getStrategy() {

    return this.strategy;
  }
}

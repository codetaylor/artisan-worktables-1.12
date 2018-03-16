package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTArtisanIngredient;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.ICraftingRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

@ZenClass("mods.artisanworktables.builder.Copy")
@SuppressWarnings("UnusedReturnValue")
public interface IZenRecipeBuilderCopyStrategy {

  @ZenMethod
  static IZenRecipeBuilderCopyStrategy byName(String recipeName) {

    try {
      return new ZenRecipeBuilderCopyStrategy(RecipeBuilderInternal.Copy.byName(recipeName));

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      return ZenRecipeBuilderCopyStrategyNoOp.INSTANCE;
    }
  }

  @ZenMethod
  static IZenRecipeBuilderCopyStrategy byRecipe(ICraftingRecipe recipe) {

    try {
      return new ZenRecipeBuilderCopyStrategy(RecipeBuilderInternal.Copy.byRecipe(recipe));

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      return ZenRecipeBuilderCopyStrategyNoOp.INSTANCE;
    }
  }

  @ZenMethod
  static IZenRecipeBuilderCopyStrategy byOutput(IIngredient[] outputs) {

    try {
      return new ZenRecipeBuilderCopyStrategy(RecipeBuilderInternal.Copy.byOutput(CTArtisanIngredient.fromArray(outputs)));

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      return ZenRecipeBuilderCopyStrategyNoOp.INSTANCE;
    }
  }

  @ZenMethod
  IZenRecipeBuilderCopyStrategy noInput();

  @ZenMethod
  IZenRecipeBuilderCopyStrategy noOutput();

  @ZenMethod
  IZenRecipeBuilderCopyStrategy replaceInput(@Nullable IIngredient toReplace, @Nullable IIngredient replacement);

  @ZenMethod
  IZenRecipeBuilderCopyStrategy replaceShapedInput(int col, int row, @Nullable IIngredient replacement);

  @ZenMethod
  IZenRecipeBuilderCopyStrategy replaceOutput(IItemStack replacement);

}

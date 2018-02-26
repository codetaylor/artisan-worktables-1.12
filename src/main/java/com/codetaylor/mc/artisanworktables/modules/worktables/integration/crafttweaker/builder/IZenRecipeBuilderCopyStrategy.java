package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder;

import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy.RecipeBuilderCopyStrategyByName;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy.RecipeBuilderCopyStrategyByOutput;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy.RecipeBuilderCopyStrategyByRecipe;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy.RecipeBuilderCopyStrategyNoOp;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderException;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.ICraftingRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.builder.Copy")
@SuppressWarnings("UnusedReturnValue")
public interface IZenRecipeBuilderCopyStrategy {

  @ZenMethod
  static IZenRecipeBuilderCopyStrategy byName(String recipeName) {

    try {
      return new RecipeBuilderCopyStrategyByName(recipeName);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      return RecipeBuilderCopyStrategyNoOp.INSTANCE;
    }
  }

  @ZenMethod
  static IZenRecipeBuilderCopyStrategy byRecipe(ICraftingRecipe recipe) {

    try {
      return new RecipeBuilderCopyStrategyByRecipe(recipe);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      return RecipeBuilderCopyStrategyNoOp.INSTANCE;
    }
  }

  @ZenMethod
  static IZenRecipeBuilderCopyStrategy byOutput(IIngredient[] outputs) {

    try {
      return new RecipeBuilderCopyStrategyByOutput(outputs);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      return RecipeBuilderCopyStrategyNoOp.INSTANCE;
    }
  }

  @ZenMethod
  IZenRecipeBuilderCopyStrategy noInput();

  @ZenMethod
  IZenRecipeBuilderCopyStrategy noOutput();

  @ZenMethod
  IZenRecipeBuilderCopyStrategy replaceOutput(IItemStack replacement);

}

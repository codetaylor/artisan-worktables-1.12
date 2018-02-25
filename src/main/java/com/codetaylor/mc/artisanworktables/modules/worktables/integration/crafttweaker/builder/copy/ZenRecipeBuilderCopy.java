package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderException;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.recipes.ICraftingRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.builder.copy.Copy")
public class ZenRecipeBuilderCopy {

  @ZenMethod
  public static IZenRecipeBuilderCopyStrategy byName(String recipeName) {

    try {
      return new RecipeBuilderCopyStrategyByName(recipeName);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      return RecipeBuilderCopyStrategyNoOp.INSTANCE;
    }
  }

  @ZenMethod
  public static IZenRecipeBuilderCopyStrategy byRecipe(ICraftingRecipe recipe) {

    try {
      return new RecipeBuilderCopyStrategyByRecipe(recipe);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      return RecipeBuilderCopyStrategyNoOp.INSTANCE;
    }
  }

  @ZenMethod
  public static IZenRecipeBuilderCopyStrategy byOutput(IIngredient[] outputs) {

    try {
      return new RecipeBuilderCopyStrategyByOutput(outputs);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      return RecipeBuilderCopyStrategyNoOp.INSTANCE;
    }
  }

}

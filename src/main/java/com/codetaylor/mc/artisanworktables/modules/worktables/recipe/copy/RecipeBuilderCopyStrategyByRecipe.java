package com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.recipes.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public class RecipeBuilderCopyStrategyByRecipe
    extends RecipeBuilderCopyStrategyBase {

  private IRecipe recipe;

  public RecipeBuilderCopyStrategyByRecipe(ICraftingRecipe recipe) throws RecipeBuilderException {

    if (recipe == null) {
      throw new RecipeBuilderException("Recipe to copy can't be null");
    }

    if (!(recipe instanceof IRecipe)) {
      throw new RecipeBuilderException("Recipe is not an instance of IRecipe");
    }

    this.recipe = (IRecipe) recipe;
  }

  @Override
  public void apply(RecipeBuilderInternal recipeBuilder, List<RecipeBuilderInternal> resultList) {

    try {
      this.doCopy(this.recipe, recipeBuilder, resultList);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logError("Unable to copy and register recipe", e);
    }
  }

}

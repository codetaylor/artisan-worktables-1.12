package com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public class RecipeBuilderCopyStrategyByRecipe
    extends RecipeBuilderCopyStrategyBase {

  private IRecipe recipe;

  public RecipeBuilderCopyStrategyByRecipe(IRecipe recipe) throws RecipeBuilderException {

    if (recipe == null) {
      throw new RecipeBuilderException("Recipe to copy can't be null");
    }

    this.recipe = recipe;
  }

  @Override
  public void apply(RecipeBuilderInternal recipeBuilder, List<RecipeBuilderInternal> resultList) throws RecipeBuilderException {

    try {
      this.doCopy(this.recipe, recipeBuilder, resultList);

    } catch (Exception e) {
      throw new RecipeBuilderException("Unable to copy recipe by recipe: " + this.recipe.getRegistryName(), e);
    }
  }

}

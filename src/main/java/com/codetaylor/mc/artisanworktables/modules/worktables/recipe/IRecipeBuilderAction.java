package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;

public interface IRecipeBuilderAction {

  void apply() throws RecipeBuilderException;

}

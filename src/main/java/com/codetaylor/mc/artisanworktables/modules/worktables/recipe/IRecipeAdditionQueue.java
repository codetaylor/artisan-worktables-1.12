package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

public interface IRecipeAdditionQueue {

  void offer(RecipeBuilderInternal recipeBuilder);

  void offerWithCopy(RecipeBuilderInternal recipeBuilder);

}

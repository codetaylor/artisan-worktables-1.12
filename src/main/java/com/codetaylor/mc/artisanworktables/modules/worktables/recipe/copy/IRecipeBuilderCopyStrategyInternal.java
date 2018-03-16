package com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IRecipeBuilderCopyStrategy;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;

import java.util.List;

public interface IRecipeBuilderCopyStrategyInternal
    extends IRecipeBuilderCopyStrategy {

  boolean isExcludeInput();

  boolean isExcludeOutput();

  boolean isValid();

  void apply(RecipeBuilderInternal recipeBuilder, List<RecipeBuilderInternal> resultList) throws RecipeBuilderException;

}

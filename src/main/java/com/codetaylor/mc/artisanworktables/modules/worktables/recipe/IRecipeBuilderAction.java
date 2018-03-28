package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;

public interface IRecipeBuilderAction {

  interface ILogger {

    void logError(String message);

    void logError(String message, Throwable t);

    void logWarning(String message);
  }

  void apply(ILogger logger) throws RecipeBuilderException;

}

package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipeBuilderAction;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.utils.BaseUndoable;

public class CTActionAdd
    extends BaseUndoable {

  private static final IRecipeBuilderAction.ILogger LOGGER = new IRecipeBuilderAction.ILogger() {

    @Override
    public void logError(String message) {

      CTLogHelper.logError(message);
    }

    @Override
    public void logError(String message, Throwable t) {

      CTLogHelper.logError(message, t);
    }

    @Override
    public void logWarning(String message) {

      CTLogHelper.logWarning(message);
    }
  };

  private final String tableName;
  private final RecipeBuilderInternal recipeBuilder;

  /* package */ CTActionAdd(String tableName, RecipeBuilderInternal recipeBuilder) {

    super("RecipeWorktable");
    this.tableName = tableName;
    this.recipeBuilder = recipeBuilder;
  }

  @Override
  public void apply() {

    try {
      this.recipeBuilder.apply(LOGGER);

    } catch (Exception e) {
      CTLogHelper.logError("Unable to register recipe", e);
    }
  }

  @Override
  protected String getRecipeInfo() {

    return CTLogHelper.getStackDescription(this.tableName);
  }
}

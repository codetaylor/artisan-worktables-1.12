package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.utils.BaseUndoable;

public class CTActionAdd
    extends BaseUndoable {

  private final String tableName;
  private final RecipeBuilderInternal recipeBuilder;

  public CTActionAdd(String tableName, RecipeBuilderInternal recipeBuilder) {

    super("RecipeWorktable");
    this.tableName = tableName;
    this.recipeBuilder = recipeBuilder;
  }

  @Override
  public void apply() {

    try {
      this.recipeBuilder.apply();

    } catch (Exception e) {
      CTLogHelper.logError("Unable to register recipe", e);
    }
  }

  @Override
  protected String getRecipeInfo() {

    return CTLogHelper.getStackDescription(this.tableName);
  }
}

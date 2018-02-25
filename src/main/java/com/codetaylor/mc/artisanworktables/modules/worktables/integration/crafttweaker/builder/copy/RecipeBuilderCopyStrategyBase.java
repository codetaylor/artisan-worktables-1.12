package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.ZenWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilder;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderCopyHelper;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderException;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.crafting.IRecipe;

public abstract class RecipeBuilderCopyStrategyBase
    implements IRecipeBuilderCopyStrategy,
    IAction {

  protected String tableName;
  protected RecipeBuilder builder;
  protected boolean excludeInput;
  protected boolean excludeOutput;
  protected IItemStack replaceOutput;

  private boolean invalid;

  @Override
  public IZenRecipeBuilderCopyStrategy excludeInput() {

    if (this.excludeOutput) {
      CTLogHelper.logErrorFromZenMethod("Can't exclude both input and output from recipe copy");
      this.invalid = true;
      return this;
    }

    this.excludeInput = true;
    return this;
  }

  @Override
  public boolean isExcludeInput() {

    return this.excludeInput;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy excludeOutput() {

    if (this.excludeInput) {
      CTLogHelper.logErrorFromZenMethod("Can't exclude both input and output from recipe copy");
      this.invalid = true;
      return this;
    }

    if (this.replaceOutput != null) {
      CTLogHelper.logErrorFromZenMethod("Can't exclude output and replace output, one or the other");
      this.invalid = true;
      return this;
    }

    this.excludeOutput = true;
    return this;
  }

  @Override
  public boolean isExcludeOutput() {

    return this.excludeOutput;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceOutput(IItemStack replacement) throws RecipeBuilderException {

    if (replacement == null) {
      throw new RecipeBuilderException("Recipe output can't be null");
    }

    if (this.excludeOutput) {
      CTLogHelper.logErrorFromZenMethod("Can't exclude output and replace output, one or the other");
      this.invalid = true;
      return this;
    }

    this.replaceOutput = replacement;
    return this;
  }

  @Override
  public boolean isValid() {

    return !this.invalid;
  }

  @Override
  public void onCreate(String tableName, RecipeBuilder builder) {

    this.tableName = tableName;
    this.builder = builder;

    RecipeBuilderCopyHook.RECIPE_COPY_ACTION_LIST.add(this);
  }

  protected void doCopy(IRecipe recipe, RecipeBuilder builder) throws RecipeBuilderException {

    if (!this.excludeInput) {
      RecipeBuilderCopyHelper.copyRecipeInput(recipe, builder);
    }

    if (this.replaceOutput != null) {
      RecipeBuilderCopyHelper.replaceRecipeOutput(recipe, this.replaceOutput, builder);

    } else if (!this.excludeOutput) {
      RecipeBuilderCopyHelper.copyRecipeOutput(recipe, builder);
    }

    builder.validate();
    CraftTweaker.LATE_ACTIONS.add(new ZenWorktable.Add(this.tableName, builder));
  }
}

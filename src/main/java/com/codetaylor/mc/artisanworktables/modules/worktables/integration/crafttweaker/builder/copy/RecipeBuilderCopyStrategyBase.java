package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.InputReplacements;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTArtisanIngredient;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTArtisanItemStack;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.ZenWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.IZenRecipeBuilderCopyStrategy;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilder;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderCopyHelper;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderException;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.crafting.IRecipe;

import javax.annotation.Nullable;

public abstract class RecipeBuilderCopyStrategyBase
    implements IRecipeBuilderCopyStrategy,
    IAction {

  protected String tableName;
  protected RecipeBuilder builder;
  protected boolean excludeInput;
  protected boolean excludeOutput;
  protected IItemStack replaceOutput;
  protected InputReplacements inputReplacements;

  private boolean invalid;

  public RecipeBuilderCopyStrategyBase() {

    this.inputReplacements = InputReplacements.NO_OP;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy noInput() {

    if (this.excludeOutput) {
      return this.setInvalid("Can't exclude both input and output from recipe copy");
    }

    if (this.inputReplacements != InputReplacements.NO_OP) {
      return this.setInvalid("Can't exclude input and set input replacements");
    }

    this.excludeInput = true;
    return this;
  }

  @Override
  public boolean isExcludeInput() {

    return this.excludeInput;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy noOutput() {

    if (this.excludeInput) {
      return this.setInvalid("Can't exclude both input and output from recipe copy");
    }

    if (this.replaceOutput != null) {
      return this.setInvalid("Can't exclude output and replace output, one or the other");
    }

    this.excludeOutput = true;
    return this;
  }

  @Override
  public boolean isExcludeOutput() {

    return this.excludeOutput;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceInput(
      @Nullable IIngredient toReplace,
      @Nullable IIngredient replacement
  ) {

    if (this.excludeInput) {
      return this.setInvalid("Can't exclude input and set input replacements");
    }

    if (toReplace == null && replacement == null) {
      return this.setInvalid("Can't replace null with null");
    }

    if (this.inputReplacements == InputReplacements.NO_OP) {
      this.inputReplacements = new InputReplacements();
    }

    this.inputReplacements.add(CTArtisanIngredient.from(toReplace), CTArtisanIngredient.from(replacement));
    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceShapedInput(int col, int row, @Nullable IIngredient replacement) {

    if (this.excludeInput) {
      return this.setInvalid("Can't exclude input and set input replacements");
    }

    if (col < 0 || col > 2) {
      return this.setInvalid("Grid column index out of bounds: 0 <= " + col + " <= 2");
    }

    if (row < 0 || row > 2) {
      return this.setInvalid("Grid row index out of bounds: 0 <= " + row + " <= 2");
    }

    if (this.inputReplacements == InputReplacements.NO_OP) {
      this.inputReplacements = new InputReplacements();
    }

    this.inputReplacements.add(col, row, CTArtisanIngredient.from(replacement));
    return this;
  }

  @Override
  public IZenRecipeBuilderCopyStrategy replaceOutput(IItemStack replacement) {

    if (replacement == null) {
      return this.setInvalid("Recipe output can't be null");
    }

    if (this.excludeOutput) {
      return this.setInvalid("Can't exclude output and replace output, one or the other");
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
      RecipeBuilderCopyHelper.copyRecipeInput(recipe, this.inputReplacements, builder);
    }

    if (this.replaceOutput != null) {
      RecipeBuilderCopyHelper.replaceRecipeOutput(recipe, CTArtisanItemStack.from(this.replaceOutput), builder);

    } else if (!this.excludeOutput) {
      RecipeBuilderCopyHelper.copyRecipeOutput(recipe, builder);
    }

    builder.validate();
    CraftTweaker.LATE_ACTIONS.add(new ZenWorktable.Add(this.tableName, builder));
  }

  // --------------------------------------------------------------------------
  // - Internal

  protected IZenRecipeBuilderCopyStrategy setInvalid(String message) {

    CTLogHelper.logErrorFromZenMethod(message);
    this.invalid = true;
    return this;
  }
}

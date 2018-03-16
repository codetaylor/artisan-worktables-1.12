package com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import net.minecraft.item.crafting.IRecipe;

import javax.annotation.Nullable;
import java.util.List;

public abstract class RecipeBuilderCopyStrategyBase
    implements IRecipeBuilderCopyStrategyInternal {

  protected boolean excludeInput;
  protected boolean excludeOutput;
  protected IArtisanItemStack replaceOutput;
  protected InputReplacements inputReplacements;

  private boolean invalid;

  public RecipeBuilderCopyStrategyBase() {

    this.inputReplacements = InputReplacements.NO_OP;
  }

  @Override
  public IRecipeBuilderCopyStrategy noInput() {

    if (this.excludeOutput) {
      return this.setInvalid("Can't exclude both input and output from recipe copy");
    }

    if (this.inputReplacements != InputReplacements.NO_OP) {
      return this.setInvalid("Can't exclude input and set input replacements");
    }

    this.excludeInput = true;
    return this;
  }

  public boolean isExcludeInput() {

    return this.excludeInput;
  }

  @Override
  public IRecipeBuilderCopyStrategy noOutput() {

    if (this.excludeInput) {
      return this.setInvalid("Can't exclude both input and output from recipe copy");
    }

    if (this.replaceOutput != null) {
      return this.setInvalid("Can't exclude output and replace output, one or the other");
    }

    this.excludeOutput = true;
    return this;
  }

  public boolean isExcludeOutput() {

    return this.excludeOutput;
  }

  @Override
  public IRecipeBuilderCopyStrategy replaceInput(
      @Nullable IArtisanIngredient toReplace,
      @Nullable IArtisanIngredient replacement
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

    this.inputReplacements.add(toReplace, replacement);
    return this;
  }

  @Override
  public IRecipeBuilderCopyStrategy replaceShapedInput(int col, int row, @Nullable IArtisanIngredient replacement) {

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

    this.inputReplacements.add(col, row, replacement);
    return this;
  }

  @Override
  public IRecipeBuilderCopyStrategy replaceOutput(IArtisanItemStack replacement) {

    if (replacement == null) {
      return this.setInvalid("Recipe output can't be null");
    }

    if (this.excludeOutput) {
      return this.setInvalid("Can't exclude output and replace output, one or the other");
    }

    this.replaceOutput = replacement;
    return this;
  }

  public boolean isValid() {

    return !this.invalid;
  }

  protected void doCopy(
      IRecipe recipe,
      RecipeBuilderInternal builder,
      List<RecipeBuilderInternal> resultList
  ) throws RecipeBuilderException {

    if (!this.excludeInput) {
      RecipeBuilderCopyHelper.copyRecipeInput(recipe, this.inputReplacements, builder);
    }

    if (this.replaceOutput != null) {
      RecipeBuilderCopyHelper.replaceRecipeOutput(recipe, this.replaceOutput, builder);

    } else if (!this.excludeOutput) {
      RecipeBuilderCopyHelper.copyRecipeOutput(recipe, builder);
    }

    builder.validate();
    resultList.add(builder);
  }

  // --------------------------------------------------------------------------
  // - Internal

  protected IRecipeBuilderCopyStrategy setInvalid(String message) {

    CTLogHelper.logErrorFromZenMethod(message);
    this.invalid = true;
    return this;
  }
}

package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.EnumGameStageRequire;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilder;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderException;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.PluginDelegate;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZenRecipeBuilder
    implements IZenRecipeBuilder {

  private final String tableName;

  private static final String INPUT_ONLY = "INPUT_ONLY";
  private static final int COPY_MODE_NONE = 0;
  private static final int COPY_MODE_BY_NAME = 1;
  private static final int COPY_MODE_BY_OUTPUT = 2;

  private RecipeBuilder recipeBuilder;
  private boolean invalid;
  private boolean inputSet;
  private boolean outputSet;
  private String copyRecipeInputName;
  private String copyRecipeOutputName;
  private IIngredient[] copyRecipesFor;
  private boolean excludeOutputFromRecipeCopy;
  private int copyMode;

  /* package */ ZenRecipeBuilder(String tableName) {

    this.tableName = tableName;
    this.recipeBuilder = new RecipeBuilder();
  }

  @Override
  public IZenRecipeBuilder setShaped(IIngredient[][] ingredients) {

    if (this.inputSet) {
      return this.setInvalid("Recipe input already set");
    }

    for (IIngredient[] ingredientArray : ingredients) {

      for (IIngredient ingredient : ingredientArray) {

        if (ingredient instanceof ILiquidStack) {
          return this.setInvalid("Liquids are not supported in ingredients");
        }
      }
    }

    try {
      this.recipeBuilder.setIngredients(CTInputHelper.toIngredientMatrix(ingredients));
      this.inputSet = true;

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setShapeless(IIngredient[] ingredients) {

    if (this.inputSet) {
      return this.setInvalid("Recipe input already set");
    }

    for (IIngredient ingredient : ingredients) {

      if (ingredient instanceof ILiquidStack) {
        return this.setInvalid("Liquids are not supported in ingredients");
      }
    }

    try {
      this.recipeBuilder.setIngredients(CTInputHelper.toIngredientArray(ingredients));
      this.inputSet = true;

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  /*
  @Override
  public IZenRecipeBuilder copyRecipe(ICraftingRecipe recipe) {

    try {

      if (recipe instanceof IShapedRecipe) {
        RecipeBuilderCopyHelper.copyShapedRecipe((IShapedRecipe) recipe, this.recipeBuilder);
        return this;

      } else if (recipe instanceof IRecipe) {
        RecipeBuilderCopyHelper.copyShapelessRecipe((IRecipe) recipe, this.recipeBuilder);
        return this;

      }
    } catch (Exception e) {
      return this.setInvalid("Unable to copy recipe: " + e.getMessage());
    }

    return this;
  }
  */

  @Override
  public IZenRecipeBuilder copyRecipeByName(String recipeName) {

    if (this.copyMode == COPY_MODE_BY_OUTPUT) {
      return this.setInvalid("Copy by name can't be called on the same builder as copy by output");
    }

    if (recipeName == null) {
      return this.setInvalid("Recipe name can't be null");
    }

    if (this.inputSet) {
      return this.setInvalid("Recipe input already set");
    }

    if (this.copyRecipeOutputName != null) {
      return this.setInvalid("Recipe output already set");
    }

    this.copyRecipeInputName = recipeName;
    this.copyRecipeOutputName = recipeName;
    this.inputSet = true;
    this.outputSet = true;
    this.copyMode = COPY_MODE_BY_NAME;
    return this;
  }

  @Override
  public IZenRecipeBuilder copyRecipeInputByName(String recipeName) {

    if (this.copyMode == COPY_MODE_BY_OUTPUT) {
      return this.setInvalid("Copy by name can't be called on the same builder as copy by output");
    }

    if (recipeName == null) {
      return this.setInvalid("Recipe name can't be null");
    }

    if (this.inputSet) {
      return this.setInvalid("Recipe input already set");
    }

    this.copyRecipeInputName = recipeName;
    this.inputSet = true;
    this.copyMode = COPY_MODE_BY_NAME;
    return this;
  }

  @Override
  public IZenRecipeBuilder copyRecipes(IIngredient[] recipeOutputs, boolean excludeOutput) {

    if (this.copyMode == COPY_MODE_BY_NAME) {
      return this.setInvalid("Copy by output can't be called on the same builder as copy by name");
    }

    if (recipeOutputs == null) {
      return this.setInvalid("Recipe output ingredient can't be null");
    }

    if (this.inputSet) {
      return this.setInvalid("Recipe input already set");
    }

    this.copyRecipesFor = recipeOutputs;
    this.excludeOutputFromRecipeCopy = excludeOutput;
    this.inputSet = true;
    this.outputSet = !excludeOutput;
    this.copyMode = COPY_MODE_BY_OUTPUT;
    return this;
  }

  @Override
  public IZenRecipeBuilder setFluid(ILiquidStack fluidIngredient) {

    try {
      this.recipeBuilder.setFluidIngredient(CTInputHelper.toFluid(fluidIngredient));

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setSecondaryIngredients(IIngredient[] ingredients) {

    if (ingredients == null || ingredients.length == 0) {
      return this.setInvalid("Secondary ingredients parameter can't be null or zero length");

    } else if (ingredients.length > 9) {
      return this.setInvalid("Exceeded max allowed 9 secondary ingredients: " + ingredients.length);
    }

    List<IIngredient> adjustedList = new ArrayList<>();

    for (IIngredient ingredient : ingredients) {

      if (ingredient instanceof ILiquidStack) {
        return this.setInvalid("Liquids are not supported in ingredients");
      }

      if (ingredient != null) {
        adjustedList.add(ingredient);
      }
    }

    try {
      this.recipeBuilder.setSecondaryIngredients(adjustedList.toArray(new IIngredient[adjustedList.size()]));

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setMirrored() {

    this.recipeBuilder.setMirrored();
    return this;
  }

  @Override
  public IZenRecipeBuilder setTool(IIngredient tool, int damage) {

    this.addTool(tool, damage);
    return this;
  }

  @Override
  public IZenRecipeBuilder addTool(IIngredient tool, int damage) {

    if (tool == null) {
      return this.setInvalid("Tools can't be null");
    }

    if (tool instanceof ILiquidStack) {
      return this.setInvalid("Tools can't be liquids");
    }

    try {
      this.recipeBuilder.addTool(CTInputHelper.toIngredient(tool), damage);

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder addOutput(IItemStack output, int weight) {

    if (output == null) {
      return this.setInvalid("Output can't be null");
    }

    if (weight <= 0) { // weight is optional, may be 0
      weight = 1;
    }

    try {
      this.recipeBuilder.addOutput(CTInputHelper.toStack(output), weight);
      this.outputSet = true;

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder copyRecipeOutputByName(String recipeName) {

    if (recipeName == null) {
      return this.setInvalid("Recipe name can't be null");
    }

    if (this.copyRecipeOutputName != null) {
      return this.setInvalid("Recipe output already set with copy method");
    }

    this.copyRecipeOutputName = recipeName;
    this.outputSet = true;
    return this;
  }

  @Override
  public IZenRecipeBuilder setExtraOutputOne(IItemStack output, float chance) {

    this.recipeBuilder.setExtraOutput(0, CTInputHelper.toStack(output), chance);
    return this;
  }

  @Override
  public IZenRecipeBuilder setExtraOutputTwo(IItemStack output, float chance) {

    this.recipeBuilder.setExtraOutput(1, CTInputHelper.toStack(output), chance);
    return this;
  }

  @Override
  public IZenRecipeBuilder setExtraOutputThree(IItemStack output, float chance) {

    this.recipeBuilder.setExtraOutput(2, CTInputHelper.toStack(output), chance);
    return this;
  }

  @Override
  public IZenRecipeBuilder requireGameStages(String require, String[] stages) {

    EnumGameStageRequire enumGameStageRequire = EnumGameStageRequire.fromName(require);

    if (enumGameStageRequire == null) {
      return this.setInvalid("Invalid gamestage requirement enum: " + require + ". Valid enums are: " + Arrays
          .toString(EnumGameStageRequire.values()));
    }

    try {
      this.recipeBuilder.requireGamestages(enumGameStageRequire, stages);

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder excludeGameStages(String[] stages) {

    try {
      this.recipeBuilder.excludeGamestages(stages);

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setMinimumTier(int minimumTier) {

    try {
      this.recipeBuilder.setMinimumTier(minimumTier);

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setExperienceRequired(int experienceRequired) {

    try {
      this.recipeBuilder.setExperienceRequired(experienceRequired);

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setLevelRequired(int levelRequired) {

    try {
      this.recipeBuilder.setLevelRequired(levelRequired);

    } catch (RecipeBuilderException e) {
      return this.setInvalid(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setConsumeExperience(boolean consumeExperience) {

    this.recipeBuilder.setConsumeExperience(consumeExperience);
    return this;
  }

  @Override
  public IZenRecipeBuilder create() {

    if (this.invalid) {
      CTLogHelper.logErrorFromZenMethod("Failed to create recipe");

    } else {

      try {

        if (this.copyMode == COPY_MODE_BY_NAME) {

          if (!this.inputSet) {
            CTLogHelper.logErrorFromZenMethod("Recipe missing input");

          } else if (!this.outputSet) {
            CTLogHelper.logErrorFromZenMethod("Recipe missing output");
          }

          ZenRecipeBuilderCopyHook.RECIPE_COPY_ACTION_LIST.add(new ZenRecipeBuilderCopyHook.CopyRecipeByNameAction(
              this.tableName,
              this.recipeBuilder,
              this.copyRecipeInputName,
              this.copyRecipeOutputName
          ));

        } else if (this.copyMode == COPY_MODE_BY_OUTPUT) {

          if (!this.inputSet) {
            CTLogHelper.logErrorFromZenMethod("Recipe missing input");

          } else if (!this.outputSet) {
            CTLogHelper.logErrorFromZenMethod("Recipe missing output");
          }

          ZenRecipeBuilderCopyHook.RECIPE_COPY_ACTION_LIST.add(new ZenRecipeBuilderCopyHook.CopyRecipesByOutputAction(
              this.tableName,
              this.recipeBuilder,
              this.copyRecipesFor,
              !this.excludeOutputFromRecipeCopy
          ));

        } else {
          this.recipeBuilder.validate();
          PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new ZenWorktable.Add(this.tableName, this.recipeBuilder));
        }

      } catch (RecipeBuilderException e) {
        CTLogHelper.logErrorFromZenMethod("Recipe failed validation: " + e.getMessage());
      }
    }

    this.reset();
    return this;
  }

  private void reset() {

    this.invalid = false;
    this.inputSet = false;
    this.outputSet = false;
    this.copyRecipeInputName = null;
    this.copyRecipeOutputName = null;
    this.copyRecipesFor = null;
    this.excludeOutputFromRecipeCopy = false;
    this.copyMode = COPY_MODE_NONE;
    this.recipeBuilder = new RecipeBuilder();
  }

  private IZenRecipeBuilder setInvalid(String message) {

    CTLogHelper.logErrorFromZenMethod(message);
    this.invalid = true;
    return this;
  }
}

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
  private RecipeBuilder recipeBuilder;
  private boolean invalid;

  /* package */ ZenRecipeBuilder(String tableName) {

    this.tableName = tableName;
    this.recipeBuilder = new RecipeBuilder();
  }

  @Override
  public IZenRecipeBuilder setShaped(IIngredient[][] ingredients) {

    for (IIngredient[] ingredientArray : ingredients) {

      for (IIngredient ingredient : ingredientArray) {

        if (ingredient instanceof ILiquidStack) {
          CTLogHelper.logErrorFromZenMethod("Liquids are not supported in ingredients");
          this.invalid = true;
          return this;
        }
      }
    }

    try {
      this.recipeBuilder.setIngredients(CTInputHelper.toIngredientMatrix(ingredients));

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
      return this;
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setShapeless(IIngredient[] ingredients) {

    for (IIngredient ingredient : ingredients) {

      if (ingredient instanceof ILiquidStack) {
        CTLogHelper.logErrorFromZenMethod("Liquids are not supported in ingredients");
        this.invalid = true;
        return this;
      }
    }

    try {
      this.recipeBuilder.setIngredients(CTInputHelper.toIngredientArray(ingredients));

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
      return this;
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setFluid(ILiquidStack fluidIngredient) {

    try {
      this.recipeBuilder.setFluidIngredient(CTInputHelper.toFluid(fluidIngredient));

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
      return this;
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setSecondaryIngredients(IIngredient[] ingredients) {

    if (ingredients == null || ingredients.length == 0) {
      CTLogHelper.logErrorFromZenMethod("Secondary ingredients parameter can't be null or zero length");
      this.invalid = true;
      return this;

    } else if (ingredients.length > 9) {
      CTLogHelper.logErrorFromZenMethod("Exceeded max allowed 9 secondary ingredients: " + ingredients.length);
      this.invalid = true;
      return this;
    }

    List<IIngredient> adjustedList = new ArrayList<>();

    for (IIngredient ingredient : ingredients) {

      if (ingredient instanceof ILiquidStack) {
        CTLogHelper.logErrorFromZenMethod("Liquids are not supported in ingredients");
        this.invalid = true;
        return this;
      }

      if (ingredient != null) {
        adjustedList.add(ingredient);
      }
    }

    try {
      this.recipeBuilder.setSecondaryIngredients(adjustedList.toArray(new IIngredient[adjustedList.size()]));

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
      return this;
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

    if (tool instanceof ILiquidStack) {
      CTLogHelper.logErrorFromZenMethod("Tools can't be liquids");
      this.invalid = true;
      return this;
    }

    try {
      this.recipeBuilder.addTool(CTInputHelper.toIngredient(tool), damage);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
      return this;
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder addOutput(IItemStack output, int weight) {

    if (weight <= 0) { // weight is optional, may be 0
      weight = 1;
    }

    try {
      this.recipeBuilder.addOutput(CTInputHelper.toStack(output), weight);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
      return this;
    }

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
      CTLogHelper.logErrorFromZenMethod("Invalid gamestage requirement enum: " + require + ". Valid enums are: " + Arrays
          .toString(EnumGameStageRequire.values()));
      this.invalid = true;
      return this;
    }

    try {
      this.recipeBuilder.requireGamestages(enumGameStageRequire, stages);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder excludeGameStages(String[] stages) {

    try {
      this.recipeBuilder.excludeGamestages(stages);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setMinimumTier(int minimumTier) {

    try {
      this.recipeBuilder.setMinimumTier(minimumTier);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setExperienceRequired(int experienceRequired) {

    try {
      this.recipeBuilder.setExperienceRequired(experienceRequired);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setLevelRequired(int levelRequired) {

    try {
      this.recipeBuilder.setLevelRequired(levelRequired);

    } catch (RecipeBuilderException e) {
      CTLogHelper.logErrorFromZenMethod(e.getMessage());
      this.invalid = true;
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
        this.recipeBuilder.validate();
        PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new ZenWorktable.Add(this.tableName, this.recipeBuilder));

      } catch (RecipeBuilderException e) {
        CTLogHelper.logErrorFromZenMethod("Recipe failed validation: " + e.getMessage());
      }
    }

    this.recipeBuilder = new RecipeBuilder();
    return this;
  }
}

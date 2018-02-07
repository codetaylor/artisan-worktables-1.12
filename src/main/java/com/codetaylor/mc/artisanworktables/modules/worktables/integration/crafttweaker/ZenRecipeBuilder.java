package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.EnumGameStageRequire;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilder;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.PluginDelegate;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;

import java.util.Arrays;

public class ZenRecipeBuilder
    implements IZenRecipeBuilder {

  private String tableName;
  private RecipeBuilder recipeBuilder;
  private int nextToolIndex;

  /* package */ ZenRecipeBuilder(String tableName) {

    this.tableName = tableName;
    this.recipeBuilder = new RecipeBuilder();
    this.nextToolIndex = 0;
  }

  @Override
  public IZenRecipeBuilder setShaped(IIngredient[][] ingredients) {

    this.recipeBuilder.setIngredients(CTInputHelper.toIngredientMatrix(ingredients));
    return this;
  }

  @Override
  public IZenRecipeBuilder setShapeless(IIngredient[] ingredients) {

    this.recipeBuilder.setIngredients(CTInputHelper.toIngredientArray(ingredients));
    return this;
  }

  @Override
  public IZenRecipeBuilder setFluid(ILiquidStack fluidIngredient) {

    this.recipeBuilder.setFluidIngredient(CTInputHelper.toFluid(fluidIngredient));
    return this;
  }

  @Override
  public IZenRecipeBuilder setSecondaryIngredients(IIngredient[] secondaryIngredients) {

    if (secondaryIngredients == null || secondaryIngredients.length == 0) {
      return this;

    } else if (secondaryIngredients.length > 11) {
      CTLogHelper.logErrorFromZenMethod("Exceeded max allowed 11 secondary ingredients: " + secondaryIngredients.length);
      return this;
    }

    this.recipeBuilder.setSecondaryIngredients(secondaryIngredients);
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

    if (this.nextToolIndex >= 4) {
      CTLogHelper.logErrorFromZenMethod("Recipes are restricted to a maximum of 4 tools!");
      return this;
    }

    this.recipeBuilder.setTool(this.nextToolIndex, CTInputHelper.toIngredient(tool), damage);
    this.nextToolIndex += 1;
    return this;
  }

  @Override
  public IZenRecipeBuilder addOutput(IItemStack output, int weight) {

    if (weight <= 0) {
      weight = 1;
    }
    this.recipeBuilder.addOutput(CTInputHelper.toStack(output), weight);
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
          .toString(EnumGameStageRequire.values()) + ". Defaulting to ANY.");
      enumGameStageRequire = EnumGameStageRequire.ANY;
    }

    this.recipeBuilder.requireGamestages(enumGameStageRequire, stages);
    return this;
  }

  @Override
  public IZenRecipeBuilder excludeGameStages(String[] stages) {

    this.recipeBuilder.excludeGamestages(stages);
    return this;
  }

  @Override
  public void create() {

    PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new ZenWorktable.Add(this.tableName, this.recipeBuilder));
  }
}

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

  /* package */ ZenRecipeBuilder(String tableName) {

    this.tableName = tableName;
    this.recipeBuilder = new RecipeBuilder();
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
  public IZenRecipeBuilder setMirrored() {

    this.recipeBuilder.setMirrored();
    return this;
  }

  @Override
  public IZenRecipeBuilder setTool(IIngredient tool, int damage) {

    this.recipeBuilder.setTool(CTInputHelper.toIngredient(tool), damage);
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

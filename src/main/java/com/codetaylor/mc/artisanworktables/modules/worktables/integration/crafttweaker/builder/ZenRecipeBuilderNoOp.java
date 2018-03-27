package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder;

import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy.IZenRecipeBuilderCopyStrategy;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;

public class ZenRecipeBuilderNoOp
    implements IZenRecipeBuilder {

  /* package */ static final ZenRecipeBuilderNoOp INSTANCE = new ZenRecipeBuilderNoOp();

  private ZenRecipeBuilderNoOp() {
    //
  }

  @Override
  public IZenRecipeBuilder setName(String recipeName) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setShaped(IIngredient[][] ingredients) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setShapeless(IIngredient[] ingredients) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setFluid(ILiquidStack fluidIngredient) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setSecondaryIngredients(IIngredient[] secondaryIngredients) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setConsumeSecondaryIngredients(boolean consumeSecondaryIngredients) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setMirrored() {

    return this;
  }

  @Override
  public IZenRecipeBuilder addTool(IIngredient tool, int damage) {

    return this;
  }

  @Override
  public IZenRecipeBuilder addOutput(IItemStack output, int weight) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setExtraOutputOne(IItemStack output, float chance) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setExtraOutputTwo(IItemStack output, float chance) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setExtraOutputThree(IItemStack output, float chance) {

    return this;
  }

  @Override
  public IZenRecipeBuilder requireGameStages(String require, String[] stages) {

    return this;
  }

  @Override
  public IZenRecipeBuilder excludeGameStages(String[] stages) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setMinimumTier(int minimumTier) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setMaximumTier(int maximumTier) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setExperienceRequired(int experienceRequired) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setLevelRequired(int levelRequired) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setConsumeExperience(boolean consumeExperience) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setRecipeFunction(IRecipeFunction recipeFunction) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setRecipeAction(IRecipeAction recipeAction) {

    return this;
  }

  @Override
  public IZenRecipeBuilder setCopy(IZenRecipeBuilderCopyStrategy copyStrategy) {

    return this;
  }

  @Override
  public IZenRecipeBuilder addRequirement(IRequirementBuilder builder) {

    return this;
  }

  @Override
  public IZenRecipeBuilder create() {

    CTLogHelper.logErrorFromZenMethod("Failed to create recipe");
    return this;
  }
}

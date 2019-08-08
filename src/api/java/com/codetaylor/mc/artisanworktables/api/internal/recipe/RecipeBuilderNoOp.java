package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementBuilder;
import net.minecraftforge.fluids.FluidStack;

public class RecipeBuilderNoOp
    implements IRecipeBuilder {

  public static final IRecipeBuilder INSTANCE = new RecipeBuilderNoOp();

  @Override
  public IRecipeBuilder setName(String name) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setIngredients(IArtisanIngredient[][] ingredients) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setIngredients(IArtisanIngredient[] ingredients) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setFluidIngredient(FluidStack fluidIngredient) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setSecondaryIngredients(IArtisanIngredient[] secondaryIngredients) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setConsumeSecondaryIngredients() {

    return this;
  }

  @Override
  public IRecipeBuilder setConsumeSecondaryIngredients(boolean consumeSecondaryIngredients) {

    return this;
  }

  @Override
  public IRecipeBuilder setMirrored() {

    return this;
  }

  @Override
  public IRecipeBuilder setMirrored(boolean mirrored) {

    return this;
  }

  @Override
  public IRecipeBuilder addTool(IArtisanIngredient tool, int toolDamage) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder addOutput(IArtisanItemStack output, int weight) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setExtraOutput(
      int index, IArtisanItemStack output, float chance
  ) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setMinimumTier(int tier) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setMaximumTier(int tier) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setExperienceRequired(int cost) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setLevelRequired(int cost) throws RecipeBuilderException {

    return this;
  }

  @Override
  public IRecipeBuilder setConsumeExperience() {

    return this;
  }

  @Override
  public IRecipeBuilder setConsumeExperience(boolean consumeExperience) {

    return this;
  }

  @Override
  public IRecipeBuilder setCopy(IRecipeBuilderCopyStrategy copyStrategy) {

    return this;
  }

  @Override
  public IRecipeBuilder addRequirement(IRequirementBuilder matchRequirementBuilder) {

    return this;
  }

  @Override
  public IRecipeBuilder setHidden() {

    return this;
  }

  @Override
  public IRecipeBuilder setHidden(boolean hidden) {

    return this;
  }

  @Override
  public IRecipeBuilder create() throws RecipeBuilderException {

    return this;
  }
}

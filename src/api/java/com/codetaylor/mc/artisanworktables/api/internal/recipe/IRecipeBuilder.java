package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumGameStageRequire;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface IRecipeBuilder {

  IRecipeBuilder setIngredients(IArtisanIngredient[][] ingredients) throws RecipeBuilderException;

  IRecipeBuilder setIngredients(IArtisanIngredient[] ingredients) throws RecipeBuilderException;

  IRecipeBuilder setFluidIngredient(FluidStack fluidIngredient) throws RecipeBuilderException;

  IRecipeBuilder setSecondaryIngredients(IArtisanIngredient[] secondaryIngredients) throws RecipeBuilderException;

  IRecipeBuilder setConsumeSecondaryIngredients(boolean consumeSecondaryIngredients);

  IRecipeBuilder setMirrored();

  IRecipeBuilder addTool(IArtisanIngredient tool, int toolDamage) throws RecipeBuilderException;

  IRecipeBuilder addOutput(IArtisanItemStack output, int weight) throws RecipeBuilderException;

  IRecipeBuilder setExtraOutput(int index, IArtisanItemStack output, float chance) throws RecipeBuilderException;

  IRecipeBuilder requireGameStages(
      EnumGameStageRequire require,
      String[] includeGameStages
  ) throws RecipeBuilderException;

  IRecipeBuilder excludeGameStages(String[] excludeGameStages) throws RecipeBuilderException;

  IRecipeBuilder setMinimumTier(int tier) throws RecipeBuilderException;

  IRecipeBuilder setMaximumTier(int tier) throws RecipeBuilderException;

  IRecipeBuilder setExperienceRequired(int cost) throws RecipeBuilderException;

  IRecipeBuilder setLevelRequired(int cost) throws RecipeBuilderException;

  IRecipeBuilder setConsumeExperience(boolean consumeExperience);

  IRecipeBuilder setCopy(IRecipeBuilderCopyStrategy copyStrategy);

  IRecipeBuilder create() throws RecipeBuilderException;
}

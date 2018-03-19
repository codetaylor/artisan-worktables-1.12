package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.IRecipeFactory;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class CTArtisanRecipeFactory
    implements IRecipeFactory {

  private IRecipeFunction recipeFunction;
  private IRecipeAction recipeAction;

  @Override
  public IArtisanRecipe create(
      IGameStageMatcher gameStageMatcher,
      List<OutputWeightPair> output,
      ToolEntry[] tools,
      List<IArtisanIngredient> ingredients,
      List<IArtisanIngredient> secondaryIngredients,
      boolean consumeSecondaryIngredients,
      @Nullable FluidStack fluidIngredient,
      int experienceRequired,
      int levelRequired,
      boolean consumeExperience,
      ExtraOutputChancePair[] extraOutputs,
      IRecipeMatrixMatcher recipeMatrixMatcher,
      boolean mirrored,
      int width,
      int height,
      int minimumTier,
      int maximumTier
  ) {

    return new CTArtisanRecipe(
        gameStageMatcher,
        output,
        tools,
        ingredients,
        secondaryIngredients,
        consumeSecondaryIngredients,
        fluidIngredient,
        experienceRequired,
        levelRequired,
        consumeExperience,
        extraOutputs,
        recipeMatrixMatcher,
        mirrored,
        width,
        height,
        minimumTier,
        maximumTier,
        this.recipeAction,
        this.recipeFunction
    );
  }

  public void setRecipeAction(IRecipeAction recipeAction) {

    this.recipeAction = recipeAction;
  }

  public void setRecipeFunction(IRecipeFunction recipeFunction) {

    this.recipeFunction = recipeFunction;
  }
}

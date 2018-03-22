package com.codetaylor.mc.artisanworktables.api.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface IRecipeFactory {

  IRecipeFactory DEFAULT = ArtisanRecipe::new;

  IArtisanRecipe create(
      Map<String, IMatchRequirement> requirementMap,
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
  );

}

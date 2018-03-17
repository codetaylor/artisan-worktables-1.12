package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.recipe.ArtisanRecipe;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class CTArtisanRecipe
    extends ArtisanRecipe {

  public CTArtisanRecipe(
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

    super(
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
        maximumTier
    );
  }

}

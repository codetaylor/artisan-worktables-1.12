package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumGameStageRequire;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTArtisanIngredient;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTArtisanItemStack;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTArtisanRecipeFactory;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy.IZenRecipeBuilderCopyStrategy;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy.ZenRecipeBuilderCopyStrategy;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
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

  private RecipeBuilderInternal recipeBuilder;

  /* package */ ZenRecipeBuilder(String tableName) throws RecipeBuilderException {

    this.recipeBuilder = RecipeBuilderInternal.get(tableName, new CTArtisanRecipeFactory());
  }

  // --------------------------------------------------------------------------
  // - Input

  @Override
  public IZenRecipeBuilder setShaped(IIngredient[][] ingredients) {

    for (IIngredient[] ingredientArray : ingredients) {

      for (IIngredient ingredient : ingredientArray) {

        if (ingredient instanceof ILiquidStack) {
          return this.logError("Liquids are not supported in ingredients");
        }
      }
    }

    try {
      this.recipeBuilder.setIngredients(CTArtisanIngredient.fromMatrix(ingredients));

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setMirrored() {

    this.recipeBuilder.setMirrored();
    return this;
  }

  @Override
  public IZenRecipeBuilder setShapeless(IIngredient[] ingredients) {

    for (IIngredient ingredient : ingredients) {

      if (ingredient instanceof ILiquidStack) {
        return this.logError("Liquids are not supported in ingredients");
      }
    }

    try {
      this.recipeBuilder.setIngredients(CTArtisanIngredient.fromArray(ingredients));

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  // --------------------------------------------------------------------------
  // - Fluid

  @Override
  public IZenRecipeBuilder setFluid(ILiquidStack fluidIngredient) {

    try {
      this.recipeBuilder.setFluidIngredient(CTInputHelper.toFluid(fluidIngredient));

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  // --------------------------------------------------------------------------
  // - Secondary Ingredients

  @Override
  public IZenRecipeBuilder setSecondaryIngredients(IIngredient[] ingredients) {

    if (ingredients == null || ingredients.length == 0) {
      return this.logError("Secondary ingredients parameter can't be null or zero length");
    }

    List<IIngredient> adjustedList = new ArrayList<>();

    for (IIngredient ingredient : ingredients) {

      if (ingredient instanceof ILiquidStack) {
        return this.logError("Liquids are not supported in secondary ingredients");
      }

      if (ingredient != null) {
        adjustedList.add(ingredient);
      }
    }

    try {
      this.recipeBuilder.setSecondaryIngredients(
          CTArtisanIngredient.fromArray(adjustedList.toArray(new IIngredient[adjustedList.size()]))
      );

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setConsumeSecondaryIngredients(boolean consumeSecondaryIngredients) {

    this.recipeBuilder.setConsumeSecondaryIngredients(consumeSecondaryIngredients);
    return this;
  }

  // --------------------------------------------------------------------------
  // - Tools

  @Override
  public IZenRecipeBuilder addTool(IIngredient tool, int damage) {

    if (tool instanceof ILiquidStack) {
      return this.logError("Tools can't be liquids");
    }

    try {
      this.recipeBuilder.addTool(CTArtisanIngredient.from(tool), damage);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  // --------------------------------------------------------------------------
  // - Output

  @Override
  public IZenRecipeBuilder addOutput(IItemStack output, int weight) {

    if (weight <= 0) {
      // Since the weight is an optional parameter, it may be zero
      // so we set it to 1 here.
      weight = 1;
    }

    try {
      this.recipeBuilder.addOutput(CTArtisanItemStack.from(output), weight);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  // --------------------------------------------------------------------------
  // - Extra Output

  @Override
  public IZenRecipeBuilder setExtraOutputOne(IItemStack output, float chance) {

    try {
      this.recipeBuilder.setExtraOutput(0, CTArtisanItemStack.from(output), chance);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setExtraOutputTwo(IItemStack output, float chance) {

    try {
      this.recipeBuilder.setExtraOutput(1, CTArtisanItemStack.from(output), chance);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setExtraOutputThree(IItemStack output, float chance) {

    try {
      this.recipeBuilder.setExtraOutput(2, CTArtisanItemStack.from(output), chance);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  // --------------------------------------------------------------------------
  // - Game Stages

  @Override
  public IZenRecipeBuilder requireGameStages(String require, String[] stages) {

    EnumGameStageRequire enumGameStageRequire = EnumGameStageRequire.fromName(require);

    if (enumGameStageRequire == null) {
      return this.logError("Invalid gamestage requirement enum: " + require + ". Valid enums are: " + Arrays
          .toString(EnumGameStageRequire.values()));
    }

    try {
      this.recipeBuilder.requireGameStages(enumGameStageRequire, stages);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder excludeGameStages(String[] stages) {

    try {
      this.recipeBuilder.excludeGameStages(stages);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  // --------------------------------------------------------------------------
  // - Tier Restriction

  @Override
  public IZenRecipeBuilder setMinimumTier(int minimumTier) {

    try {
      this.recipeBuilder.setMinimumTier(minimumTier);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setMaximumTier(int maximumTier) {

    try {
      this.recipeBuilder.setMaximumTier(maximumTier);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  // --------------------------------------------------------------------------
  // - Experience

  @Override
  public IZenRecipeBuilder setExperienceRequired(int experienceRequired) {

    try {
      this.recipeBuilder.setExperienceRequired(experienceRequired);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setLevelRequired(int levelRequired) {

    try {
      this.recipeBuilder.setLevelRequired(levelRequired);

    } catch (RecipeBuilderException e) {
      return this.logError(e.getMessage());
    }

    return this;
  }

  @Override
  public IZenRecipeBuilder setConsumeExperience(boolean consumeExperience) {

    this.recipeBuilder.setConsumeExperience(consumeExperience);
    return this;
  }

  // --------------------------------------------------------------------------
  // - Copy

  @Override
  public IZenRecipeBuilder setCopy(IZenRecipeBuilderCopyStrategy copyStrategy) {

    ZenRecipeBuilderCopyStrategy strategy = (ZenRecipeBuilderCopyStrategy) copyStrategy;
    this.recipeBuilder.setCopy(strategy.getStrategy());
    return this;
  }

  // --------------------------------------------------------------------------
  // - Create

  @Override
  public IZenRecipeBuilder create() {

    try {
      this.recipeBuilder.create();

    } catch (RecipeBuilderException e) {
      this.logError("Failed to create recipe");
      this.logError(e.getMessage());
    }

    this.recipeBuilder = new RecipeBuilderInternal(
        this.recipeBuilder.getTableName(),
        this.recipeBuilder.getRecipeAdditionQueue(),
        new CTArtisanRecipeFactory()
    );

    return this;
  }

  // --------------------------------------------------------------------------
  // - Internal

  private IZenRecipeBuilder logError(String message) {

    CTLogHelper.logErrorFromZenMethod(message);
    return this;
  }
}

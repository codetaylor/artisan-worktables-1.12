package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder;

import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy.IZenRecipeBuilderCopyStrategy;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.artisanworktables.builder.RecipeBuilder")
public interface IZenRecipeBuilder {

  Map<String, IZenRecipeBuilder> BUILDER_MAP = new HashMap<>();

  @ZenMethod
  static IZenRecipeBuilder get(String table) {

    table = table.toLowerCase();

    if (!ArtisanAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          ArtisanAPI.getWorktableNames()
      ));
      return ZenRecipeBuilderNoOp.INSTANCE;
    }

    IZenRecipeBuilder builder = BUILDER_MAP.get(table);

    if (builder == null) {

      try {
        builder = new ZenRecipeBuilder(table);
        BUILDER_MAP.put(table, builder);

      } catch (RecipeBuilderException e) {
        CTLogHelper.logErrorFromZenMethod(e.getMessage());
        return ZenRecipeBuilderNoOp.INSTANCE;
      }
    }

    return builder;
  }

  @ZenMethod
  IZenRecipeBuilder setShaped(IIngredient[][] ingredients);

  @ZenMethod
  IZenRecipeBuilder setShapeless(IIngredient[] ingredients);

  @ZenMethod
  IZenRecipeBuilder setFluid(ILiquidStack fluidIngredient);

  @ZenMethod
  IZenRecipeBuilder setSecondaryIngredients(IIngredient[] secondaryIngredients);

  @ZenMethod
  IZenRecipeBuilder setConsumeSecondaryIngredients(boolean consumeSecondaryIngredients);

  @ZenMethod
  IZenRecipeBuilder setMirrored();

  @ZenMethod
  IZenRecipeBuilder addTool(IIngredient tool, int damage);

  @ZenMethod
  IZenRecipeBuilder addOutput(IItemStack output, @Optional int weight);

  @ZenMethod
  IZenRecipeBuilder setExtraOutputOne(IItemStack output, float chance);

  @ZenMethod
  IZenRecipeBuilder setExtraOutputTwo(IItemStack output, float chance);

  @ZenMethod
  IZenRecipeBuilder setExtraOutputThree(IItemStack output, float chance);

  @ZenMethod
  IZenRecipeBuilder requireGameStages(String require, String[] stages);

  @ZenMethod
  IZenRecipeBuilder excludeGameStages(String[] stages);

  @ZenMethod
  IZenRecipeBuilder setMinimumTier(int minimumTier);

  @ZenMethod
  IZenRecipeBuilder setMaximumTier(int maximumTier);

  @ZenMethod
  IZenRecipeBuilder setExperienceRequired(int experienceRequired);

  @ZenMethod
  IZenRecipeBuilder setLevelRequired(int levelRequired);

  @ZenMethod
  IZenRecipeBuilder setConsumeExperience(boolean consumeExperience);

  // --------------------------------------------------------------------------
  // - Recipe Function / Action

  @ZenMethod
  IZenRecipeBuilder setRecipeFunction(IRecipeFunction recipeFunction);

  @ZenMethod
  IZenRecipeBuilder setRecipeAction(IRecipeAction recipeAction);

  // --------------------------------------------------------------------------
  // - Copy

  @ZenMethod
  IZenRecipeBuilder setCopy(IZenRecipeBuilderCopyStrategy copyStrategy);

  // --------------------------------------------------------------------------
  // - Create

  @ZenMethod
  IZenRecipeBuilder create();

}

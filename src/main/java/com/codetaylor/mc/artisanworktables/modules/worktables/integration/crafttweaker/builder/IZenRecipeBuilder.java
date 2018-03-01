package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder;

import com.codetaylor.mc.artisanworktables.modules.worktables.api.ArtisanWorktablesAPI;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
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

    if (!ArtisanWorktablesAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          ArtisanWorktablesAPI.getWorktableNames()
      ));
      return ZenRecipeBuilderNoOp.INSTANCE;
    }

    IZenRecipeBuilder builder = BUILDER_MAP.get(table);

    if (builder == null) {
      builder = new ZenRecipeBuilder(table);
      BUILDER_MAP.put(table, builder);
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
  IZenRecipeBuilder setMirrored();

  @Deprecated
  @ZenMethod
  IZenRecipeBuilder setTool(IIngredient tool, int damage);

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
  // - Copy

  @ZenMethod
  IZenRecipeBuilder setCopy(IZenRecipeBuilderCopyStrategy copyStrategy);

  // --------------------------------------------------------------------------
  // - Create

  @ZenMethod
  IZenRecipeBuilder create();

}

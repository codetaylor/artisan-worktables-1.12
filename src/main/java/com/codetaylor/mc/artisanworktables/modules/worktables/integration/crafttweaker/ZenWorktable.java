package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumGameStageRequire;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.IZenRecipeBuilder;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.PluginDelegate;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.Worktable")
public class ZenWorktable {

  // --------------------------------------------------------------------------
  // - Shaped

  @ZenMethod
  public static void addRecipeShaped(
      String table,
      IItemStack result,
      IIngredient tool,
      int toolDamage,
      boolean mirrored,
      IIngredient[][] input,
      @Optional IItemStack secondaryOutput,
      @Optional float secondaryOutputChance,
      @Optional IItemStack tertiaryOutput,
      @Optional float tertiaryOutputChance,
      @Optional IItemStack quaternaryOutput,
      @Optional float quaternaryOutputChance
  ) {

    table = table.toLowerCase();

    if (!ArtisanAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          ArtisanAPI.getWorktableNames()
      ));
      return;
    }

    try {
      RecipeBuilderInternal recipeBuilder = new RecipeBuilderInternal(table, ModuleWorktables.RECIPE_ADDITION_QUEUE);
      recipeBuilder.addOutput(CTArtisanItemStack.from(result), 1);
      recipeBuilder.addTool(CTArtisanIngredient.from(tool), toolDamage);

      if (mirrored) {
        recipeBuilder.setMirrored();
      }

      recipeBuilder.setIngredients(CTArtisanIngredient.fromMatrix(input));

      if (secondaryOutput != null) {
        recipeBuilder.setExtraOutput(0, CTArtisanItemStack.from(secondaryOutput), secondaryOutputChance);
      }

      if (tertiaryOutput != null) {
        recipeBuilder.setExtraOutput(1, CTArtisanItemStack.from(tertiaryOutput), tertiaryOutputChance);
      }

      if (quaternaryOutput != null) {
        recipeBuilder.setExtraOutput(2, CTArtisanItemStack.from(quaternaryOutput), quaternaryOutputChance);
      }

      PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new CTActionAdd(table, recipeBuilder));

    } catch (Exception e) {
      CTLogHelper.logErrorFromZenMethod("Unable to build recipe: " + e.getMessage());
    }

  }

  @ZenMethod
  public static void addStagedRecipeShaped(
      String gameStageName,
      String table,
      IItemStack result,
      IIngredient tool,
      int toolDamage,
      boolean mirrored,
      IIngredient[][] input,
      @Optional IItemStack secondaryOutput,
      @Optional float secondaryOutputChance,
      @Optional IItemStack tertiaryOutput,
      @Optional float tertiaryOutputChance,
      @Optional IItemStack quaternaryOutput,
      @Optional float quaternaryOutputChance
  ) {

    table = table.toLowerCase();

    if (!ArtisanAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          ArtisanAPI.getWorktableNames()
      ));
      return;
    }

    try {
      RecipeBuilderInternal recipeBuilder = new RecipeBuilderInternal(table, ModuleWorktables.RECIPE_ADDITION_QUEUE);
      recipeBuilder.addOutput(CTArtisanItemStack.from(result), 1);
      recipeBuilder.addTool(CTArtisanIngredient.from(tool), toolDamage);

      if (mirrored) {
        recipeBuilder.setMirrored();
      }

      recipeBuilder.setIngredients(CTArtisanIngredient.fromMatrix(input));

      if (secondaryOutput != null) {
        recipeBuilder.setExtraOutput(0, CTArtisanItemStack.from(secondaryOutput), secondaryOutputChance);
      }

      if (tertiaryOutput != null) {
        recipeBuilder.setExtraOutput(1, CTArtisanItemStack.from(tertiaryOutput), tertiaryOutputChance);
      }

      if (quaternaryOutput != null) {
        recipeBuilder.setExtraOutput(2, CTArtisanItemStack.from(quaternaryOutput), quaternaryOutputChance);
      }

      recipeBuilder.requireGameStages(EnumGameStageRequire.ANY, new String[]{gameStageName});

      PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new CTActionAdd(table, recipeBuilder));

    } catch (Exception e) {
      CTLogHelper.logErrorFromZenMethod("Unable to build recipe: " + e.getMessage());
    }

  }

  // --------------------------------------------------------------------------
  // - Shapeless

  @ZenMethod
  public static void addRecipeShapeless(
      String table,
      IItemStack result,
      IIngredient tool,
      int toolDamage,
      IIngredient[] input,
      @Optional IItemStack secondaryOutput,
      @Optional float secondaryOutputChance,
      @Optional IItemStack tertiaryOutput,
      @Optional float tertiaryOutputChance,
      @Optional IItemStack quaternaryOutput,
      @Optional float quaternaryOutputChance
  ) {

    table = table.toLowerCase();

    if (!ArtisanAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          ArtisanAPI.getWorktableNames()
      ));
      return;
    }

    try {
      RecipeBuilderInternal recipeBuilder = new RecipeBuilderInternal(table, ModuleWorktables.RECIPE_ADDITION_QUEUE);
      recipeBuilder.addOutput(CTArtisanItemStack.from(result), 1);
      recipeBuilder.addTool(CTArtisanIngredient.from(tool), toolDamage);

      recipeBuilder.setIngredients(CTArtisanIngredient.fromArray(input));

      if (secondaryOutput != null) {
        recipeBuilder.setExtraOutput(0, CTArtisanItemStack.from(secondaryOutput), secondaryOutputChance);
      }

      if (tertiaryOutput != null) {
        recipeBuilder.setExtraOutput(1, CTArtisanItemStack.from(tertiaryOutput), tertiaryOutputChance);
      }

      if (quaternaryOutput != null) {
        recipeBuilder.setExtraOutput(2, CTArtisanItemStack.from(quaternaryOutput), quaternaryOutputChance);
      }

      PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new CTActionAdd(table, recipeBuilder));

    } catch (Exception e) {
      CTLogHelper.logErrorFromZenMethod("Unable to build recipe: " + e.getMessage());
    }

  }

  @ZenMethod
  public static void addStagedRecipeShapeless(
      String gameStageName,
      String table,
      IItemStack result,
      IIngredient tool,
      int toolDamage,
      IIngredient[] input,
      @Optional IItemStack secondaryOutput,
      @Optional float secondaryOutputChance,
      @Optional IItemStack tertiaryOutput,
      @Optional float tertiaryOutputChance,
      @Optional IItemStack quaternaryOutput,
      @Optional float quaternaryOutputChance
  ) {

    table = table.toLowerCase();

    if (!ArtisanAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          ArtisanAPI.getWorktableNames()
      ));
      return;
    }

    try {
      RecipeBuilderInternal recipeBuilder = new RecipeBuilderInternal(table, ModuleWorktables.RECIPE_ADDITION_QUEUE);
      recipeBuilder.addOutput(CTArtisanItemStack.from(result), 1);
      recipeBuilder.addTool(CTArtisanIngredient.from(tool), toolDamage);

      recipeBuilder.setIngredients(CTArtisanIngredient.fromArray(input));

      if (secondaryOutput != null) {
        recipeBuilder.setExtraOutput(0, CTArtisanItemStack.from(secondaryOutput), secondaryOutputChance);
      }

      if (tertiaryOutput != null) {
        recipeBuilder.setExtraOutput(1, CTArtisanItemStack.from(tertiaryOutput), tertiaryOutputChance);
      }

      if (quaternaryOutput != null) {
        recipeBuilder.setExtraOutput(2, CTArtisanItemStack.from(quaternaryOutput), quaternaryOutputChance);
      }

      recipeBuilder.requireGameStages(EnumGameStageRequire.ANY, new String[]{gameStageName});

      PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new CTActionAdd(table, recipeBuilder));

    } catch (Exception e) {
      CTLogHelper.logErrorFromZenMethod("Unable to build recipe: " + e.getMessage());
    }

  }

  // --------------------------------------------------------------------------
  // - Builder

  @ZenMethod
  public static IZenRecipeBuilder createRecipeBuilder(String table) {

    return IZenRecipeBuilder.get(table);
  }

}

package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.EnumGameStageRequire;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipe;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilder;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipe;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.PluginDelegate;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.utils.BaseUndoable;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.Worktable")
public class ZenWorktable {

  // --------------------------------------------------------------------------
  // - Shaped
  // --------------------------------------------------------------------------

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

    if (!WorktableAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          WorktableAPI.getWorktableNames()
      ));
      return;
    }

    try {
      RecipeBuilder recipeBuilder = new RecipeBuilder();
      recipeBuilder.addOutput(CTInputHelper.toStack(result), 1);
      recipeBuilder.addTool(CTInputHelper.toIngredient(tool), toolDamage);

      if (mirrored) {
        recipeBuilder.setMirrored();
      }

      recipeBuilder.setIngredients(CTInputHelper.toIngredientMatrix(input));

      if (secondaryOutput != null) {
        recipeBuilder.setExtraOutput(0, CTInputHelper.toStack(secondaryOutput), secondaryOutputChance);
      }

      if (tertiaryOutput != null) {
        recipeBuilder.setExtraOutput(1, CTInputHelper.toStack(tertiaryOutput), tertiaryOutputChance);
      }

      if (quaternaryOutput != null) {
        recipeBuilder.setExtraOutput(2, CTInputHelper.toStack(quaternaryOutput), quaternaryOutputChance);
      }

      PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new Add(table, recipeBuilder));

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

    if (!WorktableAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          WorktableAPI.getWorktableNames()
      ));
      return;
    }

    try {
      RecipeBuilder recipeBuilder = new RecipeBuilder();
      recipeBuilder.addOutput(CTInputHelper.toStack(result), 1);
      recipeBuilder.addTool(CTInputHelper.toIngredient(tool), toolDamage);

      if (mirrored) {
        recipeBuilder.setMirrored();
      }

      recipeBuilder.setIngredients(CTInputHelper.toIngredientMatrix(input));

      if (secondaryOutput != null) {
        recipeBuilder.setExtraOutput(0, CTInputHelper.toStack(secondaryOutput), secondaryOutputChance);
      }

      if (tertiaryOutput != null) {
        recipeBuilder.setExtraOutput(1, CTInputHelper.toStack(tertiaryOutput), tertiaryOutputChance);
      }

      if (quaternaryOutput != null) {
        recipeBuilder.setExtraOutput(2, CTInputHelper.toStack(quaternaryOutput), quaternaryOutputChance);
      }

      recipeBuilder.requireGamestages(EnumGameStageRequire.ANY, new String[]{gameStageName});

      PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new Add(table, recipeBuilder));

    } catch (Exception e) {
      CTLogHelper.logErrorFromZenMethod("Unable to build recipe: " + e.getMessage());
    }

  }

  // --------------------------------------------------------------------------
  // - Shapeless
  // --------------------------------------------------------------------------

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

    if (!WorktableAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          WorktableAPI.getWorktableNames()
      ));
      return;
    }

    try {
      RecipeBuilder recipeBuilder = new RecipeBuilder();
      recipeBuilder.addOutput(CTInputHelper.toStack(result), 1);
      recipeBuilder.addTool(CTInputHelper.toIngredient(tool), toolDamage);

      recipeBuilder.setIngredients(CTInputHelper.toIngredientArray(input));

      if (secondaryOutput != null) {
        recipeBuilder.setExtraOutput(0, CTInputHelper.toStack(secondaryOutput), secondaryOutputChance);
      }

      if (tertiaryOutput != null) {
        recipeBuilder.setExtraOutput(1, CTInputHelper.toStack(tertiaryOutput), tertiaryOutputChance);
      }

      if (quaternaryOutput != null) {
        recipeBuilder.setExtraOutput(2, CTInputHelper.toStack(quaternaryOutput), quaternaryOutputChance);
      }

      PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new Add(table, recipeBuilder));

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

    if (!WorktableAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          WorktableAPI.getWorktableNames()
      ));
      return;
    }

    try {
      RecipeBuilder recipeBuilder = new RecipeBuilder();
      recipeBuilder.addOutput(CTInputHelper.toStack(result), 1);
      recipeBuilder.addTool(CTInputHelper.toIngredient(tool), toolDamage);

      recipeBuilder.setIngredients(CTInputHelper.toIngredientArray(input));

      if (secondaryOutput != null) {
        recipeBuilder.setExtraOutput(0, CTInputHelper.toStack(secondaryOutput), secondaryOutputChance);
      }

      if (tertiaryOutput != null) {
        recipeBuilder.setExtraOutput(1, CTInputHelper.toStack(tertiaryOutput), tertiaryOutputChance);
      }

      if (quaternaryOutput != null) {
        recipeBuilder.setExtraOutput(2, CTInputHelper.toStack(quaternaryOutput), quaternaryOutputChance);
      }

      recipeBuilder.requireGamestages(EnumGameStageRequire.ANY, new String[]{gameStageName});

      PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new Add(table, recipeBuilder));

    } catch (Exception e) {
      CTLogHelper.logErrorFromZenMethod("Unable to build recipe: " + e.getMessage());
    }

  }

  // --------------------------------------------------------------------------
  // - Builder
  // --------------------------------------------------------------------------

  @ZenMethod
  public static IZenRecipeBuilder createRecipeBuilder(String table) {

    table = table.toLowerCase();

    if (!WorktableAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          WorktableAPI.getWorktableNames()
      ));
      return ZenRecipeBuilderNoOp.INSTANCE;
    }

    return new ZenRecipeBuilder(table);
  }

  // --------------------------------------------------------------------------
  // - Internal
  // --------------------------------------------------------------------------

  public static class Add
      extends BaseUndoable {

    private final String tableName;
    private final RecipeBuilder recipeBuilder;

    Add(String tableName, RecipeBuilder recipeBuilder) {

      super("RecipeWorktable");
      this.tableName = tableName;
      this.recipeBuilder = recipeBuilder;
    }

    @Override
    public void apply() {

      try {
        RegistryRecipe registry = WorktableAPI.getWorktableRecipeRegistry(this.tableName);
        IRecipe recipeWorktable = this.recipeBuilder.create();
        registry.addRecipe(recipeWorktable);

      } catch (Exception e) {
        CTLogHelper.logError("Unable to register recipe", e);
      }
    }

    @Override
    protected String getRecipeInfo() {

      return CTLogHelper.getStackDescription(this.tableName);
    }
  }

}

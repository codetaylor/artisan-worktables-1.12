package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.EnumWorktableType;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.PluginDelegate;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.utils.BaseUndoable;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

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

    EnumWorktableType type;

    try {
      type = EnumWorktableType.fromName(table);

    } catch (Exception e) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + Arrays.toString(EnumWorktableType.NAMES));
      return;
    }

    PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new AddShaped(
        null,
        type,
        CTInputHelper.toStack(result),
        CTInputHelper.toIngredient(tool),
        CTInputHelper.toIngredientMatrix(input),
        toolDamage,
        mirrored,
        CTInputHelper.toStack(secondaryOutput),
        secondaryOutputChance,
        CTInputHelper.toStack(tertiaryOutput),
        tertiaryOutputChance,
        CTInputHelper.toStack(quaternaryOutput),
        quaternaryOutputChance
    ));
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

    EnumWorktableType type;

    try {
      type = EnumWorktableType.fromName(table);

    } catch (Exception e) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + Arrays.toString(EnumWorktableType.NAMES));
      return;
    }

    PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new AddShaped(
        gameStageName,
        type,
        CTInputHelper.toStack(result),
        CTInputHelper.toIngredient(tool),
        CTInputHelper.toIngredientMatrix(input),
        toolDamage,
        mirrored,
        CTInputHelper.toStack(secondaryOutput),
        secondaryOutputChance,
        CTInputHelper.toStack(tertiaryOutput),
        tertiaryOutputChance,
        CTInputHelper.toStack(quaternaryOutput),
        quaternaryOutputChance
    ));
  }

  private static class AddShaped
      extends BaseUndoable {

    private final String gameStageName;
    private final EnumWorktableType type;
    private final ItemStack result;
    private final Ingredient tool;
    private final Ingredient[][] input;
    private final int toolDamage;
    private final boolean mirrored;
    private final ItemStack secondaryOutput;
    private final float secondaryOutputChance;
    private final ItemStack tertiaryOutput;
    private final float tertiaryOutputChance;
    private final ItemStack quaternaryOutput;
    private final float quaternaryOutputChance;

    AddShaped(
        String gameStageName,
        EnumWorktableType type,
        ItemStack result,
        Ingredient tool,
        Ingredient[][] input,
        int toolDamage,
        boolean mirrored,
        ItemStack secondaryOutput,
        float secondaryOutputChance,
        ItemStack tertiaryOutput,
        float tertiaryOutputChance,
        ItemStack quaternaryOutput,
        float quaternaryOutputChance
    ) {

      super("WorktableShaped");
      this.gameStageName = gameStageName;
      this.type = type;
      this.result = result;
      this.tool = tool;
      this.input = input;
      this.toolDamage = toolDamage;
      this.mirrored = mirrored;
      this.secondaryOutput = secondaryOutput;
      this.secondaryOutputChance = secondaryOutputChance;
      this.tertiaryOutput = tertiaryOutput;
      this.tertiaryOutputChance = tertiaryOutputChance;
      this.quaternaryOutput = quaternaryOutput;
      this.quaternaryOutputChance = quaternaryOutputChance;
    }

    @Override
    public void apply() {

      RegistryRecipeWorktable registry = WorktableAPI.getRecipeRegistry(this.type);

      registry.addRecipeShaped(
          this.gameStageName,
          this.result,
          this.tool,
          this.input,
          this.toolDamage,
          this.mirrored,
          this.secondaryOutput,
          this.secondaryOutputChance,
          this.tertiaryOutput,
          this.tertiaryOutputChance,
          this.quaternaryOutput,
          this.quaternaryOutputChance
      );
    }

    @Override
    protected String getRecipeInfo() {

      return CTLogHelper.getStackDescription(this.result);
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

    EnumWorktableType type;

    try {
      type = EnumWorktableType.fromName(table);

    } catch (Exception e) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + Arrays.toString(EnumWorktableType.NAMES));
      return;
    }

    PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new AddShapeless(
        null,
        type,
        CTInputHelper.toStack(result),
        CTInputHelper.toIngredient(tool),
        CTInputHelper.toIngredientArray(input),
        toolDamage,
        CTInputHelper.toStack(secondaryOutput),
        secondaryOutputChance,
        CTInputHelper.toStack(tertiaryOutput),
        tertiaryOutputChance,
        CTInputHelper.toStack(quaternaryOutput),
        quaternaryOutputChance
    ));
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

    EnumWorktableType type;

    try {
      type = EnumWorktableType.fromName(table);

    } catch (Exception e) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + Arrays.toString(EnumWorktableType.NAMES));
      return;
    }

    PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new AddShapeless(
        gameStageName,
        type,
        CTInputHelper.toStack(result),
        CTInputHelper.toIngredient(tool),
        CTInputHelper.toIngredientArray(input),
        toolDamage,
        CTInputHelper.toStack(secondaryOutput),
        secondaryOutputChance,
        CTInputHelper.toStack(tertiaryOutput),
        tertiaryOutputChance,
        CTInputHelper.toStack(quaternaryOutput),
        quaternaryOutputChance
    ));
  }

  private static class AddShapeless
      extends BaseUndoable {

    private final String gameStageName;
    private final EnumWorktableType type;
    private final ItemStack result;
    private final Ingredient tool;
    private final Ingredient[] input;
    private final int toolDamage;
    private final ItemStack secondaryOutput;
    private final float secondaryOutputChance;
    private final ItemStack tertiaryOutput;
    private final float tertiaryOutputChance;
    private final ItemStack quaternaryOutput;
    private final float quaternaryOutputChance;

    AddShapeless(
        String gameStageName,
        EnumWorktableType type,
        ItemStack result,
        Ingredient tool,
        Ingredient[] input,
        int toolDamage,
        ItemStack secondaryOutput,
        float secondaryOutputChance,
        ItemStack tertiaryOutput,
        float tertiaryOutputChance,
        ItemStack quaternaryOutput,
        float quaternaryOutputChance
    ) {

      super("WorktableShapeless");
      this.gameStageName = gameStageName;
      this.type = type;
      this.result = result;
      this.tool = tool;
      this.input = input;
      this.toolDamage = toolDamage;
      this.secondaryOutput = secondaryOutput;
      this.secondaryOutputChance = secondaryOutputChance;
      this.tertiaryOutput = tertiaryOutput;
      this.tertiaryOutputChance = tertiaryOutputChance;
      this.quaternaryOutput = quaternaryOutput;
      this.quaternaryOutputChance = quaternaryOutputChance;
    }

    @Override
    public void apply() {

      RegistryRecipeWorktable registry = WorktableAPI.getRecipeRegistry(this.type);

      registry.addRecipeShapeless(
          this.gameStageName,
          this.result,
          this.tool,
          this.input,
          this.toolDamage,
          this.secondaryOutput,
          this.secondaryOutputChance,
          this.tertiaryOutput,
          this.tertiaryOutputChance,
          this.quaternaryOutput,
          this.quaternaryOutputChance
      );
    }

    @Override
    protected String getRecipeInfo() {

      return CTLogHelper.getStackDescription(this.result);
    }
  }
}

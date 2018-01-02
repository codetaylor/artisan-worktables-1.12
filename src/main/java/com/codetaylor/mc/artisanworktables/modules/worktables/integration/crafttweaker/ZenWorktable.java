package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.PluginDelegate;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.utils.BaseUndoable;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
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
      IIngredient[][] input
  ) {

    PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new AddShaped(
        table,
        CTInputHelper.toStack(result),
        CTInputHelper.toIngredient(tool),
        CTInputHelper.toIngredientMatrix(input),
        toolDamage,
        mirrored
    ));
  }

  private static class AddShaped
      extends BaseUndoable {

    private String table;
    private final ItemStack result;
    private final Ingredient tool;
    private final Ingredient[][] input;
    private final int toolDamage;
    private final boolean mirrored;

    AddShaped(String table, ItemStack result, Ingredient tool, Ingredient[][] input, int toolDamage, boolean mirrored) {

      super("WorktableShaped");
      this.table = table;
      this.result = result;
      this.tool = tool;
      this.input = input;
      this.toolDamage = toolDamage;
      this.mirrored = mirrored;
    }

    @Override
    public void apply() {

      RegistryRecipeWorktable registry = WorktableAPI.RECIPE_REGISTRY_MAP.get(this.table);

      if (registry != null) {
        registry.addRecipeShaped(this.result, this.tool, this.input, this.toolDamage, this.mirrored);

      } else {
        CTLogHelper.logError("Unrecognized table name: " + this.table + ", valid values are: " + Arrays.toString(
            BlockWorktable.EnumType.NAMES));
      }
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
      IIngredient[] input
  ) {

    PluginDelegate.addAddition(ModuleWorktables.MOD_ID, new AddShapeless(
        table,
        CTInputHelper.toStack(result),
        CTInputHelper.toIngredient(tool),
        CTInputHelper.toIngredientArray(input),
        toolDamage
    ));
  }

  private static class AddShapeless
      extends BaseUndoable {

    private String table;
    private final ItemStack result;
    private final Ingredient tool;
    private final Ingredient[] input;
    private final int toolDamage;

    AddShapeless(String table, ItemStack result, Ingredient tool, Ingredient[] input, int toolDamage) {

      super("WorktableShapeless");
      this.table = table;
      this.result = result;
      this.tool = tool;
      this.input = input;
      this.toolDamage = toolDamage;
    }

    @Override
    public void apply() {

      RegistryRecipeWorktable registry = WorktableAPI.RECIPE_REGISTRY_MAP.get(this.table);

      if (registry != null) {
        registry.addRecipeShapeless(this.result, this.tool, this.input, this.toolDamage);

      } else {
        CTLogHelper.logError("Unrecognized table name: " + this.table + ", valid values are: " + Arrays.toString(
            BlockWorktable.EnumType.values()));
      }
    }

    @Override
    protected String getRecipeInfo() {

      return CTLogHelper.getStackDescription(this.result);
    }
  }
}

package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilder;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderCopyHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.utils.BaseUndoable;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.mc1120.CraftTweaker;
import crafttweaker.mc1120.events.ScriptRunEvent;
import crafttweaker.mc1120.recipes.MCRecipeManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber
public class ZenRecipeBuilderCopyHook {

  /* package */ static final List<IAction> RECIPE_COPY_ACTION_LIST = new ArrayList<>();

  @SubscribeEvent
  public static void onScriptEvent(ScriptRunEvent.Post event) {

    // Hacks!

    // This is a hack intended to hook CraftTweaker and run code immediately before
    // CT processes recipe removals. At this point, we have access to the same
    // recipes that CT does and can safely initialize any copy recipes.

    // Running this immediately before CT removes any recipes allows us to process
    // our recipe copy actions while the recipes still exist and after all recipes
    // 'should' be loaded. After the recipe is copied, the builder is validated and
    // added to CT's LATE_ACTIONS collection, which is processed after all recipe
    // additions and removals.

    MCRecipeManager.recipesToRemove.add(0, new MCRecipeManager.ActionBaseRemoveRecipes() {

      @Override
      public void apply() {

        // initialize copy recipes
        for (IAction action : RECIPE_COPY_ACTION_LIST) {
          action.apply();
        }
      }

      @Override
      public String describe() {

        return null;
      }
    });

  }

  public static class CopyRecipesByOutputAction
      extends BaseUndoable {

    private final String tableName;
    private final RecipeBuilder recipeBuilder;
    private final IIngredient[] copyRecipes;
    private final boolean copyOutput;

    protected CopyRecipesByOutputAction(
        String tableName,
        RecipeBuilder recipeBuilder,
        IIngredient[] copyRecipes,
        boolean copyOutput
    ) {

      super("RecipeWorktable");
      this.tableName = tableName;
      this.recipeBuilder = recipeBuilder;
      this.copyRecipes = copyRecipes;
      this.copyOutput = copyOutput;
    }

    @Override
    public void apply() {

      try {
        Collection<IRecipe> recipes = ForgeRegistries.RECIPES.getValuesCollection();

        Set<IRecipe> toCopy = new HashSet<>();

        for (IRecipe recipe : recipes) {

          for (IIngredient copyRecipe : this.copyRecipes) {

            if (!recipe.getRecipeOutput().isEmpty()
                && copyRecipe.matches(CTInputHelper.toIItemStack(recipe.getRecipeOutput()))) {
              toCopy.add(recipe);
            }
          }
        }

        for (IRecipe recipe : toCopy) {
          RecipeBuilder recipeBuilderCopy = this.recipeBuilder.copy();
          RecipeBuilderCopyHelper.copyRecipeInput(recipe, recipeBuilderCopy);

          if (this.copyOutput) {
            RecipeBuilderCopyHelper.copyRecipeOutput(recipe, recipeBuilderCopy);
          }

          recipeBuilderCopy.validate();
          CraftTweaker.LATE_ACTIONS.add(new ZenWorktable.Add(this.tableName, recipeBuilderCopy));
        }

      } catch (Exception e) {
        CTLogHelper.logError("Unable to copy and register recipe", e);
      }
    }

    @Override
    protected String getRecipeInfo() {

      return CTLogHelper.getStackDescription(this.tableName);
    }
  }

  public static class CopyRecipeByNameAction
      extends BaseUndoable {

    private final String tableName;
    private final RecipeBuilder recipeBuilder;
    private final String copyRecipeInputName;
    private final String copyRecipeOutputName;

    CopyRecipeByNameAction(
        String tableName,
        RecipeBuilder recipeBuilder,
        @Nullable String copyRecipeInputName,
        @Nullable String copyRecipeOutputName
    ) {

      super("RecipeWorktable");
      this.tableName = tableName;
      this.recipeBuilder = recipeBuilder;
      this.copyRecipeInputName = copyRecipeInputName;
      this.copyRecipeOutputName = copyRecipeOutputName;
    }

    @Override
    public void apply() {

      // We assume if we're here, one of these must be true.

      boolean copyRecipeInput = this.copyRecipeInputName != null;
      boolean copyRecipeOutput = this.copyRecipeOutputName != null;

      try {
        Set<Map.Entry<ResourceLocation, IRecipe>> entries = ForgeRegistries.RECIPES.getEntries();

        for (Map.Entry<ResourceLocation, IRecipe> recipeEntry : entries) {
          String recipeName = recipeEntry.getKey().toString();

          if (copyRecipeInput && recipeName.equals(this.copyRecipeInputName)) {
            RecipeBuilderCopyHelper.copyRecipeInput(recipeEntry.getValue(), this.recipeBuilder);
            copyRecipeInput = false; // stop looking for input
          }

          if (copyRecipeOutput && recipeName.equals(this.copyRecipeOutputName)) {
            RecipeBuilderCopyHelper.copyRecipeOutput(recipeEntry.getValue(), this.recipeBuilder);
            copyRecipeOutput = false; // stop looking for output
          }

          if (!copyRecipeInput && !copyRecipeOutput) {
            // We've found everything we're looking for, exit the loop.
            break;
          }
        }

        this.recipeBuilder.validate();
        CraftTweaker.LATE_ACTIONS.add(new ZenWorktable.Add(this.tableName, this.recipeBuilder));

      } catch (Exception e) {
        CTLogHelper.logError("Unable to copy and register recipe", e);
      }
    }

    @Override
    protected String getRecipeInfo() {

      return CTLogHelper.getStackDescription(this.tableName);
    }
  }

}

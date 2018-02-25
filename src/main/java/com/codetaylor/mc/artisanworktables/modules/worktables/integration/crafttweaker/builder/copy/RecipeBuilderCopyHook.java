package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import crafttweaker.IAction;
import crafttweaker.mc1120.events.ScriptRunEvent;
import crafttweaker.mc1120.recipes.MCRecipeManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class RecipeBuilderCopyHook {

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

}

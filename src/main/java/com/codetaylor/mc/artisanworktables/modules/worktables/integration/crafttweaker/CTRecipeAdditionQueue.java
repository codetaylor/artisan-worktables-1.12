package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipeAdditionQueue;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.IRecipeBuilderCopyStrategyInternal;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import crafttweaker.mc1120.CraftTweaker;
import crafttweaker.mc1120.events.ScriptRunEvent;
import crafttweaker.mc1120.recipes.MCRecipeManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class CTRecipeAdditionQueue
    implements IRecipeAdditionQueue {

  private final List<RecipeBuilderInternal> recipeBuilderList = new ArrayList<>();
  private final List<RecipeBuilderInternal> recipeBuilderWithCopyList = new ArrayList<>();

  @Override
  public void offer(RecipeBuilderInternal recipeBuilder) {

    this.recipeBuilderList.add(recipeBuilder);
  }

  @Override
  public void offerWithCopy(RecipeBuilderInternal recipeBuilder) {

    this.recipeBuilderWithCopyList.add(recipeBuilder);
  }

  @SubscribeEvent
  public void onScriptEvent(ScriptRunEvent.Post event) {

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
        for (RecipeBuilderInternal builder : CTRecipeAdditionQueue.this.recipeBuilderWithCopyList) {

          try {
            IRecipeBuilderCopyStrategyInternal recipeCopyStrategy = builder.getRecipeCopyStrategy();

            if (recipeCopyStrategy != null) {
              // execute copy strategy
              recipeCopyStrategy.apply(builder, CTRecipeAdditionQueue.this.recipeBuilderList);
            }

          } catch (RecipeBuilderException e) {
            ModuleWorktables.LOG.error("", e);
          }
        }

        for (RecipeBuilderInternal builder : CTRecipeAdditionQueue.this.recipeBuilderList) {

          CraftTweaker.LATE_ACTIONS.add(new CTActionAdd(builder.getTableName(), builder));
        }

        CTRecipeAdditionQueue.this.recipeBuilderWithCopyList.clear();
        CTRecipeAdditionQueue.this.recipeBuilderList.clear();
      }

      @Override
      public String describe() {

        return "ArtisanWorktables recipe hook queue";
      }
    });

  }

}

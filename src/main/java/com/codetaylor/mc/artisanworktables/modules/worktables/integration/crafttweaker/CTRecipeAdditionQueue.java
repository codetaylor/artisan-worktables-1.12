package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipeAdditionQueue;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.IRecipeBuilderCopyStrategyInternal;
import crafttweaker.mc1120.CraftTweaker;
import crafttweaker.mc1120.events.ActionApplyEvent;
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
  public void on(ActionApplyEvent.Pre event) {

    // This is intended to run immediately before CrT applies any actions.

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

}
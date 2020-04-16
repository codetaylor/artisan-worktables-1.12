package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipeAdditionQueue;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.EnumCopyPhase;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.IRecipeBuilderCopyStrategyInternal;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.CraftTweaker;
import crafttweaker.mc1120.events.ActionApplyEvent;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
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

    this.doCopyForPhase(EnumCopyPhase.Pre);

    for (RecipeBuilderInternal builder : CTRecipeAdditionQueue.this.recipeBuilderList) {

      CraftTweaker.LATE_ACTIONS.add(new CTActionAdd(builder.getTableName(), builder));
    }
    CTRecipeAdditionQueue.this.recipeBuilderList.clear();
  }

  @SubscribeEvent
  public void on(ActionApplyEvent.Post event) {

    // This is intended to run after all CrT actions.

    this.doCopyForPhase(EnumCopyPhase.Post);

    for (RecipeBuilderInternal builder : CTRecipeAdditionQueue.this.recipeBuilderList) {

      CraftTweakerAPI.apply(new CTActionAdd(builder.getTableName(), builder));
    }
    CTRecipeAdditionQueue.this.recipeBuilderList.clear();
    CTRecipeAdditionQueue.this.recipeBuilderWithCopyList.clear();
  }

  private void doCopyForPhase(EnumCopyPhase copyPhase) {

    for (RecipeBuilderInternal builder : CTRecipeAdditionQueue.this.recipeBuilderWithCopyList) {

      try {
        IRecipeBuilderCopyStrategyInternal recipeCopyStrategy = builder.getRecipeCopyStrategy();

        if (recipeCopyStrategy != null && recipeCopyStrategy.getCopyPhase() == copyPhase) {
          // execute copy strategy
          recipeCopyStrategy.apply(builder, CTRecipeAdditionQueue.this.recipeBuilderList);
        }

      } catch (Exception e) {
        ModuleWorktables.LOG.error("", e);
      }
    }
  }

}
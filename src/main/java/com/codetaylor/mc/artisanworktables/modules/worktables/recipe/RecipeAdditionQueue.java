package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.IRecipeBuilderCopyStrategyInternal;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdditionQueue
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

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    for (RecipeBuilderInternal builder : this.recipeBuilderWithCopyList) {
      IRecipeBuilderCopyStrategyInternal recipeCopyStrategy = builder.getRecipeCopyStrategy();

      if (recipeCopyStrategy != null) {

        try {
          recipeCopyStrategy.apply(builder, this.recipeBuilderList);

        } catch (RecipeBuilderException e) {
          ModuleWorktables.LOG.error("", e);
        }
      }
    }

    for (RecipeBuilderInternal builder : this.recipeBuilderList) {

      try {
        builder.apply();

      } catch (RecipeBuilderException e) {
        ModuleWorktables.LOG.error("", e);
      }
    }

    this.recipeBuilderList.clear();
    this.recipeBuilderWithCopyList.clear();
  }

}

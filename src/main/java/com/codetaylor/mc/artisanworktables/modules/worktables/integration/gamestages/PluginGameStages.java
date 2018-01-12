package com.codetaylor.mc.artisanworktables.modules.worktables.integration.gamestages;

import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.WorktableNameReference;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei.PluginJEI;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipeWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handles adding and removing worktable recipes from JEI on the client when
 * a game stage is added or removed.
 */
public class PluginGameStages {

  public PluginGameStages() {

    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void gameStageAddedEvent(GameStageEvent.Added event) {

    this.processStagedRecipes(event.getStageName(), (wrapper) -> PluginJEI.RECIPE_REGISTRY.unhideRecipe(wrapper));
  }

  @SubscribeEvent
  public void gameStageRemovedEvent(GameStageEvent.Removed event) {

    this.processStagedRecipes(event.getStageName(), (wrapper) -> PluginJEI.RECIPE_REGISTRY.hideRecipe(wrapper));
  }

  @SubscribeEvent
  public void gameStageClientSyncEvent(GameStageEvent.ClientSync event) {

    Collection<String> unlockedStages = PlayerDataHandler.getStageData(event.getPlayer()).getUnlockedStages();

    for (String unlockedStage : unlockedStages) {
      this.processStagedRecipes(unlockedStage, (wrapper) -> PluginJEI.RECIPE_REGISTRY.unhideRecipe(wrapper));
    }
  }

  private void processStagedRecipes(String stageName, Consumer<IRecipeWrapper> consumer) {

    if (!FMLCommonHandler.instance().getEffectiveSide().isClient()) {
      return;
    }

    if (PluginJEI.RECIPE_REGISTRY == null) {
      return;
    }

    for (String name : WorktableNameReference.getAllowedWorktableNames()) {
      RegistryRecipeWorktable registry = WorktableAPI.getRecipeRegistry(name);

      if (registry != null) {
        List<IRecipeWorktable> recipeList = registry.getRecipeList(new ArrayList<>());
        String uid = PluginJEI.createUID(name);

        for (IRecipeWorktable recipe : recipeList) {

          if (stageName.equals(recipe.getGameStageName())) {
            IRecipeWrapper recipeWrapper = PluginJEI.RECIPE_REGISTRY.getRecipeWrapper(recipe, uid);

            if (recipeWrapper != null) {
              consumer.accept(recipeWrapper);
            }
          }
        }
      }
    }
  }

}

package com.codetaylor.mc.artisanworktables.modules.worktables.integration.gamestages;

import com.codetaylor.mc.artisanworktables.api.ArtisanWorktablesAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei.PluginJEI;
import com.codetaylor.mc.artisanworktables.api.recipe.IAWRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.AWRecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Handles adding and removing worktable recipes from JEI on the client when
 * a game stage is added or removed.
 */
public class PluginGameStages {

  public PluginGameStages() {

    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void gameStageAddedEvent(GameStageEvent.Added event) {

    this.processStagedRecipes();
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void gameStageRemovedEvent(GameStageEvent.Removed event) {

    this.processStagedRecipes();
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void gameStageClientSyncEvent(GameStageEvent.ClientSync event) {

    this.processStagedRecipes();
  }

  @SideOnly(Side.CLIENT)
  private void processStagedRecipes() {

    if (!FMLCommonHandler.instance().getEffectiveSide().isClient()) {
      return;
    }

    if (PluginJEI.RECIPE_REGISTRY == null) {
      return;
    }

    Collection<String> unlockedStages = PlayerDataHandler.getStageData(Minecraft.getMinecraft().player)
        .getUnlockedStages();

    for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
      AWRecipeRegistry registry = ArtisanWorktablesAPI.getWorktableRecipeRegistry(name);

      if (registry != null) {

        for (EnumTier tier : EnumTier.values()) {
          List<IAWRecipe> recipeList = registry.getRecipeListByTier(new ArrayList<>(), tier);

          for (IAWRecipe recipe : recipeList) {
            String uid = PluginJEI.createUID(name, tier);
            IRecipeWrapper recipeWrapper = PluginJEI.RECIPE_REGISTRY.getRecipeWrapper(recipe, uid);

            if (recipeWrapper == null) {
              continue;
            }

            if (recipe.matchGameStages(unlockedStages)) {
              PluginJEI.RECIPE_REGISTRY.unhideRecipe(recipeWrapper);

            } else {
              PluginJEI.RECIPE_REGISTRY.hideRecipe(recipeWrapper);
            }
          }
        }
      }
    }
  }

}

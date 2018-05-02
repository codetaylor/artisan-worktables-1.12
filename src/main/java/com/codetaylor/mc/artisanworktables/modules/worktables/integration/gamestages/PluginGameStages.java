package com.codetaylor.mc.artisanworktables.modules.worktables.integration.gamestages;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesRequirement;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesRequirementContext;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei.PluginJEI;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.darkhax.gamestages.event.GameStageEvent;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
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
  public void gameStageClientSyncEvent(StagesSyncedEvent event) {

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

    EntityPlayerSP player = Minecraft.getMinecraft().player;
    Collection<String> unlockedStages = GameStagesHelper.getUnlockedStages(player);
    GameStagesRequirementContext context = (GameStagesRequirementContext) ArtisanAPI
        .getRequirementContext(new ResourceLocation(ModArtisanWorktables.MOD_ID, "gamestages"));
    context.setUnlockedStages(unlockedStages);

    for (String name : ArtisanAPI.getWorktableNames()) {
      RecipeRegistry registry = ArtisanAPI.getWorktableRecipeRegistry(name);

      if (registry != null) {

        for (EnumTier tier : EnumTier.values()) {
          List<IArtisanRecipe> recipeList = registry.getRecipeListByTier(new ArrayList<>(), tier);
          String uid = PluginJEI.createUID(name, tier);

          for (IArtisanRecipe recipe : recipeList) {
            IRecipeWrapper recipeWrapper = PluginJEI.RECIPE_REGISTRY.getRecipeWrapper(recipe, uid);

            if (recipeWrapper == null) {
              continue;
            }

            IRequirement requirement = recipe.getRequirement(GameStagesRequirement.LOCATION);

            //noinspection unchecked
            if (requirement == null || requirement.match(context)) {
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

package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.api.ArtisanWorktablesAPI;
import com.codetaylor.mc.artisanworktables.api.recipe.AWRecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.recipe.IAWRecipe;
import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.Recipe;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PluginJEI
    implements IModPlugin {

  public static IRecipeRegistry RECIPE_REGISTRY;

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {

    JEICategoryFactory categoryFactory = new JEICategoryFactory(registry.getJeiHelpers().getGuiHelper());

    for (EnumTier tier : EnumTier.values()) {

      if (!ModuleWorktablesConfig.isTierEnabled(tier)) {
        continue;
      }

      for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
        registry.addRecipeCategories(categoryFactory.createCategory(name, tier));
      }
    }
  }

  @Override
  public void register(IModRegistry registry) {

    for (EnumTier tier : EnumTier.values()) {

      if (!ModuleWorktablesConfig.isTierEnabled(tier)) {
        continue;
      }

      for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
        registry.addRecipeCatalyst(
            this.getWorktableAsItemStack(name, tier),
            PluginJEI.createUID(name, tier)
        );
      }

      for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
        registry.handleRecipes(
            Recipe.class,
            JEIRecipeWrapper::new,
            PluginJEI.createUID(name, tier)
        );
      }

      IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();
      for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
        transferRegistry.addRecipeTransferHandler(new JEIRecipeTransferInfoWorktable(
            name,
            PluginJEI.createUID(name, tier),
            tier
        ));
      }

      for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
        List<IAWRecipe> recipeList = new ArrayList<>();
        AWRecipeRegistry recipeRegistry = ArtisanWorktablesAPI.getWorktableRecipeRegistry(name);
        recipeList = recipeRegistry.getRecipeListByTier(recipeList, tier);
        registry.addRecipes(recipeList, PluginJEI.createUID(name, tier));
      }
    }
  }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    // Expose the recipe registry for use in the game stages event handler.
    RECIPE_REGISTRY = jeiRuntime.getRecipeRegistry();

    // If gamestages is loaded, hide all of the staged worktable recipes from JEI.
    if (ModuleWorktables.MOD_LOADED_GAMESTAGES) {
      Set<String> unlockedStages = Collections.emptySet();

      for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
        AWRecipeRegistry registry = ArtisanWorktablesAPI.getWorktableRecipeRegistry(name);

        if (registry != null) {

          for (EnumTier tier : EnumTier.values()) {
            List<IAWRecipe> recipeList = registry.getRecipeListByTier(new ArrayList<>(), tier);

            for (IAWRecipe recipe : recipeList) {

              if (!recipe.matchGameStages(unlockedStages)) {
                IRecipeWrapper recipeWrapper = RECIPE_REGISTRY.getRecipeWrapper(
                    recipe,
                    PluginJEI.createUID(name, tier)
                );

                if (recipeWrapper != null) {
                  RECIPE_REGISTRY.hideRecipe(recipeWrapper);
                }
              }
            }
          }
        }
      }
    }
  }

  public static String createUID(String name, EnumTier tier) {

    return ModuleWorktables.MOD_ID + "_" + name + "_" + tier.getName();
  }

  private ItemStack getWorktableAsItemStack(String name, EnumTier tier) {

    EnumType type = EnumType.fromName(name);

    switch (tier) {

      case WORKTABLE:
        return new ItemStack(ModuleWorktables.Blocks.WORKTABLE, 1, type.getMeta());

      case WORKSTATION:
        return new ItemStack(ModuleWorktables.Blocks.WORKSTATION, 1, type.getMeta());

      case WORKSHOP:
        return new ItemStack(ModuleWorktables.Blocks.WORKSHOP, 1, type.getMeta());

      default:
        throw new IllegalArgumentException("Unknown tier: " + tier);
    }
  }
}

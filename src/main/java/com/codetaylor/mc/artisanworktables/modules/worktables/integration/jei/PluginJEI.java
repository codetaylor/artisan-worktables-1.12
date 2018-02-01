package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipeWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import mezz.jei.api.*;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PluginJEI
    implements IModPlugin {

  public static IRecipeRegistry RECIPE_REGISTRY;

  private IJeiHelpers jeiHelpers;

  @Override
  public void register(IModRegistry registry) {

    this.jeiHelpers = registry.getJeiHelpers();

    // Workbench
    for (String name : WorktableAPI.getWorktableNames()) {
      registry.addRecipeCategories(this.createWorkbenchCategory(name, registry.getJeiHelpers().getGuiHelper()));
    }

    for (String name : WorktableAPI.getWorktableNames()) {
      registry.addRecipeCatalyst(
          WorktableAPI.getWorktableAsItemStack(name),
          PluginJEI.createUID(name)
      );
    }

    for (String name : WorktableAPI.getWorktableNames()) {
      registry.handleRecipes(
          RecipeWorktable.class,
          recipe -> new JEIRecipeWrapperWorktable(recipe, this.jeiHelpers.getGuiHelper()),
          PluginJEI.createUID(name)
      );
    }

    // TODO: recipe click area

    // Transfer handlers
    IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();
    for (String name : WorktableAPI.getWorktableNames()) {
      transferRegistry.addRecipeTransferHandler(new JEIRecipeTransferInfoWorktable(
          name,
          PluginJEI.createUID(name)
      ));
    }

    for (String name : WorktableAPI.getWorktableNames()) {
      List<IRecipeWorktable> recipeList = new ArrayList<>();
      RegistryRecipeWorktable recipeRegistry = WorktableAPI.getWorktableRecipeRegistry(name);
      recipeRegistry.getRecipeList(recipeList);
      registry.addRecipes(recipeList, PluginJEI.createUID(name));
    }
  }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    // Expose the recipe registry for use in the game stages event handler.
    RECIPE_REGISTRY = jeiRuntime.getRecipeRegistry();

    // If gamestages is loaded, hide all of the staged worktable recipes from JEI.
    if (ModuleWorktables.MOD_LOADED_GAMESTAGES) {
      Set<String> unlockedStages = Collections.emptySet();

      for (String name : WorktableAPI.getWorktableNames()) {
        RegistryRecipeWorktable registry = WorktableAPI.getWorktableRecipeRegistry(name);

        if (registry != null) {
          List<IRecipeWorktable> recipeList = registry.getRecipeList(new ArrayList<>());

          for (IRecipeWorktable recipe : recipeList) {

            if (!recipe.matchGameStages(unlockedStages)) {
              IRecipeWrapper recipeWrapper = RECIPE_REGISTRY.getRecipeWrapper(
                  recipe,
                  PluginJEI.createUID(name)
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

  private JEICategoryWorktable createWorkbenchCategory(String name, IGuiHelper guiHelper) {

    return new JEICategoryWorktable(
        PluginJEI.createUID(name),
        this.createTitleTranslateKey(name),
        this.createBackground(name),
        guiHelper
    );
  }

  private IDrawable createBackground(String name) {

    IGuiHelper guiHelper = this.jeiHelpers.getGuiHelper();
    ResourceLocation resourceLocation = new ResourceLocation(
        ModuleWorktables.MOD_ID,
        String.format(ModuleWorktables.Textures.WORKTABLE_GUI, name)
    );
    return guiHelper.createDrawable(resourceLocation, 0 + 3, 0 + 3, 176 - 6, 80);
  }

  private String createTitleTranslateKey(String name) {

    return String.format(ModuleWorktables.Lang.WORKTABLE_TITLE, name);
  }

  public static String createUID(String name) {

    return ModuleWorktables.MOD_ID + "_" + name;
  }
}

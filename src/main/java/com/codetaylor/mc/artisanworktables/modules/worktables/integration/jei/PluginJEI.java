package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.event.ArtisanUpdateJEIRecipeVisibilityEvent;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.api.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PluginJEI
    implements IModPlugin {

  public static IRecipeRegistry RECIPE_REGISTRY;

  private IIngredientRegistry ingredientRegistry;

  public PluginJEI() {

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {

    JEICategoryFactory categoryFactory = new JEICategoryFactory(registry.getJeiHelpers().getGuiHelper());

    for (EnumTier tier : EnumTier.values()) {

      if (!ModuleWorktablesConfig.isTierEnabled(tier)) {
        continue;
      }

      for (String name : ArtisanAPI.getWorktableNames()) {
        registry.addRecipeCategories(categoryFactory.createCategory(name, tier));
      }
    }
  }

  @Override
  public void register(IModRegistry registry) {

    registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModuleWorktables.Blocks.TAB_ICON));

    this.ingredientRegistry = registry.getIngredientRegistry();

    for (EnumTier tier : EnumTier.values()) {

      if (!ModuleWorktablesConfig.isTierEnabled(tier)) {
        continue;
      }

      for (String name : ArtisanAPI.getWorktableNames()) {
        registry.addRecipeCatalyst(
            this.getWorktableAsItemStack(name, tier),
            PluginJEI.createUID(name, tier)
        );
      }

      for (String name : ArtisanAPI.getWorktableNames()) {
        registry.handleRecipes(
            ArtisanRecipe.class,
            JEIRecipeWrapper::new,
            PluginJEI.createUID(name, tier)
        );
      }

      IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();
      for (String name : ArtisanAPI.getWorktableNames()) {
        transferRegistry.addRecipeTransferHandler(new JEIRecipeTransferInfoWorktable(
            name,
            PluginJEI.createUID(name, tier),
            tier
        ));
      }

      // Used to allow transferring vanilla recipes into tables that support the feature.
      transferRegistry.addRecipeTransferHandler(new JEIRecipeTransferInfoVanilla());

      for (String name : ArtisanAPI.getWorktableNames()) {
        List<IArtisanRecipe> recipeList = new ArrayList<>();
        RecipeRegistry recipeRegistry = ArtisanAPI.getWorktableRecipeRegistry(name);
        recipeList = recipeRegistry.getRecipeListByTier(tier, recipeList);
        registry.addRecipes(recipeList, PluginJEI.createUID(name, tier));
      }
    }
  }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    // Expose the recipe registry for use in the game stages event handler.
    RECIPE_REGISTRY = jeiRuntime.getRecipeRegistry();

    this.hideTables();
    this.hideRecipes();
  }

  private void hideTables() {

    Collection<ItemStack> toRemove = new ArrayList<>();

    for (Map.Entry<String, Boolean> entry : ModuleWorktablesConfig.ENABLE_TABLE_TYPE.entrySet()) {

      if (!entry.getValue()) {

        for (EnumTier tier : EnumTier.values()) {

          if (ModuleWorktablesConfig.isTierEnabled(tier)) {
            toRemove.add(this.getWorktableAsItemStack(entry.getKey(), tier));
          }
        }
      }
    }

    if (!toRemove.isEmpty()) {
      this.ingredientRegistry.removeIngredientsAtRuntime(ItemStack.class, toRemove);
    }
  }

  private void hideRecipes() {

    // Hide recipes that should be hidden.
    for (String name : ArtisanAPI.getWorktableNames()) {
      RecipeRegistry registry = ArtisanAPI.getWorktableRecipeRegistry(name);

      if (registry != null) {

        for (EnumTier tier : EnumTier.values()) {
          List<IArtisanRecipe> recipeList = registry.getRecipeListByTier(tier, new ArrayList<>());

          for (IArtisanRecipe recipe : recipeList) {

            if (recipe.isHidden() || this.shouldHideRecipe(recipe)) {
              String uid = PluginJEI.createUID(name, tier);
              IRecipeWrapper recipeWrapper = RECIPE_REGISTRY.getRecipeWrapper(recipe, uid);

              if (recipeWrapper != null) {
                RECIPE_REGISTRY.hideRecipe(recipeWrapper, uid);
              }
            }
          }
        }
      }
    }
  }

  private boolean shouldHideRecipe(IArtisanRecipe recipe) {

    Collection<IRequirement> values = recipe.getRequirements().values();

    for (IRequirement requirement : values) {

      if (requirement.shouldJEIHideOnLoad()) {
        return true;
      }
    }

    return false;
  }

  @SubscribeEvent
  public void on(ArtisanUpdateJEIRecipeVisibilityEvent event) {

    if (!FMLCommonHandler.instance().getEffectiveSide().isClient()) {
      return;
    }

    if (PluginJEI.RECIPE_REGISTRY == null) {
      return;
    }

    // loop through each worktable type, each tier, each recipe

    for (String name : ArtisanAPI.getWorktableNames()) {
      RecipeRegistry registry = ArtisanAPI.getWorktableRecipeRegistry(name);

      if (registry != null) {

        for (EnumTier tier : EnumTier.values()) {
          List<IArtisanRecipe> recipeList = registry.getRecipeListByTier(tier, new ArrayList<>());
          String uid = PluginJEI.createUID(name, tier);

          for (IArtisanRecipe recipe : recipeList) {
            IRecipeWrapper recipeWrapper = PluginJEI.RECIPE_REGISTRY.getRecipeWrapper(recipe, uid);

            if (recipeWrapper == null) {
              continue;
            }

            boolean shouldHide = false;

            for (IRequirement requirement : recipe.getRequirements().values()) {

              if (requirement.shouldJEIHideOnUpdate()) {
                shouldHide = true;
                break;
              }
            }

            //noinspection unchecked
            if (shouldHide) {
              PluginJEI.RECIPE_REGISTRY.hideRecipe(recipeWrapper, uid);

            } else {
              PluginJEI.RECIPE_REGISTRY.unhideRecipe(recipeWrapper, uid);
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

    return this.getWorktableAsItemStack(EnumType.fromName(name), tier);
  }

  private ItemStack getWorktableAsItemStack(EnumType type, EnumTier tier) {

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

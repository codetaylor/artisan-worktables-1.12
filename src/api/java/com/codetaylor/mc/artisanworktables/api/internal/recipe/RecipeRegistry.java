package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.*;

public class RecipeRegistry
    extends IForgeRegistryEntry.Impl<RecipeRegistry> {

  private final List<IArtisanRecipe> recipeList;
  private final Map<String, IArtisanRecipe> recipeMap;

  public RecipeRegistry(String modId, String name) {

    this.setRegistryName(modId, name);
    this.recipeList = Collections.synchronizedList(new ArrayList<>());
    this.recipeMap = new HashMap<>();
  }

  public List<IArtisanRecipe> getRecipeListByTier(EnumTier tier, List<IArtisanRecipe> result) {

    synchronized (this.recipeList) {

      for (IArtisanRecipe recipe : this.recipeList) {

        if (recipe.matchTier(tier)) {
          result.add(recipe);
        }
      }
    }

    return result;
  }

  @Nullable
  public IArtisanRecipe addRecipe(IArtisanRecipe recipe) {

    String recipeName = recipe.getName();

    if (this.recipeMap.containsKey(recipeName)) {
      LogManager.getLogger(ArtisanAPI.MOD_ID.get()).error("Duplicate recipe registration skipped: " + recipeName);
      return null;

    } else {
      this.recipeList.add(recipe);
      this.recipeMap.put(recipeName, recipe);
      return recipe;
    }
  }

  public boolean hasRecipe(String recipeName) {

    return this.recipeMap.containsKey(recipeName);
  }

  @Nullable
  public IArtisanRecipe getRecipe(String recipeName) {

    return this.recipeMap.get(recipeName);
  }

  @Nullable
  public IArtisanRecipe findRecipe(
      int playerExperience,
      int playerLevels,
      boolean isPlayerCreative,
      ItemStack[] tools,
      ICraftingMatrixStackHandler craftingMatrix,
      @Nullable FluidStack fluidStack,
      ISecondaryIngredientMatcher secondaryIngredientMatcher,
      EnumTier tier,
      Map<ResourceLocation, IRequirementContext> requirementContextMap
  ) {

    // If the recipe list is empty, short-circuit.
    if (this.recipeList.isEmpty()) {
      return null;
    }

    synchronized (this.recipeList) {

      // Next, check the last recipe first.
      IArtisanRecipe lastRecipe = this.recipeList.get(this.recipeList.size() - 1);
      boolean lastRecipeMatches = lastRecipe.matches(
          requirementContextMap,
          playerExperience,
          playerLevels,
          isPlayerCreative,
          tools,
          craftingMatrix,
          fluidStack,
          secondaryIngredientMatcher,
          tier
      );

      if (lastRecipeMatches) {
        return lastRecipe;
      }

      // Next, loop through the remaining recipes in reverse.
      for (int i = this.recipeList.size() - 2; i >= 0; i--) {
        IArtisanRecipe recipe = this.recipeList.get(i);

        boolean matches = recipe.matches(
            requirementContextMap,
            playerExperience,
            playerLevels,
            isPlayerCreative,
            tools,
            craftingMatrix,
            fluidStack,
            secondaryIngredientMatcher,
            tier
        );

        if (matches) {
          // If the recipe matches, move it to the end of the list. This ensures that the
          // recipe will be checked faster next time, increasing performance for shift +
          // click crafting operations.
          //
          // Worst case remove: O(n) for re-indexing.
          // Worst case add: O(1) because we're adding to the end of the list.
          this.recipeList.remove(i);
          this.recipeList.add(recipe);
          return recipe;
        }
      }
    }

    // Finally, if no recipe was matched, return null.
    return null;
  }

  public boolean containsRecipeWithTool(ItemStack tool) {

    synchronized (this.recipeList) {

      for (IArtisanRecipe recipe : this.recipeList) {

        if (recipe.usesTool(tool)) {
          return true;
        }
      }
    }

    return false;
  }

}

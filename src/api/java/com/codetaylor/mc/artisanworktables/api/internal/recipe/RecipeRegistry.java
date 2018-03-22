package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeRegistry
    extends IForgeRegistryEntry.Impl<RecipeRegistry> {

  private List<IArtisanRecipe> recipeList;

  public RecipeRegistry(String modId, String name) {

    this.setRegistryName(modId, name);
    this.recipeList = new ArrayList<>();
  }

  public List<IArtisanRecipe> getRecipeList(List<IArtisanRecipe> result) {

    result.addAll(this.recipeList);
    return result;
  }

  public List<IArtisanRecipe> getRecipeListByTier(List<IArtisanRecipe> result, EnumTier tier) {

    for (IArtisanRecipe recipe : this.recipeList) {

      if (recipe.matchTier(tier)) {
        result.add(recipe);
      }
    }

    return result;
  }

  public IArtisanRecipe addRecipe(IArtisanRecipe recipe) {

    this.recipeList.add(recipe);
    return recipe;
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

    // Finally, if no recipe was matched, return null.
    return null;
  }

  public boolean containsRecipeWithToolInSlot(ItemStack tool, int toolIndex) {

    for (IArtisanRecipe recipe : this.recipeList) {

      if (recipe.isValidTool(tool, toolIndex)) {
        return true;
      }
    }

    return false;
  }

  public boolean containsRecipeWithToolInAnySlot(ItemStack tool) {

    for (IArtisanRecipe recipe : this.recipeList) {
      int toolCount = recipe.getToolCount();

      for (int i = 0; i < toolCount; i++) {

        if (recipe.isValidTool(tool, i)) {
          return true;
        }
      }
    }

    return false;
  }

}

package com.codetaylor.mc.artisanworktables.api.recipe;

import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AWRecipeRegistry {

  private List<IAWRecipe> recipeList;

  public AWRecipeRegistry() {

    this.recipeList = new ArrayList<>();
  }

  public List<IAWRecipe> getRecipeList(List<IAWRecipe> result) {

    result.addAll(this.recipeList);
    return result;
  }

  public List<IAWRecipe> getRecipeListByTier(List<IAWRecipe> result, EnumTier tier) {

    for (IAWRecipe recipe : this.recipeList) {

      if (recipe.matchTier(tier)) {
        result.add(recipe);
      }
    }

    return result;
  }

  public IAWRecipe addRecipe(IAWRecipe recipe) {

    this.recipeList.add(recipe);
    return recipe;
  }

  @Nullable
  public IAWRecipe findRecipe(
      int playerExperience,
      int playerLevels,
      boolean isPlayerCreative,
      ItemStack[] tools,
      ICraftingMatrixStackHandler craftingMatrix,
      @Nullable FluidStack fluidStack,
      ISecondaryIngredientMatcher secondaryIngredientMatcher,
      EnumTier tier,
      Collection<String> unlockedPlayerStages
  ) {

    for (IAWRecipe recipe : this.recipeList) {

      boolean matches = recipe.matches(
          unlockedPlayerStages,
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
        return recipe;
      }
    }

    return null;
  }

  public boolean containsRecipeWithToolInSlot(ItemStack tool, int toolIndex) {

    for (IAWRecipe recipe : this.recipeList) {

      if (recipe.isValidTool(tool, toolIndex)) {
        return true;
      }
    }

    return false;
  }

  public boolean containsRecipeWithToolInAnySlot(ItemStack tool) {

    for (IAWRecipe recipe : this.recipeList) {
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

package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.CraftingMatrixStackHandler;
import com.codetaylor.mc.athenaeum.util.EnchantmentHelper;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RegistryRecipe {

  private List<IRecipe> recipeList;

  public RegistryRecipe() {

    this.recipeList = new ArrayList<>();
  }

  public List<IRecipe> getRecipeList(List<IRecipe> result) {

    result.addAll(this.recipeList);
    return result;
  }

  public List<IRecipe> getRecipeListByTier(List<IRecipe> result, EnumTier tier) {

    for (IRecipe recipe : this.recipeList) {

      if (recipe.matchTier(tier)) {
        result.add(recipe);
      }
    }

    return result;
  }

  public IRecipe addRecipe(IRecipe recipe) {

    this.recipeList.add(recipe);
    return recipe;
  }

  @Nullable
  public IRecipe findRecipe(
      EntityPlayer player,
      ItemStack[] tools,
      CraftingMatrixStackHandler craftingMatrix,
      @Nullable FluidStack fluidStack,
      ISecondaryIngredientMatcher secondaryIngredientMatcher,
      EnumTier tier
  ) {

    Collection<String> unlockedStages;

    if (ModuleWorktables.MOD_LOADED_GAMESTAGES
        && player != null) {
      unlockedStages = this.getUnlockedStages(player);

    } else {
      unlockedStages = Collections.emptySet();
    }

    int xp = 0;
    int levels = 0;
    boolean isCreative = false;

    if (player != null) {
      xp = EnchantmentHelper.getPlayerExperienceTotal(player);
      levels = player.experienceLevel;
      isCreative = player.isCreative();
    }

    for (IRecipe recipe : this.recipeList) {

      boolean matches = recipe.matches(
          unlockedStages,
          xp,
          levels,
          isCreative,
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

    for (IRecipe recipe : this.recipeList) {

      if (recipe.isValidTool(tool, toolIndex)) {
        return true;
      }
    }

    return false;
  }

  public boolean containsRecipeWithToolInAnySlot(ItemStack tool) {

    for (IRecipe recipe : this.recipeList) {
      int toolCount = recipe.getToolCount();

      for (int i = 0; i < toolCount; i++) {

        if (recipe.isValidTool(tool, i)) {
          return true;
        }
      }
    }

    return false;
  }

  private Collection<String> getUnlockedStages(EntityPlayer player) {

    return PlayerDataHandler.getStageData(player).getUnlockedStages();
  }
}

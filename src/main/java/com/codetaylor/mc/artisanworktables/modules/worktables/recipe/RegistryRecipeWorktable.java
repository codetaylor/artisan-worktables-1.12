package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.CraftingMatrixStackHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RegistryRecipeWorktable {

  private List<IRecipeWorktable> recipeList;

  public RegistryRecipeWorktable() {

    this.recipeList = new ArrayList<>();
  }

  public List<IRecipeWorktable> getRecipeList(List<IRecipeWorktable> result) {

    result.addAll(this.recipeList);
    return result;
  }

  public IRecipeWorktable addRecipe(IRecipeWorktable recipe) {

    this.recipeList.add(recipe);
    return recipe;
  }

  @Nullable
  public IRecipeWorktable findRecipe(
      EntityPlayer player,
      ItemStack tool,
      CraftingMatrixStackHandler craftingMatrix
  ) {

    Collection<String> unlockedStages;

    if (ModuleWorktables.MOD_LOADED_GAMESTAGES
        && player != null) {
      unlockedStages = this.getUnlockedStages(player);

    } else {
      unlockedStages = Collections.emptySet();
    }

    for (IRecipeWorktable recipe : this.recipeList) {

      if (recipe.matches(unlockedStages, tool, craftingMatrix)) {
        return recipe;
      }
    }

    return null;
  }

  public boolean containsRecipeWithTool(ItemStack tool) {

    for (IRecipeWorktable recipe : this.recipeList) {

      if (recipe.isValidTool(tool)) {
        return true;
      }
    }

    return false;
  }

  private Collection<String> getUnlockedStages(EntityPlayer player) {

    return PlayerDataHandler.getStageData(player).getUnlockedStages();
  }
}

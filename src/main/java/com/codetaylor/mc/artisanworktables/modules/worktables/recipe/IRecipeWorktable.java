package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.CraftingMatrixStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import java.util.Collection;
import java.util.List;

public interface IRecipeWorktable {

  boolean matches(
      Collection<String> unlockedStages,
      ItemStack tool,
      CraftingMatrixStackHandler craftingMatrix
  );

  int getWidth();

  int getHeight();

  boolean isShaped();

  boolean isMirrored();

  int getToolDamage();

  boolean matchGameStages(Collection<String> unlockedStages);

  ItemStack getSecondaryOutput();

  float getSecondaryOutputChance();

  ItemStack getTertiaryOutput();

  float getTertiaryOutputChance();

  ItemStack getQuaternaryOutput();

  float getQuaternaryOutputChance();

  boolean isValidTool(ItemStack tool);

  ItemStack[] getTools();

  List<Ingredient> getIngredients();

  List<OutputWeightPair> getOutput();
}

package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.CraftingMatrixStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public interface IRecipeWorktable {

  boolean matches(
      Collection<String> unlockedStages,
      ItemStack tool,
      CraftingMatrixStackHandler craftingMatrix,
      FluidStack fluidStack
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

  List<Ingredient> getIngredientList();

  FluidStack getFluidIngredient();

  List<OutputWeightPair> getOutputWeightPairList();

  ItemStack selectOutput(Random random);

  ItemStack getBaseOutput();

  boolean hasMultipleWeightedOutputs();
}

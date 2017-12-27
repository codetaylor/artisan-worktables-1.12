package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.CraftingMatrixStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public interface IRecipeWorktable {

  boolean matches(ItemStack tool, CraftingMatrixStackHandler craftingMatrix);

  int getToolDamage();

  boolean isValidTool(ItemStack tool);

  ItemStack[] getTools();

  NonNullList<Ingredient> getIngredients();

  ItemStack getOutput();
}

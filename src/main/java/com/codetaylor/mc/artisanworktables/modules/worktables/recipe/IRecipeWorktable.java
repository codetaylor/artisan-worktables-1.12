package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.CraftingMatrixStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

public interface IRecipeWorktable {

  boolean matches(
      EntityPlayer player,
      ItemStack tool,
      CraftingMatrixStackHandler craftingMatrix
  );

  int getToolDamage();

  @Nullable
  String getGameStageName();

  boolean isValidTool(ItemStack tool);

  ItemStack[] getTools();

  NonNullList<Ingredient> getIngredients();

  ItemStack getOutput();
}

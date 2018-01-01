package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.CraftingMatrixStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.ArrayList;
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

  public IRecipeWorktable addRecipeShaped(
      ItemStack result,
      Ingredient tool,
      Ingredient[][] inputs,
      int toolDamage,
      boolean mirrored
  ) {

    NonNullList<Ingredient> inputList = NonNullList.create();
    int width = 0;

    for (Ingredient[] row : inputs) {

      if (row.length > width) {
        width = row.length;
      }

      Collections.addAll(inputList, row);
    }

    int height = inputs.length;

    RecipeWorktableShaped recipe = new RecipeWorktableShaped(
        width,
        height,
        tool.getMatchingStacks(),
        inputList,
        result,
        toolDamage,
        mirrored
    );

    this.recipeList.add(recipe);
    return recipe;
  }

  public IRecipeWorktable addRecipeShapeless(
      ItemStack result,
      Ingredient tool,
      Ingredient[] inputs,
      int toolDamage
  ) {

    NonNullList<Ingredient> inputList = NonNullList.create();
    Collections.addAll(inputList, inputs);

    IRecipeWorktable recipe = new RecipeWorktableShapeless(
        tool.getMatchingStacks(),
        inputList,
        result,
        toolDamage
    );

    this.recipeList.add(recipe);
    return recipe;
  }

  @Nullable
  public IRecipeWorktable findRecipe(ItemStack tool, CraftingMatrixStackHandler craftingMatrix) {

    for (IRecipeWorktable recipe : this.recipeList) {

      if (recipe.matches(tool, craftingMatrix)) {
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
}

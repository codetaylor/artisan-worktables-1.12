package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import net.minecraft.item.ItemStack;

import java.util.List;

public class WorkshopCraftingGridHelper
    implements ICraftingGridHelper {

  private final int craftInputSlot1;
  private final int craftOutputSlot;

  public WorkshopCraftingGridHelper(int craftInputSlot1, int craftOutputSlot) {

    this.craftInputSlot1 = craftInputSlot1;
    this.craftOutputSlot = craftOutputSlot;
  }

  @Override
  public <T> void setInputs(IGuiIngredientGroup<T> ingredientGroup, List<List<T>> inputs) {

    int width, height;

    if (inputs.size() > 16) {
      width = height = 5;

    } else if (inputs.size() > 9) {
      width = height = 4;

    } else if (inputs.size() > 4) {
      width = height = 3;

    } else if (inputs.size() > 1) {
      width = height = 2;

    } else {
      width = height = 1;
    }

    setInputs(ingredientGroup, inputs, width, height);
  }

  @Override
  public <T> void setInputs(IGuiIngredientGroup<T> ingredientGroup, List<List<T>> inputs, int width, int height) {

    for (int i = 0; i < inputs.size(); i++) {
      List<T> recipeItem = inputs.get(i);
      int index = getCraftingIndex(i, width, height);

      setInput(ingredientGroup, index, recipeItem);
    }
  }

  private <T> void setInput(IRecipeLayout recipeLayout, int inputIndex, List<T> input) {

    if (!input.isEmpty()) {
      T firstInput = input.get(0);
      //noinspection unchecked
      Class<T> ingredientClass = (Class<T>) firstInput.getClass();
      IGuiIngredientGroup<T> ingredientsGroup = recipeLayout.getIngredientsGroup(ingredientClass);
      setInput(ingredientsGroup, inputIndex, input);
    }
  }

  private <T> void setInput(IGuiIngredientGroup<T> guiIngredients, int inputIndex, List<T> input) {

    guiIngredients.set(this.craftInputSlot1 + inputIndex, input);
  }

  @Override
  public void setInputStacks(IGuiItemStackGroup guiItemStacks, List<List<ItemStack>> input) {

    int width, height;

    if (input.size() > 16) {
      width = height = 5;

    } else if (input.size() > 9) {
      width = height = 4;

    } else if (input.size() > 4) {
      width = height = 3;

    } else if (input.size() > 1) {
      width = height = 2;

    } else {
      width = height = 1;
    }

    setInputStacks(guiItemStacks, input, width, height);
  }

  @Override
  public void setInputStacks(IGuiItemStackGroup guiItemStacks, List<List<ItemStack>> input, int width, int height) {

    for (int i = 0; i < input.size(); i++) {
      List<ItemStack> recipeItem = input.get(i);
      int index = getCraftingIndex(i, width, height);

      setInput(guiItemStacks, index, recipeItem);
    }
  }

  private int getCraftingIndex(int i, int width, int height) {

    int result = (i / width) * 5 + (i % width);
    return result;
  }

  @Override
  public void setOutput(IGuiItemStackGroup guiItemStacks, List<ItemStack> output) {

    guiItemStacks.set(this.craftOutputSlot, output);
  }
}

package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumTier;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class JEICategoryWorkstation
    extends JEICategoryBase {

  private final ICraftingGridHelper craftingGridHelper;

  public JEICategoryWorkstation(
      String name,
      EnumTier tier,
      String uid,
      String titleTranslateKey,
      IDrawable background,
      IGuiHelper guiHelper
  ) {

    super(titleTranslateKey, background, name, tier, uid, guiHelper);
    this.craftingGridHelper = guiHelper.createCraftingGridHelper(1, 0);
  }

  @Override
  public void setRecipe(
      IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients
  ) {

    IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
    IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();

    JEIRecipeWrapper wrapper = (JEIRecipeWrapper) recipeWrapper;
    List<List<ItemStack>> tools = wrapper.getTools();
    List<List<ItemStack>> inputs = wrapper.getInputs();
    List<List<ItemStack>> secondaryInputs = wrapper.getSecondaryInputs();
    List<ItemStack> outputs = wrapper.getOutput();

    stacks.init(0, false, 108 - 3 + 6, 34 - 3);
    stacks.set(0, outputs);

    this.setupTooltip(stacks, wrapper.getWeightedOutput());

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int index = 1 + x + (y * 3);
        stacks.init(index, true, x * 18 + 16, y * 18 + 13);
      }
    }

    if (wrapper.isShaped()) {
      this.craftingGridHelper.setInputs(stacks, inputs, wrapper.getWidth(), wrapper.getHeight());

    } else {
      this.craftingGridHelper.setInputs(stacks, inputs);
    }

    stacks.init(10, true, 74, 20);
    stacks.set(10, tools.get(0));

    stacks.init(11, false, 148, 13);
    stacks.init(12, false, 148, 18 + 13);
    stacks.init(13, false, 148, 36 + 13);

    ItemStack extraOutput = wrapper.getSecondaryOutput();

    if (!extraOutput.isEmpty()) {
      stacks.set(11, extraOutput);
    }

    extraOutput = wrapper.getTertiaryOutput();

    if (!extraOutput.isEmpty()) {
      stacks.set(12, extraOutput);
    }

    extraOutput = wrapper.getQuaternaryOutput();

    if (!extraOutput.isEmpty()) {
      stacks.set(13, extraOutput);
    }

    int capacity = ModuleWorktablesConfig.FLUID_CAPACITY_WORKTABLE.get(this.tableName.toLowerCase());

    fluidStacks.init(14, true, 5, 14, 6, 52, capacity, true, null);
    fluidStacks.set(14, wrapper.getFluidStack());

    stacks.init(15, true, 74, 20 + 22);

    if (tools.size() > 1) {
      stacks.set(15, tools.get(1));
    }

    for (int i = 0; i < 9; i++) {
      // The input flag is set to false here to prevent JEI from trying to transfer
      // recipe items into these slots when the transfer button is clicked.
      stacks.init(16 + i, false, 4 + (18 * i), 71);

      if (i + 1 <= secondaryInputs.size()) {
        stacks.set(16 + i, secondaryInputs.get(i));
      }
    }

    recipeLayout.setRecipeTransferButton(157, 67 + 22);
  }

}

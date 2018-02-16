package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumTier;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class JEICategoryWorkshop
    extends JEICategoryBase {

  private final ICraftingGridHelper craftingGridHelper;

  public JEICategoryWorkshop(
      String name,
      EnumTier tier,
      String uid,
      String titleTranslateKey,
      IDrawable background,
      IGuiHelper guiHelper
  ) {

    super(titleTranslateKey, background, name, tier, uid, guiHelper);
    this.craftingGridHelper = new WorkshopCraftingGridHelper(1, 0);
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

    stacks.init(0, false, 147, 39);
    stacks.set(0, outputs);

    this.setupTooltip(stacks, wrapper.getWeightedOutput());

    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 5; x++) {
        int index = 1 + x + (y * 5);
        stacks.init(index, true, x * 18 + 16, y * 18 + 3);
      }
    }

    if (wrapper.isShaped()) {
      this.craftingGridHelper.setInputs(stacks, inputs, wrapper.getWidth(), wrapper.getHeight());

    } else {
      this.craftingGridHelper.setInputs(stacks, inputs);
    }

    // This assumes there will always be at least one tool
    stacks.init(26, true, 110, 6);
    stacks.set(26, tools.get(0));

    stacks.init(27, false, 184, 21);
    stacks.init(28, false, 184, 18 + 21);
    stacks.init(29, false, 184, 36 + 21);

    ItemStack extraOutput = wrapper.getSecondaryOutput();

    if (!extraOutput.isEmpty()) {
      stacks.set(27, extraOutput);
    }

    extraOutput = wrapper.getTertiaryOutput();

    if (!extraOutput.isEmpty()) {
      stacks.set(28, extraOutput);
    }

    extraOutput = wrapper.getQuaternaryOutput();

    if (!extraOutput.isEmpty()) {
      stacks.set(29, extraOutput);
    }

    int capacity = ModuleWorktablesConfig.FLUID_CAPACITY_WORKSHOP.get(this.tableName.toLowerCase());

    fluidStacks.init(30, true, 5, 4, 6, 88, capacity, true, null);
    fluidStacks.set(30, wrapper.getFluidStack());

    stacks.init(31, true, 110, 6 + 22);
    stacks.init(32, true, 110, 6 + 44);
    stacks.init(33, true, 110, 6 + 66);

    if (tools.size() > 1) {
      stacks.set(31, tools.get(1));
    }

    if (tools.size() > 2) {
      stacks.set(32, tools.get(2));
    }

    if (tools.size() > 3) {
      stacks.set(33, tools.get(3));
    }

    for (int i = 0; i < 25; i++) {
      // The input flag is set to false here to prevent JEI from trying to transfer
      // recipe items into these slots when the transfer button is clicked.
      stacks.init(34 + i, false, 4 + (18 * i), 97);

      if (i + 1 <= secondaryInputs.size()) {
        stacks.set(34 + i, secondaryInputs.get(i));
      }
    }

    recipeLayout.setRecipeTransferButton(188, 80);
  }

}

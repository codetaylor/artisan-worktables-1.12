package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class JEICategoryWorktable
    extends JEICategoryBase {

  private final ICraftingGridHelper craftingGridHelper;

  public JEICategoryWorktable(
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

    JEIRecipeWrapper wrapperWorktable = (JEIRecipeWrapper) recipeWrapper;
    List<List<ItemStack>> tools = wrapperWorktable.getTools();
    List<List<ItemStack>> inputs = wrapperWorktable.getInputs();
    List<ItemStack> outputs = wrapperWorktable.getOutput();

    stacks.init(0, false, 111, 31);
    stacks.set(0, outputs);

    this.setupTooltip(stacks, wrapperWorktable.getWeightedOutput());

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int index = 1 + x + (y * 3);
        stacks.init(index, true, x * 18 + 16, y * 18 + 13);
      }
    }

    if (wrapperWorktable.isShaped()) {
      this.craftingGridHelper.setInputs(stacks, inputs, wrapperWorktable.getWidth(), wrapperWorktable.getHeight());

    } else {
      this.craftingGridHelper.setInputs(stacks, inputs);
    }

    stacks.init(10, true, 74, 31);

    if (tools.size() > 0) {
      stacks.set(10, tools.get(0));
    }

    stacks.init(11, false, 148, 13);
    stacks.init(12, false, 148, 31);
    stacks.init(13, false, 148, 49);

    ItemStack extraOutput = wrapperWorktable.getSecondaryOutput();

    if (!extraOutput.isEmpty()) {
      stacks.set(11, extraOutput);
    }

    extraOutput = wrapperWorktable.getTertiaryOutput();

    if (!extraOutput.isEmpty()) {
      stacks.set(12, extraOutput);
    }

    extraOutput = wrapperWorktable.getQuaternaryOutput();

    if (!extraOutput.isEmpty()) {
      stacks.set(13, extraOutput);
    }

    int capacity = ModuleWorktablesConfig.FLUID_CAPACITY_WORKTABLE.get(this.tableName.toLowerCase());

    fluidStacks.init(14, true, 5, 14, 6, 52, capacity, true, null);
    fluidStacks.set(14, wrapperWorktable.getFluidStack());

    recipeLayout.setRecipeTransferButton(157, 67);
  }

}

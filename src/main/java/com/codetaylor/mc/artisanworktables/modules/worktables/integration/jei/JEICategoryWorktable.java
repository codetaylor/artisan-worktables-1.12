package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.List;

public class JEICategoryWorktable
    implements IRecipeCategory {

  private String uid;
  private String titleTranslateKey;
  private IDrawable background;
  private ICraftingGridHelper craftingGridHelper;

  public JEICategoryWorktable(String uid, String titleTranslateKey, IDrawable background, IGuiHelper guiHelper) {

    this.uid = uid;
    this.titleTranslateKey = titleTranslateKey;
    this.background = background;
    this.craftingGridHelper = guiHelper.createCraftingGridHelper(1, 0);
  }

  @Override
  public String getUid() {

    return this.uid;
  }

  @Override
  public String getTitle() {

    return I18n.format(this.titleTranslateKey);
  }

  @Override
  public String getModName() {

    return ModuleWorktables.MOD_NAME;
  }

  @Override
  public IDrawable getBackground() {

    return this.background;
  }

  @Override
  public void setRecipe(
      IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients
  ) {

    IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

    JEIRecipeWrapperWorktable wrapperWorktable = (JEIRecipeWrapperWorktable) recipeWrapper;
    List<ItemStack> tools = wrapperWorktable.getTools();
    List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
    List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class).get(0);

    stacks.init(0, false, 108 - 3, 34 - 3);
    stacks.set(0, outputs);

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int index = 1 + x + (y * 3);
        stacks.init(index, true, x * 18 + 13 - 3, y * 18 + 16 - 3);
      }
    }

    if (wrapperWorktable.isShaped()) {
      this.craftingGridHelper.setInputs(stacks, inputs, wrapperWorktable.getWidth(), wrapperWorktable.getHeight());

    } else {
      this.craftingGridHelper.setInputs(stacks, inputs);
    }

    stacks.init(10, true, 71 - 3, 34 - 3);
    stacks.set(10, tools);

    stacks.init(11, false, 145 - 3, 16 - 3);
    stacks.init(12, false, 145 - 3, 18 + 16 - 3);
    stacks.init(13, false, 145 - 3, 36 + 16 - 3);

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

    recipeLayout.setRecipeTransferButton(157, 67); //x=157
  }
}

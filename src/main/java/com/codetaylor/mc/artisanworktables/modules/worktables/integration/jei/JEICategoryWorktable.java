package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
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

  public JEICategoryWorktable(String uid, String titleTranslateKey, IDrawable background) {

    this.uid = uid;
    this.titleTranslateKey = titleTranslateKey;
    this.background = background;
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

    List<ItemStack> tools = ((JEIRecipeWrapperWorktable) recipeWrapper).getTools();
    List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
    List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class).get(0);

    stacks.init(0, false, 123 - 3, 34 - 3);
    stacks.set(0, outputs);

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int index = 1 + x + (y * 3);
        stacks.init(index, true, x * 18 + 28 - 3, y * 18 + 16 - 3);
      }
    }

    for (int i = 1; i <= inputs.size(); i++) {
      stacks.set(i, inputs.get(i - 1));
    }

    stacks.init(10, true, 86 - 3, 34 - 3);
    stacks.set(10, tools);

    recipeLayout.setRecipeTransferButton(156, 66);
  }
}

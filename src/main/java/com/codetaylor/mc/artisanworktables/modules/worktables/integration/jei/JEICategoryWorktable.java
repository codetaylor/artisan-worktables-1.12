package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.OutputWeightPair;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JEICategoryWorktable
    implements IRecipeCategory {

  private String tableName;
  private String uid;
  private String titleTranslateKey;
  private IDrawable background;
  private ICraftingGridHelper craftingGridHelper;

  public JEICategoryWorktable(
      String name,
      String uid,
      String titleTranslateKey,
      IDrawable background,
      IGuiHelper guiHelper
  ) {

    this.tableName = name;
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
    IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();

    JEIRecipeWrapperWorktable wrapperWorktable = (JEIRecipeWrapperWorktable) recipeWrapper;
    List<ItemStack> tools = wrapperWorktable.getTools();
    List<List<ItemStack>> inputs = wrapperWorktable.getInputs();
    List<ItemStack> outputs = wrapperWorktable.getOutput();

    stacks.init(0, false, 108 - 3 + 6, 34 - 3);
    stacks.set(0, outputs);

    this.setupTooltip(stacks, wrapperWorktable.getWeightedOutput());

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        int index = 1 + x + (y * 3);
        stacks.init(index, true, x * 18 + 13 - 3 + 6, y * 18 + 16 - 3);
      }
    }

    if (wrapperWorktable.isShaped()) {
      this.craftingGridHelper.setInputs(stacks, inputs, wrapperWorktable.getWidth(), wrapperWorktable.getHeight());

    } else {
      this.craftingGridHelper.setInputs(stacks, inputs);
    }

    stacks.init(10, true, 71 - 3 + 6, 34 - 3);
    stacks.set(10, tools);

    stacks.init(11, false, 145 - 3 + 6, 16 - 3);
    stacks.init(12, false, 145 - 3 + 6, 18 + 16 - 3);
    stacks.init(13, false, 145 - 3 + 6, 36 + 16 - 3);

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

    int capacity = 4000;

    try {
      Field field = ReflectionHelper.findField(ModuleWorktablesConfig.FluidCapacity.class, this.tableName.toUpperCase());
      capacity = (int) field.get(ModuleWorktablesConfig.FLUID_CAPACITY);

    } catch (IllegalAccessException e) {
      //
    }

    fluidStacks.init(14, true, 5, 14, 6, 52, capacity, true, null);
    fluidStacks.set(14, wrapperWorktable.getFluidStack());

    recipeLayout.setRecipeTransferButton(157, 67);
  }

  private void setupTooltip(
      IGuiItemStackGroup stacks, List<OutputWeightPair> weightedOutput
  ) {

    if (weightedOutput.size() > 1) {
      int sum = 0;

      for (OutputWeightPair pair : weightedOutput) {
        sum += pair.getWeight();
      }

      final int weightSum = sum;

      stacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {

        if (slotIndex == 0) {

          for (OutputWeightPair pair : weightedOutput) {

            if (ItemStack.areItemStacksEqual(pair.getOutput(), ingredient)) {
              int chance = Math.round(pair.getWeight() / (float) weightSum * 100);

              List<String> result = new ArrayList<>();
              result.add(tooltip.get(0));
              result.add(I18n.format(
                  ModuleWorktables.Lang.JEI_TOOLTIP_CHANCE,
                  TextFormatting.GRAY,
                  String.valueOf(chance)
              ));

              for (int i = 1; i < tooltip.size(); i++) {
                result.add(tooltip.get(i));
              }

              tooltip.clear();
              tooltip.addAll(result);
            }
          }
        }
      });
    }
  }
}

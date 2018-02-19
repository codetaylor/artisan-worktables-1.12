package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.api.recipe.OutputWeightPair;
import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public abstract class JEICategoryBase
    implements IRecipeCategory {

  protected String tableName;
  protected EnumTier tier;
  protected String uid;
  protected IGuiHelper guiHelper;
  protected String titleTranslateKey;
  protected IDrawable background;

  public JEICategoryBase(
      String titleTranslateKey,
      IDrawable background,
      String name,
      EnumTier tier,
      String uid,
      IGuiHelper guiHelper
  ) {

    this.titleTranslateKey = titleTranslateKey;
    this.background = background;
    this.tableName = name;
    this.tier = tier;
    this.uid = uid;
    this.guiHelper = guiHelper;
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

  protected void setupTooltip(
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

  @Override
  public void drawExtras(Minecraft minecraft) {

    // This is a really ugly hack. Since this method is called directly
    // before the IRecipeWrapper#drawInfo, this is the only way to let
    // the recipe wrapper know what category we're drawing in.

    // The recipe wrapper uses this information to adjust the position
    // of the extra drawn info based on which category background is
    // being displayed.

    JEIRecipeWrapper.CATEGORY_TIER = this.tier;
  }
}

package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.CraftingMatrixStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class RecipeWorktableShaped
    extends RecipeWorktableBase {

  private final int width;
  private final int height;
  private boolean mirrored;

  public RecipeWorktableShaped(
      String gameStageName,
      int width,
      int height,
      ItemStack[] tools,
      NonNullList<Ingredient> input,
      ItemStack result,
      int toolDamage,
      boolean mirrored,
      ItemStack secondaryOutput,
      float secondaryOutputChance,
      ItemStack tertiaryOutput,
      float tertiaryOutputChance,
      ItemStack quaternaryOutput,
      float quaternaryOutputChance
  ) {

    super(
        gameStageName,
        result,
        tools,
        toolDamage,
        input,
        secondaryOutput,
        secondaryOutputChance,
        tertiaryOutput,
        tertiaryOutputChance,
        quaternaryOutput,
        quaternaryOutputChance
    );
    this.width = width;
    this.height = height;
    this.mirrored = mirrored;
  }

  public int getWidth() {

    return this.width;
  }

  public int getHeight() {

    return this.height;
  }



  @Override
  protected boolean matches(CraftingMatrixStackHandler craftingMatrix) {

    for (int x = 0; x <= craftingMatrix.getWidth() - this.width; ++x) {

      for (int y = 0; y <= craftingMatrix.getHeight() - this.height; ++y) {

        if (this.checkMatch(craftingMatrix, x, y, false)) {
          return true;
        }

        if (this.mirrored && this.checkMatch(craftingMatrix, x, y, true)) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean checkMatch(CraftingMatrixStackHandler craftingMatrix, int startX, int startY, boolean mirror) {

    for (int x = 0; x < craftingMatrix.getWidth(); ++x) {

      for (int y = 0; y < craftingMatrix.getHeight(); ++y) {

        int subX = x - startX;
        int subY = y - startY;
        Ingredient ingredient = Ingredient.EMPTY;

        if (subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {

          if (mirror) {
            ingredient = this.ingredients.get(this.width - subX - 1 + subY * this.width);

          } else {
            ingredient = this.ingredients.get(subX + subY * this.width);
          }
        }

        if (!ingredient.apply(craftingMatrix.getStackInSlot(x + y * craftingMatrix.getWidth()))) {
          return false;
        }
      }
    }

    return true;
  }

}

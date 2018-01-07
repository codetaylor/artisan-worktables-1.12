package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeWorktableBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeWorktableShaped;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JEIRecipeWrapperWorktable
    implements IRecipeWrapper {

  private RecipeWorktableBase recipe;
  private boolean shaped;
  private List<List<ItemStack>> inputs;
  private List<ItemStack> tools;
  private ItemStack output;
  private int width;
  private int height;

  public JEIRecipeWrapperWorktable(
      RecipeWorktableBase recipe,
      boolean shaped
  ) {

    this.recipe = recipe;
    this.shaped = shaped;
    this.inputs = new ArrayList<>();
    this.tools = new ArrayList<>();

    for (Ingredient input : this.recipe.getIngredients()) {
      this.inputs.add(Arrays.asList(input.getMatchingStacks()));
    }

    this.tools = Arrays.asList(this.recipe.getTools());
    this.output = this.recipe.getOutput();

    if (recipe instanceof RecipeWorktableShaped) {
      this.width = ((RecipeWorktableShaped) recipe).getWidth();
      this.height = ((RecipeWorktableShaped) recipe).getHeight();
    }
  }

  public boolean isShaped() {

    return this.shaped;
  }

  public int getWidth() {

    return this.width;
  }

  public int getHeight() {

    return this.height;
  }

  public List<ItemStack> getTools() {

    return this.tools;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {

    ingredients.setInputLists(ItemStack.class, this.inputs);
    ingredients.setOutput(ItemStack.class, this.output);
  }

  @Override
  public void drawInfo(
      Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY
  ) {

    GlStateManager.pushMatrix();
    //GlStateManager.scale(0.5, 0.5, 1);
    String label = "-" + this.recipe.getToolDamage();
    minecraft.fontRenderer.drawString(
        label,
        (95 - 3) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
        (55 - 3),
        0xFFFFFFFF,
        true
    );
    GlStateManager.popMatrix();
  }
}

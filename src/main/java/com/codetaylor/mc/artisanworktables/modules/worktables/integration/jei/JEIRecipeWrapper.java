package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.OutputWeightPair;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.Recipe;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumTier;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import crafttweaker.api.item.IIngredient;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JEIRecipeWrapper
    implements IRecipeWrapper {

  private static final ResourceLocation RECIPE_BACKGROUND = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      "textures/gui/recipe_background.png"
  );

  public static EnumTier CATEGORY_TIER = EnumTier.WORKTABLE;

  private Recipe recipe;
  private List<List<ItemStack>> inputs;
  private List<List<ItemStack>> secondaryInputs;
  private List<List<ItemStack>> tools;
  private List<ItemStack> output;

  public JEIRecipeWrapper(
      Recipe recipe
  ) {

    this.recipe = recipe;
    this.inputs = new ArrayList<>();
    this.secondaryInputs = new ArrayList<>();
    this.tools = new ArrayList<>();

    for (Ingredient input : this.recipe.getIngredientList()) {
      this.inputs.add(Arrays.asList(input.getMatchingStacks()));
    }

    for (int i = 0; i < recipe.getToolCount(); i++) {
      this.tools.add(Arrays.asList(this.recipe.getTools(i)));
    }

    List<OutputWeightPair> output = this.recipe.getOutputWeightPairList();
    this.output = new ArrayList<>(output.size());

    for (OutputWeightPair pair : output) {
      this.output.add(pair.getOutput());
    }

    for (IIngredient ingredient : this.recipe.getSecondaryIngredients()) {
      this.secondaryInputs.add(CTInputHelper.getMatchingStacks(ingredient, new ArrayList<>()));
    }
  }

  public List<List<ItemStack>> getInputs() {

    return this.inputs;
  }

  public FluidStack getFluidStack() {

    return this.recipe.getFluidIngredient();
  }

  public List<OutputWeightPair> getWeightedOutput() {

    return this.recipe.getOutputWeightPairList();
  }

  public List<ItemStack> getOutput() {

    return this.output;
  }

  public boolean isShaped() {

    return this.recipe.isShaped();
  }

  public int getWidth() {

    return this.recipe.getWidth();
  }

  public int getHeight() {

    return this.recipe.getHeight();
  }

  public List<List<ItemStack>> getTools() {

    return this.tools;
  }

  public List<List<ItemStack>> getSecondaryInputs() {

    return this.secondaryInputs;
  }

  public ItemStack getSecondaryOutput() {

    return this.recipe.getSecondaryOutput();
  }

  public ItemStack getTertiaryOutput() {

    return this.recipe.getTertiaryOutput();
  }

  public ItemStack getQuaternaryOutput() {

    return this.recipe.getQuaternaryOutput();
  }

  @Override
  public void getIngredients(IIngredients ingredients) {

    List<List<ItemStack>> inputs = new ArrayList<>();
    inputs.addAll(this.inputs);
    inputs.addAll(this.tools);
    inputs.addAll(this.secondaryInputs);
    ingredients.setInputLists(ItemStack.class, inputs);

    FluidStack fluidIngredient = this.recipe.getFluidIngredient();

    if (fluidIngredient != null) {
      ingredients.setInput(FluidStack.class, fluidIngredient);
    }

    List<ItemStack> output = new ArrayList<>();
    output.addAll(this.output);
    output.add(this.getSecondaryOutput());
    output.add(this.getTertiaryOutput());
    output.add(this.getQuaternaryOutput());
    ingredients.setOutputs(ItemStack.class, output);
  }

  @Override
  public void drawInfo(
      Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY
  ) {

    GlStateManager.pushMatrix();
    GlStateManager.translate(0, 0, 1000);

    String experienceString = null;

    if (this.recipe.getExperienceRequired() > 0) {

      if (this.recipe.consumeExperience()) {
        experienceString = I18n.format(ModuleWorktables.Lang.JEI_XP_COST, this.recipe.getExperienceRequired());

      } else {
        experienceString = I18n.format(ModuleWorktables.Lang.JEI_XP_REQUIRED, this.recipe.getExperienceRequired());
      }

    } else if (this.recipe.getLevelRequired() > 0) {

      if (this.recipe.consumeExperience()) {
        experienceString = I18n.format(ModuleWorktables.Lang.JEI_LEVEL_COST, this.recipe.getLevelRequired());

      } else {
        experienceString = I18n.format(ModuleWorktables.Lang.JEI_LEVEL_REQUIRED, this.recipe.getLevelRequired());
      }
    }

    if (CATEGORY_TIER == EnumTier.WORKTABLE) {

      String label = "-" + this.recipe.getToolDamage(0);
      minecraft.fontRenderer.drawString(
          label,
          (80 - 3 + 6) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
          (55 - 3),
          0xFFFFFFFF,
          true
      );

      if (experienceString != null) {
        minecraft.fontRenderer.drawString(
            experienceString,
            5,
            recipeHeight - 10,
            0xFF80FF20,
            true
        );
      }

    } else if (CATEGORY_TIER == EnumTier.WORKSTATION) {

      for (int i = 0; i < this.recipe.getToolCount(); i++) {
        String label = "-" + this.recipe.getToolDamage(i);
        minecraft.fontRenderer.drawString(
            label,
            (80 - 3 + 6) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
            (55 - 3) - 19 + (22 * i),
            0xFFFFFFFF,
            true
        );
      }

      if (experienceString != null) {
        minecraft.fontRenderer.drawString(
            experienceString,
            5,
            recipeHeight - 10,
            0xFF80FF20,
            true
        );
      }
    }
    GlStateManager.popMatrix();

    GlStateManager.pushMatrix();
    GlStateManager.scale(0.5, 0.5, 1);
    GlStateManager.translate(0, 0, 1000);
    GlStateManager.enableDepth();
    GlStateManager.pushMatrix();
    int xPos = 334;
    int yPos = 32;

    if (CATEGORY_TIER == EnumTier.WORKSHOP) {
      xPos += 36 * 2;
      yPos += 16;
    }

    if (!this.recipe.getSecondaryOutput().isEmpty()) {
      String label = (int) (this.recipe.getSecondaryOutputChance() * 100) + "%";
      minecraft.fontRenderer.drawString(
          label,
          (xPos - 3) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
          yPos,
          0xFFFFFFFF,
          true
      );
    }

    if (!this.recipe.getTertiaryOutput().isEmpty()) {
      String label = (int) (this.recipe.getTertiaryOutputChance() * 100) + "%";
      minecraft.fontRenderer.drawString(
          label,
          (xPos - 3) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
          (yPos + 36),
          0xFFFFFFFF,
          true
      );
    }

    if (!this.recipe.getQuaternaryOutput().isEmpty()) {
      String label = (int) (this.recipe.getQuaternaryOutputChance() * 100) + "%";
      minecraft.fontRenderer.drawString(
          label,
          (xPos - 3) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
          (yPos + 72),
          0xFFFFFFFF,
          true
      );
    }

    GlStateManager.popMatrix();

    if (!this.recipe.isShaped()) {
      GuiHelper.drawTexturedRect(minecraft, RECIPE_BACKGROUND, 221, 8, 18, 17, 100, 0, 0, 1, 1);
    }

    GlStateManager.popMatrix();

    // TODO: attempt to move the following tooltip to IRecipeCategory#getTooltipStrings
    GlStateManager.pushMatrix();
    GlStateManager.translate(0, -8, 0);

    if (!this.recipe.isShaped()) {
      if (mouseX >= 110 && mouseX <= 110 + 9 && mouseY >= 4 && mouseY <= 4 + 9) {
        List<String> tooltip = new ArrayList<>();
        tooltip.add(I18n.format(ModuleWorktables.Lang.JEI_TOOLTIP_SHAPELESS_RECIPE));
        GuiUtils.drawHoveringText(
            tooltip,
            110,
            4,
            minecraft.displayWidth,
            minecraft.displayHeight,
            200,
            minecraft.fontRenderer
        );
      }
    }
    GlStateManager.popMatrix();
  }
}

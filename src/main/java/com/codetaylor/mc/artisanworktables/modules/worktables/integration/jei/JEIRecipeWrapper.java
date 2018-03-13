package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanIngredient;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanItemStack;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.OutputWeightPair;
import com.codetaylor.mc.artisanworktables.api.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
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

  private ArtisanRecipe artisanRecipe;
  private List<List<ItemStack>> inputs;
  private List<List<ItemStack>> secondaryInputs;
  private List<List<ItemStack>> tools;
  private List<ItemStack> output;

  public JEIRecipeWrapper(
      ArtisanRecipe artisanRecipe
  ) {

    this.artisanRecipe = artisanRecipe;
    this.inputs = new ArrayList<>();
    this.secondaryInputs = new ArrayList<>();
    this.tools = new ArrayList<>();

    for (IArtisanIngredient input : this.artisanRecipe.getIngredientList()) {
      this.inputs.add(Arrays.asList(input.toIngredient().getMatchingStacks()));
    }

    for (int i = 0; i < artisanRecipe.getToolCount(); i++) {
      IArtisanItemStack[] tools = this.artisanRecipe.getTools(i);
      List<ItemStack> itemStackList = new ArrayList<>(tools.length);

      for (IArtisanItemStack tool : tools) {
        itemStackList.add(tool.toItemStack());
      }

      this.tools.add(itemStackList);
    }

    List<OutputWeightPair> output = this.artisanRecipe.getOutputWeightPairList();
    this.output = new ArrayList<>(output.size());

    for (OutputWeightPair pair : output) {
      this.output.add(pair.getOutput().toItemStack());
    }

    for (IArtisanIngredient ingredient : this.artisanRecipe.getSecondaryIngredients()) {
      this.secondaryInputs.add(Arrays.asList(ingredient.toIngredient().getMatchingStacks()));
    }
  }

  public List<List<ItemStack>> getInputs() {

    return this.inputs;
  }

  public FluidStack getFluidStack() {

    return this.artisanRecipe.getFluidIngredient();
  }

  public List<OutputWeightPair> getWeightedOutput() {

    return this.artisanRecipe.getOutputWeightPairList();
  }

  public List<ItemStack> getOutput() {

    return this.output;
  }

  public boolean isShaped() {

    return this.artisanRecipe.isShaped();
  }

  public int getWidth() {

    return this.artisanRecipe.getWidth();
  }

  public int getHeight() {

    return this.artisanRecipe.getHeight();
  }

  public List<List<ItemStack>> getTools() {

    return this.tools;
  }

  public List<List<ItemStack>> getSecondaryInputs() {

    return this.secondaryInputs;
  }

  public ItemStack getSecondaryOutput() {

    return this.artisanRecipe.getSecondaryOutput().toItemStack();
  }

  public ItemStack getTertiaryOutput() {

    return this.artisanRecipe.getTertiaryOutput().toItemStack();
  }

  public ItemStack getQuaternaryOutput() {

    return this.artisanRecipe.getQuaternaryOutput().toItemStack();
  }

  @Override
  public void getIngredients(IIngredients ingredients) {

    List<List<ItemStack>> inputs = new ArrayList<>();
    inputs.addAll(this.inputs);
    inputs.addAll(this.tools);
    inputs.addAll(this.secondaryInputs);
    ingredients.setInputLists(ItemStack.class, inputs);

    FluidStack fluidIngredient = this.artisanRecipe.getFluidIngredient();

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

    if (this.artisanRecipe.getExperienceRequired() > 0) {

      if (this.artisanRecipe.consumeExperience()) {
        experienceString = I18n.format(ModuleWorktables.Lang.JEI_XP_COST, this.artisanRecipe.getExperienceRequired());

      } else {
        experienceString = I18n.format(
            ModuleWorktables.Lang.JEI_XP_REQUIRED,
            this.artisanRecipe.getExperienceRequired()
        );
      }

    } else if (this.artisanRecipe.getLevelRequired() > 0) {

      if (this.artisanRecipe.consumeExperience()) {
        experienceString = I18n.format(ModuleWorktables.Lang.JEI_LEVEL_COST, this.artisanRecipe.getLevelRequired());

      } else {
        experienceString = I18n.format(ModuleWorktables.Lang.JEI_LEVEL_REQUIRED, this.artisanRecipe.getLevelRequired());
      }
    }

    if (CATEGORY_TIER == EnumTier.WORKTABLE) {

      String label = "-" + this.artisanRecipe.getToolDamage(0);
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

      for (int i = 0; i < this.artisanRecipe.getToolCount(); i++) {
        String label = "-" + this.artisanRecipe.getToolDamage(i);
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

    } else if (CATEGORY_TIER == EnumTier.WORKSHOP) {

      for (int i = 0; i < this.artisanRecipe.getToolCount(); i++) {
        String label = "-" + this.artisanRecipe.getToolDamage(i);
        minecraft.fontRenderer.drawString(
            label,
            (119) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
            (39) + (22 * i),
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

    if (CATEGORY_TIER == EnumTier.WORKSHOP) {

      if (!this.artisanRecipe.getSecondaryOutput().isEmpty()) {
        String label = (int) (this.artisanRecipe.getSecondaryOutputChance() * 100) + "%";
        minecraft.fontRenderer.drawString(
            label,
            (256) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
            12,
            0xFFFFFFFF,
            true
        );
      }

      if (!this.artisanRecipe.getTertiaryOutput().isEmpty()) {
        String label = (int) (this.artisanRecipe.getTertiaryOutputChance() * 100) + "%";
        minecraft.fontRenderer.drawString(
            label,
            (256 + 36) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
            12,
            0xFFFFFFFF,
            true
        );
      }

      if (!this.artisanRecipe.getQuaternaryOutput().isEmpty()) {
        String label = (int) (this.artisanRecipe.getQuaternaryOutputChance() * 100) + "%";
        minecraft.fontRenderer.drawString(
            label,
            (256 + 72) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
            12,
            0xFFFFFFFF,
            true
        );
      }
    } else {

      int xPos = 334;
      int yPos = 32;

      if (!this.artisanRecipe.getSecondaryOutput().isEmpty()) {
        String label = (int) (this.artisanRecipe.getSecondaryOutputChance() * 100) + "%";
        minecraft.fontRenderer.drawString(
            label,
            (xPos - 3) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
            yPos,
            0xFFFFFFFF,
            true
        );
      }

      if (!this.artisanRecipe.getTertiaryOutput().isEmpty()) {
        String label = (int) (this.artisanRecipe.getTertiaryOutputChance() * 100) + "%";
        minecraft.fontRenderer.drawString(
            label,
            (xPos - 3) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
            (yPos + 36),
            0xFFFFFFFF,
            true
        );
      }

      if (!this.artisanRecipe.getQuaternaryOutput().isEmpty()) {
        String label = (int) (this.artisanRecipe.getQuaternaryOutputChance() * 100) + "%";
        minecraft.fontRenderer.drawString(
            label,
            (xPos - 3) - minecraft.fontRenderer.getStringWidth(label) * 0.5f,
            (yPos + 72),
            0xFFFFFFFF,
            true
        );
      }
    }

    GlStateManager.popMatrix();

    if (!this.artisanRecipe.isShaped()) {

      if (CATEGORY_TIER == EnumTier.WORKSHOP) {
        GuiHelper.drawTexturedRect(minecraft, RECIPE_BACKGROUND, 288, 58, 18, 17, 0, 0, 0, 1, 1);

      } else {
        GuiHelper.drawTexturedRect(minecraft, RECIPE_BACKGROUND, 234, 8, 18, 17, 0, 0, 0, 1, 1);
      }
    }

    GlStateManager.popMatrix();

    // TODO: attempt to move the following tooltip to IRecipeCategory#getTooltipStrings
    GlStateManager.pushMatrix();
    GlStateManager.translate(0, -8, 0);

    if (!this.artisanRecipe.isShaped()) {

      int x = 117;
      int y = 4;

      if (CATEGORY_TIER == EnumTier.WORKSHOP) {
        x = 144;
        y = 29;
      }

      if (mouseX >= x && mouseX <= x + 9 && mouseY >= y && mouseY <= y + 9) {
        List<String> tooltip = new ArrayList<>();
        tooltip.add(I18n.format(ModuleWorktables.Lang.JEI_TOOLTIP_SHAPELESS_RECIPE));
        GuiUtils.drawHoveringText(
            tooltip,
            mouseX,
            mouseY,
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

package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.CraftingMatrixStackHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public abstract class RecipeWorktableBase
    implements IRecipeWorktable {

  protected String gameStageName;
  protected ItemStack[] tools;
  protected ItemStack output;
  protected int toolDamage;
  protected NonNullList<Ingredient> ingredients;
  protected ItemStack secondaryOutput;
  protected float secondaryOutputChance;
  protected ItemStack tertiaryOutput;
  protected float tertiaryOutputChance;
  protected ItemStack quaternaryOutput;
  protected float quaternaryOutputChance;

  /* package */ RecipeWorktableBase(
      @Nullable String gameStageName,
      ItemStack output,
      ItemStack[] tools,
      int toolDamage,
      NonNullList<Ingredient> ingredients,
      ItemStack secondaryOutput,
      float secondaryOutputChance,
      ItemStack tertiaryOutput,
      float tertiaryOutputChance,
      ItemStack quaternaryOutput,
      float quaternaryOutputChance
  ) {

    this.gameStageName = (gameStageName == null) ? null : gameStageName.toLowerCase();
    this.output = output;
    this.tools = tools;
    this.toolDamage = toolDamage;
    this.ingredients = ingredients;
    this.secondaryOutput = secondaryOutput;
    this.secondaryOutputChance = MathHelper.clamp(secondaryOutputChance, 0, 1);
    this.tertiaryOutput = tertiaryOutput;
    this.tertiaryOutputChance = MathHelper.clamp(tertiaryOutputChance, 0, 1);
    this.quaternaryOutput = quaternaryOutput;
    this.quaternaryOutputChance = MathHelper.clamp(quaternaryOutputChance, 0, 1);
  }

  @Override
  @Nullable
  public String getGameStageName() {

    return this.gameStageName;
  }

  @Override
  public ItemStack getSecondaryOutput() {

    if (this.secondaryOutput.isEmpty()) {
      return ItemStack.EMPTY;
    }

    return this.secondaryOutput.copy();
  }

  @Override
  public float getSecondaryOutputChance() {

    return this.secondaryOutputChance;
  }

  @Override
  public ItemStack getTertiaryOutput() {

    if (this.tertiaryOutput.isEmpty()) {
      return ItemStack.EMPTY;
    }

    return this.tertiaryOutput.copy();
  }

  @Override
  public float getTertiaryOutputChance() {

    return this.tertiaryOutputChance;
  }

  @Override
  public ItemStack getQuaternaryOutput() {

    if (this.quaternaryOutput.isEmpty()) {
      return ItemStack.EMPTY;
    }

    return this.quaternaryOutput.copy();
  }

  @Override
  public float getQuaternaryOutputChance() {

    return this.quaternaryOutputChance;
  }

  @Override
  public boolean isValidTool(ItemStack tool) {

    for (ItemStack itemStack : this.tools) {

      // We can't use itemStack.isItemEqualIgnoreDurability(tool) here because
      // apparently Tinker's tools don't set the max durability on the tool
      // which causes that check to fail because it thinks the item can't be
      // damaged. Instead, we assume the item being used has durability and
      // just compare items.
      if (itemStack.getItem() == tool.getItem()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public ItemStack[] getTools() {

    return this.tools;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {

    return this.ingredients;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  @Override
  public int getToolDamage() {

    return this.toolDamage;
  }

  @Override
  public boolean matches(
      EntityPlayer player,
      ItemStack tool,
      CraftingMatrixStackHandler craftingMatrix
  ) {

    // Do we have the correct tool?
    if (!this.isValidTool(tool)) {
      return false;
    }

    // Does the tool have enough durability for this recipe?
    /*if (tool.getItemDamage() + this.toolDamage > tool.getMaxDamage()) {
      return false;
    }*/

    if (ModuleWorktables.MOD_LOADED_GAMESTAGES
        && this.gameStageName != null) {

      if (!this.gameStageCheck(player)) {
        return false;
      }
    }

    return this.matches(craftingMatrix);
  }

  protected abstract boolean matches(CraftingMatrixStackHandler craftingMatrix);

  protected boolean gameStageCheck(EntityPlayer playerIn) {

    return playerIn != null && PlayerDataHandler.getStageData(playerIn).hasUnlockedStage(this.gameStageName);
  }
}

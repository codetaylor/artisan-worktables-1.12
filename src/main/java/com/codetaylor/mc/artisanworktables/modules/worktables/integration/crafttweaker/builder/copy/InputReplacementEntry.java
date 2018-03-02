package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import crafttweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class InputReplacementEntry {

  private final IIngredient toReplace;
  private final IIngredient replacement;

  public InputReplacementEntry(@Nullable IIngredient toReplace, @Nullable IIngredient replacement) {

    this.toReplace = toReplace;
    this.replacement = replacement;
  }

  public boolean matches(Ingredient toMatch) {

    if (this.toReplace == null) {

      return (toMatch == null || toMatch == Ingredient.EMPTY);
    }

    for (ItemStack itemStack : toMatch.getMatchingStacks()) {

      if (this.toReplace.matches(CTInputHelper.toIItemStack(itemStack))) {
        return true;
      }
    }

    return false;
  }

  public IIngredient getReplacement() {

    return this.replacement;
  }
}

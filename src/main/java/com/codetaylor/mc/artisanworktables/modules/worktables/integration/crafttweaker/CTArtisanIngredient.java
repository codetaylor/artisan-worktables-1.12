package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.ArtisanIngredient;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ArtisanItemStack;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanIngredient;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanItemStack;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import crafttweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CTArtisanIngredient
    implements IArtisanIngredient {

  public static IArtisanIngredient[][] fromMatrix(@Nonnull IIngredient[][] ingredients) {

    IArtisanIngredient[][] result = new IArtisanIngredient[ingredients.length][];

    for (int row = 0; row < ingredients.length; row++) {
      result[row] = new IArtisanIngredient[ingredients[row].length];

      for (int col = 0; col < ingredients[row].length; col++) {
        result[row][col] = CTArtisanIngredient.from(ingredients[row][col]);
      }
    }

    return result;
  }

  public static IArtisanIngredient[] fromArray(@Nonnull IIngredient[] ingredients) {

    IArtisanIngredient[] result = new IArtisanIngredient[ingredients.length];

    for (int i = 0; i < ingredients.length; i++) {
      result[i] = CTArtisanIngredient.from(ingredients[i]);
    }

    return result;
  }

  public static IArtisanIngredient from(@Nullable IIngredient ingredient) {

    if (ingredient == null) {
      return ArtisanIngredient.EMPTY;
    }

    return new CTArtisanIngredient(ingredient);
  }

  private final IIngredient ingredient;

  private CTArtisanIngredient(@Nonnull IIngredient ingredient) {

    this.ingredient = ingredient;
  }

  @Override
  public boolean isEmpty() {

    return false;
  }

  @Override
  public int getAmount() {

    return this.ingredient.getAmount();
  }

  @Override
  public boolean matches(IArtisanItemStack itemStack) {

    if (itemStack instanceof CTArtisanItemStack) {
      return this.ingredient.matches(((CTArtisanItemStack) itemStack).getIItemStack());
    }

    return this.matches(itemStack.toItemStack());
  }

  @Override
  public boolean matches(ItemStack itemStack) {

    return !itemStack.isEmpty()
        && this.ingredient.matches(CTInputHelper.toIItemStack(itemStack));
  }

  @Override
  public boolean matchesIgnoreAmount(IArtisanItemStack itemStack) {

    if (itemStack instanceof CTArtisanItemStack) {
      return this.ingredient.amount(1).matches(((CTArtisanItemStack) itemStack).getIItemStack().amount(1));
    }

    return this.matchesIgnoreAmount(itemStack.toItemStack());
  }

  @Override
  public boolean matchesIgnoreAmount(ItemStack itemStack) {

    if (itemStack.isEmpty()) {
      return false;
    }

    itemStack = itemStack.copy();
    itemStack.setCount(1);
    return this.ingredient.amount(1).matches(CTInputHelper.toIItemStack(itemStack));
  }

  @Override
  public IArtisanItemStack[] getMatchingStacks() {

    List<ItemStack> matchingStacks = CTInputHelper.getMatchingStacks(this.ingredient, new ArrayList<>());
    IArtisanItemStack[] result = new IArtisanItemStack[matchingStacks.size()];

    for (int i = 0; i < matchingStacks.size(); i++) {
      result[i] = ArtisanItemStack.from(matchingStacks.get(i));
    }

    return result;
  }

  @Override
  public Ingredient toIngredient() {

    return CTInputHelper.toIngredient(this.ingredient);
  }

  public IIngredient getIngredient() {

    return this.ingredient;
  }
}

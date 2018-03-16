package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanItemStack;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ArtisanItemStack;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CTArtisanItemStack
    implements IArtisanItemStack {

  public static IArtisanItemStack from(@Nullable IItemStack itemStack) {

    if (itemStack == null) {
      return ArtisanItemStack.EMPTY;
    }

    return new CTArtisanItemStack(itemStack);
  }

  private final IItemStack itemStack;

  private CTArtisanItemStack(@Nonnull IItemStack itemStack) {

    this.itemStack = itemStack;
  }

  @Override
  public int getAmount() {

    return this.itemStack.getAmount();
  }

  @Override
  public Item getItem() {

    return CTInputHelper.toStack(this.itemStack).getItem();
  }

  @Override
  public boolean isEmpty() {

    return this.itemStack.isEmpty();
  }

  @Override
  public ItemStack toItemStack() {

    return CTInputHelper.toStack(this.itemStack).copy();
  }

  @Override
  public IArtisanItemStack copy() {

    return CTArtisanItemStack.from(this.itemStack.amount(this.itemStack.getAmount()));
  }

  /* package */ IItemStack getIItemStack() {

    return this.itemStack;
  }
}

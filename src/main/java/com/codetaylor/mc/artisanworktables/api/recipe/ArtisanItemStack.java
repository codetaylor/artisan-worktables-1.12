package com.codetaylor.mc.artisanworktables.api.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ArtisanItemStack
    implements IArtisanItemStack {

  public static final IArtisanItemStack EMPTY = new ArtisanItemStack(ItemStack.EMPTY);

  public static IArtisanItemStack from(@Nonnull ItemStack itemStack) {

    if (itemStack.isEmpty()) {
      return ArtisanItemStack.EMPTY;
    }

    return new ArtisanItemStack(itemStack.copy());
  }

  private final ItemStack itemStack;

  private ArtisanItemStack(@Nonnull ItemStack itemStack) {

    this.itemStack = itemStack;
  }

  @Override
  public int getAmount() {

    return this.itemStack.getCount();
  }

  @Override
  public Item getItem() {

    return this.itemStack.getItem();
  }

  @Override
  public boolean isEmpty() {

    return this.itemStack.isEmpty();
  }

  @Override
  public ItemStack toItemStack() {

    return this.itemStack.copy();
  }

  @Override
  public IArtisanItemStack copy() {

    return ArtisanItemStack.from(this.itemStack);
  }
}

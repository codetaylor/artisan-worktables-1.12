package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nullable;

public class HashCodeUtil {

  public static int get(IArtisanItemStack itemStack) {

    return HashCodeUtil.get(itemStack.toItemStack());
  }

  public static int get(ItemStack itemStack) {

    HashCodeBuilder builder = new HashCodeBuilder()
        .append(itemStack.getCount())
        .append(itemStack.getItem().getUnlocalizedName())
        .append(itemStack.getItemDamage());

    if (itemStack.getTagCompound() != null) {
      builder.append(itemStack.getTagCompound().hashCode());
    }

    return builder.build();
  }

  public static int get(IArtisanIngredient ingredient) {

    return HashCodeUtil.get(ingredient.toIngredient());
  }

  public static int get(Ingredient ingredient) {

    HashCodeBuilder builder = new HashCodeBuilder();
    ItemStack[] matchingStacks = ingredient.getMatchingStacks();

    for (ItemStack itemStack : matchingStacks) {
      builder.append(HashCodeUtil.get(itemStack));
    }

    return builder.build();
  }

  public static int get(@Nullable FluidStack fluidStack) {

    HashCodeBuilder builder = new HashCodeBuilder();

    if (fluidStack != null) {
      builder.append(fluidStack.getFluid().getClass().getName().hashCode());
      builder.append(fluidStack.amount);

      if (fluidStack.tag != null) {
        builder.append(fluidStack.tag.hashCode());
      }
    }

    return builder.build();
  }

  public static int get(ToolIngredientEntry entry) {

    return new HashCodeBuilder()
        .append(HashCodeUtil.get(entry.getTool()))
        .append(entry.getDamage())
        .build();
  }

  public static int get(OutputWeightPair pair) {

    return new HashCodeBuilder()
        .append(HashCodeUtil.get(pair.getOutput()))
        .append(pair.getWeight())
        .build();
  }

  public static int get(ExtraOutputChancePair pair) {

    return new HashCodeBuilder()
        .append(HashCodeUtil.get(pair.getOutput()))
        .append(pair.getChance())
        .build();
  }

  private HashCodeUtil() {
    //
  }

}

package com.codetaylor.mc.artisanworktables.api.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class DefaultToolHandler
    implements IToolHandler {

  public static final DefaultToolHandler INSTANCE = new DefaultToolHandler();

  @Override
  public boolean matches(ItemStack itemStack) {

    return itemStack.isItemStackDamageable();
  }

  @Override
  public boolean matches(ItemStack tool, ItemStack toolToMatch) {

    return (tool.getItem() == toolToMatch.getItem());
  }

  @Override
  public boolean canApplyUses(ItemStack itemStack, int uses, boolean restrictDurability) {

    return !restrictDurability || (itemStack.getItemDamage() + uses <= itemStack.getMaxDamage());
  }

  @Override
  public ItemStack applyUses(World world, ItemStack itemStack, int uses, @Nullable EntityPlayer player) {

    ItemStack result = itemStack.copy();
    boolean broken = itemStack.attemptDamageItem(uses, new Random(), null)
        || itemStack.getItemDamage() == itemStack.getMaxDamage();
    if (broken) {
      itemStack.shrink(1);
    }
    return result;
  }
}

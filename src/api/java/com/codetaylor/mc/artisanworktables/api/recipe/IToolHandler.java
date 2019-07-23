package com.codetaylor.mc.artisanworktables.api.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IToolHandler {

  /**
   * @param itemStack the tool
   * @return true if this tool handler is responsible for the given tool
   */
  boolean matches(ItemStack itemStack);

  /**
   * @param itemStack the tool
   * @param damage    the damage to be applied to the tool
   * @return true if the tool can accept all given damage
   */
  boolean canAcceptAllDamage(ItemStack itemStack, int damage);

  /**
   *
   * @param world
   * @param itemStack the tool
   * @param damage    the damage
   * @param player
   * @param simulate  if true, no damage will actually be applied
   * @return true if the tool is broken as a result of the applied damage
   */
  boolean applyDamage(World world, ItemStack itemStack, int damage, EntityPlayer player, boolean simulate);

}

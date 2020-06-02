package com.codetaylor.mc.artisanworktables.api.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IToolHandler {

  /**
   * @param itemStack the tool
   * @return true if this tool handler is responsible for the given tool
   */
  boolean matches(ItemStack itemStack);

  /**
   * @param tool        the tool
   * @param toolToMatch the tool to match
   * @return true if the given tool matches the tool to match
   */
  boolean matches(ItemStack tool, ItemStack toolToMatch);

  /**
   * @param itemStack          the tool
   * @param uses               the number of uses that would be applied to the tool
   * @param restrictDurability if true, must have sufficient durability remaining
   * @return true if the tool can apply all the uses
   */
  boolean canApplyUses(ItemStack itemStack, int uses, boolean restrictDurability);

  /**
   * @param world
   * @param itemStack the tool
   * @param uses      the number of uses to be applied to the tool
   * @param player
   * @return a new stack representing the item after the uses have been applied (usually in the form of item damage)
   */
  ItemStack applyUses(World world, ItemStack itemStack, int uses, @Nullable EntityPlayer player);

}

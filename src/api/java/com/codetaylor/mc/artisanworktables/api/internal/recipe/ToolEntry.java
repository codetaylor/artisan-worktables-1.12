package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.recipe.IToolHandler;
import net.minecraft.item.ItemStack;

public class ToolEntry {

  private final IArtisanIngredient tool;
  private final ItemStack[] toolItemStacks;
  private final int damage;

  public ToolEntry(IArtisanIngredient tool, int damage) {

    this.tool = tool;
    this.damage = damage;

    IArtisanItemStack[] toolMatchingStacks = tool.getMatchingStacks();
    this.toolItemStacks = new ItemStack[toolMatchingStacks.length];

    for (int i = 0; i < toolMatchingStacks.length; i++) {
      this.toolItemStacks[i] = toolMatchingStacks[i].toItemStack();
    }
  }

  public ItemStack[] getToolStacks() {

    return this.toolItemStacks;
  }

  public IArtisanIngredient getTool() {

    return this.tool;
  }

  public int getDamage() {

    return this.damage;
  }

  public boolean matches(IToolHandler handler, ItemStack tool) {

    for (int i = 0; i < this.toolItemStacks.length; i++) {

      if (handler.matches(tool, this.toolItemStacks[i])) {
        return true;
      }
    }

    return false;
  }
}

package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.recipe.IToolHandler;
import net.minecraft.item.ItemStack;

public class ToolEntry {

  private IArtisanIngredient tool;
  private IArtisanItemStack[] toolStacks;
  private ItemStack[] toolItemStacks;
  private int damage;

  public ToolEntry(IArtisanIngredient tool, int damage) {

    this.tool = tool;
    this.toolStacks = tool.getMatchingStacks();
    this.damage = damage;
    this.toolItemStacks = new ItemStack[this.toolStacks.length];

    for (int i = 0; i < this.toolStacks.length; i++) {
      this.toolItemStacks[i] = this.toolStacks[i].toItemStack();
    }
  }

  public IArtisanItemStack[] getToolStacks() {

    return this.toolStacks;
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

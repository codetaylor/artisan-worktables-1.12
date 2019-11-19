package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.recipe.IToolHandler;
import net.minecraft.item.ItemStack;

public class ToolEntry {

  private IArtisanItemStack[] tool;
  private ItemStack[] toolItemStacks;
  private int damage;

  public ToolEntry(ToolIngredientEntry entry) {

    this(entry.getTool().getMatchingStacks(), entry.getDamage());
  }

  public ToolEntry(IArtisanItemStack[] tool, int damage) {

    this.tool = tool;
    this.damage = damage;
    this.toolItemStacks = new ItemStack[this.tool.length];

    for (int i = 0; i < this.tool.length; i++) {
      this.toolItemStacks[i] = this.tool[i].toItemStack();
    }
  }

  public IArtisanItemStack[] getTool() {

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

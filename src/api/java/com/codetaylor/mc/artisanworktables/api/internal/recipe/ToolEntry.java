package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.recipe.IToolHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ToolEntry {

  private static final ThreadLocal<List<ItemStack>> TOOL_CACHE = ThreadLocal.withInitial(ArrayList::new);

  private static ItemStack cache(ItemStack itemStack) {

    if (itemStack.hasTagCompound()) {
      return itemStack;
    }

    List<ItemStack> list = TOOL_CACHE.get();

    for (ItemStack stack : list) {

      if (stack.getItem() == itemStack.getItem()
          && stack.getMetadata() == itemStack.getMetadata()) {
        return stack;
      }
    }

    list.add(itemStack);
    return itemStack;
  }

  private final IArtisanIngredient tool;
  private final ItemStack[] toolItemStacks;
  private final int damage;

  public ToolEntry(IArtisanIngredient tool, int damage) {

    this.tool = tool;
    this.damage = damage;

    IArtisanItemStack[] toolMatchingStacks = tool.getMatchingStacks();
    this.toolItemStacks = new ItemStack[toolMatchingStacks.length];

    for (int i = 0; i < toolMatchingStacks.length; i++) {
      this.toolItemStacks[i] = ToolEntry.cache(toolMatchingStacks[i].toItemStack());
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

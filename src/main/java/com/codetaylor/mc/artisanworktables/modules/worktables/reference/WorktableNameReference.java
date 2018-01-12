package com.codetaylor.mc.artisanworktables.modules.worktables.reference;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorktableNameReference {

  private static final List<String> WORKTABLE_NAME_LIST = new ArrayList<>();

  static {

    for (EnumWorktableType type : EnumWorktableType.values()) {
      WORKTABLE_NAME_LIST.add(type.getName());
    }

    WORKTABLE_NAME_LIST.add("mage");
  }

  public static boolean isAllowedWorktableName(String name) {

    return WORKTABLE_NAME_LIST.contains(name.toLowerCase());
  }

  public static List<String> getAllowedWorktableNames() {

    return Collections.unmodifiableList(WORKTABLE_NAME_LIST);
  }

  public static ItemStack getJEIRecipeCatalyst(String name) {

    try {
      EnumWorktableType type = EnumWorktableType.fromName(name);
      return new ItemStack(ModuleWorktables.Blocks.WORKTABLE, 1, type.getMeta());

    } catch (Exception e) {
      //
    }

    if ("mage".equals(name)) {
      return new ItemStack(ModuleWorktables.Blocks.WORKTABLE_MAGE, 1, 1);
    }

    return ItemStack.EMPTY;
  }

}

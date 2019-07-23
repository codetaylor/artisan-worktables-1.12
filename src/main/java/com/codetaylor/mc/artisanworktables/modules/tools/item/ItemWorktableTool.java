package com.codetaylor.mc.artisanworktables.modules.tools.item;

import com.codetaylor.mc.artisanworktables.api.internal.tool.CustomMaterial;
import com.codetaylor.mc.artisanworktables.api.tool.ItemWorktableToolBase;
import com.codetaylor.mc.artisanworktables.api.tool.reference.EnumWorktableToolType;
import net.minecraft.item.ItemStack;

import java.util.Collections;

public class ItemWorktableTool
    extends ItemWorktableToolBase {

  public ItemWorktableTool(
      EnumWorktableToolType type,
      CustomMaterial material
  ) {

    super(material.getToolMaterial(), Collections.emptySet(), type, material);
    this.setMaxStackSize(1);
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {

    return false;
  }

}

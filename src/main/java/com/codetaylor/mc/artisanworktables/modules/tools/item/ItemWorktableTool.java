package com.codetaylor.mc.artisanworktables.modules.tools.item;

import com.codetaylor.mc.artisanworktables.modules.tools.ModuleTools;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumMaterial;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import javax.annotation.Nonnull;
import java.util.Collections;

public class ItemWorktableTool
    extends ItemTool {

  private String name;
  private EnumMaterial material;

  public ItemWorktableTool(
      String name, EnumMaterial material
  ) {

    super(material.getToolMaterial(), Collections.emptySet());
    this.name = name;
    this.material = material;
    this.setMaxStackSize(1);
  }

  public String getName() {

    return this.name;
  }

  public EnumMaterial getMaterial() {

    return this.material;
  }

  @Nonnull
  @Override
  public String getItemStackDisplayName(@Nonnull ItemStack stack) {

    Item item = stack.getItem();

    if (item instanceof ItemWorktableTool) {
      EnumMaterial material = ((ItemWorktableTool) item).getMaterial();
      String parameter = I18n.format(String.format(ModuleTools.Lang.MATERIAL_STRING, material.getName()));
      return I18n.format(this.getUnlocalizedNameInefficiently(stack) + ".name", parameter);
    }

    return super.getItemStackDisplayName(stack);
  }
}

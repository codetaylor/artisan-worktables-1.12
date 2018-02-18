package com.codetaylor.mc.artisanworktables.modules.tools.material;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import java.awt.*;

public class CustomMaterialConverter {

  public CustomMaterial convert(DataCustomMaterial data) {

    // Convert tool material
    Item.ToolMaterial toolMaterial = EnumHelper.addToolMaterial(
        "artisanworktables:" + data.getName().toUpperCase(),
        data.getHarvestLevel(),
        data.getMaxUses(),
        data.getEfficiency(),
        data.getDamage(),
        data.getEnchantability()
    );

    // Convert color
    Integer colorInt = Integer.decode("0x" + data.getColor());
    int color = new Color(colorInt).getRGB();

    return new CustomMaterial(data, toolMaterial, color);
  }

}

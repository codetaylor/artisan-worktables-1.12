package com.codetaylor.mc.artisanworktables.modules.tools.material;

import net.minecraft.item.Item;

public class CustomMaterial {

  private DataCustomMaterial dataCustomMaterial;
  private Item.ToolMaterial toolMaterial;
  private int color;

  public CustomMaterial(DataCustomMaterial dataCustomMaterial, Item.ToolMaterial toolMaterial, int color) {

    this.dataCustomMaterial = dataCustomMaterial;
    this.toolMaterial = toolMaterial;
    this.color = color;
  }

  public DataCustomMaterial getDataCustomMaterial() {

    return this.dataCustomMaterial;
  }

  public Item.ToolMaterial getToolMaterial() {

    return this.toolMaterial;
  }

  public int getColor() {

    return this.color;
  }
}

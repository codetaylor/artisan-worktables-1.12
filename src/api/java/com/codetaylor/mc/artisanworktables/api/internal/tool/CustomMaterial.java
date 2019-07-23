package com.codetaylor.mc.artisanworktables.api.internal.tool;

import com.codetaylor.mc.artisanworktables.api.tool.ICustomToolMaterial;
import net.minecraft.item.Item;

public class CustomMaterial {

  private ICustomToolMaterial dataCustomMaterial;
  private Item.ToolMaterial toolMaterial;
  private int color;
  private String ingredientString;

  public CustomMaterial(
      ICustomToolMaterial dataCustomMaterial,
      Item.ToolMaterial toolMaterial,
      int color,
      String ingredientString
  ) {

    this.dataCustomMaterial = dataCustomMaterial;
    this.toolMaterial = toolMaterial;
    this.color = color;
    this.ingredientString = ingredientString;
  }

  public ICustomToolMaterial getDataCustomMaterial() {

    return this.dataCustomMaterial;
  }

  public Item.ToolMaterial getToolMaterial() {

    return this.toolMaterial;
  }

  public int getColor() {

    return this.color;
  }

  public String getIngredientString() {

    return this.ingredientString;
  }
}

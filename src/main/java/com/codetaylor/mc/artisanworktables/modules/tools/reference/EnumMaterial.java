package com.codetaylor.mc.artisanworktables.modules.tools.reference;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.awt.*;

public enum EnumMaterial {

  WOOD("wood", Item.ToolMaterial.WOOD, new Color(0x73523E), false, "plankWood"),
  STONE("stone", Item.ToolMaterial.STONE, new Color(0x969696), false, "stone"),
  IRON("iron", Item.ToolMaterial.IRON, new Color(0xD4D4D4), true, "ingotIron"),
  GOLD("gold", Item.ToolMaterial.GOLD, new Color(0xFFE947), false, "ingotGold"),
  DIAMOND("diamond", Item.ToolMaterial.DIAMOND, new Color(0x33EBCB), false, "gemDiamond"),

  FLINT("flint", ModuleMaterials.FLINT, new Color(0x1A1A1A), true, Items.FLINT),
  ALUMINUM("aluminum", ModuleMaterials.ALUMINUM, new Color(0xC5C6D0), true, "ingotAluminum"),
  BRONZE("bronze", ModuleMaterials.BRONZE, new Color(0xE8983F), true, "ingotBronze"),
  CONSTANTAN("constantan", ModuleMaterials.CONSTANTAN, new Color(0xBD8D46), true, "ingotConstantan"),
  COPPER("copper", ModuleMaterials.COPPER, new Color(0xFFA131), true, "ingotCopper"),
  ELECTRUM("electrum", ModuleMaterials.ELECTRUM, new Color(0xFFE947), true, "ingotElectrum"),
  INVAR("invar", ModuleMaterials.INVAR, new Color(0x8E9A95), true, "ingotInvar"),
  LEAD("lead", ModuleMaterials.LEAD, new Color(0x8A93B1), true, "ingotLead"),
  NICKEL("nickel", ModuleMaterials.NICKEL, new Color(0xA2975D), true, "ingotNickel"),
  PLATINUM("platinum", ModuleMaterials.PLATINUM, new Color(0x4BACD8), true, "ingotPlatinum"),
  SILVER("silver", ModuleMaterials.SILVER, new Color(0x7B9DA4), true, "ingotSilver"),
  STEEL("steel", ModuleMaterials.STEEL, new Color(0x858585), false, "ingotSteel"),
  TIN("tin", ModuleMaterials.TIN, new Color(0x7C9AB2), true, "ingotTin"),

  MANASTEEL("manasteel", ModuleMaterials.MANASTEEL, new Color(0x3389ff), true, "ingotManasteel"),
  ELEMENTIUM("elementium", ModuleMaterials.ELEMENTIUM, new Color(0xf15cae), true, "ingotElvenElementium"),
  TERRASTEEL("terrasteel", ModuleMaterials.TERRASTEEL, new Color(0x53f900), true, "ingotTerrasteel");

  private String name;
  private Item.ToolMaterial toolMaterial;
  private int color;
  private boolean highlighted;
  private Object recipeIngredient;

  EnumMaterial(String name, Item.ToolMaterial toolMaterial, Color color, boolean highlighted, Object recipeIngredient) {

    this.name = name;
    this.toolMaterial = toolMaterial;
    this.color = color.getRGB();
    this.highlighted = highlighted;
    this.recipeIngredient = recipeIngredient;
  }

  public String getName() {

    return this.name;
  }

  public Item.ToolMaterial getToolMaterial() {

    return this.toolMaterial;
  }

  public int getColor() {

    return this.color;
  }

  public boolean isHighlighted() {

    return this.highlighted;
  }

  public Object getRecipeIngredient() {

    return this.recipeIngredient;
  }
}
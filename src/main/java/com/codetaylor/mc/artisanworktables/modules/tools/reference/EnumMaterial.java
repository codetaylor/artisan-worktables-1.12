package com.codetaylor.mc.artisanworktables.modules.tools.reference;

import net.minecraft.item.Item;

import java.awt.*;

public enum EnumMaterial {

  WOOD("wood", Item.ToolMaterial.WOOD, new Color(0x493615), false, "ore:plankWood"),
  STONE("stone", Item.ToolMaterial.STONE, new Color(0x969696), false, "minecraft:stone"),
  IRON("iron", Item.ToolMaterial.IRON, new Color(0xD4D4D4), true, "ore:ingotIron"),
  GOLD("gold", Item.ToolMaterial.GOLD, new Color(0xFFE947), false, "ore:ingotGold"),
  DIAMOND("diamond", Item.ToolMaterial.DIAMOND, new Color(0x33EBCB), false, "ore:gemDiamond"),

  FLINT("flint", ModMaterials.FLINT, new Color(0x1A1A1A), true, "minecraft:flint"),
  ALUMINUM("aluminum", ModMaterials.ALUMINUM, new Color(0xC5C6D0), true, "ore:ingotAluminum"),
  BRONZE("bronze", ModMaterials.BRONZE, new Color(0xE8983F), true, "ore:ingotBronze"),
  CONSTANTAN("constantan", ModMaterials.CONSTANTAN, new Color(0xBD8D46), true, "ore:ingotConstantan"),
  COPPER("copper", ModMaterials.COPPER, new Color(0xFFA131), true, "ore:ingotCopper"),
  ELECTRUM("electrum", ModMaterials.ELECTRUM, new Color(0xFFE947), true, "ore:ingotElectrum"),
  INVAR("invar", ModMaterials.INVAR, new Color(0x8E9A95), true, "ore:ingotInvar"),
  LEAD("lead", ModMaterials.LEAD, new Color(0x8A93B1), true, "ore:ingotLead"),
  NICKEL("nickel", ModMaterials.NICKEL, new Color(0xA2975D), true, "ore:ingotNickel"),
  PLATINUM("platinum", ModMaterials.PLATINUM, new Color(0x4BACD8), true, "ore:ingotPlatinum"),
  SILVER("silver", ModMaterials.SILVER, new Color(0x7B9DA4), true, "ore:ingotSilver"),
  STEEL("steel", ModMaterials.STEEL, new Color(0x858585), false, "ore:ingotSteel"),
  TIN("tin", ModMaterials.TIN, new Color(0x7C9AB2), true, "ore:ingotTin");

  private String name;
  private Item.ToolMaterial toolMaterial;
  private int color;
  private boolean highlighted;
  private String recipeIngredient;

  EnumMaterial(String name, Item.ToolMaterial toolMaterial, Color color, boolean highlighted, String recipeIngredient) {

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

  public String getRecipeIngredient() {

    return this.recipeIngredient;
  }
}
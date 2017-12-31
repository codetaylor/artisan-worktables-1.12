package com.codetaylor.mc.artisanworktables.modules.tools.reference;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class ModMaterials {

  // @formatter:off
  public static final Item.ToolMaterial FLINT = EnumHelper.addToolMaterial("artisanworktables:FLINT", 1, 150, 3.8f, 1.0f, 10);

  // Thermal Foundation tool material stats
  // https://github.com/CoFH/ThermalFoundation/blob/master/src/main/java/cofh/thermalfoundation/init/TFEquipment.java
  public static final Item.ToolMaterial ALUMINUM = EnumHelper.addToolMaterial("artisanworktables:ALUMINUM", 1, 225, 10.0F, 1.0F, 14);
  public static final Item.ToolMaterial BRONZE = EnumHelper.addToolMaterial("artisanworktables:BRONZE", 2, 500, 6.0F, 2.0F, 15);
  public static final Item.ToolMaterial CONSTANTAN = EnumHelper.addToolMaterial("artisanworktables:CONSTANTAN",2, 275, 6.0F, 1.5F, 20);
  public static final Item.ToolMaterial COPPER = EnumHelper.addToolMaterial("artisanworktables:COPPER", 1, 175, 4.0F, 0.75F, 6);
  public static final Item.ToolMaterial ELECTRUM = EnumHelper.addToolMaterial("artisanworktables:ELECTRUM", 0, 100, 14.0F, 0.5F, 30);
  public static final Item.ToolMaterial INVAR = EnumHelper.addToolMaterial("artisanworktables:INVAR", 2, 450, 7.0F, 3.0F, 16);
  public static final Item.ToolMaterial LEAD = EnumHelper.addToolMaterial("artisanworktables:LEAD", 1, 150, 5.0F, 1.0F, 9);
  public static final Item.ToolMaterial NICKEL = EnumHelper.addToolMaterial("artisanworktables:NICKEL", 2, 300, 6.5F, 2.5F, 18);
  public static final Item.ToolMaterial PLATINUM = EnumHelper.addToolMaterial("artisanworktables:PLATINUM", 4, 1700, 9.0F, 4.0F, 9);
  public static final Item.ToolMaterial SILVER = EnumHelper.addToolMaterial("artisanworktables:SILVER", 2, 200, 6.0F, 1.5F, 20);
  public static final Item.ToolMaterial STEEL = EnumHelper.addToolMaterial("artisanworktables:STEEL", 2, 500, 6.5F, 2.5F, 10);
  public static final Item.ToolMaterial TIN = EnumHelper.addToolMaterial("artisanworktables:TIN", 1, 200, 4.5F, 1.0F, 7);
  // @formatter:on
}

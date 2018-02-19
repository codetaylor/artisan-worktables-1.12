package com.codetaylor.mc.artisanworktables.modules.tools.material;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.athenaeum.util.StringHelper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DataCustomMaterialListFactory {

  public DataCustomMaterialList create() {

    List<DataCustomMaterial> list = new ArrayList<>();

    for (EnumMaterial material : EnumMaterial.values()) {

      Color color = new Color(material.getColor());

      Object recipeIngredient = material.getRecipeIngredient();
      String ingredient;

      if (recipeIngredient instanceof String) {
        ingredient = "ore:" + recipeIngredient;

      } else {
        ResourceLocation resourceLocation = ((Item) recipeIngredient).getRegistryName();
        ingredient = resourceLocation.getResourceDomain() + ":" + resourceLocation.getResourcePath();
      }

      list.add(new DataCustomMaterial(
          material.getName(),
          material.getToolMaterial().getHarvestLevel(),
          material.getToolMaterial().getMaxUses(),
          material.getToolMaterial().getEfficiency(),
          material.getToolMaterial().getAttackDamage(),
          material.getToolMaterial().getEnchantability(),
          String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()),
          material.isHighlighted(),
          ingredient,
          "material.athenaeum." + material.getName().toLowerCase(),
          "tool" + StringHelper.capitalizeFirstLetter(material.getName())
      ));
    }

    return new DataCustomMaterialList(list);
  }

}

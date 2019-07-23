package com.codetaylor.mc.artisanworktables.modules.tools.material;

import com.codetaylor.mc.artisanworktables.api.tool.ICustomToolMaterial;

public class CustomMaterialValidator {

  public void validate(ICustomToolMaterial data) throws CustomMaterialValidationException {

    if (data.getName() == null || data.getName().isEmpty()) {
      throw new CustomMaterialValidationException("Missing or empty material name");
    }

    if (data.getHarvestLevel() < 0) {
      throw new CustomMaterialValidationException("Material: [" + data.getName() + "], key: [harvestLevel] harvest level can't be < 0");
    }

    if (data.getMaxUses() < 1) {
      throw new CustomMaterialValidationException("Material: [" + data.getName() + "], key: [maxUses] max uses can't be < 1");
    }

    if (data.getEfficiency() < 0) {
      throw new CustomMaterialValidationException("Material: [" + data.getName() + "], key: [efficiency] efficiency can't be < 0");
    }

    if (data.getDamage() < 0) {
      throw new CustomMaterialValidationException("Material: [" + data.getName() + "], key: [damage] damage can't be < 0");
    }

    if (data.getEnchantability() < 0) {
      throw new CustomMaterialValidationException("Material: [" + data.getName() + "], key: [enchantability] enchantability can't be < 0");
    }

    if (data.getColor() == null || data.getColor().isEmpty() || data.getColor().length() != 6) {
      throw new CustomMaterialValidationException("Material: [" + data.getName() + "], key: [color] invalid color, must be 6 characters");
    }

    try {
      Integer.decode("0x" + data.getColor());

    } catch (Exception e) {
      throw new CustomMaterialValidationException("Material: [" + data.getName() + "], key: [shiny] invalid color string: " + data
          .getColor());
    }

    if (data.getIngredientString() == null || data.getIngredientString().isEmpty()) {
      throw new CustomMaterialValidationException("Material: [" + data.getName() + "], key: [ingredient] missing or empty ingredient");
    }

    if (data.getLangKey() == null || data.getLangKey().isEmpty()) {
      throw new CustomMaterialValidationException("Material: [" + data.getName() + "], key: [langKey] missing or empty lang key");
    }

    if (data.getOreDictKey() == null || data.getOreDictKey().isEmpty()) {
      throw new CustomMaterialValidationException("Material: [" + data.getName() + "], key: [oreDictKey] missing or empty ore dict key");
    }
  }

}

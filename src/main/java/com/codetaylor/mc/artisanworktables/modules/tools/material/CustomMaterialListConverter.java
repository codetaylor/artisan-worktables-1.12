package com.codetaylor.mc.artisanworktables.modules.tools.material;

import java.util.ArrayList;
import java.util.List;

public class CustomMaterialListConverter {

  private CustomMaterialConverter customMaterialConverter;

  public CustomMaterialListConverter(CustomMaterialConverter customMaterialConverter) {

    this.customMaterialConverter = customMaterialConverter;
  }

  public List<CustomMaterial> convert(DataCustomMaterialList data) {

    List<CustomMaterial> result = new ArrayList<>();
    List<DataCustomMaterial> list = data.getList();

    for (DataCustomMaterial dataCustomMaterial : list) {
      result.add(this.customMaterialConverter.convert(dataCustomMaterial));
    }

    return result;
  }
}

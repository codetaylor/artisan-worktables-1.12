package com.codetaylor.mc.artisanworktables.modules.tools;

import com.codetaylor.mc.artisanworktables.api.internal.config.IModuleToolsConfig;

import java.util.Arrays;
import java.util.List;

public class ModuleToolsConfigAPIWrapper
    implements IModuleToolsConfig {

  @Override
  public boolean enableDurabilityTooltip() {

    return ModuleToolsConfig.CLIENT.ENABLE_DURABILITY_TOOLTIP;
  }

  @Override
  public String getToolByTypeOreDictPrefix() {

    return ModuleToolsConfig.TOOL_BY_TYPE_ORE_DICT_PREFIX;
  }

  @Override
  public String getToolByMaterialOreDictPrefix() {

    return ModuleToolsConfig.TOOL_BY_MATERIAL_ORE_DICT_PREFIX;
  }

  @Override
  public boolean enableModule() {

    return ModuleToolsConfig.ENABLE_MODULE;
  }

  @Override
  public boolean enableToolRecipes() {

    return ModuleToolsConfig.ENABLE_TOOL_RECIPES;
  }

  @Override
  public List<String> getEnabledToolTypes(List<String> result) {

    result.addAll(Arrays.asList(ModuleToolsConfig.ENABLED_TOOL_TYPES));
    return result;
  }

  @Override
  public boolean enableToolMaterialOreDictGroups() {

    return ModuleToolsConfig.ENABLE_TOOL_MATERIAL_ORE_DICT_GROUPS;
  }

  @Override
  public boolean enableToolTypeOreDictGroups() {

    return ModuleToolsConfig.ENABLE_TOOL_TYPE_ORE_DICT_GROUPS;
  }

  @Override
  public boolean enableToolRepair() {

    return ModuleToolsConfig.ENABLE_TOOL_REPAIR;
  }
}

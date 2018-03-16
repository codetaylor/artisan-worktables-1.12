package com.codetaylor.mc.artisanworktables.api.internal.config;

import java.util.List;

public interface IModuleToolsConfig {

  boolean enableDurabilityTooltip();

  String getToolByTypeOreDictPrefix();

  String getToolByMaterialOreDictPrefix();

  boolean enableModule();

  boolean enableToolRecipes();

  List<String> getEnabledToolTypes(List<String> result);

  boolean enableToolMaterialOreDictGroups();

  boolean enableToolTypeOreDictGroups();

}

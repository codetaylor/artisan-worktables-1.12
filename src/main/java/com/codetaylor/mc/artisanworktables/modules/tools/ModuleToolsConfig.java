package com.codetaylor.mc.artisanworktables.modules.tools;

import net.minecraftforge.common.config.Config;

@Config(modid = ModuleTools.MOD_ID, name = ModuleTools.MOD_ID + ".module.Tools")
public class ModuleToolsConfig {

  @Config.Comment({"Set to false to disable all tools."})
  public static boolean ENABLE_MODULE = true;

  @Config.Comment({"Set to false to disable all tool recipes."})
  public static boolean ENABLE_TOOL_RECIPES = true;

}

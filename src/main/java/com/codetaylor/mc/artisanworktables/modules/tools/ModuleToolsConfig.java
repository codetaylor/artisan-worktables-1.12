package com.codetaylor.mc.artisanworktables.modules.tools;

import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumMaterial;
import net.minecraftforge.common.config.Config;

import java.util.ArrayList;
import java.util.List;

@Config(modid = ModuleTools.MOD_ID, name = ModuleTools.MOD_ID + ".module.Tools")
public class ModuleToolsConfig {

  @Config.Comment({"Set to false to disable all tools."})
  public static boolean ENABLE_MODULE = true;

  @Config.Comment({"To disable a tool, remove it from this list."})
  public static String[] ENABLED_TOOLS;

  static {

    List<String> list = new ArrayList<>();

    for (String toolName : ModuleTools.TOOL_NAMES) {

      for (EnumMaterial material : ModuleTools.MATERIALS_VANILLA) {
        list.add(toolName + ":" + material.getName());
      }
    }

    ENABLED_TOOLS = list.toArray(new String[list.size()]);
  }

}

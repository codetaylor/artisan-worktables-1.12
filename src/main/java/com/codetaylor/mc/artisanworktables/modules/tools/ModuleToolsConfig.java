package com.codetaylor.mc.artisanworktables.modules.tools;

import com.codetaylor.mc.athenaeum.reference.EnumMaterial;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumWorktableToolType;
import net.minecraftforge.common.config.Config;

@Config(modid = ModuleTools.MOD_ID, name = ModuleTools.MOD_ID + ".module.Tools")
public class ModuleToolsConfig {

  @Config.Comment({
      "Set to false to disable all tools.",
      "This supersedes all other tool settings."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_MODULE = true;

  @Config.Comment({
      "Set to false to disable all tool recipes."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_TOOL_RECIPES = true;

  @Config.Comment({
      "To disable a tool type, remove it from this list.",
      "If new tool types are added to the mod, you may have to regenerate your config file",
      "or manually add them to this list to activate them."
  })
  @Config.RequiresMcRestart
  public static String[] ENABLED_TOOL_TYPES;

  static {
    ENABLED_TOOL_TYPES = new String[EnumWorktableToolType.values().length];

    for (int i = 0; i < EnumWorktableToolType.values().length; i++) {
      ENABLED_TOOL_TYPES[i] = EnumWorktableToolType.values()[i].getName();
    }
  }

  @Config.Comment({
      "To disable a tool material, remove it from this list.",
      "If new tool materials are added to the mod, you may have to regenerate your config file",
      "or manually add them to this list to activate them."
  })
  @Config.RequiresMcRestart
  public static String[] ENABLED_TOOL_MATERIALS;

  static {
    ENABLED_TOOL_MATERIALS = new String[EnumMaterial.values().length];

    for (int i = 0; i < EnumMaterial.values().length; i++) {
      ENABLED_TOOL_MATERIALS[i] = EnumMaterial.values()[i].getName();
    }
  }
}

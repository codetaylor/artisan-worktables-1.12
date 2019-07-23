package com.codetaylor.mc.artisanworktables.modules.tools;

import com.codetaylor.mc.artisanworktables.api.tool.reference.EnumWorktableToolType;
import net.minecraftforge.common.config.Config;

@Config(modid = ModuleTools.MOD_ID, name = ModuleTools.MOD_ID + "/" + ModuleTools.MOD_ID + ".module.Tools")
public class ModuleToolsConfig {

  public static Client CLIENT = new Client();

  public static class Client {

    @Config.Comment({
        "Set to false to disable the durability tooltip on tools from this mod."
    })
    public boolean ENABLE_DURABILITY_TOOLTIP = true;

  }

  @Config.Comment({
      "Change the ore dict prefix for each tool type group."
  })
  public static String TOOL_BY_TYPE_ORE_DICT_PREFIX = "artisans";

  @Config.Comment({
      "Change the ore dict prefix for each tool material type group.",
      "This is used when generating the tool material .json file.",
      "Changing this will have no effect if the 'Custom' tool material file has already been generated.",
      "You will need to regenerate the file by deleting it and running the game, or manually change the file."
  })
  public static String TOOL_BY_MATERIAL_ORE_DICT_PREFIX = "artisansTool";

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
      "Set to false to prevent creation of ore dict groups for tools by material type, ie. 'toolFlint' or 'toolCopper'."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_TOOL_MATERIAL_ORE_DICT_GROUPS = true;

  @Config.Comment({
      "Set to false to prevent creation of ore dict groups for tools by type, ie. 'artisansHammer' or 'artisansChisel'."
  })
  public static boolean ENABLE_TOOL_TYPE_ORE_DICT_GROUPS = true;

  @Config.Comment({
      "Set to false to prevent tools from being repaired.",
      "Default: " + true
  })
  public static boolean ENABLE_TOOL_REPAIR = true;
}

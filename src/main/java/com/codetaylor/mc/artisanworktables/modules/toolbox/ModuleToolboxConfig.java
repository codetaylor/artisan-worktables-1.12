package com.codetaylor.mc.artisanworktables.modules.toolbox;

import net.minecraftforge.common.config.Config;

@Config(modid = ModuleToolbox.MOD_ID, name = ModuleToolbox.MOD_ID + ".module.Toolbox")
public class ModuleToolboxConfig {

  @Config.Comment({
      "Set to false to disable the toolbox."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_MODULE = true;

  @Config.Comment({
      "Set to false to allow any item to be placed into the toolbox."
  })
  public static boolean RESTRICT_TOOLBOX_TO_TOOLS_ONLY = true;
}

package com.codetaylor.mc.artisanworktables.modules.automator;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import net.minecraftforge.common.config.Config;

@Config(modid = ModuleWorktables.MOD_ID, name = ModuleWorktables.MOD_ID + "/module.Automator")
public class ModuleAutomatorConfig {

  @Config.Comment({
      "Set to false to disable this module.",
      "Default: " + true
  })
  public static boolean ENABLE_MODULE = true;

}

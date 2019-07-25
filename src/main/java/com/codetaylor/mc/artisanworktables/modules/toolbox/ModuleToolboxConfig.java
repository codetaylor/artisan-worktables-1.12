package com.codetaylor.mc.artisanworktables.modules.toolbox;

import net.minecraftforge.common.config.Config;

@Config(modid = ModuleToolbox.MOD_ID, name = ModuleToolbox.MOD_ID + "/module.Toolbox")
public class ModuleToolboxConfig {

  @Config.Comment({
      "Set to false to disable the toolbox."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_MODULE = true;

  public static Toolbox TOOLBOX = new Toolbox();

  public static class Toolbox {

    @Config.Comment({
        "Set to false to remove the toolbox."
    })
    @Config.RequiresMcRestart
    public boolean ENABLED = true;

    @Config.Comment({
        "Set to false to allow any item to be placed into the toolbox.",
        "If set to true, only tools that are part of any worktable recipe may be stored in the toolbox."
    })
    public boolean RESTRICT_TO_TOOLS_ONLY = true;

    @Config.Comment({
        "Set to false to prevent the toolbox from keeping its contents when broken."
    })
    public boolean KEEP_CONTENTS_WHEN_BROKEN = true;
  }

  public static MechanicalToolbox MECHANICAL_TOOLBOX = new MechanicalToolbox();

  public static class MechanicalToolbox {

    @Config.Comment({
        "Set to false to remove the mechanical toolbox."
    })
    @Config.RequiresMcRestart
    public boolean ENABLED = true;

    @Config.Comment({
        "Set to false to allow any item to be placed into the mechanical toolbox.",
        "If set to true, only tools that are part of any worktable recipe may be stored in the mechanical toolbox."
    })
    public boolean RESTRICT_TO_TOOLS_ONLY = true;

    @Config.Comment({
        "Set to false to prevent the mechanical toolbox from keeping its contents when broken."
    })
    public boolean KEEP_CONTENTS_WHEN_BROKEN = true;
  }

}

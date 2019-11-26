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

  public static MechanicalArtisan MECHANICAL_ARTISAN = new MechanicalArtisan();

  public static class MechanicalArtisan {

    @Config.Comment({
        "The RF capacity of the device.",
        "Default: " + 10000
    })
    public int RF_CAPACITY = 10000;

    @Config.Comment({
        "The max transfer rate, RF per tick.",
        "Default: " + 100
    })
    public int RF_PER_TICK = 100;

    @Config.Comment({
        "The fluid capacity of the device's fluid tanks.",
        "Default: " + 8000
    })
    public int FLUID_CAPACITY = 8000;
  }
}

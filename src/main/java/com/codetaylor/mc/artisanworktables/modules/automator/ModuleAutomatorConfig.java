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
        "The AP capacity of the device.",
        "Default: " + 10000
    })
    public int AP_CAPACITY = 10000;

    @Config.Comment({
        "The max transfer rate, AP per tick.",
        "Default: " + 100
    })
    public int AP_PER_TICK = 100;
  }

  public static RFConverter RF_CONVERTER = new RFConverter();

  public static class RFConverter {

    @Config.Comment({
        "The AP conversion rate for the RF converter.",
        "Default: " + 1
    })
    public int AP_PER_RF = 1;
  }
}

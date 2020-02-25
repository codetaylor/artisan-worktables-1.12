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

    @Config.Comment({
        "The number of ticks required per craft.",
        "Default: " + (10 * 20)
    })
    public int TICKS_PER_CRAFT = 10 * 20;

    @Config.Comment({
        "The base amount of RF consumed per craft.",
        "Default: " + 1000
    })
    public int RF_PER_CRAFT = 1000;

    @Config.Comment({
        "Additional RF per item ingredient.",
        "Default: " + 100
    })
    public int RF_PER_ITEM_INGREDIENT = 100;

    @Config.Comment({
        "Additional RF per mB of fluid ingredient.",
        "Default: " + 1
    })
    public int RF_PER_MB_FLUID_INGREDIENT = 1;
  }
}

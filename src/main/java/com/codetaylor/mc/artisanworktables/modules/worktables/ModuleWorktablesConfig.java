package com.codetaylor.mc.artisanworktables.modules.worktables;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModuleWorktables.MOD_ID, name = ModuleWorktables.MOD_ID + ".module.Worktables")
public class ModuleWorktablesConfig {

  public static FluidCapacity FLUID_CAPACITY = new FluidCapacity();

  public static class FluidCapacity {

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresWorldRestart
    public int BLACKSMITH = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresWorldRestart
    public int CARPENTER = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresWorldRestart
    public int CHEMIST = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresWorldRestart
    public int ENGINEER = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresWorldRestart
    public int JEWELER = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresWorldRestart
    public int MAGE = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresWorldRestart
    public int MASON = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresWorldRestart
    public int SCRIBE = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresWorldRestart
    public int TAILOR = 4000;

  }

  @Mod.EventBusSubscriber(modid = ModuleWorktables.MOD_ID)
  private static class EventHandler {

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {

      if (event.getModID().equals(ModuleWorktables.MOD_ID)) {
        ConfigManager.sync(ModuleWorktables.MOD_ID, Config.Type.INSTANCE);
      }
    }
  }
}

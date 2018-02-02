package com.codetaylor.mc.artisanworktables.modules.worktables;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModuleWorktables.MOD_ID, name = ModuleWorktables.MOD_ID + ".module.Worktables")
public class ModuleWorktablesConfig {

  @Config.Comment({
      "If set to true, crafting tools must have sufficient durability remaining to perform the craft.",
      "If set to false, this restriction is ignored."
  })
  public static boolean RESTRICT_CRAFT_MINIUMUM_DURABILITY = true;

  public static FluidCapacity FLUID_CAPACITY = new FluidCapacity();

  public static class FluidCapacity {

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresMcRestart
    public int BASIC = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresMcRestart
    public int BLACKSMITH = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresMcRestart
    public int CARPENTER = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresMcRestart
    public int CHEMIST = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresMcRestart
    public int ENGINEER = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresMcRestart
    public int JEWELER = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresMcRestart
    public int MAGE = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresMcRestart
    public int MASON = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresMcRestart
    public int SCRIBE = 4000;

    @Config.Comment("Worktable fluid capacity (milli-buckets).")
    @Config.RequiresMcRestart
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

package com.codetaylor.mc.artisanworktables.modules.worktables;

import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Config(modid = ModuleWorktables.MOD_ID, name = ModuleWorktables.MOD_ID + ".module.Worktables")
public class ModuleWorktablesConfig {

  @Config.Comment({
      "If set to true, crafting tools must have sufficient durability remaining to perform the craft.",
      "If set to false, this restriction is ignored."
  })
  public static boolean RESTRICT_CRAFT_MINIMUM_DURABILITY = true;

  @Config.Comment("Worktable fluid capacity (milli-buckets).")
  @Config.RequiresMcRestart
  public static Map<String, Integer> FLUID_CAPACITY_WORKTABLE = new HashMap<>();

  static {

    for (String name : WorktableAPI.getWorktableNames()) {
      FLUID_CAPACITY_WORKTABLE.put(name, 4000);
    }
  }

  @Config.Comment("Workstation fluid capacity (milli-buckets).")
  @Config.RequiresMcRestart
  public static Map<String, Integer> FLUID_CAPACITY_WORKSTATION = new HashMap<>();

  static {

    for (String name : WorktableAPI.getWorktableNames()) {
      FLUID_CAPACITY_WORKSTATION.put(name, 8000);
    }
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

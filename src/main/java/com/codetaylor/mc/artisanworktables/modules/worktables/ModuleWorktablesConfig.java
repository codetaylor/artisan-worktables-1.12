package com.codetaylor.mc.artisanworktables.modules.worktables;

import com.codetaylor.mc.artisanworktables.api.ArtisanWorktablesAPI;
import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Config(modid = ModuleWorktables.MOD_ID, name = ModuleWorktables.MOD_ID + "/" + ModuleWorktables.MOD_ID + ".module.Worktables")
public class ModuleWorktablesConfig {

  @Config.Comment({
      "Set to false to disable worktables."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_WORKTABLES = true;

  @Config.Comment({
      "Set to false to disable workstations."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_WORKSTATIONS = true;

  @Config.Comment({
      "Set to false to disable workshops."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_WORKSHOPS = true;

  @Config.Comment({
      "If set to true, crafting tools must have sufficient durability remaining to perform the craft.",
      "If set to false, this restriction is ignored."
  })
  public static boolean RESTRICT_CRAFT_MINIMUM_DURABILITY = true;

  @Config.Comment("Worktable fluid capacity (milli-buckets).")
  @Config.RequiresMcRestart
  public static Map<String, Integer> FLUID_CAPACITY_WORKTABLE = new HashMap<>();

  static {

    for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
      FLUID_CAPACITY_WORKTABLE.put(name, 4000);
    }
  }

  @Config.Comment("Workstation fluid capacity (milli-buckets).")
  @Config.RequiresMcRestart
  public static Map<String, Integer> FLUID_CAPACITY_WORKSTATION = new HashMap<>();

  static {

    for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
      FLUID_CAPACITY_WORKSTATION.put(name, 8000);
    }
  }

  @Config.Comment("Workshop fluid capacity (milli-buckets).")
  @Config.RequiresMcRestart
  public static Map<String, Integer> FLUID_CAPACITY_WORKSHOP = new HashMap<>();

  static {

    for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
      FLUID_CAPACITY_WORKSHOP.put(name, 16000);
    }
  }

  public static Client CLIENT = new Client();

  public static class Client {

    @Config.Comment("Here you can change the gui text highlight color. (Hexadecimal)")
    public Map<String, String> TEXT_HIGHLIGHT_COLOR = new HashMap<>();

    /* package */ Client() {

      for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
        Color color = new Color(ArtisanWorktablesAPI.getWorktableTextHighlightColor(name));
        String hex = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        this.TEXT_HIGHLIGHT_COLOR.put(name, hex);
      }
    }

    public int getTextHighlightColor(String name) {

      return Integer.decode("0x" + this.TEXT_HIGHLIGHT_COLOR.get(name));
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

  public static boolean isTierEnabled(EnumTier tier) {

    switch (tier) {
      case WORKTABLE:
        return ModuleWorktablesConfig.ENABLE_WORKTABLES;
      case WORKSTATION:
        return ModuleWorktablesConfig.ENABLE_WORKSTATIONS;
      case WORKSHOP:
        return ModuleWorktablesConfig.ENABLE_WORKSHOPS;
      default:
        throw new IllegalArgumentException("Unknown tier: " + tier);
    }
  }
}

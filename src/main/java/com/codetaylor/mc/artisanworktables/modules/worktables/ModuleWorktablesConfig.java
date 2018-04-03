package com.codetaylor.mc.artisanworktables.modules.worktables;

import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
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

    for (String name : ArtisanAPI.getWorktableNames()) {
      FLUID_CAPACITY_WORKTABLE.put(name, 4000);
    }
  }

  @Config.Comment("Workstation fluid capacity (milli-buckets).")
  @Config.RequiresMcRestart
  public static Map<String, Integer> FLUID_CAPACITY_WORKSTATION = new HashMap<>();

  static {

    for (String name : ArtisanAPI.getWorktableNames()) {
      FLUID_CAPACITY_WORKSTATION.put(name, 8000);
    }
  }

  @Config.Comment("Workshop fluid capacity (milli-buckets).")
  @Config.RequiresMcRestart
  public static Map<String, Integer> FLUID_CAPACITY_WORKSHOP = new HashMap<>();

  static {

    for (String name : ArtisanAPI.getWorktableNames()) {
      FLUID_CAPACITY_WORKSHOP.put(name, 16000);
    }
  }

  public static Client CLIENT = new Client();

  public static class Client {

    @Config.Comment("Here you can change the gui text highlight color. (Hexadecimal)")
    public Map<String, String> TEXT_HIGHLIGHT_COLOR = new HashMap<>();

    /* package */ Client() {

      for (String name : ArtisanAPI.getWorktableNames()) {
        Color color = new Color(EnumType.fromName(name).getTextOutlineColor());
        String hex = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        this.TEXT_HIGHLIGHT_COLOR.put(name, hex);
      }
    }

    public int getTextHighlightColor(String name) {

      return Integer.decode("0x" + this.TEXT_HIGHLIGHT_COLOR.get(name));
    }

  }

  public static Pattern PATTERN = new Pattern();

  public static class Pattern {

    @Config.Comment({
        "Set to false to prevent players from sneak-clicking to clear the patterns.",
        "This is useful if you want to provide your own method for clearing patterns."
    })
    public boolean ENABLE_SNEAK_CLICK_TO_CLEAR = true;

    @Config.Comment({
        "Set to false to disable pattern creation for worktables."
    })
    public boolean ENABLE_PATTERN_CREATION_FOR_WORKTABLES = true;

    @Config.Comment({
        "Set to false to disable pattern creation for workstations."
    })
    public boolean ENABLE_PATTERN_CREATION_FOR_WORKSTATIONS = true;

    @Config.Comment({
        "Set to false to disable pattern creation for workshops."
    })
    public boolean ENABLE_PATTERN_CREATION_FOR_WORKSHOPS = true;
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

  public static boolean patternSlotsEnabledForTier(EnumTier tier) {

    switch (tier) {
      case WORKTABLE:
        return ModuleWorktablesConfig.PATTERN.ENABLE_PATTERN_CREATION_FOR_WORKTABLES;

      case WORKSTATION:
        return ModuleWorktablesConfig.PATTERN.ENABLE_PATTERN_CREATION_FOR_WORKSTATIONS;

      case WORKSHOP:
        return ModuleWorktablesConfig.PATTERN.ENABLE_PATTERN_CREATION_FOR_WORKSHOPS;

      default:
        throw new IllegalArgumentException("Unknonw tier: " + tier);
    }
  }

}

package com.codetaylor.mc.artisanworktables.modules.worktables;

import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.athenaeum.util.ArrayHelper;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.List;

@Config(modid = ModuleWorktables.MOD_ID, name = ModuleWorktables.MOD_ID + "/module.Worktables")
public class ModuleWorktablesConfig {

  @Config.Comment({
      "Tables in this list will allow crafting any of the vanilla recipes.",
      "Table id format is (type):(tier)",
      "By default, all tables are allowed."
  })
  public static String[] ENABLE_VANILLA_CRAFTING;

  static {
    List<String> list = new ArrayList<>();
    for (EnumTier tier : EnumTier.values()) {
      for (EnumType type : EnumType.values()) {
        list.add(type.getName() + ":" + tier.getName());
      }
    }
    Collections.sort(list);
    ENABLE_VANILLA_CRAFTING = list.toArray(new String[0]);
  }

  public static boolean isVanillaCraftingEnabledFor(EnumType type, EnumTier tier) {

    String key = type.getName() + ":" + tier.getName();
    return ArrayHelper.contains(ENABLE_VANILLA_CRAFTING, key);
  }

  @Config.Comment({
      "Set to true to enable log warnings for duplicate recipe names.",
      "If you're using a lot of your own recipe names, you can enable this",
      "from time to time to check that you haven't accidentally used the",
      "same name twice."
  })
  public static boolean ENABLE_DUPLICATE_RECIPE_NAME_WARNINGS = false;

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
      "Set to false to disable the joined tabs for worktables."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_TABS_WORKTABLES = true;

  @Config.Comment({
      "Set to false to disable the joined tabs for workstations."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_TABS_WORKSTATIONS = true;

  @Config.Comment({
      "Set to false to disable the joined tabs for workshops."
  })
  @Config.RequiresMcRestart
  public static boolean ENABLE_TABS_WORKSHOPS = true;

  @Config.Comment({
      "Set to false to selectively disable a table type across all table tiers."
  })
  public static Map<String, Boolean> ENABLE_TABLE_TYPE = new HashMap<>();

  static {

    for (EnumType type : EnumType.values()) {
      ENABLE_TABLE_TYPE.put(type.getName(), true);
    }
  }

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

    @Config.Comment(
        "Here you can change the gui text highlight color. (Hexadecimal)"
    )
    public Map<String, String> TEXT_HIGHLIGHT_COLOR = new HashMap<>();

    @Config.Comment(
        "Here you can change the gui fluid tank overlay color. (Hexadecimal)"
    )
    public Map<String, String> FLUID_TANK_OVERLAY_COLOR = new HashMap<>();

    @Config.Comment({
        "Here you can change the background color of the crafting grid slots,",
        "tool slots, secondary input slots, and extra output slots. (Hexadecimal)",
        "Set to 'off' to use existing background."
    })
    public Map<String, String> SLOT_BACKGROUND_COLOR = new HashMap<>();

    @Config.Comment({
        "Here you can change the background color of the fluid tank. (Hexadecimal)",
        "Set to 'off' to use existing background."
    })
    public Map<String, String> FLUID_TANK_BACKGROUND_COLOR = new HashMap<>();

    @Config.Comment({
        "Here you can change the background color of the main output slot. (Hexadecimal)",
        "Set to 'off' to use existing background."
    })
    public Map<String, String> MAIN_OUTPUT_SLOT_BACKGROUND_COLOR = new HashMap<>();

    @Config.Comment({
        "Here you can change the background color of the player's inventory slots. (Hexadecimal)",
        "Set to 'off' to use existing background."
    })
    public Map<String, String> PLAYER_INVENTORY_SLOT_BACKGROUND_COLOR = new HashMap<>();

    /* package */ Client() {

      List<String> worktableNames = ArtisanAPI.getWorktableNames();

      // Text highlight color
      for (String name : worktableNames) {
        Color color = new Color(EnumType.fromName(name).getTextOutlineColor());
        String hex = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        this.TEXT_HIGHLIGHT_COLOR.put(name, hex);
      }

      // Fluid tank overlay color
      for (String name : worktableNames) {
        Color color = new Color(EnumType.fromName(name).getTextOutlineColor());
        String hex = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        this.FLUID_TANK_OVERLAY_COLOR.put(name, hex);
      }

      // Crafting grid slot background color
      for (String name : worktableNames) {
        this.SLOT_BACKGROUND_COLOR.put(name, "off");
      }

      // Fluid tank background color
      for (String name : worktableNames) {
        this.FLUID_TANK_BACKGROUND_COLOR.put(name, "off");
      }

      // Main output slot background color
      for (String name : worktableNames) {
        this.MAIN_OUTPUT_SLOT_BACKGROUND_COLOR.put(name, "off");
      }

      // Player inventory slot background color
      for (String name : worktableNames) {
        this.PLAYER_INVENTORY_SLOT_BACKGROUND_COLOR.put(name, "off");
      }
    }

    public int getTextHighlightColor(String name) {

      return Integer.decode("0x" + this.TEXT_HIGHLIGHT_COLOR.get(name));
    }

    public int getFluidTankOverlayColor(String name) {

      return Integer.decode("0x" + this.FLUID_TANK_OVERLAY_COLOR.get(name));
    }

    private Integer getColorOrNull(Map<String, String> map, String name) {

      String s = map.get(name);

      if ("off".equals(s)) {
        return null;
      }

      return Integer.decode("0x" + s);
    }

    @Nullable
    public Integer getCraftingGridSlotBackgroundColor(String name) {

      return this.getColorOrNull(this.SLOT_BACKGROUND_COLOR, name);
    }

    @Nullable
    public Integer getFluidTankBackgroundColor(String name) {

      return this.getColorOrNull(this.FLUID_TANK_BACKGROUND_COLOR, name);
    }

    @Nullable
    public Integer getMainOutputSlotBackgroundColor(String name) {

      return this.getColorOrNull(this.MAIN_OUTPUT_SLOT_BACKGROUND_COLOR, name);
    }

    @Nullable
    public Integer getPlayerInventorySlotBackgroundColor(String name) {

      return this.getColorOrNull(this.PLAYER_INVENTORY_SLOT_BACKGROUND_COLOR, name);
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

  public static boolean isTypeEnabled(EnumType type) {

    return ENABLE_TABLE_TYPE.get(type.getName());
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
        throw new IllegalArgumentException("Unknown tier: " + tier);
    }
  }

}

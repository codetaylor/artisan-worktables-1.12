package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public enum EnumGameStageRequire {
  ALL("all"),
  ANY("any");

  private static final Map<String, EnumGameStageRequire> NAME_ENUM_MAP;

  static {
    NAME_ENUM_MAP = new LinkedHashMap<>();

    for (EnumGameStageRequire require : EnumGameStageRequire.values()) {
      NAME_ENUM_MAP.put(require.name, require);
    }
  }

  private String name;

  EnumGameStageRequire(String name) {

    this.name = name;
  }

  @Nullable
  public static EnumGameStageRequire fromName(String name) {

    return NAME_ENUM_MAP.get(name.toLowerCase());
  }
}

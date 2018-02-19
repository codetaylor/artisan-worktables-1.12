package com.codetaylor.mc.artisanworktables.modules.worktables.reference;

public enum EnumTier {

  WORKTABLE(0, "worktable"),
  WORKSTATION(1, "workstation"),
  WORKSHOP(2, "workshop");

  private int id;
  private String name;

  EnumTier(int id, String name) {

    this.id = id;
    this.name = name;
  }

  public int getId() {

    return this.id;
  }

  public String getName() {

    return this.name;
  }

  public static EnumTier fromId(int id) {

    for (EnumTier tier : EnumTier.values()) {

      if (tier.getId() == id) {
        return tier;
      }
    }

    throw new IllegalArgumentException("Invalid id: " + id);
  }
}

package com.codetaylor.mc.artisanworktables.modules.worktables.reference;

public enum EnumTier {

  WORKTABLE(0, "worktable"),
  WORKSTATION(1, "workstation");

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
}

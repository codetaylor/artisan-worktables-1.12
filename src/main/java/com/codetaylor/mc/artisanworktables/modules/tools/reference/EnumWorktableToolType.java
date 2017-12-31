package com.codetaylor.mc.artisanworktables.modules.tools.reference;

public enum EnumWorktableToolType {

  BLACKSMITHS_CUTTERS("blacksmiths_cutters"),
  BLACKSMITHS_HAMMER("blacksmiths_hammer"),
  CARPENTERS_HAMMER("carpenters_hammer"),
  CARPENTERS_HANDSAW("carpenters_handsaw"),
  JEWELERS_GEMCUTTER("jewelers_gemcutter"),
  JEWELERS_PLIERS("jewelers_pliers"),
  MASONS_CHISEL("masons_chisel"),
  MASONS_TROWEL("masons_trowel"),
  TAILORS_NEEDLE("tailors_needle"),
  TAILORS_SHEARS("tailors_shears");

  private String name;

  EnumWorktableToolType(String name) {

    this.name = name;
  }

  public String getName() {

    return this.name;
  }
}

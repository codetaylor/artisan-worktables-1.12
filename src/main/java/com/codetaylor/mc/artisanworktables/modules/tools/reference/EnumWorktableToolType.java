package com.codetaylor.mc.artisanworktables.modules.tools.reference;

public enum EnumWorktableToolType {

  BLACKSMITHS_CUTTERS("blacksmiths_cutters"),
  BLACKSMITHS_HAMMER("blacksmiths_hammer"),
  CARPENTERS_HAMMER("carpenters_hammer"),
  CARPENTERS_HANDSAW("carpenters_handsaw"),
  ENGINEERS_DRIVER("engineers_driver"),
  ENGINEERS_SPANNER("engineers_spanner"),
  JEWELERS_GEMCUTTER("jewelers_gemcutter"),
  JEWELERS_PLIERS("jewelers_pliers"),
  MAGES_ATHAME("mages_athame"),
  MAGES_GRIMOIRE("mages_grimoire"),
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

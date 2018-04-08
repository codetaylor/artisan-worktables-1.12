package com.codetaylor.mc.artisanworktables.modules.tools.reference;

public enum EnumWorktableToolType {

  // Blacksmith
  BLACKSMITHS_CUTTERS("artisans_cutters", "Cutters"),
  BLACKSMITHS_HAMMER("artisans_hammer", "Hammer"),

  // Carpenter
  CARPENTERS_HAMMER("artisans_framing_hammer", "FramingHammer"),
  CARPENTERS_HANDSAW("artisans_handsaw", "Handsaw"),

  // Chef
  CHEFS_CUTTING_BOARD("artisans_cutting_board", "CuttingBoard"),
  CHEFS_PAN("artisans_pan", "Pan"),

  // Chemist
  CHEMISTS_BEAKER("artisans_beaker", "Beaker"),
  CHEMISTS_BURNER("artisans_burner", "Burner"),

  // Engineer
  ENGINEERS_DRIVER("artisans_driver", "Driver"),
  ENGINEERS_SPANNER("artisans_spanner", "Spanner"),

  // Farmer
  FARMERS_LENS("artisans_lens", "Lens"),
  FARMERS_SIFTER("artisans_sifter", "Sifter"),

  // Jeweler
  JEWELERS_CUTTER("artisans_cutter", "Cutter"),
  JEWELERS_PLIERS("artisans_pliers", "Pliers"),

  // Mage
  MAGES_ATHAME("artisans_athame", "Athame"),
  MAGES_GRIMOIRE("artisans_grimoire", "Grimoire"),

  // Mason
  MASONS_CHISEL("artisans_chisel", "Chisel"),
  MASONS_TROWEL("artisans_trowel", "Trowel"),

  // Potter
  POTTERS_CARVER("artisans_carver", "Carver"),

  // Scribe
  SCRIBES_COMPASS("artisans_compass", "Compass"),
  SCRIBES_QUILL("artisans_quill", "Quill"),

  // Tailor
  TAILORS_NEEDLE("artisans_needle", "Needle"),
  TAILORS_SHEARS("artisans_shears", "Shears"),

  // Tanner
  TANNERS_PUNCH("artisans_punch", "Punch"),
  TANNERS_GROOVER("artisans_groover", "Groover"),

  // Universal
  UNIVERSAL_MORTAR("artisans_mortar", "Mortar"),
  UNIVERSAL_KNIFE("artisans_knife", "Knife");

  private final String name;
  private final String oreDictSuffix;

  EnumWorktableToolType(String name, String oreDictSuffix) {

    this.name = name;
    this.oreDictSuffix = oreDictSuffix;
  }

  public String getName() {

    return this.name;
  }

  public String getOreDictSuffix() {

    return this.oreDictSuffix;
  }
}

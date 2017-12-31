package com.codetaylor.mc.artisanworktables.modules.tools.reference;

import net.minecraft.init.Items;

import java.util.EnumMap;

public class ModuleRecipes {

  public static final EnumMap<EnumWorktableToolType, Object[]> RECIPE_MAP;
  public static final String MATERIAL_ALIAS = "#material_alias";

  static {
    RECIPE_MAP = new EnumMap<>(EnumWorktableToolType.class);

    RECIPE_MAP.put(
        EnumWorktableToolType.BLACKSMITHS_CUTTERS,
        new Object[]{
            ". .",
            " x ",
            "s s",
            'x', Items.STRING,
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.BLACKSMITHS_HAMMER,
        new Object[]{
            " .x",
            " s.",
            "s  ",
            'x', Items.STRING,
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.CARPENTERS_HAMMER,
        new Object[]{
            " ..",
            " sx",
            "s  ",
            'x', Items.STRING,
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.CARPENTERS_HANDSAW,
        new Object[]{
            " .s",
            ".s ",
            "s  ",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.JEWELERS_GEMCUTTER,
        new Object[]{
            "  x",
            " ..",
            "s  ",
            'x', Items.STRING,
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.JEWELERS_PLIERS,
        new Object[]{
            ". .",
            "sxs",
            "s s",
            'x', Items.STRING,
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.MASONS_CHISEL,
        new Object[]{
            "  .",
            " . ",
            "s  ",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.MASONS_TROWEL,
        new Object[]{
            "  .",
            " s.",
            "s  ",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.TAILORS_NEEDLE,
        new Object[]{
            "  .",
            " .x",
            "s  ",
            'x', Items.STRING,
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.TAILORS_SHEARS,
        new Object[]{
            " . ",
            "sx.",
            " s ",
            'x', Items.STRING,
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );
  }

  public static Object[] getRecipeDefinition(EnumWorktableToolType type, Object substitution) {

    Object[] objects = RECIPE_MAP.get(type);

    if (objects == null) {
      throw new RuntimeException("Missing recipe definition for: " + type);
    }

    Object[] result = new Object[objects.length];
    System.arraycopy(objects, 0, result, 0, objects.length);

    for (int i = 0; i < result.length; i++) {

      if (result[i] == MATERIAL_ALIAS) {
        result[i] = substitution;
      }
    }

    return result;
  }

}

package com.codetaylor.mc.artisanworktables.modules.tools.recipe;

import com.codetaylor.mc.artisanworktables.modules.tools.item.ItemWorktableTool;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumWorktableToolType;
import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.EnumMap;
import java.util.List;

public class ModuleToolsRecipes {

  /**
   * Contains the recipe definitions for each tool type.
   */
  private static final EnumMap<EnumWorktableToolType, Object[]> RECIPE_MAP;

  /**
   * The alias used for ingredient substitution. This is replaced before a recipe is registered.
   */
  private static final String MATERIAL_ALIAS = "#material_alias";

  static {
    RECIPE_MAP = new EnumMap<>(EnumWorktableToolType.class);

    RECIPE_MAP.put(
        EnumWorktableToolType.BLACKSMITHS_CUTTERS,
        new Object[]{
            ". .",
            " x ",
            "s s",
            'x', "string",
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
            'x', "string",
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
            'x', "string",
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
        EnumWorktableToolType.JEWELERS_CUTTER,
        new Object[]{
            "  x",
            " ..",
            "s  ",
            'x', "string",
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
            'x', "string",
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
            'x', "string",
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
            'x', "string",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.ENGINEERS_DRIVER,
        new Object[]{
            "  .",
            " . ",
            "sx ",
            'x', "string",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.ENGINEERS_SPANNER,
        new Object[]{
            " . ",
            " s.",
            "s  ",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.MAGES_ATHAME,
        new Object[]{
            "  .",
            "x. ",
            "sl ",
            'l', "gemLapis",
            'x', "string",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.MAGES_GRIMOIRE,
        new Object[]{
            " . ",
            "xbx",
            " . ",
            'b', Items.BOOK,
            'x', "string",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.SCRIBES_COMPASS,
        new Object[]{
            " s ",
            "sxs",
            ". .",
            'x', "string",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.SCRIBES_QUILL,
        new Object[]{
            "  f",
            " .x",
            ".  ",
            'x', "string",
            'f', Items.FEATHER,
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.CHEMISTS_BURNER,
        new Object[]{
            " . ",
            " . ",
            "ppp",
            'p', "plankWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.CHEMISTS_BEAKER,
        new Object[]{
            ".g.",
            " g ",
            'g', "blockGlass",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.FARMERS_LENS,
        new Object[]{
            "   ",
            ".g.",
            "s  ",
            'g', "paneGlassColorless",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.FARMERS_SIFTER,
        new Object[]{
            "s.s",
            "sxs",
            "s.s",
            'x', "string",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.CHEFS_CUTTING_BOARD,
        new Object[]{
            "  s",
            " . ",
            ".bb",
            'b', "slabWood",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.CHEFS_PAN,
        new Object[]{
            "s  ",
            "c..",
            'c', Items.CLAY_BALL,
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.UNIVERSAL_MORTAR,
        new Object[]{
            "  s",
            ".f ",
            " . ",
            'f', Items.FLINT,
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.UNIVERSAL_KNIFE,
        new Object[]{
            "  .",
            "x. ",
            "sx ",
            'x', "string",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

    RECIPE_MAP.put(
        EnumWorktableToolType.TANNERS_PUNCH,
        new Object[]{
            "  .",
            " sx",
            ".x ",
            'x', "string",
            's', "stickWood",
            '.', MATERIAL_ALIAS
        }
    );

  }

  /**
   * Iterates through all given items in the tool list and registers a recipe for each.
   *
   * @param registry the recipe registry
   * @param modId    the mod id
   * @param toolList the tool list
   */
  public static void register(IForgeRegistry<IRecipe> registry, String modId, List<ItemWorktableTool> toolList) {

    RecipeItemParser recipeItemParser = new RecipeItemParser();

    for (ItemWorktableTool item : toolList) {

      try {
        // Convert ingredient
        String ingredientString = item.getMaterial().getIngredientString();

        ParseResult parseResult = recipeItemParser.parse(ingredientString);

        if (parseResult == ParseResult.NULL) {
          throw new MalformedRecipeItemException("Unable to parse ingredient [" + item.getMaterial()
              .getIngredientString() + "] for material [" + item.getMaterial() + "]");
        }

        Object ingredient;

        if ("ore".equals(parseResult.getDomain())) {
          ingredient = parseResult.getPath();

        } else {

          Item parsedItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parseResult.getDomain(), parseResult.getPath()));

          if (parsedItem == null) {
            throw new MalformedRecipeItemException("Unable to find registered item: " + parseResult.toString());
          }

          if (parseResult.getMeta() == OreDictionary.WILDCARD_VALUE) {
            throw new MalformedRecipeItemException("Wildcard value not accepted for tool material ingredients: " + parseResult
                .toString());
          }

          ingredient = new ItemStack(parsedItem, 1, parseResult.getMeta());
        }

        Object[] recipeDefinition = ModuleToolsRecipes.getRecipeDefinition(
            item.getType(),
            ingredient
        );

        if (recipeDefinition == null) {
          throw new RuntimeException("Missing recipe definition for tool type: " + item.getType().getName());
        }

        ShapedOreRecipe recipe = new ShapedOreRecipe(null, item, recipeDefinition);
        recipe.setRegistryName(new ResourceLocation(
            modId,
            "recipe." + item.getName() + "." + item.getMaterial().getDataCustomMaterial().getName()
        ));

        registry.register(recipe);

      } catch (Exception e) {
        throw new RuntimeException("Error registering recipe", e);
      }
    }
  }

  /**
   * Returns an object array containing the recipe shape and ingredient composition. Replaces
   * the material alias token with the given substitution.
   *
   * @param type         the tool type
   * @param substitution the substitution
   * @return object array containing the recipe shape and ingredient composition
   */
  private static Object[] getRecipeDefinition(EnumWorktableToolType type, Object substitution) {

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

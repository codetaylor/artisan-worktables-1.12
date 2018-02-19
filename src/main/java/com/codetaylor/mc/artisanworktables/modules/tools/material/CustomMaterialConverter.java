package com.codetaylor.mc.artisanworktables.modules.tools.material;

import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;

public class CustomMaterialConverter {

  private RecipeItemParser recipeItemParser;

  public CustomMaterialConverter(RecipeItemParser recipeItemParser) {

    this.recipeItemParser = recipeItemParser;
  }

  public CustomMaterial convert(DataCustomMaterial data) throws MalformedRecipeItemException {

    // Convert tool material
    Item.ToolMaterial toolMaterial = EnumHelper.addToolMaterial(
        "artisanworktables:" + data.getName().toUpperCase(),
        data.getHarvestLevel(),
        data.getMaxUses(),
        data.getEfficiency(),
        data.getDamage(),
        data.getEnchantability()
    );

    // Convert color
    Integer colorInt = Integer.decode("0x" + data.getColor());
    int color = new Color(colorInt).getRGB();

    // Convert ingredient
    ParseResult parseResult = this.recipeItemParser.parse(data.getIngredientString());

    if (parseResult == ParseResult.NULL) {
      throw new MalformedRecipeItemException("Unable to parse ingredient [" + data.getIngredientString() + "] for material [" + data
          .getName() + "]");
    }

    Object ingredient;

    if ("ore".equals(parseResult.getDomain())) {
      ingredient = parseResult.getPath();

    } else {

      Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parseResult.getDomain(), parseResult.getPath()));

      if (item == null) {
        throw new MalformedRecipeItemException("Unable to find registered item: " + parseResult.toString());
      }

      if (parseResult.getMeta() == OreDictionary.WILDCARD_VALUE) {
        throw new MalformedRecipeItemException("Wildcard value not accepted for tool material ingredients: " + parseResult
            .toString());
      }

      ingredient = new ItemStack(item, 1, parseResult.getMeta());
    }

    return new CustomMaterial(data, toolMaterial, color, ingredient);
  }

}

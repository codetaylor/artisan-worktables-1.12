package com.codetaylor.mc.artisanworktables.modules.tools.material;

import com.codetaylor.mc.artisanworktables.api.internal.tool.CustomMaterial;
import com.codetaylor.mc.artisanworktables.api.tool.ICustomToolMaterial;
import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import java.awt.*;

public class CustomMaterialConverter {

  private RecipeItemParser recipeItemParser;

  public CustomMaterialConverter(RecipeItemParser recipeItemParser) {

    this.recipeItemParser = recipeItemParser;
  }

  public CustomMaterial convert(ICustomToolMaterial data) throws MalformedRecipeItemException {

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

    return new CustomMaterial(data, toolMaterial, color, data.getIngredientString());
  }

}

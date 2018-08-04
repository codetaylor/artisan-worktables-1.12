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

    return new CustomMaterial(data, toolMaterial, color, data.getIngredientString());
  }

}

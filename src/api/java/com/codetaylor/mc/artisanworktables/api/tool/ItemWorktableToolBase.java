package com.codetaylor.mc.artisanworktables.api.tool;

import com.codetaylor.mc.artisanworktables.api.ArtisanConfig;
import com.codetaylor.mc.artisanworktables.api.internal.tool.CustomMaterial;
import com.codetaylor.mc.artisanworktables.api.tool.reference.EnumWorktableToolType;
import com.codetaylor.mc.athenaeum.parser.recipe.item.MalformedRecipeItemException;
import com.codetaylor.mc.athenaeum.parser.recipe.item.ParseResult;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public abstract class ItemWorktableToolBase
    extends ItemTool {

  public static final String TOOLTIP_DURABILITY = "item.artisanworktables.tooltip.durability";

  protected EnumWorktableToolType type;
  protected CustomMaterial material;
  protected Ingredient repairIngredient;

  private static final Logger LOGGER = LogManager.getLogger(ItemWorktableToolBase.class);

  public ItemWorktableToolBase(ToolMaterial materialIn, Set<Block> effectiveBlocksIn, EnumWorktableToolType type, CustomMaterial material) {

    super(materialIn, effectiveBlocksIn);
    this.type = type;
    this.material = material;

    if (ArtisanConfig.MODULE_TOOLS_CONFIG.enableToolRepair()) {
      this.repairIngredient = this.getRepairIngredient(this.getMaterial().getIngredientString());
    }
  }

  @Override
  public boolean isEnchantable(@Nonnull ItemStack stack) {

    return ArtisanConfig.MODULE_TOOLS_CONFIG.enableToolEnchanting();
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

    return ArtisanConfig.MODULE_TOOLS_CONFIG.enableToolRepair()
        && ArtisanConfig.MODULE_TOOLS_CONFIG.allowToolEnchantment(enchantment)
        && super.canApplyAtEnchantingTable(stack, enchantment);
  }

  @Nullable
  private Ingredient getRepairIngredient(String ingredientString) {

    try {
      ParseResult parseResult = RecipeItemParser.INSTANCE.parse(ingredientString);

      if ("ore".equals(parseResult.getDomain())) {
        return new OreIngredient(parseResult.getPath());

      } else {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parseResult.getDomain(), parseResult.getPath()));

        if (item != null) {
          return Ingredient.fromStacks(new ItemStack(item, 1, parseResult.getMeta()));
        }
      }

    } catch (MalformedRecipeItemException e) {
      LOGGER.error("", e);
    }

    return null;
  }

  public EnumWorktableToolType getType() {

    return this.type;
  }

  public String getName() {

    return this.type.getName();
  }

  public CustomMaterial getMaterial() {

    return this.material;
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, @Nonnull ItemStack repair) {

    return this.repairIngredient != null && this.repairIngredient.apply(repair);
  }

  @Nonnull
  @Override
  public String getItemStackDisplayName(@Nonnull ItemStack stack) {

    // This is called on the server so we can't use net/minecraft/client/resources/I18n here.

    Item item = stack.getItem();

    if (item instanceof ItemWorktableToolBase) {
      CustomMaterial material = ((ItemWorktableToolBase) item).getMaterial();
      String parameter = I18n.translateToLocalFormatted(material.getDataCustomMaterial().getLangKey());
      return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".name", parameter);
    }

    return super.getItemStackDisplayName(stack);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(
      ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn
  ) {

    super.addInformation(stack, worldIn, tooltip, flagIn);

    if (ArtisanConfig.MODULE_TOOLS_CONFIG.enableDurabilityTooltip()) {
      tooltip.add(TextFormatting.GRAY + net.minecraft.client.resources.I18n.format(
          ItemWorktableToolBase.TOOLTIP_DURABILITY,
          stack.getMaxDamage() - stack.getItemDamage(),
          stack.getMaxDamage()
      ));
    }
  }
}

package com.codetaylor.mc.artisanworktables.modules.tools.item;

import com.codetaylor.mc.artisanworktables.modules.tools.ModuleTools;
import com.codetaylor.mc.artisanworktables.modules.tools.ModuleToolsConfig;
import com.codetaylor.mc.artisanworktables.modules.tools.material.CustomMaterial;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumWorktableToolType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ItemWorktableTool
    extends ItemTool {

  private EnumWorktableToolType type;
  private CustomMaterial material;

  public ItemWorktableTool(
      EnumWorktableToolType type,
      CustomMaterial material
  ) {

    super(material.getToolMaterial(), Collections.emptySet());
    this.type = type;
    this.material = material;
    this.setMaxStackSize(1);
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
  public boolean isEnchantable(ItemStack stack) {

    return false;
  }

  @Nonnull
  @Override
  public String getItemStackDisplayName(@Nonnull ItemStack stack) {

    // This is called on the server so we can't use net/minecraft/client/resources/I18n here.

    Item item = stack.getItem();

    if (item instanceof ItemWorktableTool) {
      CustomMaterial material = ((ItemWorktableTool) item).getMaterial();
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

    if (ModuleToolsConfig.CLIENT.ENABLE_DURABILITY_TOOLTIP) {
      tooltip.add(TextFormatting.GRAY + net.minecraft.client.resources.I18n.format(
          ModuleTools.Lang.TOOLTIP_DURABILITY,
          stack.getMaxDamage() - stack.getItemDamage(),
          stack.getMaxDamage()
      ));
    }
  }
}

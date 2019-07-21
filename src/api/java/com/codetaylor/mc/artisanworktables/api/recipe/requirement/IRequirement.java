package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRequirement<C extends IRequirementContext> {

  /**
   * @return the requirement's resource location
   */
  ResourceLocation getResourceLocation();

  /**
   * @param context the requirement context
   * @return true if the requirement passes with the given context
   */
  boolean match(C context);

  /**
   * @return true if JEI should hide any recipe with this requirement when it loads
   */
  @SideOnly(Side.CLIENT)
  default boolean shouldJEIHideOnLoad() {

    return false;
  }

  @SideOnly(Side.CLIENT)
  default boolean shouldJEIHideOnUpdate() {

    return false;
  }
}

package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraft.util.ResourceLocation;

public interface IRequirement<C extends IRequirementContext> {

  ResourceLocation getResourceLocation();

  boolean match(C context);

}

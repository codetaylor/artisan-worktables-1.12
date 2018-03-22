package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraft.util.ResourceLocation;

public interface IMatchRequirement<C extends IMatchRequirementContext> {

  ResourceLocation getResourceLocation();

  boolean match(C context);

}

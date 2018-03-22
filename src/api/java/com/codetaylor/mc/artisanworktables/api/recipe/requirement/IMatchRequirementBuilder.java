package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IMatchRequirementBuilder<C extends IMatchRequirementContext, R extends IMatchRequirement<C>> {

  @Nonnull
  String getModId();

  @Nonnull
  ResourceLocation getResourceLocation();

  @Nullable
  R create();

}

package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRequirementBuilder<C extends IRequirementContext, R extends IRequirement<C>> {

  @Nonnull
  String getRequirementId();

  @Nonnull
  ResourceLocation getResourceLocation();

  @Nullable
  R create();

}

package com.codetaylor.mc.artisanworktables.api.recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IMatchRequirementBuilder<C extends IMatchRequirementContext, R extends IMatchRequirement<C>> {

  @Nonnull
  String getModId();

  @Nullable
  R create();

}

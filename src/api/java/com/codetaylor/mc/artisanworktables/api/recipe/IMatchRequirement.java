package com.codetaylor.mc.artisanworktables.api.recipe;

public interface IMatchRequirement<C extends IMatchRequirementContext> {

  String getModId();

  boolean match(C context);

}

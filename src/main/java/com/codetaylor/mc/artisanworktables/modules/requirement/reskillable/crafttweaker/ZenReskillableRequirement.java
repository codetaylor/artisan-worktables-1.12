package com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.crafttweaker;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.integration.requirement.Reskillable")
public class ZenReskillableRequirement {

  @ZenMethod
  public static ZenReskillableRequirementBuilder add(String requirementString) {

    ZenReskillableRequirementBuilder builder = new ZenReskillableRequirementBuilder();
    builder.add(requirementString);
    return builder;
  }

  @ZenMethod
  public static ZenReskillableRequirementBuilder addAll(String[] requirementStrings) {

    ZenReskillableRequirementBuilder builder = new ZenReskillableRequirementBuilder();
    builder.addAll(requirementStrings);
    return builder;
  }

}

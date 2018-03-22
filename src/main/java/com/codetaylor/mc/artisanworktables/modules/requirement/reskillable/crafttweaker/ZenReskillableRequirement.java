package com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.crafttweaker;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.integration.requirement.Reskillable")
public class ZenReskillableRequirement {

  @ZenMethod
  public static ZenReskillableMatchRequirementBuilder add(String requirementString) {

    ZenReskillableMatchRequirementBuilder builder = new ZenReskillableMatchRequirementBuilder();
    builder.add(requirementString);
    return builder;
  }

  @ZenMethod
  public static ZenReskillableMatchRequirementBuilder addAll(String[] requirementStrings) {

    ZenReskillableMatchRequirementBuilder builder = new ZenReskillableMatchRequirementBuilder();
    builder.addAll(requirementStrings);
    return builder;
  }

  private ZenReskillableRequirement() {
    //
  }

}

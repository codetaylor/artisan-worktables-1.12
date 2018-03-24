package com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.crafttweaker;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.integration.requirement.GameStages")
public class ZenGameStagesRequirement {

  @ZenMethod
  public static ZenGameStagesRequirementBuilder allOf(String[] requireAllStages) {

    ZenGameStagesRequirementBuilder builder = new ZenGameStagesRequirementBuilder();
    return builder.allOf(requireAllStages);
  }

  @ZenMethod
  public static ZenGameStagesRequirementBuilder anyOf(String[] requireAnyStage) {

    ZenGameStagesRequirementBuilder builder = new ZenGameStagesRequirementBuilder();
    return builder.anyOf(requireAnyStage);
  }

  @ZenMethod
  public static ZenGameStagesRequirementBuilder exclude(String[] excludeStages) {

    ZenGameStagesRequirementBuilder builder = new ZenGameStagesRequirementBuilder();
    return builder.exclude(excludeStages);
  }

}

package com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.crafttweaker;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.integration.requirement.GameStages")
public class ZenGameStagesRequirement {

  @ZenMethod
  public static ZenGameStagesRequirementBuilder all(String[] requireAllStages) {

    ZenGameStagesRequirementBuilder builder = new ZenGameStagesRequirementBuilder();
    return builder.all(requireAllStages);
  }

  @ZenMethod
  public static ZenGameStagesRequirementBuilder any(String[] requireAnyStage) {

    ZenGameStagesRequirementBuilder builder = new ZenGameStagesRequirementBuilder();
    return builder.any(requireAnyStage);
  }

  @ZenMethod
  public static ZenGameStagesRequirementBuilder exclude(String[] excludeStages) {

    ZenGameStagesRequirementBuilder builder = new ZenGameStagesRequirementBuilder();
    return builder.exclude(excludeStages);
  }

}

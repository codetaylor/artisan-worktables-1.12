package com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.crafttweaker;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.integration.requirement.GameStages")
public class ZenGameStagesRequirement {

  @ZenMethod
  public static ZenGameStagesMatchRequirementBuilder all(String[] requireAllStages) {

    ZenGameStagesMatchRequirementBuilder builder = new ZenGameStagesMatchRequirementBuilder();
    return builder.all(requireAllStages);
  }

  @ZenMethod
  public static ZenGameStagesMatchRequirementBuilder any(String[] requireAnyStage) {

    ZenGameStagesMatchRequirementBuilder builder = new ZenGameStagesMatchRequirementBuilder();
    return builder.any(requireAnyStage);
  }

  @ZenMethod
  public static ZenGameStagesMatchRequirementBuilder exclude(String[] excludeStages) {

    ZenGameStagesMatchRequirementBuilder builder = new ZenGameStagesMatchRequirementBuilder();
    return builder.exclude(excludeStages);
  }

  private ZenGameStagesRequirement() {
    //
  }

}

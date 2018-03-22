package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.integration.requirement;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.integration.requirement.GameStages")
public class ZenGameStagesRequirement {

  @ZenMethod
  public static ZenGameStagesMatchRequirementBuilder requirement() {

    return new ZenGameStagesMatchRequirementBuilder();
  }

  private ZenGameStagesRequirement() {
    //
  }

}

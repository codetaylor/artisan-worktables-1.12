package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.integration.requirement;

import com.codetaylor.mc.artisanworktables.api.recipe.IMatchRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.IMatchRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.gamestages.GameStagesMatchRequirement;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.gamestages.GameStagesMatchRequirementBuilder;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ZenGameStagesMatchRequirementBuilder
    implements IMatchRequirementBuilder {

  private GameStagesMatchRequirementBuilder builder;

  public ZenGameStagesMatchRequirementBuilder() {

    this.builder = new GameStagesMatchRequirementBuilder();
  }

  @ZenMethod
  public ZenGameStagesMatchRequirementBuilder all(String[] requireAllStages) {

    this.builder.all(requireAllStages);
    return this;
  }

  @ZenMethod
  public ZenGameStagesMatchRequirementBuilder one(String[] requireOneStage) {

    this.builder.one(requireOneStage);
    return this;
  }

  @ZenMethod
  public ZenGameStagesMatchRequirementBuilder exclude(String[] excludeStages) {

    this.builder.exclude(excludeStages);
    return this;
  }

  @Nonnull
  @Override
  public String getModId() {

    return GameStagesMatchRequirement.MOD_ID;
  }

  @Nullable
  @Override
  public IMatchRequirement create() {

    return this.builder.create();
  }

}

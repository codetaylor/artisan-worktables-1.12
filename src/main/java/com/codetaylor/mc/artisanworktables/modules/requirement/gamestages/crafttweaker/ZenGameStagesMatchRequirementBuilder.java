package com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesMatchRequirement;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesMatchRequirementBuilder;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ZenGameStagesMatchRequirementBuilder
    implements IMatchRequirementBuilder {

  private GameStagesMatchRequirementBuilder builder;

  /* package */ ZenGameStagesMatchRequirementBuilder() {

    this.builder = new GameStagesMatchRequirementBuilder();
  }

  @ZenMethod
  public ZenGameStagesMatchRequirementBuilder all(String[] requireAllStages) {

    this.builder.all(requireAllStages);
    return this;
  }

  @ZenMethod
  public ZenGameStagesMatchRequirementBuilder any(String[] requireOneStage) {

    this.builder.any(requireOneStage);
    return this;
  }

  @ZenMethod
  public ZenGameStagesMatchRequirementBuilder exclude(String[] excludeStages) {

    this.builder.exclude(excludeStages);
    return this;
  }

  @Nonnull
  @Override
  public String getRequirementId() {

    return GameStagesMatchRequirement.REQUIREMENT_ID;
  }

  @Nonnull
  @Override
  public ResourceLocation getResourceLocation() {

    return GameStagesMatchRequirement.LOCATION;
  }

  @Nullable
  @Override
  public IMatchRequirement create() {

    return this.builder.create();
  }

}

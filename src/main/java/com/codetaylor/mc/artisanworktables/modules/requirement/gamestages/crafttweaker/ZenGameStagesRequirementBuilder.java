package com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesRequirement;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesRequirementBuilder;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ZenGameStagesRequirementBuilder
    implements IMatchRequirementBuilder {

  private GameStagesRequirementBuilder builder;

  /* package */ ZenGameStagesRequirementBuilder() {

    this.builder = new GameStagesRequirementBuilder();
  }

  @ZenMethod
  public ZenGameStagesRequirementBuilder all(String[] requireAllStages) {

    this.builder.all(requireAllStages);
    return this;
  }

  @ZenMethod
  public ZenGameStagesRequirementBuilder any(String[] requireAnyStage) {

    this.builder.any(requireAnyStage);
    return this;
  }

  @ZenMethod
  public ZenGameStagesRequirementBuilder exclude(String[] excludeStages) {

    this.builder.exclude(excludeStages);
    return this;
  }

  @Nonnull
  @Override
  public String getRequirementId() {

    return GameStagesRequirement.REQUIREMENT_ID;
  }

  @Nonnull
  @Override
  public ResourceLocation getResourceLocation() {

    return GameStagesRequirement.LOCATION;
  }

  @Nullable
  @Override
  public IMatchRequirement create() {

    return this.builder.create();
  }

}

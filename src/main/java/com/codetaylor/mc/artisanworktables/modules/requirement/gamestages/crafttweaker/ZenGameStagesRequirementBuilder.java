package com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesRequirement;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesRequirementBuilder;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ZenGameStagesRequirementBuilder
    implements IRequirementBuilder {

  private GameStagesRequirementBuilder builder;

  /* package */ ZenGameStagesRequirementBuilder() {

    this.builder = new GameStagesRequirementBuilder();
  }

  @ZenMethod
  public ZenGameStagesRequirementBuilder allOf(String[] requireAllStages) {

    this.builder.allOf(requireAllStages);
    return this;
  }

  @ZenMethod
  public ZenGameStagesRequirementBuilder anyOf(String[] requireAnyStage) {

    this.builder.anyOf(requireAnyStage);
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
  public IRequirement create() {

    return this.builder.create();
  }

}

package com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement.ReskillableMatchRequirement;
import com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement.ReskillableMatchRequirementBuilder;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ZenReskillableMatchRequirementBuilder
    implements IMatchRequirementBuilder {

  private ReskillableMatchRequirementBuilder builder;

  /* package */ ZenReskillableMatchRequirementBuilder() {

    this.builder = new ReskillableMatchRequirementBuilder();
  }

  @ZenMethod
  public ZenReskillableMatchRequirementBuilder add(String requirementString) {

    this.builder.add(requirementString);
    return this;
  }

  @ZenMethod
  public ZenReskillableMatchRequirementBuilder addAll(String[] requirementStrings) {

    this.builder.addAll(requirementStrings);
    return this;
  }

  @Nonnull
  @Override
  public String getRequirementId() {

    return ReskillableMatchRequirement.REQUIREMENT_ID;
  }

  @Nonnull
  @Override
  public ResourceLocation getResourceLocation() {

    return ReskillableMatchRequirement.LOCATION;
  }

  @Nullable
  @Override
  public IMatchRequirement create() {

    return this.builder.create();
  }
}

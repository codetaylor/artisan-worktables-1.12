package com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement.ReskillableRequirement;
import com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement.ReskillableRequirementBuilder;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ZenReskillableRequirementBuilder
    implements IRequirementBuilder {

  private ReskillableRequirementBuilder builder;

  /* package */ ZenReskillableRequirementBuilder() {

    this.builder = new ReskillableRequirementBuilder();
  }

  @ZenMethod
  public ZenReskillableRequirementBuilder add(String requirementString) {

    this.builder.add(requirementString);
    return this;
  }

  @ZenMethod
  public ZenReskillableRequirementBuilder addAll(String[] requirementStrings) {

    this.builder.addAll(requirementStrings);
    return this;
  }

  @Nonnull
  @Override
  public String getRequirementId() {

    return ReskillableRequirement.REQUIREMENT_ID;
  }

  @Nonnull
  @Override
  public ResourceLocation getResourceLocation() {

    return ReskillableRequirement.LOCATION;
  }

  @Nullable
  @Override
  public IRequirement create() {

    return this.builder.create();
  }
}

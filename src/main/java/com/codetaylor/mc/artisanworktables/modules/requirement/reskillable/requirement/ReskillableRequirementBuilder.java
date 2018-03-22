package com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement;

import codersafterdark.reskillable.api.data.RequirementHolder;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementBuilder;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReskillableRequirementBuilder
    implements IMatchRequirementBuilder<ReskillableRequirementContext, ReskillableRequirement> {

  private List<String> requirementStringList;

  public ReskillableRequirementBuilder() {

    this.requirementStringList = new ArrayList<>();
  }

  public ReskillableRequirementBuilder add(@Nonnull String requirementString) {

    this.requirementStringList.add(requirementString);
    return this;
  }

  public ReskillableRequirementBuilder addAll(@Nonnull String... requirementStrings) {

    this.requirementStringList.addAll(Arrays.asList(requirementStrings));
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
  public ReskillableRequirement create() {

    String[] requirementStringArray = this.requirementStringList.toArray(new String[this.requirementStringList.size()]);
    RequirementHolder requirementHolder = RequirementHolder.fromStringList(requirementStringArray);
    return new ReskillableRequirement(requirementHolder);
  }
}

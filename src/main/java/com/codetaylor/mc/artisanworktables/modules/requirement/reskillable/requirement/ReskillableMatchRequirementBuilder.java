package com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement;

import codersafterdark.reskillable.api.data.RequirementHolder;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementBuilder;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReskillableMatchRequirementBuilder
    implements IMatchRequirementBuilder<ReskillableMatchRequirementContext, ReskillableMatchRequirement> {

  private List<String> requirementStringList;

  public ReskillableMatchRequirementBuilder() {

    this.requirementStringList = new ArrayList<>();
  }

  public ReskillableMatchRequirementBuilder add(@Nonnull String requirementString) {

    this.requirementStringList.add(requirementString);
    return this;
  }

  public ReskillableMatchRequirementBuilder addAll(@Nonnull String... requirementStrings) {

    this.requirementStringList.addAll(Arrays.asList(requirementStrings));
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
  public ReskillableMatchRequirement create() {

    String[] requirementStringArray = this.requirementStringList.toArray(new String[this.requirementStringList.size()]);
    RequirementHolder requirementHolder = RequirementHolder.fromStringList(requirementStringArray);
    return new ReskillableMatchRequirement(requirementHolder);
  }
}

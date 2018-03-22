package com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement;

import codersafterdark.reskillable.api.data.RequirementHolder;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirement;
import com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.ModuleRequirementReskillable;
import net.minecraft.util.ResourceLocation;

public class ReskillableRequirement
    implements IMatchRequirement<ReskillableRequirementContext> {

  public static final String REQUIREMENT_ID = "reskillable";
  public static final ResourceLocation LOCATION = new ResourceLocation(
      ModuleRequirementReskillable.MOD_ID,
      REQUIREMENT_ID
  );

  private final RequirementHolder requirementHolder;

  public ReskillableRequirement(RequirementHolder requirementHolder) {

    this.requirementHolder = requirementHolder;
  }

  @Override
  public ResourceLocation getResourceLocation() {

    return LOCATION;
  }

  @Override
  public boolean match(ReskillableRequirementContext context) {

    return context.getPlayerData().matchStats(this.requirementHolder);
  }
}

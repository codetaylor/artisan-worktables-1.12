package com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.SkillRequirement;
import codersafterdark.reskillable.api.requirement.TraitRequirement;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.ModuleRequirementReskillable;
import net.minecraft.util.ResourceLocation;

public class ReskillableRequirement
    implements IRequirement<ReskillableRequirementContext> {

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

    PlayerData playerData = context.getPlayerData();

    for (Requirement requirement : this.requirementHolder.getRequirements()) {

      if (requirement instanceof SkillRequirement) {
        Skill requirementSkill = ((SkillRequirement) requirement).getSkill();
        int requirementLevel = ((SkillRequirement) requirement).getLevel();

        PlayerSkillInfo playerSkillInfo = playerData.getSkillInfo(requirementSkill);
        int playerSkillInfoLevel = playerSkillInfo.getLevel();

        if (playerSkillInfoLevel < requirementLevel) {
          return false;
        }

      } else if (requirement instanceof TraitRequirement) {
        Unlockable requirementUnlockable = ((TraitRequirement) requirement).getUnlockable();
        Skill requirementParentSkill = requirementUnlockable.getParentSkill();

        PlayerSkillInfo playerSkillInfo = playerData.getSkillInfo(requirementParentSkill);

        if (!playerSkillInfo.isUnlocked(requirementUnlockable)) {
          return false;
        }
      }
    }

    return true;
    //return context.getPlayerData().matchStats(this.requirementHolder);
  }
}

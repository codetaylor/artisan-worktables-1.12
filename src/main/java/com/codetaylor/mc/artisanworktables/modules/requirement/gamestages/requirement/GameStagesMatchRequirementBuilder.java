package com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement;

import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementBuilder;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameStagesMatchRequirementBuilder
    implements IMatchRequirementBuilder<GameStagesMatchRequirementContext, GameStagesMatchRequirement> {

  private Set<String> excludeStages;
  private Set<String> requireOneStage;
  private Set<String> requireAllStages;

  public GameStagesMatchRequirementBuilder() {

    this.excludeStages = Collections.emptySet();
    this.requireOneStage = Collections.emptySet();
    this.requireAllStages = Collections.emptySet();
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

  public GameStagesMatchRequirementBuilder all(@Nonnull String... requireAllStages) {

    this.requireAllStages = new HashSet<>();
    this.requireAllStages.addAll(Arrays.asList(requireAllStages));
    return this;
  }

  public GameStagesMatchRequirementBuilder any(@Nonnull String... requireOneStage) {

    this.requireOneStage = new HashSet<>();
    this.requireOneStage.addAll(Arrays.asList(requireOneStage));
    return this;
  }

  public GameStagesMatchRequirementBuilder exclude(@Nonnull String... excludeStages) {

    this.excludeStages = new HashSet<>();
    this.excludeStages.addAll(Arrays.asList(excludeStages));
    return this;
  }

  @Override
  public GameStagesMatchRequirement create() {

    return new GameStagesMatchRequirement(this.requireAllStages, this.requireOneStage, this.excludeStages);
  }
}

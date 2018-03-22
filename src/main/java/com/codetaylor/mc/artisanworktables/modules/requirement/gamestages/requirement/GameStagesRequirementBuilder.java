package com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement;

import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementBuilder;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameStagesRequirementBuilder
    implements IMatchRequirementBuilder<GameStagesRequirementContext, GameStagesRequirement> {

  private Set<String> excludeStages;
  private Set<String> requireOneStage;
  private Set<String> requireAllStages;

  public GameStagesRequirementBuilder() {

    this.excludeStages = Collections.emptySet();
    this.requireOneStage = Collections.emptySet();
    this.requireAllStages = Collections.emptySet();
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

  public GameStagesRequirementBuilder all(@Nonnull String... requireAllStages) {

    this.requireAllStages = new HashSet<>();
    this.requireAllStages.addAll(Arrays.asList(requireAllStages));
    return this;
  }

  public GameStagesRequirementBuilder any(@Nonnull String... requireOneStage) {

    this.requireOneStage = new HashSet<>();
    this.requireOneStage.addAll(Arrays.asList(requireOneStage));
    return this;
  }

  public GameStagesRequirementBuilder exclude(@Nonnull String... excludeStages) {

    this.excludeStages = new HashSet<>();
    this.excludeStages.addAll(Arrays.asList(excludeStages));
    return this;
  }

  @Override
  public GameStagesRequirement create() {

    return new GameStagesRequirement(this.requireAllStages, this.requireOneStage, this.excludeStages);
  }
}

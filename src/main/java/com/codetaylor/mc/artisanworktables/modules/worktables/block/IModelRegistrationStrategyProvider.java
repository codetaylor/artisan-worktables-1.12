package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.athenaeum.registry.strategy.IClientModelRegistrationStrategy;

public interface IModelRegistrationStrategyProvider {

  IClientModelRegistrationStrategy getModelRegistrationStrategy();

}

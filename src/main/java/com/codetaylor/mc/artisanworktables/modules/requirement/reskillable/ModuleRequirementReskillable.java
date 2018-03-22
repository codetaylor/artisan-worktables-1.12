package com.codetaylor.mc.artisanworktables.modules.requirement.reskillable;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementBuilderSupplier;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementContextSupplier;
import com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement.ReskillableRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement.ReskillableRequirementContext;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModuleRequirementReskillable
    extends ModuleBase {

  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;

  public ModuleRequirementReskillable() {

    super(0, MOD_ID);

    MinecraftForge.EVENT_BUS.register(this);

    this.registerIntegrationPlugin(
        "crafttweaker",
        "com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.crafttweaker.ZenReskillableRequirement"
    );
  }

  @SubscribeEvent
  public void onRegisterRequirementContextSupplierEvent(RegistryEvent.Register<RequirementContextSupplier> event) {

    event.getRegistry().register(
        new RequirementContextSupplier(MOD_ID, "reskillable", ReskillableRequirementContext::new)
    );
  }

  @SubscribeEvent
  public void onRegisterRequirementBuilderSupplierEvent(RegistryEvent.Register<RequirementBuilderSupplier> event) {

    event.getRegistry().register(
        new RequirementBuilderSupplier(MOD_ID, "reskillable", ReskillableRequirementBuilder::new)
    );
  }
}

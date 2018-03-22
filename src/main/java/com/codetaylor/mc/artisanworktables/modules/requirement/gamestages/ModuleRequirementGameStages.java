package com.codetaylor.mc.artisanworktables.modules.requirement.gamestages;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementBuilderSupplier;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementContextSupplier;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesMatchRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesMatchRequirementContext;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModuleRequirementGameStages
    extends ModuleBase {

  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;

  public ModuleRequirementGameStages() {

    super(0, MOD_ID);

    MinecraftForge.EVENT_BUS.register(this);

    this.registerIntegrationPlugin(
        "crafttweaker",
        "com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.crafttweaker.ZenGameStagesRequirement"
    );
  }

  @SubscribeEvent
  public void onRegisterRequirementContextSupplierEvent(RegistryEvent.Register<RequirementContextSupplier> event) {

    event.getRegistry().register(
        new RequirementContextSupplier(MOD_ID, "gamestages", GameStagesMatchRequirementContext::new)
    );
  }

  @SubscribeEvent
  public void onRegisterRequirementBuilderSupplierEvent(RegistryEvent.Register<RequirementBuilderSupplier> event) {

    event.getRegistry().register(
        new RequirementBuilderSupplier(MOD_ID, "gamestages", GameStagesMatchRequirementBuilder::new)
    );
  }

}

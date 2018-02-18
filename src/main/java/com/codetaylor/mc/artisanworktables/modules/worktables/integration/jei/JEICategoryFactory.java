package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumTier;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import net.minecraft.util.ResourceLocation;

/* package */ class JEICategoryFactory {

  private IGuiHelper guiHelper;

  /* package */ JEICategoryFactory(IGuiHelper guiHelper) {

    this.guiHelper = guiHelper;
  }

  /* package */ JEICategoryBase createCategory(
      String name,
      EnumTier tier
  ) {

    switch (tier) {

      case WORKTABLE:
        return new JEICategoryWorktable(
            name,
            tier,
            PluginJEI.createUID(name, tier),
            this.createTitleTranslateKey(name, tier),
            this.createBackground(name, tier, this.guiHelper),
            this.guiHelper
        );

      case WORKSTATION:
        return new JEICategoryWorkstation(
            name,
            tier,
            PluginJEI.createUID(name, tier),
            this.createTitleTranslateKey(name, tier),
            this.createBackground(name, tier, this.guiHelper),
            this.guiHelper
        );

      case WORKSHOP:
        return new JEICategoryWorkshop(
            name,
            tier,
            PluginJEI.createUID(name, tier),
            this.createTitleTranslateKey(name, tier),
            this.createBackground(name, tier, this.guiHelper),
            this.guiHelper
        );

      default:
        throw new IllegalArgumentException("Unknown tier: " + tier);
    }
  }

  private IDrawable createBackground(String name, EnumTier tier, IGuiHelper guiHelper) {

    if (tier == EnumTier.WORKTABLE) {
      ResourceLocation resourceLocation = new ResourceLocation(
          ModuleWorktables.MOD_ID,
          String.format(ModuleWorktables.Textures.WORKTABLE_GUI, name)
      );
      return guiHelper.createDrawable(resourceLocation, 3, 3, 170, 80);

    } else if (tier == EnumTier.WORKSTATION) {
      ResourceLocation resourceLocation = new ResourceLocation(
          ModuleWorktables.MOD_ID,
          String.format(ModuleWorktables.Textures.WORKSTATION_GUI, name)
      );
      return guiHelper.createDrawable(resourceLocation, 3, 3, 170, 102);

    } else if (tier == EnumTier.WORKSHOP) {
      ResourceLocation resourceLocation = new ResourceLocation(
          ModuleWorktables.MOD_ID,
          String.format(ModuleWorktables.Textures.WORKSHOP_GUI, name)
      );
      return guiHelper.createDrawable(resourceLocation, 3, 13, 170, 128);

    } else {
      throw new IllegalArgumentException("Unknown tier: " + tier);
    }
  }

  private String createTitleTranslateKey(String name, EnumTier tier) {

    switch (tier) {

      case WORKTABLE:
        return String.format(ModuleWorktables.Lang.WORKTABLE_TITLE, name);

      case WORKSTATION:
        return String.format(ModuleWorktables.Lang.WORKSTATION_TITLE, name);

      case WORKSHOP:
        return String.format(ModuleWorktables.Lang.WORKSHOP_TITLE, name);

      default:
        throw new IllegalArgumentException("Unknown tier: " + tier);
    }
  }
}

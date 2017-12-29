package com.codetaylor.mc.artisanworktables.modules.worktables;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.item.ItemWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.*;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.registry.strategy.VariantBlockItemModelRegistrationStrategy;
import net.minecraft.creativetab.CreativeTabs;

public class ModuleWorktables
    extends ModuleBase {

  public static final String MOD_NAME = ModArtisanWorktables.NAME;
  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModArtisanWorktables.CREATIVE_TAB;
  public static final ModArtisanWorktables MOD_INSTANCE = ModArtisanWorktables.INSTANCE;

  public static class Lang {

    public static final String WORKTABLE_TITLE = "tile.%s.worktable.%s.name";
  }

  public static class Textures {

    public static final String WORKTABLE_GUI = "textures/gui/worktable_%s.png";
  }

  public static class Blocks {

    public static final BlockWorktable WORKTABLE = new BlockWorktable();
  }

  public ModuleWorktables() {

    super(0);
    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    this.registerIntegrationPlugin(
        "crafttweaker",
        "com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.ZenWorktable"
    );

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei.PluginJEI"
    );
  }

  @Override
  public void onRegister(Registry registry) {

    registry.registerBlock(
        Blocks.WORKTABLE,
        new ItemWorktable(Blocks.WORKTABLE),
        BlockWorktable.NAME
    );
    registry.registerTileEntities(
        TileEntityWorktableBlacksmith.class,
        TileEntityWorktableCarpenter.class,
        TileEntityWorktableJeweler.class,
        TileEntityWorktableMason.class,
        TileEntityWorktableTailor.class,
        TileEntityWorktableBasic.class
    );
  }

  @Override
  public void onClientRegister(Registry registry) {

    registry.registerItemModelStrategy(new VariantBlockItemModelRegistrationStrategy<>(
        Blocks.WORKTABLE.getDefaultState(),
        BlockWorktable.VARIANT
    ));
  }
}

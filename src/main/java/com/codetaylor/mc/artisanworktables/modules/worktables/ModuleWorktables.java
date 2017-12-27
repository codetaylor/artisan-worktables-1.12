package com.codetaylor.mc.artisanworktables.modules.worktables;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.item.ItemWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.*;
import com.codetaylor.mc.athenaeum.helper.ModelRegistrationHelper;
import com.codetaylor.mc.athenaeum.helper.TileEntityRegistrationHelper;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables.Blocks.WORKBENCH_BASIC;

public class ModuleWorktables
    extends ModuleBase {

  public static final String MOD_NAME = ModArtisanWorktables.NAME;
  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModArtisanWorktables.CREATIVE_TAB;
  public static final ModArtisanWorktables MOD_INSTANCE = ModArtisanWorktables.INSTANCE;

  public ModuleWorktables() {

    super(0);

    this.registerIntegrationPlugin(
        "crafttweaker",
        "com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.ZenWorkbench"
    );

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei.PluginJEI"
    );
  }

  public static class Lang {

    public static final String WORKTABLE_TITLE = "tile.%s.worktable.%s.name";
  }

  public static class Textures {

    public static final String WORKTABLE_GUI = "textures/gui/worktable_%s.png";
  }

  public static class Blocks {

    @GameRegistry.ObjectHolder(ModuleWorktables.MOD_ID + ":" + BlockWorktable.NAME)
    public static final BlockWorktable WORKBENCH_BASIC;

    static {
      WORKBENCH_BASIC = null;
    }
  }

  @Override
  public void onRegisterBlockEvent(RegistryEvent.Register<Block> event) {

    // Blocks
    event.getRegistry().registerAll(
        new BlockWorktable()
    );
  }

  @Override
  public void onRegisterItemEvent(RegistryEvent.Register<Item> event) {

    // Item Blocks
    event.getRegistry().registerAll(
        new ItemWorktable(WORKBENCH_BASIC)
    );
  }

  @Override
  public void onRegisterTileEntitiesEvent() {

    TileEntityRegistrationHelper.registerTileEntities(
        MOD_ID,
        TileEntityWorktableBlacksmith.class,
        TileEntityWorktableCarpenter.class,
        TileEntityWorktableJeweler.class,
        TileEntityWorktableMason.class,
        TileEntityWorktableTailor.class,
        TileEntityWorktableBasic.class
    );
  }

  @Override
  public void onClientRegisterModelsEvent(ModelRegistryEvent event) {

    // Workbench Basic
    ModelRegistrationHelper.registerVariantBlockItemModels(
        WORKBENCH_BASIC.getDefaultState(),
        BlockWorktable.VARIANT
    );
  }
}

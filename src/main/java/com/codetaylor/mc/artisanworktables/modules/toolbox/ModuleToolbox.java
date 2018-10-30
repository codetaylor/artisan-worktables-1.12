package com.codetaylor.mc.artisanworktables.modules.toolbox;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.modules.toolbox.block.BlockMechanicalToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.block.BlockToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityMechanicalToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import net.minecraft.creativetab.CreativeTabs;

public class ModuleToolbox
    extends ModuleBase {

  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModArtisanWorktables.CREATIVE_TAB;

  public static class Lang {

    public static final String TOOLBOX_TITLE = "tile." + MOD_ID + ".toolbox.name";
    public static final String MECHANICAL_TOOLBOX_TITLE = "tile." + MOD_ID + ".mechanical_toolbox.name";
  }

  public static class Blocks {

    public static final BlockToolbox TOOLBOX = new BlockToolbox();
    public static final BlockMechanicalToolbox MECHANICAL_TOOLBOX = new BlockMechanicalToolbox();
  }

  public ModuleToolbox() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();
  }

  @Override
  public void onRegister(Registry registry) {

    super.onRegister(registry);

    if (ModuleToolboxConfig.TOOLBOX.ENABLED) {
      registry.registerBlockWithItem(
          Blocks.TOOLBOX,
          BlockToolbox.NAME
      );
      registry.registerTileEntities(
          TileEntityToolbox.class
      );
    }

    if (ModuleToolboxConfig.MECHANICAL_TOOLBOX.ENABLED) {
      registry.registerBlockWithItem(
          Blocks.MECHANICAL_TOOLBOX,
          BlockMechanicalToolbox.NAME
      );
      registry.registerTileEntities(
          TileEntityMechanicalToolbox.class
      );
    }
  }

  @Override
  public void onClientRegister(Registry registry) {

    super.onClientRegister(registry);

    if (ModuleToolboxConfig.TOOLBOX.ENABLED) {
      registry.registerClientModelRegistrationStrategy(() -> {
        ModelRegistrationHelper.registerBlockItemModel(Blocks.TOOLBOX.getDefaultState());
      });
    }

    if (ModuleToolboxConfig.MECHANICAL_TOOLBOX.ENABLED) {
      registry.registerClientModelRegistrationStrategy(() -> {
        ModelRegistrationHelper.registerBlockItemModel(Blocks.MECHANICAL_TOOLBOX.getDefaultState());
      });
    }
  }

}

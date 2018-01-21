package com.codetaylor.mc.artisanworktables.modules.toolbox;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.modules.toolbox.block.BlockToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TESRToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.registry.Registry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleToolbox
    extends ModuleBase {

  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModArtisanWorktables.CREATIVE_TAB;

  public static class Lang {

    public static final String TOOLBOX_TITLE = "tile.toolbox.name";
  }

  public static class Blocks {

    public static final BlockToolbox TOOLBOX = new BlockToolbox();
  }

  public ModuleToolbox() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();
  }

  @Override
  public void onRegister(Registry registry) {

    super.onRegister(registry);

    registry.registerBlock(
        Blocks.TOOLBOX,
        BlockToolbox.NAME
    );
    registry.registerTileEntities(
        TileEntityToolbox.class
    );
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void onClientRegisterModelsEvent(ModelRegistryEvent event) {

    super.onClientRegisterModelsEvent(event);

    ClientRegistry.bindTileEntitySpecialRenderer(
        TileEntityToolbox.class,
        new TESRToolbox()
    );
  }
}

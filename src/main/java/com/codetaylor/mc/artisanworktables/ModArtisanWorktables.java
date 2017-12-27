package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.athenaeum.gui.GuiHandler;
import com.codetaylor.mc.athenaeum.module.ModBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(
    modid = ModArtisanWorktables.MOD_ID,
    version = ModArtisanWorktables.VERSION,
    name = ModArtisanWorktables.NAME,
    dependencies = ModArtisanWorktables.DEPENDENCIES
)
public class ModArtisanWorktables
    extends ModBase {

  public static final String MOD_ID = Reference.MOD_ID;
  public static final String VERSION = Reference.VERSION;
  public static final String NAME = Reference.NAME;
  public static final String DEPENDENCIES = Reference.DEPENDENCIES;

  public static final boolean IS_DEV = Reference.IS_DEV;

  @SuppressWarnings("unused")
  @Mod.Instance
  public static ModArtisanWorktables INSTANCE;

  public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID) {

    @Override
    public ItemStack getTabIconItem() {

      return new ItemStack(Item.getItemFromBlock(ModuleWorktables.Blocks.WORKBENCH_BASIC), 1, 0);
    }
  };

  public ModArtisanWorktables() {

    super(MOD_ID);

    this.registerModules(
        ModuleWorktables.class
    );
  }

  @Mod.EventHandler
  public void onConstructionEvent(FMLConstructionEvent event) {

    super._onConstructionEvent(event);
  }

  @Mod.EventHandler
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

    super._onPreInitializationEvent(event);
  }

  @Mod.EventHandler
  public void onInitializationEvent(FMLInitializationEvent event) {

    super._onInitializationEvent(event);
  }

  @Mod.EventHandler
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    super._onPostInitializationEvent(event);
  }

  @Mod.EventHandler
  public void onLoadCompleteEvent(FMLLoadCompleteEvent event) {

    super._onLoadCompleteEvent(event);
  }

  @Mod.EventHandler
  public void onServerAboutToStartEvent(FMLServerAboutToStartEvent event) {

    super._onServerAboutToStartEvent(event);
  }

  @Mod.EventHandler
  public void onServerStartingEvent(FMLServerStartingEvent event) {

    super._onServerStartingEvent(event);
  }

  @Mod.EventHandler
  public void onServerStartedEvent(FMLServerStartedEvent event) {

    super._onServerStartedEvent(event);
  }

  @Mod.EventHandler
  public void onServerStoppingEvent(FMLServerStoppingEvent event) {

    super._onServerStoppingEvent(event);
  }

  @Mod.EventHandler
  public void onServerStoppedEvent(FMLServerStoppedEvent event) {

    super._onServerStoppedEvent(event);
  }

}

package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.artisanworktables.modules.tools.ModuleTools;
import com.codetaylor.mc.artisanworktables.modules.tools.ModuleToolsConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.athenaeum.gui.GuiHandler;
import com.codetaylor.mc.athenaeum.module.ModuleManager;
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
public class ModArtisanWorktables {

  public static final String MOD_ID = Reference.MOD_ID;
  public static final String VERSION = Reference.VERSION;
  public static final String NAME = Reference.NAME;
  public static final String DEPENDENCIES = Reference.DEPENDENCIES;

  public static final boolean IS_DEV = Reference.IS_DEV;

  @Mod.Instance
  @SuppressWarnings("unused")
  public static ModArtisanWorktables INSTANCE;

  public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID) {

    @Override
    public ItemStack getTabIconItem() {

      return new ItemStack(Item.getItemFromBlock(ModuleWorktables.Blocks.WORKTABLE), 1, 0);
    }
  };

  private final ModuleManager moduleManager;

  public ModArtisanWorktables() {

    this.moduleManager = new ModuleManager(MOD_ID);
    this.moduleManager.registerModules(
        ModuleWorktables.class
    );

    if (ModuleToolsConfig.ENABLE_MODULE) {
      this.moduleManager.registerModules(ModuleTools.class);
    }
  }

  @Mod.EventHandler
  public void onConstructionEvent(FMLConstructionEvent event) {

    this.moduleManager.onConstructionEvent();
    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onInitializationEvent(FMLInitializationEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onLoadCompleteEvent(FMLLoadCompleteEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerAboutToStartEvent(FMLServerAboutToStartEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStartingEvent(FMLServerStartingEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStartedEvent(FMLServerStartedEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStoppingEvent(FMLServerStoppingEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

  @Mod.EventHandler
  public void onServerStoppedEvent(FMLServerStoppedEvent event) {

    this.moduleManager.routeFMLStateEvent(event);
  }

}

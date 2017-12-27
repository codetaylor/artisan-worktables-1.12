package com.sudoplay.mc.pwcustom;

import com.codetaylor.mc.athenaeum.gui.GuiHandler;
import com.codetaylor.mc.athenaeum.module.ModBase;
import com.sudoplay.mc.pwcustom.modules.blocks.ModuleBlocks;
import com.sudoplay.mc.pwcustom.modules.casts.ModuleCasts;
import com.sudoplay.mc.pwcustom.modules.craftingparts.ModuleCraftingParts;
import com.sudoplay.mc.pwcustom.modules.enchanting.ModuleEnchanting;
import com.sudoplay.mc.pwcustom.modules.knives.ModuleKnives;
import com.sudoplay.mc.pwcustom.modules.portals.ModulePortals;
import com.sudoplay.mc.pwcustom.modules.sawing.ModuleSawing;
import com.sudoplay.mc.pwcustom.modules.toolparts.ModuleToolParts;
import com.sudoplay.mc.pwcustom.modules.workbench.ModuleWorkbench;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(
    modid = ModPWCustom.MOD_ID,
    version = ModPWCustom.VERSION,
    name = ModPWCustom.NAME,
    dependencies = ModPWCustom.DEPENDENCIES
)
public class ModPWCustom
    extends ModBase {

  public static final String MOD_ID = Reference.MOD_ID;
  public static final String VERSION = Reference.VERSION;
  public static final String NAME = Reference.NAME;
  public static final String DEPENDENCIES = Reference.DEPENDENCIES;

  public static final boolean IS_DEV = Reference.IS_DEV;

  @SuppressWarnings("unused")
  @Mod.Instance
  public static ModPWCustom INSTANCE;

  public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID) {

    @Override
    public ItemStack getTabIconItem() {

      return new ItemStack(ModulePortals.Items.PORTAL_WAND, 1, 0);
    }
  };

  public ModPWCustom() {

    super(MOD_ID);

    this.registerModules(
        ModuleWorkbench.class,
        ModulePortals.class,
        ModuleSawing.class,
        ModuleToolParts.class,
        ModuleCasts.class,
        ModuleEnchanting.class,
        ModuleBlocks.class,
        ModuleCraftingParts.class,
        ModuleKnives.class
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

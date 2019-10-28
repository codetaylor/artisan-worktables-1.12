package com.codetaylor.mc.artisanworktables.modules.automator;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.modules.automator.block.BlockAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.block.BlockAutomatorPowerRF;
import com.codetaylor.mc.artisanworktables.modules.automator.network.CSPacketAutomatorTabStateChange;
import com.codetaylor.mc.artisanworktables.modules.automator.network.SCPacketAutomatorTabStateChange;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomatorPowerRF;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.ModelRegistrationHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModuleAutomator
    extends ModuleBase {

  public static final String MOD_NAME = ModArtisanWorktables.NAME;
  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModArtisanWorktables.CREATIVE_TAB;
  public static final Logger LOG = LogManager.getLogger(MOD_ID);

  public static class Lang {

//    public static final String GUI_TOOLTIP_FLUID_DESTROY = "gui." + MOD_ID + ".tooltip.fluid.destroy";
//    public static final String GUI_TOOLTIP_FLUID_EMPTY = "gui." + MOD_ID + ".tooltip.fluid.empty";
  }

  public static class Textures {

//    public static final String WORKTABLE_GUI = "textures/gui/worktable_%s.png";
//    public static final String WORKSTATION_GUI = "textures/gui/workstation_%s.png";
//    public static final String WORKSHOP_GUI = "textures/gui/workshop_%s.png";
  }

  @GameRegistry.ObjectHolder(ModuleAutomator.MOD_ID)
  public static class Blocks {

    @GameRegistry.ObjectHolder(BlockAutomator.NAME)
    public static final BlockAutomator AUTOMATOR;

    @GameRegistry.ObjectHolder(BlockAutomatorPowerRF.NAME)
    public static final BlockAutomatorPowerRF AUTOMATOR_POWER_RF;

    static {
      AUTOMATOR = null;
      AUTOMATOR_POWER_RF = null;
    }
//    public static final BlockWorktable WORKTABLE = new BlockWorktable();
//    public static final BlockWorkstation WORKSTATION = new BlockWorkstation();
//    public static final BlockWorkshop WORKSHOP = new BlockWorkshop();
  }

  public static class Items {

//    public static final ItemDesignPattern DESIGN_PATTERN = new ItemDesignPattern();
  }

  public static IPacketService PACKET_SERVICE;

  public ModuleAutomator() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

//    MinecraftForge.EVENT_BUS.register(this);

    PACKET_SERVICE = this.enableNetwork();
  }

  @Override
  public void onNetworkRegister(IPacketRegistry registry) {

    registry.register(
        CSPacketAutomatorTabStateChange.class,
        CSPacketAutomatorTabStateChange.class,
        Side.SERVER
    );

    registry.register(
        SCPacketAutomatorTabStateChange.class,
        SCPacketAutomatorTabStateChange.class,
        Side.CLIENT
    );
  }

  @Override
  public void onRegister(Registry registry) {

    registry.registerBlockWithItem(new BlockAutomator(), BlockAutomator.NAME);
    registry.registerBlockWithItem(new BlockAutomatorPowerRF(), BlockAutomatorPowerRF.NAME);

    //noinspection unchecked
    this.registerTileEntities(
        registry,
        TileAutomator.class,
        TileAutomatorPowerRF.class
    );
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    registry.registerClientModelRegistrationStrategy(() -> {
      ModelRegistrationHelper.registerBlockItemModels(
          Blocks.AUTOMATOR,
          Blocks.AUTOMATOR_POWER_RF
      );
    });
  }

  @SuppressWarnings("unchecked")
  public void registerTileEntities(Registry registry, Class<? extends TileEntity>... tileEntityClasses) {

    for (Class<? extends TileEntity> tileEntityClass : tileEntityClasses) {
      this.registerTileEntity(registry, tileEntityClass);
    }
  }

  public void registerTileEntity(Registry registry, Class<? extends TileEntity> tileEntityClass) {

    registry.registerTileEntityRegistrationStrategy(() -> GameRegistry.registerTileEntity(
        tileEntityClass,
        new ResourceLocation(registry.getModId(), "tile." + tileEntityClass.getSimpleName())
    ));
  }
}

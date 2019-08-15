package com.codetaylor.mc.artisanworktables.modules.worktables;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.recipe.RecipeBuilder;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementBuilderSupplier;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementContextSupplier;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorkshop;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorkstation;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTRecipeAdditionQueue;
import com.codetaylor.mc.artisanworktables.modules.worktables.item.ItemDesignPattern;
import com.codetaylor.mc.artisanworktables.modules.worktables.item.ItemDesignPatternBakedModel;
import com.codetaylor.mc.artisanworktables.modules.worktables.item.ItemDesignPatternMeshDefinition;
import com.codetaylor.mc.artisanworktables.modules.worktables.item.ItemWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.network.*;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipeAdditionQueue;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeAdditionQueue;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workshop.TileEntityWorkshop;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workshop.TileEntityWorkshopDesigner;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workshop.TileEntityWorkshopMage;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workstation.TileEntityWorkstation;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workstation.TileEntityWorkstationDesigner;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workstation.TileEntityWorkstationMage;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.worktable.TileEntityWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.worktable.TileEntityWorktableDesigner;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.worktable.TileEntityWorktableMage;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.network.IPacketRegistry;
import com.codetaylor.mc.athenaeum.network.IPacketService;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.Injector;
import crafttweaker.api.recipes.ICraftingRecipe;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModuleWorktables
    extends ModuleBase {

  public static final String MOD_NAME = ModArtisanWorktables.NAME;
  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModArtisanWorktables.CREATIVE_TAB;
  public static final Logger LOG = LogManager.getLogger(MOD_ID);

  public static class Lang {

    public static final String WORKTABLE_TITLE = "tile." + MOD_ID + ".worktable.%s.name";
    public static final String WORKSTATION_TITLE = "tile." + MOD_ID + ".workstation.%s.name";
    public static final String WORKSHOP_TITLE = "tile." + MOD_ID + ".workshop.%s.name";

    public static final String JEI_TOOLTIP_SHAPELESS_RECIPE = "jei." + MOD_ID + ".tooltip.shapeless.recipe";
    public static final String JEI_TOOLTIP_CHANCE = "jei." + MOD_ID + ".tooltip.chance";
    public static final String JEI_XP_COST = "jei." + MOD_ID + ".xp.cost";
    public static final String JEI_XP_REQUIRED = "jei." + MOD_ID + ".xp.required";
    public static final String JEI_LEVEL_COST = "jei." + MOD_ID + ".level.cost";
    public static final String JEI_LEVEL_REQUIRED = "jei." + MOD_ID + ".level.required";

    public static final String GUI_TOOLTIP_FLUID_DESTROY = "gui." + MOD_ID + ".tooltip.fluid.destroy";
    public static final String GUI_TOOLTIP_FLUID_EMPTY = "gui." + MOD_ID + ".tooltip.fluid.empty";

    public static final String ITEM_TOOLTIP_CREATIVE_TABLE_OREDICT_NONE = "item." + MOD_ID + ".tooltip.creative.table.oredict.none";
    public static final String ITEM_TOOLTIP_CREATIVE_TABLE_OREDICT_HEADER = "item." + MOD_ID + ".tooltip.creative.table.oredict.header";
  }

  public static class Textures {

    public static final String WORKTABLE_GUI = "textures/gui/worktable_%s.png";
    public static final String WORKSTATION_GUI = "textures/gui/workstation_%s.png";
    public static final String WORKSHOP_GUI = "textures/gui/workshop_%s.png";
  }

  public static class Blocks {

    public static final BlockWorktable WORKTABLE = new BlockWorktable();
    public static final BlockWorkstation WORKSTATION = new BlockWorkstation();
    public static final BlockWorkshop WORKSHOP = new BlockWorkshop();
  }

  public static class Items {

    public static final ItemDesignPattern DESIGN_PATTERN = new ItemDesignPattern();
  }

  public static IPacketService PACKET_SERVICE;
  public static IRecipeAdditionQueue RECIPE_ADDITION_QUEUE;

  public ModuleWorktables() {

    super(0, MOD_ID);

    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    MinecraftForge.EVENT_BUS.register(this);

    PACKET_SERVICE = this.enableNetwork();

    this.registerIntegrationPlugin(
        "crafttweaker",
        "com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.IZenRecipeBuilder"
    );

    this.registerIntegrationPlugin(
        "crafttweaker",
        "com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy.IZenRecipeBuilderCopyStrategy"
    );

    this.registerIntegrationPlugin(
        "jei",
        "com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei.PluginJEI"
    );
  }

  @Override
  public void onConstructionEvent(FMLConstructionEvent event) {

    this.injectAPI();

    if (Loader.isModLoaded("crafttweaker")) {
      RECIPE_ADDITION_QUEUE = new CTRecipeAdditionQueue();
      MinecraftForge.EVENT_BUS.register(RECIPE_ADDITION_QUEUE);

    } else {
      RECIPE_ADDITION_QUEUE = new RecipeAdditionQueue();
      MinecraftForge.EVENT_BUS.register(RECIPE_ADDITION_QUEUE);
    }

    super.onConstructionEvent(event);
  }

  @SubscribeEvent
  public void onNewRegistryEvent(RegistryEvent.NewRegistry event) {

    new RegistryBuilder<RequirementContextSupplier>()
        .setName(new ResourceLocation(MOD_ID, "requirement_context_supplier"))
        .setType(RequirementContextSupplier.class)
        .create();

    new RegistryBuilder<RequirementBuilderSupplier>()
        .setName(new ResourceLocation(MOD_ID, "requirement_builder_supplier"))
        .setType(RequirementBuilderSupplier.class)
        .create();

    new RegistryBuilder<RecipeRegistry>()
        .setName(new ResourceLocation(MOD_ID, "recipe_registry"))
        .setType(RecipeRegistry.class)
        .create();

  }

  @SubscribeEvent
  public void onRegisterRecipeRegistryEvent(RegistryEvent.Register<RecipeRegistry> event) {

    IForgeRegistry<RecipeRegistry> registry = event.getRegistry();

    for (String name : ArtisanAPI.getWorktableNames()) {
      registry.register(new RecipeRegistry(MOD_ID, name));
    }
  }

  @Override
  public void onNetworkRegister(IPacketRegistry registry) {

    // Server
    registry.register(
        CSPacketWorktableTab.class,
        CSPacketWorktableTab.class,
        Side.SERVER
    );
    registry.register(
        CSPacketWorktableTankDestroyFluid.class,
        CSPacketWorktableTankDestroyFluid.class,
        Side.SERVER
    );
    registry.register(
        CSPacketWorktableClear.class,
        CSPacketWorktableClear.class,
        Side.SERVER
    );
    registry.register(
        CSPacketWorktableCreativeToggle.class,
        CSPacketWorktableCreativeToggle.class,
        Side.SERVER
    );
    registry.register(
        CSPacketWorktableLockedModeToggle.class,
        CSPacketWorktableLockedModeToggle.class,
        Side.SERVER
    );

    // Client
    registry.register(
        SCPacketWorktableFluidUpdate.class,
        SCPacketWorktableFluidUpdate.class,
        Side.CLIENT
    );
    registry.register(
        SCPacketWorktableContainerJoinedBlockBreak.class,
        SCPacketWorktableContainerJoinedBlockBreak.class,
        Side.CLIENT
    );
  }

  @Override
  public void onRegister(Registry registry) {

    if (ModuleWorktablesConfig.ENABLE_WORKTABLES) {

      registry.registerBlock(
          Blocks.WORKTABLE,
          new ItemWorktable(Blocks.WORKTABLE),
          BlockWorktable.NAME
      );

      registry.registerTileEntities(
          TileEntityWorktable.class,
          TileEntityWorktableMage.class,
          TileEntityWorktableDesigner.class
      );
    }

    if (ModuleWorktablesConfig.ENABLE_WORKSTATIONS) {

      registry.registerBlock(
          Blocks.WORKSTATION,
          new ItemWorktable(Blocks.WORKSTATION),
          BlockWorkstation.NAME
      );

      registry.registerTileEntities(
          TileEntityWorkstation.class,
          TileEntityWorkstationMage.class,
          TileEntityWorkstationDesigner.class
      );
    }

    if (ModuleWorktablesConfig.ENABLE_WORKSHOPS) {

      registry.registerBlock(
          Blocks.WORKSHOP,
          new ItemWorktable(Blocks.WORKSHOP),
          BlockWorkshop.NAME
      );

      registry.registerTileEntities(
          TileEntityWorkshop.class,
          TileEntityWorkshopMage.class,
          TileEntityWorkshopDesigner.class
      );
    }

    registry.registerItem(Items.DESIGN_PATTERN, ItemDesignPattern.NAME);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientRegister(Registry registry) {

    if (ModuleWorktablesConfig.ENABLE_WORKTABLES) {
      registry.registerClientModelRegistrationStrategy(Blocks.WORKTABLE.getModelRegistrationStrategy());
    }

    if (ModuleWorktablesConfig.ENABLE_WORKSTATIONS) {
      registry.registerClientModelRegistrationStrategy(Blocks.WORKSTATION.getModelRegistrationStrategy());
    }

    if (ModuleWorktablesConfig.ENABLE_WORKSHOPS) {
      registry.registerClientModelRegistrationStrategy(Blocks.WORKSHOP.getModelRegistrationStrategy());
    }

    registry.registerClientModelRegistrationStrategy(() -> {
      ModelBakery.registerItemVariants(
          Items.DESIGN_PATTERN,
          new ResourceLocation(MOD_ID, ItemDesignPattern.NAME + "_" + ItemDesignPattern.EnumType.BLANK.getName())
      );
      ModelBakery.registerItemVariants(
          Items.DESIGN_PATTERN,
          new ResourceLocation(MOD_ID, ItemDesignPattern.NAME + "_" + ItemDesignPattern.EnumType.WRITTEN.getName())
      );
      ModelLoader.setCustomMeshDefinition(Items.DESIGN_PATTERN, new ItemDesignPatternMeshDefinition());
    });
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onModelBakeEvent(ModelBakeEvent event) {

    IRegistry<ModelResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();
    String matchName = ItemDesignPattern.NAME + "_" + ItemDesignPattern.EnumType.WRITTEN.getName();

    for (ModelResourceLocation modelResourceLocation : modelRegistry.getKeys()) {

      if (modelResourceLocation.getResourceDomain().equals(MOD_ID)) {

        if (modelResourceLocation.getResourcePath().equals(matchName)) {

          modelRegistry.putObject(
              modelResourceLocation,
              new ItemDesignPatternBakedModel(modelRegistry.getObject(modelResourceLocation))
          );
        }
      }
    }
  }

  private void injectAPI() {

    Injector injector = new Injector();

    injector.inject(
        RecipeBuilder.class,
        "RECIPE_BUILDER_PROVIDER",
        (IRecipeBuilderProvider) RecipeBuilderInternal::get
    );

    injector.inject(
        RecipeBuilder.Copy.class,
        "RECIPE_BUILDER_COPY_STRATEGY_PROVIDER",
        new IRecipeBuilderCopyStrategyProvider() {

          @Override
          public IRecipeBuilderCopyStrategy byName(String recipeName) {

            return RecipeBuilder.Copy.byName(recipeName);
          }

          @Override
          public IRecipeBuilderCopyStrategy byRecipe(ICraftingRecipe recipe) {

            return RecipeBuilder.Copy.byRecipe(recipe);
          }

          @Override
          public IRecipeBuilderCopyStrategy byOutput(IArtisanIngredient[] outputs) {

            return RecipeBuilder.Copy.byOutput(outputs);
          }
        }
    );
  }

}

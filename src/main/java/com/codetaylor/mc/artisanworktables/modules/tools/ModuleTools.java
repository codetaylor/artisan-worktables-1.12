package com.codetaylor.mc.artisanworktables.modules.tools;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.modules.tools.item.ItemWorktableTool;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumMaterial;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumWorktableToolType;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.ModuleRecipes;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.registry.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class ModuleTools
    extends ModuleBase {

  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModArtisanWorktables.CREATIVE_TAB;

  public static final class Lang {

    public static final String MATERIAL_STRING = "material.artisanworktables.%s";
  }

  public final List<EnumMaterial> materialList = new ArrayList<>();
  public final List<ItemWorktableTool> registeredToolList = new ArrayList<>();

  public ModuleTools() {

    super(0);
    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void onRegister(Registry registry) {

    super.onRegister(registry);

    //noinspection unchecked
    this.materialList.addAll(Arrays.asList(new EnumMaterial[]{
        EnumMaterial.WOOD,
        EnumMaterial.FLINT,
        EnumMaterial.STONE,
        EnumMaterial.IRON,
        EnumMaterial.GOLD,
        EnumMaterial.DIAMOND
    }));

    if (ModuleToolsConfig.ENABLE_THERMAL_FOUNDATION_MATERIALS) {
      //noinspection unchecked
      this.materialList.addAll(Arrays.asList(new EnumMaterial[]{
          EnumMaterial.ALUMINUM,
          EnumMaterial.BRONZE,
          EnumMaterial.CONSTANTAN,
          EnumMaterial.COPPER,
          EnumMaterial.ELECTRUM,
          EnumMaterial.INVAR,
          EnumMaterial.LEAD,
          EnumMaterial.NICKEL,
          EnumMaterial.PLATINUM,
          EnumMaterial.SILVER,
          EnumMaterial.STEEL,
          EnumMaterial.TIN
      }));
    }

    for (EnumWorktableToolType type : EnumWorktableToolType.values()) {
      String name = type.getName();

      for (EnumMaterial material : this.materialList) {
        String materialName = material.getName();

        ItemWorktableTool item = new ItemWorktableTool(type, material);
        this.registeredToolList.add(item);
        ResourceLocation registryName = new ResourceLocation(MOD_ID, name + "_" + materialName);
        registry.registerItem(item, registryName);
        item.setUnlocalizedName(registryName.getResourceDomain().replaceAll("_", ".")
            + "." + name.replaceAll("_", "."));
      }
    }
  }

  @Override
  public void onInitializationEvent(FMLInitializationEvent event) {

    super.onInitializationEvent(event);

  }

  @Override
  public void onClientRegisterModelsEvent(ModelRegistryEvent event) {

    super.onClientRegisterModelsEvent(event);

    /*
    Register different model resource locations depending on if the material is highlighted or not.
     */

    // TODO: this needs to be migrated to a custom (?) model registration strategy
    for (ItemWorktableTool item : this.registeredToolList) {
      String resourcePath = item.getMaterial().isHighlighted() ? item.getName() + "_highlighted" : item.getName();
      ResourceLocation location = new ResourceLocation(MOD_ID, resourcePath);
      ModelResourceLocation modelResourceLocation = new ModelResourceLocation(location, "inventory");
      ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);
    }
  }

  @SubscribeEvent
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    if (!ModuleToolsConfig.ENABLE_TOOL_RECIPES) {
      return;
    }

    /*
    Go through all the registered worktable tools and register the appropriate recipe for each.
     */

    IForgeRegistry<IRecipe> registry = event.getRegistry();

    for (ItemWorktableTool item : this.registeredToolList) {
      Object[] recipeDefinition = ModuleRecipes.getRecipeDefinition(
          item.getType(),
          item.getMaterial().getRecipeIngredient()
      );

      ShapedOreRecipe recipe = new ShapedOreRecipe(null, item, recipeDefinition);
      recipe.setRegistryName(new ResourceLocation(
          MOD_ID,
          "recipe." + item.getName() + "." + item.getMaterial().getName()
      ));

      registry.register(recipe);
    }
  }

  @Override
  public void onClientInitializationEvent(FMLInitializationEvent event) {

    super.onClientInitializationEvent(event);

    /*
    Register item color handlers to colorize layer 1 of each item model.
     */

    ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

    itemColors.registerItemColorHandler(
        (stack, tintIndex) -> (tintIndex == 1)
            ? ((ItemWorktableTool) stack.getItem()).getMaterial().getColor()
            : 0xFFFFFF,
        this.registeredToolList.toArray(new ItemWorktableTool[this.registeredToolList.size()])
    );
  }
}

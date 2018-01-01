package com.codetaylor.mc.artisanworktables.modules.tools;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.modules.tools.item.ItemWorktableTool;
import com.codetaylor.mc.artisanworktables.modules.tools.recipe.ModuleRecipes;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumMaterial;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumWorktableToolType;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.registry.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ModuleTools
    extends ModuleBase {

  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModArtisanWorktables.CREATIVE_TAB;

  public static final class Lang {

    public static final String MATERIAL_STRING = "material.artisanworktables.%s";
  }

  private final List<EnumMaterial> materialList = new ArrayList<>();
  private final List<ItemWorktableTool> registeredToolList = new ArrayList<>();

  public ModuleTools() {

    super(0);
    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void onRegister(Registry registry) {

    super.onRegister(registry);

    this.materialList.add(EnumMaterial.WOOD);
    this.materialList.add(EnumMaterial.FLINT);
    this.materialList.add(EnumMaterial.STONE);
    this.materialList.add(EnumMaterial.IRON);
    this.materialList.add(EnumMaterial.GOLD);
    this.materialList.add(EnumMaterial.DIAMOND);

    if (ModuleToolsConfig.ENABLE_THERMAL_FOUNDATION_MATERIALS) {
      this.materialList.add(EnumMaterial.ALUMINUM);
      this.materialList.add(EnumMaterial.BRONZE);
      this.materialList.add(EnumMaterial.CONSTANTAN);
      this.materialList.add(EnumMaterial.COPPER);
      this.materialList.add(EnumMaterial.ELECTRUM);
      this.materialList.add(EnumMaterial.INVAR);
      this.materialList.add(EnumMaterial.LEAD);
      this.materialList.add(EnumMaterial.NICKEL);
      this.materialList.add(EnumMaterial.PLATINUM);
      this.materialList.add(EnumMaterial.SILVER);
      this.materialList.add(EnumMaterial.STEEL);
      this.materialList.add(EnumMaterial.TIN);
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
  public void onClientRegister(Registry registry) {

    super.onClientRegister(registry);

    registry.registerItemModelStrategy(() -> {

      for (ItemWorktableTool item : ModuleTools.this.registeredToolList) {
        String resourcePath = item.getMaterial().isHighlighted() ? item.getName() + "_highlighted" : item.getName();
        ResourceLocation location = new ResourceLocation(MOD_ID, resourcePath);
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(location, "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);
      }
    });
  }

  @Override
  public void onInitializationEvent(FMLInitializationEvent event) {

    super.onInitializationEvent(event);

    for (ItemWorktableTool item : this.registeredToolList) {
      OreDictionary.registerOre(item.getType().getName(), item);
    }
  }

  @SubscribeEvent
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    if (ModuleToolsConfig.ENABLE_TOOL_RECIPES) {
      ModuleRecipes.register(event.getRegistry(), MOD_ID, this.registeredToolList);
    }
  }

  @Override
  public void onClientInitializationEvent(FMLInitializationEvent event) {

    super.onClientInitializationEvent(event);

    // Register item color handlers to colorize layer 1 of each item model
    // using the color stored in the material enum.

    ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

    itemColors.registerItemColorHandler(
        (stack, tintIndex) -> (tintIndex == 1)
            ? ((ItemWorktableTool) stack.getItem()).getMaterial().getColor()
            : 0xFFFFFF,
        this.registeredToolList.toArray(new ItemWorktableTool[this.registeredToolList.size()])
    );
  }
}

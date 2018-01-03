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
import java.util.Arrays;
import java.util.List;

public class ModuleTools
    extends ModuleBase {

  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModArtisanWorktables.CREATIVE_TAB;

  public static final class Lang {

    public static final String MATERIAL_STRING = "material.artisanworktables.%s";
  }

  private final List<ItemWorktableTool> registeredToolList = new ArrayList<>();

  public ModuleTools() {

    super(0, MOD_ID);
    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void onRegister(Registry registry) {

    super.onRegister(registry);

    final List<EnumMaterial> materialList = new ArrayList<>();

    // Vanilla materials
    materialList.add(EnumMaterial.WOOD);
    materialList.add(EnumMaterial.FLINT);
    materialList.add(EnumMaterial.STONE);
    materialList.add(EnumMaterial.IRON);
    materialList.add(EnumMaterial.GOLD);
    materialList.add(EnumMaterial.DIAMOND);

    // Thermal Foundation materials
    materialList.add(EnumMaterial.ALUMINUM);
    materialList.add(EnumMaterial.BRONZE);
    materialList.add(EnumMaterial.CONSTANTAN);
    materialList.add(EnumMaterial.COPPER);
    materialList.add(EnumMaterial.ELECTRUM);
    materialList.add(EnumMaterial.INVAR);
    materialList.add(EnumMaterial.LEAD);
    materialList.add(EnumMaterial.NICKEL);
    materialList.add(EnumMaterial.PLATINUM);
    materialList.add(EnumMaterial.SILVER);
    materialList.add(EnumMaterial.STEEL);
    materialList.add(EnumMaterial.TIN);

    List<String> allowedToolTypeList = new ArrayList<>();
    allowedToolTypeList.addAll(Arrays.asList(ModuleToolsConfig.ENABLED_TOOL_TYPES));

    if (allowedToolTypeList.isEmpty()) {
      // User has disabled all tool types.
      return;
    }

    List<String> allowedToolMaterialList = new ArrayList<>();
    allowedToolMaterialList.addAll(Arrays.asList(ModuleToolsConfig.ENABLED_TOOL_MATERIALS));

    if (allowedToolMaterialList.isEmpty()) {
      // User has disabled all tool materials.
      return;
    }

    for (EnumWorktableToolType type : EnumWorktableToolType.values()) {
      String typeName = type.getName();

      if (!allowedToolTypeList.contains(typeName)) {
        // User has disabled this tool type.
        continue;
      }

      for (EnumMaterial material : materialList) {
        String materialName = material.getName();

        if (!allowedToolMaterialList.contains(materialName)) {
          // User has disabled this material.
          continue;
        }

        ItemWorktableTool item = new ItemWorktableTool(type, material);
        this.registeredToolList.add(item);
        ResourceLocation registryName = new ResourceLocation(MOD_ID, typeName + "_" + materialName);
        registry.registerItem(item, registryName);
        item.setUnlocalizedName(registryName.getResourceDomain().replaceAll("_", ".")
            + "." + typeName.replaceAll("_", "."));
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

    if (ModuleToolsConfig.ENABLE_TOOL_RECIPES
        && !this.registeredToolList.isEmpty()) {
      ModuleRecipes.register(event.getRegistry(), MOD_ID, this.registeredToolList);
    }
  }

  @Override
  public void onClientInitializationEvent(FMLInitializationEvent event) {

    super.onClientInitializationEvent(event);

    if (this.registeredToolList.isEmpty()) {
      // No tools were registered.
      return;
    }

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

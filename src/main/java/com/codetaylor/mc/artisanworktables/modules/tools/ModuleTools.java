package com.codetaylor.mc.artisanworktables.modules.tools;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.modules.tools.item.ItemWorktableTool;
import com.codetaylor.mc.artisanworktables.modules.tools.material.*;
import com.codetaylor.mc.artisanworktables.modules.tools.recipe.ModuleToolsRecipes;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumWorktableToolType;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.parser.recipe.item.RecipeItemParser;
import com.codetaylor.mc.athenaeum.registry.Registry;
import com.codetaylor.mc.athenaeum.util.FileHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModuleTools
    extends ModuleBase {

  public static final String MOD_ID = ModArtisanWorktables.MOD_ID;
  public static final CreativeTabs CREATIVE_TAB = ModArtisanWorktables.CREATIVE_TAB;

  public static final class Lang {

    public static final String TOOLTIP_DURABILITY = "item." + MOD_ID + ".tooltip.durability";
  }

  private final List<ItemWorktableTool> registeredToolList = new ArrayList<>();

  private List<CustomMaterial> materialList = Collections.emptyList();

  public ModuleTools() {

    super(0, MOD_ID);
    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();

    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File configurationDirectory = event.getModConfigurationDirectory();
    Path configurationPath = Paths.get(configurationDirectory.toString(), ModuleTools.MOD_ID);

    if (!Files.exists(configurationPath)) {

      try {
        Files.createDirectories(configurationPath);

      } catch (IOException e) {
        event.getModLog().error("", e);
      }
    }

    Path generatedPath = Paths.get(
        configurationPath.toString(),
        "artisanworktables.module.Tools.Materials.Generated.json"
    );
    Path customPath = Paths.get(
        configurationPath.toString(),
        "artisanworktables.module.Tools.Materials.Custom.json"
    );

    // Delete the generated file if it exists.
    if (Files.exists(generatedPath)) {

      try {
        Files.delete(generatedPath);

      } catch (IOException e) {
        event.getModLog().error("", e);
      }
    }

    // Create and write the generated file.
    BufferedWriter writer = null;

    try {
      writer = Files.newBufferedWriter(generatedPath);
      gson.toJson(new DataCustomMaterialListFactory().create(), writer);
      writer.close();

    } catch (IOException e) {
      event.getModLog().error("", e);

    } finally {
      FileHelper.closeSilently(writer);
    }

    // Copy the generated file to the custom file if the custom file doesn't exist.
    if (!Files.exists(customPath)) {

      try {
        Files.copy(generatedPath, customPath);

      } catch (IOException e) {
        event.getModLog().error("", e);
      }
    }

    BufferedReader reader = null;

    try {
      reader = Files.newBufferedReader(customPath);
      DataCustomMaterialList dataCustomMaterialList = gson.fromJson(reader, DataCustomMaterialList.class);
      CustomMaterialListConverter customMaterialListConverter = new CustomMaterialListConverter(
          new CustomMaterialValidator(),
          new CustomMaterialConverter(
              new RecipeItemParser()
          )
      );
      this.materialList = customMaterialListConverter.convert(dataCustomMaterialList, event.getModLog());

    } catch (IOException e) {
      event.getModLog().error("", e);

    } finally {
      FileHelper.closeSilently(reader);
    }

    super.onPreInitializationEvent(event);
  }

  @Override
  public void onRegister(Registry registry) {

    super.onRegister(registry);

    List<String> allowedToolTypeList = new ArrayList<>();
    allowedToolTypeList.addAll(Arrays.asList(ModuleToolsConfig.ENABLED_TOOL_TYPES));

    if (allowedToolTypeList.isEmpty()) {
      // User has disabled all tool types.
      return;
    }

    if (this.materialList.isEmpty()) {
      // User has disabled all tool materials.
      return;
    }

    for (EnumWorktableToolType type : EnumWorktableToolType.values()) {
      String typeName = type.getName();

      if (!allowedToolTypeList.contains(typeName)) {
        // User has disabled this tool type.
        continue;
      }

      for (CustomMaterial material : materialList) {
        String materialName = material.getDataCustomMaterial().getName();

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
        String resourcePath = item.getMaterial()
            .getDataCustomMaterial()
            .isShiny() ? item.getName() + "_highlighted" : item.getName();
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

    if (ModuleToolsConfig.ENABLE_TOOL_MATERIAL_ORE_DICT_GROUPS) {

      for (ItemWorktableTool item : this.registeredToolList) {
        OreDictionary.registerOre(item.getMaterial().getDataCustomMaterial().getOreDictKey(), item);
      }
    }
  }

  @SubscribeEvent
  public void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {

    if (ModuleToolsConfig.ENABLE_TOOL_RECIPES
        && !this.registeredToolList.isEmpty()) {
      ModuleToolsRecipes.register(event.getRegistry(), MOD_ID, this.registeredToolList);
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

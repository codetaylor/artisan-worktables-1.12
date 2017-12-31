package com.codetaylor.mc.artisanworktables.modules.tools;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.modules.tools.item.ItemWorktableTool;
import com.codetaylor.mc.artisanworktables.modules.tools.reference.EnumMaterial;
import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.registry.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

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

  public static final String[] TOOL_NAMES = new String[]{
      "blacksmiths_cutters",
      "blacksmiths_hammer",
      "carpenters_hammer",
      "carpenters_handsaw",
      "jewelers_gemcutter",
      "jewelers_pliers",
      "masons_chisel",
      "masons_trowel",
      "tailors_needle",
      "tailors_shears"
  };

  public static final EnumMaterial[] MATERIALS_VANILLA = new EnumMaterial[]{
      EnumMaterial.WOOD,
      EnumMaterial.FLINT,
      EnumMaterial.STONE,
      EnumMaterial.IRON,
      EnumMaterial.GOLD,
      EnumMaterial.DIAMOND
  };

  public static final List<ItemWorktableTool> TOOL_LIST = new ArrayList<>();

  public ModuleTools() {

    super(0);
    this.setRegistry(new Registry(MOD_ID, CREATIVE_TAB));
    this.enableAutoRegistry();
  }

  @Override
  public void onRegister(Registry registry) {

    super.onRegister(registry);

    for (String name : TOOL_NAMES) {

      for (EnumMaterial material : MATERIALS_VANILLA) {
        String materialName = material.getName();

        if (Arrays.stream(ModuleToolsConfig.ENABLED_TOOLS)
            .anyMatch(s -> s.startsWith(name) && s.endsWith(materialName))) {
          ItemWorktableTool item = new ItemWorktableTool(name, material);
          this.TOOL_LIST.add(item);
          ResourceLocation registryName = new ResourceLocation(MOD_ID, name + "_" + materialName);
          registry.registerItem(item, registryName);
          item.setUnlocalizedName(registryName.getResourceDomain().replaceAll("_", ".")
              + "." + name.replaceAll("_", "."));
        }
      }
    }
  }

  @Override
  public void onClientRegisterModelsEvent(ModelRegistryEvent event) {

    super.onClientRegisterModelsEvent(event);

    // TODO: this needs to be migrated to a custom (?) model registration strategy
    for (ItemWorktableTool item : TOOL_LIST) {
      String resourcePath = item.getMaterial().isHighlighted() ? item.getName() + "_highlighted" : item.getName();
      ResourceLocation location = new ResourceLocation(MOD_ID, resourcePath);
      ModelResourceLocation modelResourceLocation = new ModelResourceLocation(location, "inventory");
      ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);
    }
  }

  @Override
  public void onClientInitializationEvent(FMLInitializationEvent event) {

    super.onClientInitializationEvent(event);

    ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

    itemColors.registerItemColorHandler(
        (stack, tintIndex) -> (tintIndex == 1)
            ? ((ItemWorktableTool) stack.getItem()).getMaterial().getColor()
            : 0xFFFFFF,
        TOOL_LIST.toArray(new ItemWorktableTool[TOOL_LIST.size()])
    );
  }
}

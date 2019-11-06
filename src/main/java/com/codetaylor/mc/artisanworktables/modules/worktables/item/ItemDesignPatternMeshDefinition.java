package com.codetaylor.mc.artisanworktables.modules.worktables.item;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ItemDesignPatternMeshDefinition
    implements ItemMeshDefinition {

  private final ModelResourceLocation modelResourceLocationBlank;
  private final ModelResourceLocation modelResourceLocationWritten;

  public ItemDesignPatternMeshDefinition() {

    this.modelResourceLocationBlank = new ModelResourceLocation(new ResourceLocation(
        ModuleWorktables.MOD_ID,
        ItemDesignPattern.NAME + "_" + ItemDesignPattern.NAME_BLANK
    ), "inventory");

    this.modelResourceLocationWritten = new ModelResourceLocation(new ResourceLocation(
        ModuleWorktables.MOD_ID,
        ItemDesignPattern.NAME + "_" + ItemDesignPattern.NAME_WRITTEN
    ), "inventory");
  }

  @Nonnull
  @Override
  public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {

    if (stack.hasTagCompound()) {
      return this.modelResourceLocationWritten;
    }

    return this.modelResourceLocationBlank;
  }
}

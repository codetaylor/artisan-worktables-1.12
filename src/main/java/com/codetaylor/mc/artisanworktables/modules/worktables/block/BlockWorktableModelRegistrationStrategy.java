package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.athenaeum.registry.strategy.IModelRegistrationStrategy;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

class BlockWorktableModelRegistrationStrategy
    implements IModelRegistrationStrategy {

  @Override
  public void register() {

    BlockWorktable.VARIANT.getAllowedValues().forEach(type -> {
      IBlockState state = ModuleWorktables.Blocks.WORKTABLE.getDefaultState()
          .withProperty(BlockWorktable.VARIANT, type);
      Block block = state.getBlock();
      Item item = Item.getItemFromBlock(block);
      ModelLoader.setCustomModelResourceLocation(
          item,
          type.getMeta(),
          new ModelResourceLocation(
              Preconditions.checkNotNull(item.getRegistryName(), "Item %s has null registry name", item),
              BlockWorktable.VARIANT.getName() + "=" + type.getName()
          )
      );
    });

    IBlockState state = ModuleWorktables.Blocks.WORKTABLE.getDefaultState()
        .withProperty(BlockWorktable.VARIANT, BlockWorktableEnumType.MAGE);
    Block block = state.getBlock();
    Item item = Item.getItemFromBlock(block);
    ModelLoader.setCustomModelResourceLocation(
        item,
        Short.MAX_VALUE / 2 + BlockWorktableEnumType.MAGE.getMeta(),
        new ModelResourceLocation(
            Preconditions.checkNotNull(item.getRegistryName(), "Item %s has null registry name", item),
            BlockWorktable.VARIANT.getName() + "=" + BlockWorktableEnumType.MAGE.getName() + "_active"
        )
    );

    ModelLoader.setCustomStateMapper(ModuleWorktables.Blocks.WORKTABLE, new DefaultStateMapper() {

      @Nonnull
      @Override
      protected ModelResourceLocation getModelResourceLocation(IBlockState state) {

        BlockWorktableEnumType type = state.getValue(BlockWorktable.VARIANT);

        if (type == BlockWorktableEnumType.MAGE) {
          Block block = state.getBlock();
          boolean active = state.getValue(BlockWorktable.ACTIVE);

          return new ModelResourceLocation(
              Preconditions.checkNotNull(
                  Block.REGISTRY.getNameForObject(block),
                  "Block %s has null registry name",
                  block
              ),
              BlockWorktable.VARIANT.getName() + "=" + type.getName() + (active ? "_active" : "")
          );

        } else {
          Block block = state.getBlock();

          return new ModelResourceLocation(
              Preconditions.checkNotNull(
                  Block.REGISTRY.getNameForObject(block),
                  "Block %s has null registry name",
                  block
              ),
              BlockWorktable.VARIANT.getName() + "=" + type.getName()
          );
        }
      }
    });
  }
}

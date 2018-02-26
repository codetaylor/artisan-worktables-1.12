package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import com.codetaylor.mc.athenaeum.registry.strategy.IClientModelRegistrationStrategy;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

class BlockModelRegistrationStrategy
    implements IClientModelRegistrationStrategy {

  private Block block;

  public BlockModelRegistrationStrategy(Block block) {

    this.block = block;
  }

  @Override
  public void register() {

    BlockBase.VARIANT.getAllowedValues().forEach(type -> {
      IBlockState state = this.block.getDefaultState()
          .withProperty(BlockBase.VARIANT, type);
      Block block = state.getBlock();
      Item item = Item.getItemFromBlock(block);
      ModelLoader.setCustomModelResourceLocation(
          item,
          type.getMeta(),
          new ModelResourceLocation(
              Preconditions.checkNotNull(item.getRegistryName(), "Item %s has null registry name", item),
              BlockBase.VARIANT.getName() + "=" + type.getName()
          )
      );
    });

    IBlockState state = this.block.getDefaultState()
        .withProperty(BlockBase.VARIANT, EnumType.MAGE);
    Block block = state.getBlock();
    Item item = Item.getItemFromBlock(block);
    ModelLoader.setCustomModelResourceLocation(
        item,
        Short.MAX_VALUE / 2 + EnumType.MAGE.getMeta(),
        new ModelResourceLocation(
            Preconditions.checkNotNull(item.getRegistryName(), "Item %s has null registry name", item),
            BlockBase.VARIANT.getName() + "=" + EnumType.MAGE.getName() + "_active"
        )
    );

    ModelLoader.setCustomStateMapper(this.block, new DefaultStateMapper() {

      @Nonnull
      @Override
      protected ModelResourceLocation getModelResourceLocation(IBlockState state) {

        EnumType type = state.getValue(BlockBase.VARIANT);

        if (type == EnumType.MAGE) {
          Block block = state.getBlock();
          boolean active = state.getValue(BlockBase.ACTIVE);

          return new ModelResourceLocation(
              Preconditions.checkNotNull(
                  Block.REGISTRY.getNameForObject(block),
                  "Block %s has null registry name",
                  block
              ),
              BlockBase.VARIANT.getName() + "=" + type.getName() + (active ? "_active" : "")
          );

        } else {
          Block block = state.getBlock();

          return new ModelResourceLocation(
              Preconditions.checkNotNull(
                  Block.REGISTRY.getNameForObject(block),
                  "Block %s has null registry name",
                  block
              ),
              BlockBase.VARIANT.getName() + "=" + type.getName()
          );
        }
      }
    });
  }
}

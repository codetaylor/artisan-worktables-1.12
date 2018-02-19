package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.element.GuiElementTabs;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.registry.strategy.IModelRegistrationStrategy;
import com.codetaylor.mc.athenaeum.tile.IContainer;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockBase
    extends Block {

  public static final IProperty<EnumType> VARIANT = PropertyEnum.create(
      "variant",
      EnumType.class
  );
  public static final IProperty<Boolean> ACTIVE = PropertyBool.create("active");

  public BlockBase(
      Material materialIn
  ) {

    super(materialIn);
  }

  @Override
  public SoundType getSoundType(
      IBlockState state, World world, BlockPos pos, @Nullable Entity entity
  ) {

    return state.getValue(VARIANT).getSoundType();
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Override
  public boolean onBlockActivated(
      World worldIn,
      BlockPos pos,
      IBlockState state,
      EntityPlayer playerIn,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ
  ) {

    if (worldIn.isRemote) {
      GuiElementTabs.RECALCULATE_TAB_OFFSETS = true;
      return true;
    }

    TileEntity tileEntity = worldIn.getTileEntity(pos);

    return !(tileEntity instanceof TileEntityBase) || ((TileEntityBase) tileEntity).onBlockActivated(
        worldIn,
        pos,
        state,
        playerIn,
        hand,
        facing,
        hitX,
        hitY,
        hitZ
    );
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

    TileEntity tileEntity = worldIn.getTileEntity(pos);

    if (tileEntity instanceof IContainer) {
      List<ItemStack> drops = ((IContainer) tileEntity).getBlockBreakDrops();

      for (ItemStack drop : drops) {
        InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), drop);
      }
    }

    super.breakBlock(worldIn, pos, state);
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, VARIANT, ACTIVE);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, EnumType.fromMeta(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(VARIANT).getMeta();
  }

  @Override
  public int damageDropped(IBlockState state) {

    return this.getMetaFromState(state);
  }

  @Override
  public IBlockState getActualState(
      IBlockState state, IBlockAccess worldIn, BlockPos pos
  ) {

    if (state.getValue(VARIANT) == EnumType.MAGE) {

      TileEntity tileEntity;

      if (worldIn instanceof ChunkCache) {
        tileEntity = ((ChunkCache) worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

      } else {
        tileEntity = worldIn.getTileEntity(pos);
      }

      if (tileEntity instanceof TileEntityBase
          && ((TileEntityBase) tileEntity).getType() == EnumType.MAGE) {
        return state.withProperty(ACTIVE, ((TileEntityBase) tileEntity).hasTool());
      }
    }

    return super.getActualState(state, worldIn, pos);
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    if (state.getValue(VARIANT) == EnumType.MAGE) {

      if (this.getActualState(state, world, pos).getValue(ACTIVE)) {
        return 8;
      }
    }

    return super.getLightValue(state, world, pos);
  }

  @Override
  public void getSubBlocks(
      CreativeTabs tab,
      NonNullList<ItemStack> list
  ) {

    for (EnumType type : EnumType.values()) {
      list.add(new ItemStack(this, 1, type.getMeta()));
    }
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return false;
  }

  @Nonnull
  public IProperty<EnumType> getVariant() {

    return VARIANT;
  }

  public IModelRegistrationStrategy getModelRegistrationStrategy() {

    return new BlockModelRegistrationStrategy(this);
  }

  @Nonnull
  public String getModelName(ItemStack itemStack) {

    return EnumType.fromMeta(itemStack.getMetadata()).getName();
  }

}

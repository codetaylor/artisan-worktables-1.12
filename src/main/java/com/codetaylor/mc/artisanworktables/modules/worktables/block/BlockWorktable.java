package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityWorktableMage;
import com.codetaylor.mc.athenaeum.registry.strategy.IModelRegistrationStrategy;
import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
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

public class BlockWorktable
    extends Block
    implements IBlockVariant<BlockWorktableEnumType>,
    IModelRegistrationStrategyProvider {

  public static final String NAME = "worktable";
  public static final IProperty<BlockWorktableEnumType> VARIANT = PropertyEnum.create(
      "variant",
      BlockWorktableEnumType.class
  );
  public static final IProperty<Boolean> ACTIVE = PropertyBool.create("active");

  public BlockWorktable() {

    super(Material.ROCK);

    this.setHardness(5);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(VARIANT, BlockWorktableEnumType.TAILOR)
        .withProperty(ACTIVE, false));
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

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    BlockWorktableEnumType type = state.getValue(VARIANT);

    try {
      return type.getTileEntityClass().newInstance();

    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
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
      return true;
    }

    playerIn.openGui(ModuleWorktables.MOD_INSTANCE, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
    return true;
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

    return this.getDefaultState().withProperty(VARIANT, BlockWorktableEnumType.fromMeta(meta));
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

    if (state.getValue(VARIANT) == BlockWorktableEnumType.MAGE) {

      TileEntity tileEntity;

      if (worldIn instanceof ChunkCache) {
        tileEntity = ((ChunkCache) worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

      } else {
        tileEntity = worldIn.getTileEntity(pos);
      }

      if (tileEntity instanceof TileEntityWorktableMage) {
        boolean active = !((TileEntityWorktableMage) tileEntity).getToolHandler().getStackInSlot(0).isEmpty();
        return state.withProperty(ACTIVE, active);
      }
    }

    return super.getActualState(state, worldIn, pos);
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {

    if (state.getValue(VARIANT) == BlockWorktableEnumType.MAGE) {

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

    for (BlockWorktableEnumType type : BlockWorktableEnumType.values()) {
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
  @Override
  public String getModelName(ItemStack itemStack) {

    return BlockWorktableEnumType.fromMeta(itemStack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public IProperty<BlockWorktableEnumType> getVariant() {

    return VARIANT;
  }

  @Override
  public IModelRegistrationStrategy getModelRegistrationStrategy() {

    return new BlockWorktableModelRegistrationStrategy();
  }

}


























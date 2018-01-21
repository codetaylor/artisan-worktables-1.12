package com.codetaylor.mc.artisanworktables.modules.toolbox.block;

import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.athenaeum.tile.IContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockToolbox
    extends Block {

  public static final String NAME = "toolbox";
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);

  public BlockToolbox() {

    super(Material.WOOD);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return false;
  }

  @Override
  public BlockFaceShape getBlockFaceShape(
      IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face
  ) {

    return BlockFaceShape.UNDEFINED;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasCustomBreakingProgress(IBlockState state) {

    return true;
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {

    return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public AxisAlignedBB getBoundingBox(
      IBlockState state, IBlockAccess source, BlockPos pos
  ) {

    return AABB;
  }

  public IBlockState getStateForPlacement(
      World worldIn,
      BlockPos pos,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ,
      int meta,
      EntityLivingBase placer
  ) {

    return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
  }

  @Override
  public void onBlockPlacedBy(
      World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack
  ) {

    EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor((double) (placer.rotationYaw * 4f / 360f) + 0.5) & 3)
        .getOpposite();
    state = state.withProperty(FACING, enumfacing);
    worldIn.setBlockState(pos, state, 3);

    if (stack.hasDisplayName()) {
      TileEntity tileentity = worldIn.getTileEntity(pos);

      if (tileentity instanceof TileEntityChest) {
        ((TileEntityChest) tileentity).setCustomName(stack.getDisplayName());
      }
    }
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
  public boolean hasTileEntity(IBlockState state) {

    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileEntityToolbox();
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    EnumFacing enumfacing = EnumFacing.getFront(meta);

    if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
      enumfacing = EnumFacing.NORTH;
    }

    return this.getDefaultState().withProperty(FACING, enumfacing);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(FACING).getIndex();
  }

  @Override
  public IBlockState withRotation(IBlockState state, Rotation rot) {

    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {

    return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, FACING);
  }
}

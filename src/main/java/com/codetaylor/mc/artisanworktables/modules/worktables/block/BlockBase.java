package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.Util;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.element.GuiElementTabs;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.registry.strategy.IClientModelRegistrationStrategy;
import com.codetaylor.mc.athenaeum.tile.IContainer;
import com.codetaylor.mc.athenaeum.util.FluidHelper;
import com.codetaylor.mc.athenaeum.util.FluidUtilFix;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class BlockBase
    extends Block {

  public static final IProperty<EnumType> VARIANT = PropertyEnum.create(
      "variant",
      EnumType.class
  );
  public static final IProperty<Boolean> ACTIVE = PropertyBool.create("active");

  public BlockBase(Material material) {

    super(material);

    EnumType[] types = EnumType.values();
    Map<String, String> harvestToolMap = this.getHarvestToolMap();

    for (EnumType type : types) {
      IBlockState blockState = this.getStateFromMeta(type.getMeta());
      IProperty<EnumType> variant = this.getVariant();
      EnumType value = blockState.getValue(variant);
      String key = value.getName();
      String harvestTool = harvestToolMap.get(key);

      if (harvestTool != null) {
        this.setHarvestLevel(harvestTool, 0, blockState);
      }
    }
  }

  @Override
  public SoundType getSoundType(
      IBlockState state, World world, BlockPos pos, @Nullable Entity entity
  ) {

    return state.getValue(this.getVariant()).getSoundType();
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

    if (tileEntity instanceof TileEntityBase) {

      FluidTank tank = ((TileEntityBase) tileEntity).getTank();

      if (FluidHelper.drainWaterFromBottle(playerIn, tank)
          || FluidHelper.drainWaterIntoBottle(playerIn, tank)
          || FluidUtilFix.interactWithFluidHandler(playerIn, hand, tank)) {
        return true;
      }

      playerIn.openGui(ModArtisanWorktables.INSTANCE, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
      return true;
    }

    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {

    if (!world.isRemote) {
      List<TileEntityBase> joinedTables = Util.getJoinedTables(new ArrayList<>(), world, pos, null);

      for (TileEntityBase table : joinedTables) {
        table.onJoinedBlockBreak(pos);
      }
    }

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof IContainer) {
      List<ItemStack> drops = ((IContainer) tileEntity).getBlockBreakDrops();

      if (!(tileEntity instanceof TileEntityBase)
          || !((TileEntityBase) tileEntity).isCreative()) {

        for (ItemStack drop : drops) {
          InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), drop);
        }
      }
    }

    super.breakBlock(world, pos, state);
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, this.getVariant(), ACTIVE);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(this.getVariant(), EnumType.fromMeta(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return state.getValue(this.getVariant()).getMeta();
  }

  @Override
  public int damageDropped(IBlockState state) {

    return this.getMetaFromState(state);
  }

  @Override
  public IBlockState getActualState(
      IBlockState state, IBlockAccess worldIn, BlockPos pos
  ) {

    if (state.getValue(this.getVariant()) == EnumType.MAGE) {

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

    if (state.getValue(this.getVariant()) == EnumType.MAGE) {

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

  protected abstract Map<String, String> getHarvestToolMap();

  @Override
  public BlockFaceShape getBlockFaceShape(
      IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face
  ) {

    if (face == EnumFacing.UP) {
      return BlockFaceShape.SOLID;
    }

    return BlockFaceShape.UNDEFINED;
  }

  @Nonnull
  public IProperty<EnumType> getVariant() {

    return VARIANT;
  }

  public IClientModelRegistrationStrategy getModelRegistrationStrategy() {

    return new BlockModelRegistrationStrategy(this);
  }

  @Nonnull
  public String getModelName(ItemStack itemStack) {

    return EnumType.fromMeta(itemStack.getMetadata()).getName();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

    TileEntity tileEntity = world.getTileEntity(pos);

    if (tileEntity instanceof TileEntityBase) {
      TileEntityBase table = (TileEntityBase) tileEntity;

      if (table.isCreative()) {
        if (state.getMaterial() != Material.AIR) {
          for (int i = 0; i < 7; ++i) {
            double d0 = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            world.spawnParticle(EnumParticleTypes.END_ROD, (double) ((float) pos.getX() + rand.nextFloat()), (double) pos.getY() + (double) rand.nextFloat() * state.getBoundingBox(world, pos).maxY, (double) ((float) pos.getZ() + rand.nextFloat()), d0, d1, d2);
          }
        }
      }
    }
  }
}

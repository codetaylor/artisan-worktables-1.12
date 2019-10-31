package com.codetaylor.mc.artisanworktables.modules.automator.block;

import com.codetaylor.mc.artisanworktables.lib.BlockPartialBase;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomatorPowerSupplierRF;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class BlockAutomatorPowerRF
    extends BlockPartialBase {

  public static final String NAME = "automator_power_rf";

  private static final IProperty<EnumType> NORTH = PropertyEnum.create("north", EnumType.class);
  private static final IProperty<EnumType> SOUTH = PropertyEnum.create("south", EnumType.class);
  private static final IProperty<EnumType> EAST = PropertyEnum.create("east", EnumType.class);
  private static final IProperty<EnumType> WEST = PropertyEnum.create("west", EnumType.class);
  private static final PropertyBool POWERED = PropertyBool.create("powered");

  private static final Map<EnumFacing, IProperty<EnumType>> PROPERTY_MAP = new EnumMap<EnumFacing, IProperty<EnumType>>(EnumFacing.class) {{
    this.put(EnumFacing.NORTH, NORTH);
    this.put(EnumFacing.SOUTH, SOUTH);
    this.put(EnumFacing.EAST, EAST);
    this.put(EnumFacing.WEST, WEST);
  }};

  private static final AxisAlignedBB AABB = AABBHelper.create(2, 0, 2, 14, 16, 14);

  public BlockAutomatorPowerRF() {

    super(Material.ROCK);
    this.setSoundType(SoundType.STONE);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(POWERED, false)
        .withProperty(NORTH, EnumType.NONE)
        .withProperty(SOUTH, EnumType.NONE)
        .withProperty(EAST, EnumType.NONE)
        .withProperty(WEST, EnumType.NONE));
  }

  // --------------------------------------------------------------------------
  // - Harvest
  // --------------------------------------------------------------------------

  @Override
  public int getHarvestLevel(@Nonnull IBlockState state) {

    return 0;
  }

  @Nullable
  @Override
  public String getHarvestTool(@Nonnull IBlockState state) {

    return "pickaxe";
  }

  // --------------------------------------------------------------------------
  // - Display
  // --------------------------------------------------------------------------

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {

    if (state.getBlock() == this) {

      {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileAutomatorPowerSupplierRF) {
          boolean powered = ((TileAutomatorPowerSupplierRF) tileEntity).isPowered();
          state = state.withProperty(POWERED, powered);
        }
      }

      for (EnumFacing facing : EnumFacing.HORIZONTALS) {
        TileEntity tileEntity = world.getTileEntity(pos.offset(facing));

        if (tileEntity == null) {
          continue;
        }

        if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
          state = state.withProperty(PROPERTY_MAP.get(facing), EnumType.ITEM);

        } else if (tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
          state = state.withProperty(PROPERTY_MAP.get(facing), EnumType.POWER);

        } else {
          state = state.withProperty(PROPERTY_MAP.get(facing), EnumType.NONE);
        }
      }
    }

    return super.getActualState(state, world, pos);
  }

  // --------------------------------------------------------------------------
  // - Collision
  // --------------------------------------------------------------------------

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  // --------------------------------------------------------------------------
  // - Tile
  // --------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return (state.getBlock() == this);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    return new TileAutomatorPowerSupplierRF();
  }

  // --------------------------------------------------------------------------
  // - Variants
  // --------------------------------------------------------------------------

  @Override
  public int getMetaFromState(IBlockState state) {

    return 0;
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, POWERED, NORTH, SOUTH, EAST, WEST);
  }

  public enum EnumType
      implements IStringSerializable {

    NONE(0, "none"),
    POWER(1, "power"),
    ITEM(2, "item");

    private static final EnumType[] META_LOOKUP = Stream.of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .toArray(EnumType[]::new);

    private final int meta;
    private final String name;

    EnumType(int meta, String name) {

      this.meta = meta;
      this.name = name;
    }

    public int getMeta() {

      return this.meta;
    }

    @Nonnull
    @Override
    public String getName() {

      return this.name;
    }

    public static EnumType fromMeta(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }
  }

}

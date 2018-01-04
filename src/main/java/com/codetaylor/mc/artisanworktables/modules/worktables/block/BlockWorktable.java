package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.*;
import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import com.codetaylor.mc.athenaeum.tile.IContainer;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class BlockWorktable
    extends Block
    implements IBlockVariant<BlockWorktable.EnumType> {

  public static final String NAME = "worktable";

  public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

  public BlockWorktable() {

    super(Material.ROCK);

    this.setHardness(5);
    this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.TAILOR));
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

    EnumType type = state.getValue(VARIANT);

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

    return new BlockStateContainer(this, VARIANT);
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
  @Override
  public String getModelName(ItemStack itemStack) {

    return EnumType.fromMeta(itemStack.getMetadata()).getName();
  }

  @Nonnull
  @Override
  public IProperty<EnumType> getVariant() {

    return VARIANT;
  }

  public enum EnumType
      implements IVariant {

    TAILOR(0, "tailor", TileEntityWorktableTailor.class, SoundType.CLOTH),
    CARPENTER(1, "carpenter", TileEntityWorktableCarpenter.class, SoundType.WOOD),
    MASON(2, "mason", TileEntityWorktableMason.class, SoundType.STONE),
    BLACKSMITH(3, "blacksmith", TileEntityWorktableBlacksmith.class, SoundType.ANVIL),
    JEWELER(4, "jeweler", TileEntityWorktableJeweler.class, SoundType.METAL),
    BASIC(5, "basic", TileEntityWorktableBasic.class, SoundType.WOOD);

    private static final EnumType[] META_LOOKUP = Stream.of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .toArray(EnumType[]::new);

    public static final String[] NAMES = Stream.of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .map(EnumType::getName)
        .toArray(String[]::new);

    private final int meta;
    private final String name;
    private Class<? extends TileEntity> tileEntityClass;
    private SoundType soundType;

    EnumType(
        int meta,
        String name,
        Class<? extends TileEntity> tileEntityClass,
        SoundType soundType
    ) {

      this.meta = meta;
      this.name = name;
      this.tileEntityClass = tileEntityClass;
      this.soundType = soundType;
    }

    @Override
    public int getMeta() {

      return this.meta;
    }

    @Override
    public String getName() {

      return this.name;
    }

    public Class<? extends TileEntity> getTileEntityClass() {

      return this.tileEntityClass;
    }

    public SoundType getSoundType() {

      return this.soundType;
    }

    public static EnumType fromName(String name) {

      EnumType[] values = EnumType.values();

      for (EnumType value : values) {

        if (value.name.equals(name)) {
          return value;
        }
      }

      throw new IllegalArgumentException("Unknown name: " + name);
    }

    public static EnumType fromMeta(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

  }
}


























package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.*;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import net.minecraft.block.SoundType;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.stream.Stream;

public enum BlockWorktableEnumType
    implements IVariant {

  TAILOR(0, "tailor", TileEntityWorktableTailor.class, SoundType.CLOTH),
  CARPENTER(1, "carpenter", TileEntityWorktableCarpenter.class, SoundType.WOOD),
  MASON(2, "mason", TileEntityWorktableMason.class, SoundType.STONE),
  BLACKSMITH(3, "blacksmith", TileEntityWorktableBlacksmith.class, SoundType.ANVIL),
  JEWELER(4, "jeweler", TileEntityWorktableJeweler.class, SoundType.METAL),
  BASIC(5, "basic", TileEntityWorktableBasic.class, SoundType.WOOD),
  ENGINEER(6, "engineer", TileEntityWorktableEngineer.class, SoundType.ANVIL),
  MAGE(7, "mage", TileEntityWorktableMage.class, SoundType.CLOTH),
  SCRIBE(8, "scribe", TileEntityWorktableScribe.class, SoundType.WOOD);

  private static final BlockWorktableEnumType[] META_LOOKUP = Stream.of(BlockWorktableEnumType.values())
      .sorted(Comparator.comparing(BlockWorktableEnumType::getMeta))
      .toArray(BlockWorktableEnumType[]::new);

  public static final String[] NAMES = Stream.of(BlockWorktableEnumType.values())
      .sorted(Comparator.comparing(BlockWorktableEnumType::getMeta))
      .map(BlockWorktableEnumType::getName)
      .toArray(String[]::new);

  private final int meta;
  private final String name;
  private Class<? extends TileEntity> tileEntityClass;
  private SoundType soundType;

  BlockWorktableEnumType(
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

  @Nonnull
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

  public static BlockWorktableEnumType fromName(String name) {

    BlockWorktableEnumType[] values = BlockWorktableEnumType.values();
    name = name.toLowerCase();

    for (BlockWorktableEnumType value : values) {

      if (value.name.equals(name)) {
        return value;
      }
    }

    throw new IllegalArgumentException("Unknown name: " + name);
  }

  public static BlockWorktableEnumType fromMeta(int meta) {

    if (meta < 0 || meta >= META_LOOKUP.length) {
      meta = 0;
    }

    return META_LOOKUP[meta];
  }

}

package com.codetaylor.mc.artisanworktables.modules.worktables.api;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.*;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import net.minecraft.block.SoundType;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.stream.Stream;

public enum EnumWorktableType
    implements IVariant {

  TAILOR(0, "tailor", TileEntityWorktableTailor.class, SoundType.CLOTH),
  CARPENTER(1, "carpenter", TileEntityWorktableCarpenter.class, SoundType.WOOD),
  MASON(2, "mason", TileEntityWorktableMason.class, SoundType.STONE),
  BLACKSMITH(3, "blacksmith", TileEntityWorktableBlacksmith.class, SoundType.ANVIL),
  JEWELER(4, "jeweler", TileEntityWorktableJeweler.class, SoundType.METAL),
  BASIC(5, "basic", TileEntityWorktableBasic.class, SoundType.WOOD),
  ENGINEER(6, "engineer", TileEntityWorktableEngineer.class, SoundType.METAL);

  private static final EnumWorktableType[] META_LOOKUP = Stream.of(EnumWorktableType.values())
      .sorted(Comparator.comparing(EnumWorktableType::getMeta))
      .toArray(EnumWorktableType[]::new);

  public static final String[] NAMES = Stream.of(EnumWorktableType.values())
      .sorted(Comparator.comparing(EnumWorktableType::getMeta))
      .map(EnumWorktableType::getName)
      .toArray(String[]::new);

  private final int meta;
  private final String name;
  private Class<? extends TileEntity> tileEntityClass;
  private SoundType soundType;

  EnumWorktableType(
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

  public static EnumWorktableType fromName(String name) {

    EnumWorktableType[] values = EnumWorktableType.values();
    name = name.toLowerCase();

    for (EnumWorktableType value : values) {

      if (value.name.equals(name)) {
        return value;
      }
    }

    throw new IllegalArgumentException("Unknown name: " + name);
  }

  public static EnumWorktableType fromMeta(int meta) {

    if (meta < 0 || meta >= META_LOOKUP.length) {
      meta = 0;
    }

    return META_LOOKUP[meta];
  }

}

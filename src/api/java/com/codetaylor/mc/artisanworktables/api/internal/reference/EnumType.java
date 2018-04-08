package com.codetaylor.mc.artisanworktables.api.internal.reference;

import com.codetaylor.mc.athenaeum.spi.IVariant;
import net.minecraft.block.SoundType;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Comparator;
import java.util.stream.Stream;

public enum EnumType
    implements IVariant {

  TAILOR(0, "tailor", SoundType.CLOTH, new Color(172, 81, 227).getRGB(), 1),
  CARPENTER(1, "carpenter", SoundType.WOOD, new Color(188, 152, 98).getRGB(), 2),
  MASON(2, "mason", SoundType.STONE, new Color(151, 151, 151).getRGB(), 4),
  BLACKSMITH(3, "blacksmith", SoundType.ANVIL, new Color(162, 162, 162).getRGB(), 5),
  JEWELER(4, "jeweler", SoundType.METAL, new Color(105, 89, 133).getRGB(), 3),
  BASIC(5, "basic", SoundType.WOOD, new Color(188, 152, 98).getRGB(), 0),
  ENGINEER(6, "engineer", SoundType.ANVIL, new Color(202, 103, 27).getRGB(), 6),
  MAGE(7, "mage", SoundType.CLOTH, new Color(172, 81, 227).getRGB(), 7),
  SCRIBE(8, "scribe", SoundType.WOOD, new Color(182, 136, 79).getRGB(), 8),
  CHEMIST(9, "chemist", SoundType.METAL, new Color(71, 97, 71).getRGB(), 9),
  FARMER(10, "farmer", SoundType.GROUND, new Color(128, 198, 82).getRGB(), 10),
  CHEF(11, "chef", SoundType.STONE, new Color(255, 255, 255).getRGB(), 11),
  DESIGNER(12, "designer", SoundType.STONE, new Color(255, 255, 255).getRGB(), 12),
  TANNER(13, "tanner", SoundType.STONE, new Color(199, 125, 79).getRGB(), 13);

  private static final EnumType[] META_LOOKUP = Stream.of(EnumType.values())
      .sorted(Comparator.comparing(EnumType::getMeta))
      .toArray(EnumType[]::new);

  public static final String[] NAMES = Stream.of(EnumType.values())
      .sorted(Comparator.comparing(EnumType::getMeta))
      .map(EnumType::getName)
      .toArray(String[]::new);

  private final int meta;
  private final String name;
  private SoundType soundType;
  private int textOutlineColor;
  private int guiTabTextureOffsetY;

  EnumType(
      int meta,
      String name,
      SoundType soundType,
      int textOutlineColor,
      int guiTabTextureOffsetY
  ) {

    this.meta = meta;
    this.name = name;
    this.soundType = soundType;
    this.textOutlineColor = textOutlineColor;
    this.guiTabTextureOffsetY = guiTabTextureOffsetY;
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

  public SoundType getSoundType() {

    return this.soundType;
  }

  public int getTextOutlineColor() {

    return this.textOutlineColor;
  }

  public int getGuiTabTextureOffsetY() {

    return this.guiTabTextureOffsetY;
  }

  public static EnumType fromName(String name) {

    EnumType[] values = EnumType.values();
    name = name.toLowerCase();

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

package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktableEnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityWorktableFluidBase;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class TileEntityWorktableMason
    extends TileEntityWorktableFluidBase {

  private static final int TEXT_SHADOW_COLOR = new Color(151, 151, 151).getRGB();
  private static final BlockWorktableEnumType TYPE = BlockWorktableEnumType.MASON;
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      String.format(ModuleWorktables.Textures.WORKTABLE_GUI, TYPE.getName())
  );

  public TileEntityWorktableMason() {

    super(3, 3);
  }

  @Override
  protected String getWorktableName() {

    return TYPE.getName();
  }

  @Override
  protected int getWorktableGuiTextShadowColor() {

    return TEXT_SHADOW_COLOR;
  }

  @Override
  protected ResourceLocation getWorktableGuiBackgroundTexture() {

    return BACKGROUND_TEXTURE;
  }

  @Override
  public int getWorktableGuiTabTextureYOffset() {

    return 4;
  }

  @Override
  public boolean canHandleJEIRecipeTransfer(String name) {

    return TYPE.getName().equals(name);
  }

  @Override
  protected int getFluidCapacity() {

    return ModuleWorktablesConfig.FLUID_CAPACITY.MASON;
  }
}

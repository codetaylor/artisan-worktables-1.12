package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktableEnumType;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class TileEntityWorktableBlacksmith
    extends TileEntityWorktableFluidBase {

  private static final int TEXT_SHADOW_COLOR = new Color(162, 162, 162).getRGB();
  private static final BlockWorktableEnumType TYPE = BlockWorktableEnumType.BLACKSMITH;
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      String.format(ModuleWorktables.Textures.WORKTABLE_GUI, TYPE.getName())
  );

  public TileEntityWorktableBlacksmith() {

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

    return 5;
  }

  @Override
  public boolean canHandleJEIRecipeTransfer(String name) {

    return TYPE.getName().equals(name);
  }

  @Override
  protected int getFluidCapacity() {

    return ModuleWorktablesConfig.FLUID_CAPACITY.BLACKSMITH;
  }
}

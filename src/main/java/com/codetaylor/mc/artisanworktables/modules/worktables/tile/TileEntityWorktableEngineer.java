package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktableEnumType;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class TileEntityWorktableEngineer
    extends TileEntityWorktableFluidBase {

  private static final int TEXT_SHADOW_COLOR = new Color(202, 103, 27).getRGB();
  private static final BlockWorktableEnumType TYPE = BlockWorktableEnumType.ENGINEER;
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      String.format(ModuleWorktables.Textures.WORKTABLE_GUI, TYPE.getName())
  );

  public TileEntityWorktableEngineer() {

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

    return 6;
  }

  @Override
  public boolean canHandleJEIRecipeTransfer(String name) {

    return TYPE.getName().equals(name);
  }

  @Override
  protected int getFluidCapacity() {

    return ModuleWorktablesConfig.FLUID_CAPACITY.ENGINEER;
  }
}

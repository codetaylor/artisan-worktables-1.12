package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class TileEntityWorktableBlacksmith
    extends
    TileEntityWorktableBase {

  private static final int TEXT_SHADOW_COLOR = new Color(162, 162, 162).getRGB();
  private static final BlockWorktable.EnumType TYPE = BlockWorktable.EnumType.BLACKSMITH;
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      String.format(ModuleWorktables.Textures.WORKTABLE_GUI, TYPE.getName())
  );

  public TileEntityWorktableBlacksmith() {

    super(3, 3);
  }

  @Override
  protected int getWorkbenchGuiTextShadowColor() {

    return TEXT_SHADOW_COLOR;
  }

  @Override
  public RegistryRecipeWorktable getRecipeRegistry() {

    return WorktableAPI.RECIPE_REGISTRY_MAP.get(TYPE.getName());
  }

  @Override
  protected ResourceLocation getBackgroundTexture() {

    return BACKGROUND_TEXTURE;
  }
}

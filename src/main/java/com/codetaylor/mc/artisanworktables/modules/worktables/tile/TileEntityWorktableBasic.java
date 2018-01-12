package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumWorktableType;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class TileEntityWorktableBasic
    extends TileEntityWorktableBase {

  private static final int TEXT_SHADOW_COLOR = new Color(188, 152, 98).getRGB();
  private static final EnumWorktableType TYPE = EnumWorktableType.BASIC;
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      String.format(ModuleWorktables.Textures.WORKTABLE_GUI, TYPE.getName())
  );

  public TileEntityWorktableBasic() {

    super(3, 3);
  }

  @Override
  protected int getWorkbenchGuiTextShadowColor() {

    return TEXT_SHADOW_COLOR;
  }

  @Override
  public RegistryRecipeWorktable getRecipeRegistry() {

    return WorktableAPI.getRecipeRegistry(TYPE);
  }

  @Override
  protected ResourceLocation getBackgroundTexture() {

    return BACKGROUND_TEXTURE;
  }

  @Override
  public int getGuiTabTextureYOffset() {

    return 0;
  }

  @Override
  public boolean canHandleJEIRecipeTransfer(String name) {

    return TYPE.getName().equals(name);
  }
}

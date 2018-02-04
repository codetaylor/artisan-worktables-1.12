package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.athenaeum.gui.Texture;
import net.minecraft.util.ResourceLocation;

public class GuiReference {

  public static final ResourceLocation TEXTURE_ATLAS = new ResourceLocation(
      "textures/atlas/blocks.png"
  );

  public static final ResourceLocation TEXTURE_GUI_ELEMENTS = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      "textures/gui/gui_elements.png"
  );

  public static final int TEXTURE_GUI_ELEMENTS_WIDTH = 256;
  public static final int TEXTURE_GUI_ELEMENTS_HEIGHT = 256;

  public static final Texture TEXTURE_FLUID_OVERLAY = new Texture(
      TEXTURE_GUI_ELEMENTS,
      57,
      1,
      TEXTURE_GUI_ELEMENTS_WIDTH,
      TEXTURE_GUI_ELEMENTS_HEIGHT
  );

}

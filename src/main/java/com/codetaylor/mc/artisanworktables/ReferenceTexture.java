package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.athenaeum.gui.Texture;
import net.minecraft.util.ResourceLocation;

public class ReferenceTexture {

  public static final ResourceLocation RESOURCE_LOCATION_GUI_ELEMENTS = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      "textures/gui/gui_elements.png"
  );

  public static final int TEXTURE_GUI_ELEMENTS_WIDTH = 256;
  public static final int TEXTURE_GUI_ELEMENTS_HEIGHT = 256;

  public static final Texture TEXTURE_FLUID_OVERLAY = new Texture(
      RESOURCE_LOCATION_GUI_ELEMENTS,
      57,
      1,
      TEXTURE_GUI_ELEMENTS_WIDTH,
      TEXTURE_GUI_ELEMENTS_HEIGHT
  );

  public static final int TEXTURE_TOOLBOX_WIDTH = 256;
  public static final int TEXTURE_TOOLBOX_HEIGHT = 256;

  public static final ResourceLocation RESOURCE_LOCATION_TOOLBOX = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      "textures/gui/toolbox.png"
  );

  public static final Texture TEXTURE_TOOLBOX = new Texture(
      RESOURCE_LOCATION_TOOLBOX,
      0,
      0,
      TEXTURE_TOOLBOX_WIDTH,
      TEXTURE_TOOLBOX_HEIGHT
  );

  public static final Texture TEXTURE_TOOLBOX_SIDE = new Texture(
      RESOURCE_LOCATION_TOOLBOX,
      176,
      0,
      TEXTURE_TOOLBOX_WIDTH,
      TEXTURE_TOOLBOX_HEIGHT
  );

  public static final int TEXTURE_TOOLBOX_MECHANICAL_WIDTH = 256;
  public static final int TEXTURE_TOOLBOX_MECHANICAL_HEIGHT = 256;

  private static final ResourceLocation RESOURCE_LOCATION_TOOLBOX_MECHANICAL = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      "textures/gui/mechanical_toolbox.png"
  );

  public static final Texture TEXTURE_TOOLBOX_MECHANICAL = new Texture(
      RESOURCE_LOCATION_TOOLBOX_MECHANICAL,
      0,
      0,
      TEXTURE_TOOLBOX_MECHANICAL_WIDTH,
      TEXTURE_TOOLBOX_MECHANICAL_HEIGHT
  );

  public static final Texture TEXTURE_TOOLBOX_MECHANICAL_SIDE = new Texture(
      RESOURCE_LOCATION_TOOLBOX_MECHANICAL,
      176,
      0,
      TEXTURE_TOOLBOX_MECHANICAL_WIDTH,
      TEXTURE_TOOLBOX_MECHANICAL_HEIGHT
  );

}

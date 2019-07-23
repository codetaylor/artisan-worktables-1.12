package com.codetaylor.mc.artisanworktables.api.tool;

import com.codetaylor.mc.artisanworktables.api.internal.tool.CustomMaterial;
import com.codetaylor.mc.artisanworktables.api.tool.reference.EnumWorktableToolType;

public interface ICustomToolProvider<I extends ItemWorktableToolBase> {

  I get(EnumWorktableToolType type, CustomMaterial customMaterial);

}

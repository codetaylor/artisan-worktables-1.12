package com.codetaylor.mc.artisanworktables.modules.toolbox.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelToolbox
    extends ModelBase {

  public ModelRenderer chestLid = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
  public ModelRenderer chestBelow;

  public ModelToolbox() {

    this.chestLid.addBox(0.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F);
    this.chestLid.rotationPointX = 1.0F;
    this.chestLid.rotationPointY = 7.0F;
    this.chestLid.rotationPointZ = 15.0F;
    this.chestBelow = (new ModelRenderer(this, 0, 19)).setTextureSize(64, 64);
    this.chestBelow.addBox(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
    this.chestBelow.rotationPointX = 1.0F;
    this.chestBelow.rotationPointY = 6.0F;
    this.chestBelow.rotationPointZ = 1.0F;
  }

  /**
   * This method renders out all parts of the chest model.
   */
  public void renderAll() {

    this.chestLid.render(0.0625F);
    this.chestBelow.render(0.0625F);
  }
}

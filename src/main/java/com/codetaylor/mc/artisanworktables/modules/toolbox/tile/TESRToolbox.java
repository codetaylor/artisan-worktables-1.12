package com.codetaylor.mc.artisanworktables.modules.toolbox.tile;

import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.block.BlockToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.model.ModelToolbox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class TESRToolbox
    extends TileEntitySpecialRenderer<TileEntityToolbox> {

  private static final ResourceLocation TEXTURE = new ResourceLocation(
      ModuleToolbox.MOD_ID,
      "textures/model/toolbox.png"
  );

  private final ModelToolbox model;

  public TESRToolbox() {

    this.model = new ModelToolbox();
  }

  @Override
  public void render(
      TileEntityToolbox tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha
  ) {

    EnumFacing facing = EnumFacing.SOUTH;

    if (tileEntity.hasWorld()) {
      World world = tileEntity.getWorld();

      if (world.getBlockState(tileEntity.getPos()).getBlock() == ModuleToolbox.Blocks.TOOLBOX) {
        IBlockState blockState = world.getBlockState(tileEntity.getPos());
        facing = blockState.getValue(BlockToolbox.FACING);
      }
    }

    if (destroyStage >= 0) {
      this.bindTexture(DESTROY_STAGES[destroyStage]);
      GlStateManager.matrixMode(GL11.GL_TEXTURE);
      GlStateManager.pushMatrix();
      GlStateManager.scale(4F, 4F, 1F);
      GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
      GlStateManager.matrixMode(GL11.GL_MODELVIEW);

    } else {
      this.bindTexture(TEXTURE);
    }

    GlStateManager.pushMatrix();
    GlStateManager.color(1F, 1F, 1F, 1F);
    GlStateManager.translate((float) x, (float) y + 1F, (float) z + 1F);
    GlStateManager.scale(1F, -1F, -1F);
    GlStateManager.translate(0.5F, 0.5F, 0.5F);

    switch (facing) {
      case NORTH: {
        GlStateManager.rotate(180F, 0F, 1F, 0F);
        break;
      }
      case SOUTH: {
        GlStateManager.rotate(0F, 0F, 1F, 0F);
        break;
      }
      case WEST: {
        GlStateManager.rotate(90F, 0F, 1F, 0F);
        break;
      }
      case EAST: {
        GlStateManager.rotate(270F, 0F, 1F, 0F);
        break;
      }
      default: {
        GlStateManager.rotate(0F, 1F, 0F, 0F);
        break;
      }
    }

    GlStateManager.translate(-0.5F, -0.5F, -0.5F);
    float currentLidAngle = tileEntity.getPreviousLidAngle() + (tileEntity.getCurrentLidAngle() - tileEntity.getPreviousLidAngle()) * partialTicks;
    currentLidAngle = 1F - currentLidAngle;
    currentLidAngle = 1F - currentLidAngle * currentLidAngle * currentLidAngle;

    this.model.chestLid.rotateAngleX = -currentLidAngle * (float) (Math.PI * 0.5);

    // Render the chest itself
    this.model.renderAll();
    if (destroyStage >= 0) {
      GlStateManager.matrixMode(GL11.GL_TEXTURE);
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(GL11.GL_MODELVIEW);
    }
    GlStateManager.popMatrix();
    GlStateManager.color(1F, 1F, 1F, 1F);
  }
}

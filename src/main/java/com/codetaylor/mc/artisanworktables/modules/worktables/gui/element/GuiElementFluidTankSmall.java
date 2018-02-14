package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.ReferenceTexture;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementClickable;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;

public class GuiElementFluidTankSmall
    extends GuiElementFluidTankBase
    implements IGuiElementClickable,
    IGuiElementTooltipProvider {

  private static final int ELEMENT_WIDTH = 6;
  private static final int ELEMENT_HEIGHT = 52;
  private static final int ELEMENT_OVERLAY_WIDTH = 6;
  private static final int ELEMENT_OVERLAY_HEIGHT = 52;

  public GuiElementFluidTankSmall(
      GuiContainerBase guiBase,
      FluidTank fluidTank,
      BlockPos blockPos,
      int overlayColor,
      int elementX,
      int elementY
  ) {

    super(guiBase, fluidTank, elementX, elementY, ELEMENT_WIDTH, ELEMENT_HEIGHT, overlayColor, blockPos);
  }

  @Override
  public void drawForegroundLayer(int mouseX, int mouseY) {

    super.drawForegroundLayer(mouseX, mouseY);

    this.textureBind(ReferenceTexture.TEXTURE_FLUID_OVERLAY);

    RenderHelper.disableStandardItemLighting();
    GlStateManager.enableBlend();
    GlStateManager.color(
        ((this.overlayColor >> 16) & 0xFF) / 255f,
        ((this.overlayColor >> 8) & 0xFF) / 255f,
        (this.overlayColor & 0xFF) / 255f,
        1f
    );

    Gui.drawModalRectWithCustomSizedTexture(
        this.elementX,
        this.elementY,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionX(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionY(),
        ELEMENT_OVERLAY_WIDTH,
        ELEMENT_OVERLAY_HEIGHT,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getWidth(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getHeight()
    );
  }

}

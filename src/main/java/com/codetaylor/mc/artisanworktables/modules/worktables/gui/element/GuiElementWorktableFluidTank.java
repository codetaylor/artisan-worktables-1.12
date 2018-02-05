package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.network.SPacketWorktableTankDestroyFluid;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementFluidTank;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementClickable;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

import java.util.List;

public class GuiElementWorktableFluidTank
    extends GuiElementFluidTank
    implements IGuiElementClickable,
    IGuiElementTooltipProvider {

  private static final int ELEMENT_WIDTH = 6;
  private static final int ELEMENT_HEIGHT = 52;

  private final BlockPos blockPos;
  private final int overlayColor;

  public GuiElementWorktableFluidTank(
      GuiContainerBase guiBase,
      FluidTank fluidTank,
      BlockPos blockPos,
      int overlayColor,
      int elementX,
      int elementY
  ) {

    super(guiBase, fluidTank, elementX, elementY, ELEMENT_WIDTH, ELEMENT_HEIGHT);
    this.blockPos = blockPos;
    this.overlayColor = overlayColor;
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
        0.5f
    );

    Gui.drawModalRectWithCustomSizedTexture(
        this.elementX,
        this.elementY,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionX(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getPositionY(),
        this.elementWidth,
        this.elementHeight,
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getWidth(),
        ReferenceTexture.TEXTURE_FLUID_OVERLAY.getHeight()
    );
  }

  @Override
  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    if (mouseButton == 0 && GuiScreen.isShiftKeyDown()) {
      ModuleWorktables.PACKET_SERVICE.sendToServer(new SPacketWorktableTankDestroyFluid(
          this.blockPos.getX(),
          this.blockPos.getY(),
          this.blockPos.getZ()
      ));
    }
  }

  @Override
  public List<String> tooltipTextGet(List<String> tooltip) {

    if (this.fluidTank.getFluid() == null || this.fluidTank.getFluidAmount() == 0) {
      tooltip.add(I18n.format(ModuleWorktables.Lang.GUI_TOOLTIP_FLUID_EMPTY));

    } else {
      Fluid fluid = this.fluidTank.getFluid().getFluid();
      tooltip.add(fluid.getLocalizedName(this.fluidTank.getFluid()));
      tooltip.add("" + TextFormatting.GRAY + this.fluidTank.getFluidAmount() + " / " + this.fluidTank.getCapacity() + " mB");
      tooltip.add(I18n.format(
          ModuleWorktables.Lang.GUI_TOOLTIP_FLUID_DESTROY,
          TextFormatting.DARK_GRAY,
          TextFormatting.DARK_GRAY
      ));
    }

    return tooltip;
  }
}

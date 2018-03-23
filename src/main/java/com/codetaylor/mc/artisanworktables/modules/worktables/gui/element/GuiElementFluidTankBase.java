package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.network.SPacketWorktableTankDestroyFluid;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementFluidTank;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.List;

public abstract class GuiElementFluidTankBase
    extends GuiElementFluidTank {

  protected final BlockPos blockPos;
  protected final int overlayColor;

  public GuiElementFluidTankBase(
      GuiContainerBase guiBase,
      FluidTank fluidTank,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight,
      int overlayColor,
      BlockPos blockPos
  ) {

    super(guiBase, fluidTank, elementX, elementY, elementWidth, elementHeight);
    this.overlayColor = overlayColor;
    this.blockPos = blockPos;
  }

  @Override
  public void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    FluidStack fluid = this.fluidTank.getFluid();

    if (fluid != null) {
      int color = fluid.getFluid().getColor();
      GlStateManager.color(
          ((color >> 16) & 0xFF) / 255f,
          ((color >> 8) & 0xFF) / 255f,
          (color & 0xFF) / 255f,
          ((color >> 24) & 0xFF) / 255f
      );
    }

    super.drawBackgroundLayer(partialTicks, mouseX, mouseY);

    if (fluid != null) {
      GlStateManager.color(1, 1, 1, 1);
    }
  }

  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    if (mouseButton == 0 && GuiScreen.isShiftKeyDown()) {
      ModuleWorktables.PACKET_SERVICE.sendToServer(new SPacketWorktableTankDestroyFluid(this.blockPos));
    }
  }

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

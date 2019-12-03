package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.network.CSPacketAutomatorFluidDestroy;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementFluidTankHorizontal;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementClickable;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipExtendedProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class GuiElementFluidTank
    extends GuiElementFluidTankHorizontal
    implements IGuiElementTooltipExtendedProvider,
    IGuiElementClickable {

  private final BlockPos tilePos;
  private final int fluidIndex;
  private final AutomatorGuiContainer guiContainer;
  private final TileAutomator.FluidHandler fluidHandler;

  public GuiElementFluidTank(
      BlockPos tilePos,
      int fluidIndex,
      AutomatorGuiContainer guiBase,
      TileAutomator.FluidHandler fluidHandler,
      int elementX, int elementY,
      int elementWidth, int elementHeight
  ) {

    super(guiBase, fluidHandler, elementX, elementY, elementWidth, elementHeight);
    this.tilePos = tilePos;
    this.fluidIndex = fluidIndex;
    this.guiContainer = guiBase;
    this.fluidHandler = fluidHandler;
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    return (this.guiContainer.getContainerState() == AutomatorContainer.EnumState.Fluid);
  }

  @Override
  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    if (mouseButton == 0 && GuiScreen.isShiftKeyDown()
        && (this.fluidHandler.getFluid() != null
        || this.fluidHandler.getMemoryStack() != null)) {
      WorldClient world = Minecraft.getMinecraft().world;
      world.playSound(this.tilePos, SoundEvents.ITEM_BUCKET_EMPTY,
          SoundCategory.BLOCKS, 1, 1, false);
      ModuleAutomator.PACKET_SERVICE.sendToServer(
          new CSPacketAutomatorFluidDestroy(this.tilePos, this.fluidIndex));
    }
  }

  @Override
  public List<String> tooltipTextGet(List<String> tooltip) {

    FluidStack fluidStack = this.fluidTank.getFluid();

    if (fluidStack == null || this.fluidTank.getFluidAmount() == 0) {
      tooltip.add(I18n.format("gui.artisanworktables.tooltip.fluid.empty"));
      tooltip.add("" + TextFormatting.GRAY + "0 / "
          + this.fluidTank.getCapacity() + " mB");

    } else {
      Fluid fluid = fluidStack.getFluid();
      tooltip.add(fluid.getLocalizedName(fluidStack));
      tooltip.add("" + TextFormatting.GRAY + this.fluidTank.getFluidAmount()
          + " / " + this.fluidTank.getCapacity() + " mB");
    }

    return tooltip;
  }

  @Override
  public List<String> tooltipTextExtendedGet(List<String> tooltip) {

    FluidStack fluidStack = this.fluidHandler.getMemoryStack();

    if (fluidStack != null && this.fluidHandler.isLocked()) {
      Fluid fluid = fluidStack.getFluid();
      tooltip.add(TextFormatting.DARK_RED + "Locked: "
          + fluid.getLocalizedName(fluidStack));

    } else if (!this.fluidHandler.isLocked()) {
      tooltip.add(TextFormatting.DARK_GREEN + "Unlocked");

    } else if (fluidStack == null) {
      tooltip.add(TextFormatting.DARK_RED + "Locked: "
          + I18n.format("gui.artisanworktables.tooltip.fluid.empty"));
    }

    tooltip.add(I18n.format(
        "gui.artisanworktables.tooltip.fluid.destroy",
        TextFormatting.DARK_GRAY,
        TextFormatting.DARK_GRAY
    ));

    return tooltip;
  }
}

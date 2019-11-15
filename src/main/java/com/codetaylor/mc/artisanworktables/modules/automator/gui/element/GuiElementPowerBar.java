package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.lib.IIntSupplier;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class GuiElementPowerBar
    extends GuiElementTextureRectangle
    implements IGuiElementTooltipProvider {

  private final IIntSupplier amountSupplier;
  private final IIntSupplier capacitySupplier;
  private final AutomatorGuiContainer guiContainer;

  public GuiElementPowerBar(
      IIntSupplier amountSupplier,
      IIntSupplier capacitySupplier,
      AutomatorGuiContainer guiBase,
      Texture texture,
      int elementX, int elementY,
      int elementWidth, int elementHeight
  ) {

    super(guiBase, new Texture[]{texture}, elementX, elementY, elementWidth, elementHeight);
    this.amountSupplier = amountSupplier;
    this.capacitySupplier = capacitySupplier;
    this.guiContainer = guiBase;
  }

  @Override
  protected int elementWidthModifiedGet() {

    float percentWidth = this.amountSupplier.get() / (float) this.capacitySupplier.get();

    if (MathHelper.epsilonEquals(percentWidth, 0)) {
      return 0;
    }

    return (int) Math.max(this.elementWidth * percentWidth, 2);
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    return (this.guiContainer.getContainerState() == AutomatorContainer.EnumState.Gear);
  }

  @Override
  public List<String> tooltipTextGet(List<String> tooltip) {

    tooltip.add(I18n.format(
        "gui.artisanworktables.tooltip.automator.power",
        this.amountSupplier.get(),
        this.capacitySupplier.get()
    ));
    return tooltip;
  }
}

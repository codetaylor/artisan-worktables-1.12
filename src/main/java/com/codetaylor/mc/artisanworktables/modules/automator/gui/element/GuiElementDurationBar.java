package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.lib.IFloatSupplier;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import net.minecraft.util.math.MathHelper;

public class GuiElementDurationBar
    extends GuiElementTextureRectangle {

  private final IFloatSupplier progressSupplier;
  private final AutomatorGuiContainer guiContainer;

  public GuiElementDurationBar(
      IFloatSupplier progressSupplier,
      AutomatorGuiContainer guiBase,
      Texture texture,
      int elementX, int elementY,
      int elementWidth, int elementHeight
  ) {

    super(guiBase, new Texture[]{texture}, elementX, elementY, elementWidth, elementHeight);
    this.progressSupplier = progressSupplier;
    this.guiContainer = guiBase;
  }

  @Override
  protected int elementWidthModifiedGet() {

    float percentWidth = this.progressSupplier.get();

    if (MathHelper.epsilonEquals(percentWidth, 0)) {
      return 0;
    }

//    return (int) Math.max(this.elementWidth * percentWidth, 2);
    return (int) (this.elementWidth * percentWidth);
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    return (this.guiContainer.getContainerState() == AutomatorContainer.EnumState.Gear);
  }
}

package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.element.GuiElementFluidTankLarge;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.element.GuiElementMageEffect;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityFluidBase;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTitle;
import net.minecraft.util.ResourceLocation;
import yalter.mousetweaks.api.MouseTweaksDisableWheelTweak;

@MouseTweaksDisableWheelTweak
public class GuiContainerWorkshop
    extends GuiContainerBase {

  public GuiContainerWorkshop(
      Container container,
      ResourceLocation backgroundTexture,
      String titleKey,
      int textShadowColor,
      TileEntityBase tileEntity,
      int width,
      int height
  ) {

    super(container, backgroundTexture, titleKey, textShadowColor, tileEntity, width, height);
  }

  @Override
  protected void addInventoryTitleElement() {

    this.guiContainerElementAdd(new GuiElementTitle(
        this,
        "container.inventory",
        8 + 18,
        this.ySize - 93
    ));
  }

  @Override
  protected void addMageEffectElement(Container container) {

    this.guiContainerElementAdd(new GuiElementMageEffect(
        this,
        container,
        115,
        35
    ));
  }

  @Override
  protected void addFluidTankElement() {

    this.guiContainerElementAdd(new GuiElementFluidTankLarge(
        this,
        ((TileEntityFluidBase) this.tileEntity).getTank(),
        this.tileEntity.getPos(),
        this.textShadowColor,
        8,
        17
    ));
  }
}

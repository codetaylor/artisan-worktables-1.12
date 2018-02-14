package com.codetaylor.mc.artisanworktables.modules.worktables.tile.workshop;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerWorkshop;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntitySecondaryInputBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityWorkshop
    extends TileEntitySecondaryInputBase {

  public TileEntityWorkshop() {
    // serialization
  }

  public TileEntityWorkshop(
      EnumType type
  ) {

    super(5, 5, ModuleWorktablesConfig.FLUID_CAPACITY_WORKSHOP.get(type.getName()), 11, type);
  }

  @Override
  protected ResourceLocation getGuiBackgroundTexture() {

    return new ResourceLocation(
        ModuleWorktables.MOD_ID,
        String.format(ModuleWorktables.Textures.WORKSHOP_GUI, this.type.getName())
    );
  }

  @Override
  protected String getTableTitleKey() {

    return String.format(ModuleWorktables.Lang.WORKSHOP_TITLE, this.getWorktableName());
  }

  @Override
  protected int getToolSlotCount() {

    return 4;
  }

  @Override
  public EnumTier getTier() {

    return EnumTier.WORKSHOP;
  }

  @Override
  public GuiContainerBase getGuiContainer(
      InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos
  ) {

    return new GuiContainerWorkshop(
        this.getContainer(inventoryPlayer, world, state, pos),
        this.getGuiBackgroundTexture(),
        this.getTableTitleKey(),
        this.getGuiTextShadowColor(),
        this,
        212,
        225
    );
  }
}

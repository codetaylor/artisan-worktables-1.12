package com.codetaylor.mc.artisanworktables.modules.worktables.tile.workstation;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerWorkstation;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntitySecondaryInputBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWorkstation
    extends TileEntitySecondaryInputBase {

  public TileEntityWorkstation() {
    // serialization
  }

  public TileEntityWorkstation(EnumType type) {

    super(3, 3, ModuleWorktablesConfig.FLUID_CAPACITY_WORKSTATION.get(type.getName()), 9, type);
  }

  @Override
  protected ResourceLocation getGuiBackgroundTexture() {

    return new ResourceLocation(
        ModuleWorktables.MOD_ID,
        String.format(ModuleWorktables.Textures.WORKSTATION_GUI, this.type.getName())
    );
  }

  @Override
  protected int getToolSlotCount() {

    return 2;
  }

  @Override
  protected String getTableTitleKey() {

    return String.format(ModuleWorktables.Lang.WORKSTATION_TITLE, this.getWorktableName());
  }

  @Override
  @SideOnly(Side.CLIENT)
  public GuiContainerBase getGuiContainer(
      InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos
  ) {

    return new GuiContainerWorkstation(
        this.getContainer(inventoryPlayer, world, state, pos),
        this.getGuiBackgroundTexture(),
        this.getTableTitleKey(),
        this.getGuiTextShadowColor(),
        this,
        176,
        189
    );
  }

  @Override
  public EnumTier getTier() {

    return EnumTier.WORKSTATION;
  }
}

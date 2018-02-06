package com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerWorktable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityWorktableBase
    extends TileEntityTypedBase {

  public TileEntityWorktableBase(EnumType type) {

    super(3, 3, ModuleWorktablesConfig.FLUID_CAPACITY_WORKTABLE.get(type.getName()), type);
  }

  @Override
  protected ResourceLocation getGuiBackgroundTexture() {

    return new ResourceLocation(
        ModuleWorktables.MOD_ID,
        String.format(ModuleWorktables.Textures.WORKTABLE_GUI, this.type.getName())
    );
  }

  @Override
  protected int getToolSlotCount() {

    return 1;
  }

  @Override
  protected String getTableTitleKey() {

    return String.format(ModuleWorktables.Lang.WORKTABLE_TITLE, this.getWorktableName());
  }

  @Override
  @SideOnly(Side.CLIENT)
  public GuiContainerBase getGuiContainer(
      InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos
  ) {

    return new GuiContainerWorktable(
        this.getContainer(inventoryPlayer, world, state, pos),
        this.getGuiBackgroundTexture(),
        this.getTableTitleKey(),
        this.getGuiTextShadowColor(),
        this,
        176,
        166
    );
  }

}

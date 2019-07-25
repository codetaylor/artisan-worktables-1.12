package com.codetaylor.mc.artisanworktables.modules.worktables.tile.worktable;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.CraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWorktable
    extends TileEntityBase {

  @SuppressWarnings("unused")
  public TileEntityWorktable() {
    // serialization
  }

  public TileEntityWorktable(EnumType type) {

    super(type);
  }

  @Override
  protected ResourceLocation getGuiBackgroundTexture() {

    return new ResourceLocation(
        ModuleWorktables.MOD_ID,
        String.format(ModuleWorktables.Textures.WORKTABLE_GUI, this.getType().getName())
    );
  }

  @Override
  protected String getTableTitleKey() {

    return String.format(ModuleWorktables.Lang.WORKTABLE_TITLE, this.getWorktableName());
  }

  @Override
  public EnumTier getTier() {

    return EnumTier.WORKTABLE;
  }

  @Override
  protected CraftingMatrixStackHandler createCraftingMatrixHandler() {

    return new CraftingMatrixStackHandler(3, 3);
  }

  @Override
  protected ObservableStackHandler createSecondaryOutputHandler() {

    return new ObservableStackHandler(3);
  }

  @Override
  protected int getFluidTankCapacity(EnumType type) {

    return ModuleWorktablesConfig.FLUID_CAPACITY_WORKTABLE.get(type.getName());
  }

  @Override
  protected ObservableStackHandler createToolHandler() {

    return new ObservableStackHandler(1);
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

  @Override
  public boolean allowTabs() {

    return ModuleWorktablesConfig.ENABLE_TABS_WORKTABLES;
  }
}

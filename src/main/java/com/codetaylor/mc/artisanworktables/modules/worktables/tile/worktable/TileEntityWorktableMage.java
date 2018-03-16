package com.codetaylor.mc.artisanworktables.modules.worktables.tile.worktable;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityMageDelegate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;

public class TileEntityWorktableMage
    extends TileEntityWorktable
    implements ITickable {

  private final TileEntityMageDelegate delegate;

  public TileEntityWorktableMage() {

    super(EnumType.MAGE);
    this.delegate = new TileEntityMageDelegate(this);
  }

  @Override
  public void update() {

    this.delegate.update();
  }

  @Override
  public void onDataPacket(
      NetworkManager manager, SPacketUpdateTileEntity packet
  ) {

    super.onDataPacket(manager, packet);
    this.delegate.onDataPacket(manager, packet);
  }

  public ItemStack getItemStackForTabDisplay(IBlockState state) {

    return this.delegate.getItemStackForTabDisplay(state);
  }
}

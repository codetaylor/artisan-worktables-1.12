package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.modules.worktables.particle.ParticleWorktableMage;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityTypedBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class TileEntityMageDelegate {

  private Random random;
  private boolean activeDirty = true;
  private boolean active;

  private TileEntityTypedBase tileEntity;

  public TileEntityMageDelegate(TileEntityTypedBase tileEntity) {

    this.tileEntity = tileEntity;
    this.random = new Random();
  }

  public void update() {

    if (this.tileEntity.getWorld() == null) {
      return;
    }

    if (this.tileEntity.getWorld().isRemote) { // client

      if (!this.tileEntity.hasTool()) {
        return;
      }

      if (this.random.nextFloat() < 0.5) {
        this.spawnParticles();
      }

    } else { // server

      boolean active = this.tileEntity.hasTool();

      if (this.active != active || this.activeDirty) {
        this.activeDirty = false;
        this.active = active;
        this.tileEntity.notifyBlockUpdate();
      }
    }
  }

  public void onDataPacket(
      NetworkManager manager, SPacketUpdateTileEntity packet
  ) {

    this.tileEntity.notifyBlockUpdate();
  }

  public ItemStack getItemStackForTabDisplay(IBlockState state) {

    Block block = state.getBlock();
    Item item = Item.getItemFromBlock(block);

    if (this.tileEntity.hasTool()) {
      return new ItemStack(item, 1, Short.MAX_VALUE / 2 + block.getMetaFromState(state));

    } else {
      return new ItemStack(item, 1, block.getMetaFromState(state));
    }
  }

  @SideOnly(Side.CLIENT)
  private void spawnParticles() {

    this.tileEntity.getWorld().spawnParticle(
        EnumParticleTypes.PORTAL,
        this.tileEntity.getPos().getX() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
        this.tileEntity.getPos().getY() + 0.5,
        this.tileEntity.getPos().getZ() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
        0,
        this.random.nextFloat(),
        0
    );

    Minecraft.getMinecraft().effectRenderer.addEffect(
        new ParticleWorktableMage(
            this.tileEntity.getWorld(),
            this.tileEntity.getPos().getX() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
            this.tileEntity.getPos().getY() + 1.5,
            this.tileEntity.getPos().getZ() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
            0,
            this.random.nextFloat(),
            0
        )
    );
  }
}

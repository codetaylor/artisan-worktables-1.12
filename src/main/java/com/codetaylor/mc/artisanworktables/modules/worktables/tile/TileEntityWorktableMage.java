package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktableEnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.particle.ParticleWorktableMage;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityWorktableFluidBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

public class TileEntityWorktableMage
    extends TileEntityWorktableFluidBase
    implements ITickable {

  private static final int TEXT_SHADOW_COLOR = new Color(172, 81, 227).getRGB();
  private static final BlockWorktableEnumType TYPE = BlockWorktableEnumType.MAGE;
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      String.format(ModuleWorktables.Textures.WORKTABLE_GUI, "mage")
  );

  private Random random;
  private boolean activeDirty = true;
  private boolean active;

  public TileEntityWorktableMage() {

    super(3, 3);
    this.random = new Random();
  }

  @Override
  protected String getWorktableName() {

    return TYPE.getName();
  }

  @Override
  protected int getWorktableGuiTextShadowColor() {

    return TEXT_SHADOW_COLOR;
  }

  @Override
  protected ResourceLocation getWorktableGuiBackgroundTexture() {

    return BACKGROUND_TEXTURE;
  }

  @Override
  public int getWorktableGuiTabTextureYOffset() {

    return 7;
  }

  @Override
  public void update() {

    if (this.world == null) {
      return;
    }

    if (this.world.isRemote) { // client

      if (this.getToolHandler().getStackInSlot(0).isEmpty()) {
        return;
      }

      if (this.random.nextFloat() < 0.5) {
        this.spawnParticles();
      }

    } else { // server

      boolean active = !this.getToolHandler().getStackInSlot(0).isEmpty();

      if (this.active != active || this.activeDirty) {
        this.activeDirty = false;
        this.active = active;
        this.notifyBlockUpdate();
      }
    }
  }

  @Override
  public void onDataPacket(
      NetworkManager manager, SPacketUpdateTileEntity packet
  ) {

    super.onDataPacket(manager, packet);
    this.notifyBlockUpdate();
  }

  @Override
  public boolean canHandleJEIRecipeTransfer(String name) {

    return TYPE.getName().equals(name);
  }

  public ItemStack getItemStackForTabDisplay(IBlockState state) {

    Block block = state.getBlock();
    Item item = Item.getItemFromBlock(block);

    if (!this.getToolHandler().getStackInSlot(0).isEmpty()) {
      return new ItemStack(item, 1, Short.MAX_VALUE / 2 + block.getMetaFromState(state));

    } else {
      return new ItemStack(item, 1, block.getMetaFromState(state));
    }
  }

  @Override
  protected int getFluidCapacity() {

    return ModuleWorktablesConfig.FLUID_CAPACITY.MAGE;
  }

  @SideOnly(Side.CLIENT)
  private void spawnParticles() {

    this.world.spawnParticle(
        EnumParticleTypes.PORTAL,
        this.pos.getX() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
        this.pos.getY() + 0.5,
        this.pos.getZ() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
        0,
        this.random.nextFloat(),
        0
    );

    Minecraft.getMinecraft().effectRenderer.addEffect(
        new ParticleWorktableMage(
            this.world,
            this.pos.getX() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
            this.pos.getY() + 1.5,
            this.pos.getZ() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
            0,
            this.random.nextFloat(),
            0
        )
    );
  }
}

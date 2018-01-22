package com.codetaylor.mc.artisanworktables.modules.toolbox.tile;

import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolboxConfig;
import com.codetaylor.mc.artisanworktables.modules.toolbox.gui.ContainerToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.gui.GuiContainerToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.athenaeum.tile.IContainer;
import com.codetaylor.mc.athenaeum.tile.IContainerProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TileEntityToolbox
    extends TileEntity
    implements IContainer,
    ITickable,
    IContainerProvider<ContainerToolbox, GuiContainerToolbox> {

  private ToolboxItemStackHandler itemHandler;
  private int numPlayersUsing;
  private int ticksSinceSync = -1;
  private float previousLidAngle;
  private float currentLidAngle;

  public TileEntityToolbox() {

    Predicate<ItemStack> predicate = itemStack -> itemStack.isEmpty()
        || !ModuleToolboxConfig.RESTRICT_TOOLBOX_TO_TOOLS_ONLY
        || WorktableAPI.containsRecipeWithTool(itemStack);

    this.itemHandler = new ToolboxItemStackHandler(predicate, 27) {

      @Override
      protected void validateSlotIndex(int slot) {

        super.validateSlotIndex(slot);
      }
    };
    this.itemHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());
  }

  @Nullable
  @Override
  public <T> T getCapability(
      Capability<T> capability, @Nullable EnumFacing facing
  ) {

    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      //noinspection unchecked
      return (T) this.itemHandler;
    }

    return super.getCapability(capability, facing);
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {

    return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
  }

  public ItemStackHandler getItemHandler() {

    return this.itemHandler;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {

    super.writeToNBT(compound);
    compound.setTag("itemHandler", this.itemHandler.serializeNBT());
    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {

    super.readFromNBT(compound);
    this.itemHandler.deserializeNBT(compound.getCompoundTag("itemHandler"));
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    return new SPacketUpdateTileEntity(this.pos, -1, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {

    this.readFromNBT(packet.getNbtCompound());
  }

  @Override
  public final NBTTagCompound getUpdateTag() {

    return this.writeToNBT(new NBTTagCompound());
  }

  @Override
  public boolean shouldRefresh(
      World world, BlockPos pos, IBlockState oldState, IBlockState newSate
  ) {

    return oldState.getBlock() != newSate.getBlock();
  }

  public boolean isUsableByPlayer(EntityPlayer player) {

    return this.getWorld().getTileEntity(this.getPos()) == this
        && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
  }

  public void openInventory(@Nonnull EntityPlayer player) {

    if (player.isSpectator()) {
      return;
    }

    if (this.world == null) {
      return;
    }

    if (this.numPlayersUsing < 0) {
      this.numPlayersUsing = 0;
    }

    this.numPlayersUsing += 1;
    this.world.addBlockEvent(this.pos, ModuleToolbox.Blocks.TOOLBOX, 1, this.numPlayersUsing);
  }

  public void closeInventory(@Nonnull EntityPlayer player) {

    if (player.isSpectator()) {
      return;
    }

    if (this.world == null) {
      return;
    }

    this.numPlayersUsing -= 1;
    this.world.addBlockEvent(this.pos, ModuleToolbox.Blocks.TOOLBOX, 1, this.numPlayersUsing);
  }

  @Override
  public List<ItemStack> getBlockBreakDrops() {

    List<ItemStack> result = new ArrayList<>();

    int slotCount = this.itemHandler.getSlots();

    for (int i = 0; i < slotCount; i++) {
      ItemStack itemStack = this.itemHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        result.add(itemStack);
      }
    }

    return result;
  }

  @Override
  public ContainerToolbox getContainer(
      InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos
  ) {

    return new ContainerToolbox(inventoryPlayer, this);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public GuiContainerToolbox getGuiContainer(
      InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos
  ) {

    return new GuiContainerToolbox(this.getContainer(inventoryPlayer, world, state, pos));
  }

  @Override
  public boolean receiveClientEvent(int id, int type) {

    if (id == 1) {
      this.numPlayersUsing = type;
    }
    return true;
  }

  @Override
  public void update() {

    if (this.world != null
        && !this.world.isRemote
        && this.numPlayersUsing != 0
        && (this.ticksSinceSync + this.pos.getX() + this.pos.getY() + this.pos.getZ()) % 200 != 0) {

      this.numPlayersUsing = 0;

      float f = 5.0f;

      for (EntityPlayer player : this.world.getEntitiesWithinAABB(
          EntityPlayer.class,
          new AxisAlignedBB(this.pos.getX() - f, this.pos.getY() - f, this.pos.getZ() - f, this
              .pos.getX() + 1 + f, this.pos.getY() + 1 + f, this.pos.getZ() + 1 + f)
      )) {

        if (player.openContainer instanceof ContainerToolbox) {
          this.numPlayersUsing += 1;
        }
      }
    }

    if (this.world != null
        && !this.world.isRemote
        && this.ticksSinceSync < 0) {
      this.world.addBlockEvent(
          this.pos,
          ModuleToolbox.Blocks.TOOLBOX,
          1,
          this.numPlayersUsing << 3
      );
    }

    this.ticksSinceSync += 1;
    this.previousLidAngle = this.currentLidAngle;
    float angle = 0.1f;

    if (this.numPlayersUsing > 0
        && MathHelper.epsilonEquals(this.currentLidAngle, 0.0f)) {
      double x = this.pos.getX() + 0.5;
      double y = this.pos.getY() + 0.5;
      double z = this.pos.getZ() + 0.5;
      //this.world.playSound(null, x, y, z, ModuleStorage.Sounds.STORAGE_LOCKER_OPEN, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f);
    }

    if (this.numPlayersUsing == 0
        && this.currentLidAngle > 0
        || this.numPlayersUsing > 0
        && this.currentLidAngle < 1.0f) {
      float tempAngle = this.currentLidAngle;

      if (this.numPlayersUsing > 0) {
        this.currentLidAngle += angle;

      } else {
        this.currentLidAngle -= angle;
      }

      if (this.currentLidAngle > 1.0f) {
        this.currentLidAngle = 1.0f;
      }

      float maxAngle = 0.5f;

      if (this.currentLidAngle < maxAngle
          && tempAngle >= maxAngle) {
        double x = this.pos.getX() + 0.5;
        double y = this.pos.getY() + 0.5;
        double z = this.pos.getZ() + 0.5;
        //this.world.playSound(null, x, y, z, ModuleStorage.Sounds.STORAGE_LOCKER_CLOSE, SoundCategory.BLOCKS, 1.0f, this.world.rand.nextFloat() * 0.1f + 0.9f);
      }

      if (this.currentLidAngle < 0.0f) {
        this.currentLidAngle = 0.0f;
      }
    }
  }

  public float getPreviousLidAngle() {

    return this.previousLidAngle;
  }

  public float getCurrentLidAngle() {

    return this.currentLidAngle;
  }

}

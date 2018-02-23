package com.codetaylor.mc.artisanworktables.modules.toolbox.tile;

import com.codetaylor.mc.artisanworktables.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolboxConfig;
import com.codetaylor.mc.artisanworktables.modules.toolbox.gui.ContainerToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.gui.GuiContainerToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.ArtisanWorktablesAPI;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.tile.IContainer;
import com.codetaylor.mc.athenaeum.tile.IContainerProvider;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TileEntityToolbox
    extends TileEntity
    implements IContainer,
    IContainerProvider<ContainerToolbox, GuiContainerToolbox> {

  private ToolboxItemStackHandler itemHandler;

  public TileEntityToolbox() {

    Predicate<ItemStack> predicate = itemStack -> itemStack.isEmpty()
        || !this.restrictToToolsOnly()
        || ArtisanWorktablesAPI.containsRecipeWithTool(itemStack);

    this.itemHandler = new ToolboxItemStackHandler(predicate, 27) {

      @Override
      protected void validateSlotIndex(int slot) {

        super.validateSlotIndex(slot);
      }
    };
    this.itemHandler.addObserver((stackHandler, slotIndex) -> this.markDirty());
  }

  protected boolean restrictToToolsOnly() {

    return ModuleToolboxConfig.TOOLBOX.RESTRICT_TO_TOOLS_ONLY;
  }

  public boolean canPlayerUse(EntityPlayer player) {

    return this.getWorld().getTileEntity(this.getPos()) == this
        && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
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

  public void notifyBlockUpdate() {

    BlockHelper.notifyBlockUpdate(this.getWorld(), this.getPos());
  }

  protected String getGuiContainerTitleKey() {

    return ModuleToolbox.Lang.TOOLBOX_TITLE;
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

    return new GuiContainerToolbox(
        this.getContainer(inventoryPlayer, world, state, pos),
        this.getGuiContainerTitleKey(),
        this.getTexture()
    );
  }

  public Texture getTexture() {

    return ReferenceTexture.TEXTURE_TOOLBOX;
  }

  public Texture getTextureSide() {

    return ReferenceTexture.TEXTURE_TOOLBOX_SIDE;
  }

}

package com.codetaylor.mc.artisanworktables.modules.automator.network;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SCPacketAutomatorTabStateChange
    implements IMessage,
    IMessageHandler<SCPacketAutomatorTabStateChange, IMessage> {

  private int stateIndex;

  @SuppressWarnings("unused")
  public SCPacketAutomatorTabStateChange() {
    // serialization
  }

  public SCPacketAutomatorTabStateChange(AutomatorContainer.State state) {

    this.stateIndex = state.getIndex();
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    this.stateIndex = buf.readByte();
  }

  @Override
  public void toBytes(ByteBuf buf) {

    buf.writeByte(this.stateIndex);
  }

  @Override
  public IMessage onMessage(SCPacketAutomatorTabStateChange message, MessageContext ctx) {

    EntityPlayerSP player = Minecraft.getMinecraft().player;
    Container openContainer = player.openContainer;

    if (openContainer instanceof AutomatorContainer) {
      AutomatorContainer container = (AutomatorContainer) openContainer;
      AutomatorContainer.State state = AutomatorContainer.State.fromIndex(message.stateIndex);
      container.setState(state);
    }

    return null;
  }
}

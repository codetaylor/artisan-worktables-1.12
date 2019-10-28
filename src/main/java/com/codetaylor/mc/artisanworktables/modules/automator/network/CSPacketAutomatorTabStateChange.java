package com.codetaylor.mc.artisanworktables.modules.automator.network;

import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CSPacketAutomatorTabStateChange
    implements IMessage,
    IMessageHandler<CSPacketAutomatorTabStateChange, IMessage> {

  private int stateIndex;

  @SuppressWarnings("unused")
  public CSPacketAutomatorTabStateChange() {
    // serialization
  }

  public CSPacketAutomatorTabStateChange(AutomatorContainer.State state) {

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
  public IMessage onMessage(CSPacketAutomatorTabStateChange message, MessageContext ctx) {

    EntityPlayerMP player = ctx.getServerHandler().player;
    Container openContainer = player.openContainer;

    if (openContainer instanceof AutomatorContainer) {
      AutomatorContainer container = (AutomatorContainer) openContainer;
      AutomatorContainer.State state = AutomatorContainer.State.fromIndex(message.stateIndex);

      if (container.setState(state)) {
        ModuleAutomator.PACKET_SERVICE.sendTo(new SCPacketAutomatorTabStateChange(state), player);
      }
    }

    return null;
  }
}

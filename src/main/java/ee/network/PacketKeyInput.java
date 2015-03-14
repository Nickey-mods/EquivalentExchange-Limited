package ee.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import ee.features.EELimited;

public class PacketKeyInput implements IMessage, IMessageHandler<PacketKeyInput,IMessage> {

	private int code;
	public PacketKeyInput(){}
	public PacketKeyInput(int key)
	{
		code = key;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		code = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(code);
	}

	@Override
	public IMessage onMessage(PacketKeyInput message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		if(message.code == 0)
		{
			if(player.inventory.hasItem(EELimited.Phil))
			{
				player.openGui(EELimited.instance,EELimited.CRAFT,player.worldObj,(int)player.posX, (int)player.posY, (int)player.posZ);
			}
		}
		return null;
	}

}

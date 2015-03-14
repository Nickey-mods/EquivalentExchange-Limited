package ee.features;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy
{
	@Override
	public EntityPlayer getEntityPlayerInstance()
	{
		return FMLClientHandler.instance().getClient().thePlayer;
	}

}

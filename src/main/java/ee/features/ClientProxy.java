package ee.features;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import ee.features.entity.EntityLavaProjectile;
import ee.features.entity.EntityMobRandomizer;
import ee.features.entity.EntityWaterProjectile;

public class ClientProxy extends CommonProxy
{
	@Override
	public EntityPlayer getEntityPlayerInstance()
	{
		return FMLClientHandler.instance().getClient().thePlayer;
	}
	public void registerRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityLavaProjectile.class,new RenderSnowball(EELimited.LavaOrb));
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterProjectile.class,new RenderSnowball(EELimited.WaterOrb));
		RenderingRegistry.registerEntityRenderingHandler(EntityMobRandomizer.class,new RenderSnowball(EELimited.Randomizer));
	}

}

package ee.features.items;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemShears;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ee.features.EELimited;
import ee.features.EEProxy;
import ee.features.NameRegistry;

public class ItemDMShears extends ItemShears {
	public ItemDMShears()
	{
		super();
		String name = NameRegistry.DMShears;
		GameRegistry.registerItem(this,name);
		this.setUnlocalizedName(name).setCreativeTab(EELimited.TabEE).setMaxDamage(0);
		EEProxy.mc.getRenderItem().getItemModelMesher().register(this, 0, new ModelResourceLocation("ee:"+name,"inventory"));
	}
}
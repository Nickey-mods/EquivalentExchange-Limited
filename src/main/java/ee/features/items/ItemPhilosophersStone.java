package ee.features.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ee.features.EELimited;
import ee.features.NameRegistry;

public class ItemPhilosophersStone extends ItemEEFunctional {
	public ItemPhilosophersStone()
    {
        super(NameRegistry.Philo);
    }
	public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer p)
    {
		if(!p.isSneaking())
		{
			p.openGui(EELimited.instance,EELimited.CRAFT,p.worldObj,(int)p.posX, (int)p.posY, (int)p.posZ);
			return var1;
		}
		return new ItemStack(EELimited.PhilTool);
    }
	public void showWorkbench(EntityPlayer p)
	{
		if(!p.worldObj.isRemote)
		{
			
		}
	}
}

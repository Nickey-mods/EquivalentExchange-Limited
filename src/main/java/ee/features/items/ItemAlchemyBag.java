package ee.features.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ee.features.EELimited;
import ee.features.NameRegistry;
import ee.gui.BagData;

public class ItemAlchemyBag extends ItemEEFunctional {

	BagData data;
	private IIcon[] icons;

	public ItemAlchemyBag() {
		super(NameRegistry.Bag);
		setHasSubtypes(true).setContainerItem(null).setMaxDamage(0);
	}
	@Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
    {
		player.openGui(EELimited.instance,EELimited.ALCH_BAG_ID, world,player.chunkCoordX,player.chunkCoordY,player.chunkCoordZ);
		return item;
    }
	@Override
	public void onUpdate(ItemStack item, World world, Entity entity, int par4, boolean par5)
	{
		if(entity instanceof EntityPlayer && par5)
		{
			EntityPlayer p = (EntityPlayer)entity;
			if(!world.isRemote)
			{
				data = getData(item,world);
				data.onUpdate(world, p);
				data.markDirty();
			}
		}
	}
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs cTab, List list)
	{
		for (int i = 0; i < 16; ++i)
			list.add(new ItemStack(item, 1, i));
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1)
	{
		return icons[MathHelper.clamp_int(par1, 0, 15)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		icons = new IIcon[16];

		for (int i = 0; i < 16; i++)
		{
			icons[i] = register.registerIcon("ee:AlchBag_"+i);
		}
	}
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y,int z, int par7, float par8, float par9, float par10)
    {
		player.openGui(EELimited.instance,EELimited.ALCH_BAG_ID, world,player.chunkCoordX,player.chunkCoordY,player.chunkCoordZ);
		return true;
    }
	public static BagData getBagData(ItemStack item, World world)
	{
		BagData data = null;
		if(item != null && item.getItem() instanceof ItemAlchemyBag)
		{
			data = ((ItemAlchemyBag)item.getItem()).getData(item, world);
		}
		return data;
	}
	public BagData getData(ItemStack var1, World var2)
	{
		String itemName = "AlchBag";
		int itemDamage = MathHelper.clamp_int(var1.getItemDamage(),0,15);
		String var3 = String.format("%s_%s", itemName, itemDamage);
		BagData var4 = (BagData)var2.loadItemData(BagData.class, var3);

		if (var4 == null)
		{
			var4 = new BagData(var3);
			var4.markDirty();
			var2.setItemData(var3, var4);
		}

		return var4;
	}
}

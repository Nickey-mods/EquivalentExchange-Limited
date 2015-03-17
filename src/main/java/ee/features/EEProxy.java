package ee.features;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ee.features.items.ItemKleinStar;

public class EEProxy
{
	public static final int MAXWORLDHEIGHT = 256;
    private static boolean initialized;
    public static Minecraft mc;
    private static EELimited ee;
    private static List<Class> peacefuls = new ArrayList<Class>();
	private static List<Class> mobs = new ArrayList<Class>();
	private static Map<Item,Integer> EMCMap = new HashMap<Item,Integer>();

    public static void Init(Minecraft var0, EELimited var1)
    {
        if (!initialized)
        {
            initialized = true;
        }
        loadEntityLists();
        registerFuels();
        mc = var0;
        ee = var1;
    }
    @SideOnly(Side.CLIENT)
    public static EntityClientPlayerMP getPlayer()
    {
        return FMLClientHandler.instance().getClient().thePlayer;
    }
    public static World getWorld()
    {
    	return getPlayer().worldObj;
    }
    public static void chatToPlayer(EntityPlayer p,String message)
    {
    	if(p.worldObj.isRemote)
    	{
    		return;
    	}
    	p.addChatMessage(new ChatComponentText(message));
    }
    public static int getEMC(ItemStack is)
    {
    	if(is.hasTagCompound())
    	{
    		return is.getTagCompound().getInteger("EMC");
    	}
    	return 0;
    }
    public static int getMaxEMC(ItemStack is)
    {
    	if(is.hasTagCompound())
    	{
    		return is.getTagCompound().getInteger("MaxEMC");
    	}
    	return 0;
    }
    public static void setEMC(ItemStack is,int EMC)
    {
    	if(is.hasTagCompound())
    	{
    		is.getTagCompound().setInteger("EMC",EMC);
    	}
    }
    public static int getItemCount(InventoryPlayer inv,Item item)
    {
    	int ret = 0;
    	for(int i = 0;i < inv.getSizeInventory();i++)
    	{
    		ItemStack is = inv.getStackInSlot(i);
    		if(is != null && is.getItem() == item)
    		{
    			ret += is.stackSize;
    		}
    	}
    	return ret;
    }
    public static void decrItem(InventoryPlayer inv,Item item,int amount)
    {
    	for(int i = 0;i < amount;i++)
    	{
    		inv.consumeInventoryItem(item);
    	}
    }
    public static boolean UseResource(EntityPlayer player,int amount)
    {
    	if(EELimited.disableResource||player == null)
    	{
    		return true;
    	}
    	if(player.capabilities.isCreativeMode)
    	{
    		return true;
    	}
    	Item item = null;
    	InventoryPlayer inv = player.inventory;
    	World w = player.worldObj;
    	for(Map.Entry<Item,Integer> entry : EMCMap.entrySet())
    	{
    		Item i = entry.getKey();
    		int in = entry.getValue();
    		if(amount % in == 0&&getItemCount(inv,i) >= (amount / in))
    		{
    			decrItem(inv,i,amount / in);
    			return true;
    		}
    	}
    	for(int i = 0;i < inv.getSizeInventory();i++)
    	{
    		ItemStack is = inv.getStackInSlot(i);
    		if(is != null && is.getItem() instanceof ItemKleinStar)
    		{
    			if(!is.hasTagCompound())
    			{
    				continue;
    			}
    			int stored = getEMC(is);
    			ItemStack is2 = is.copy();
    			if(stored >= amount)
    			{
    				setEMC(is2,stored - amount);
    				inv.setInventorySlotContents(i, is2);
    				inv.markDirty();
    				return true;
    			}
    			else
    			{
    				continue;
    			}
    		}
    	}
    	//Do something
    	return false;
    }
    public static void setEntityImmuneToFire(Entity target,boolean flag)
    {
    	Class clas = Entity.class;
    	try {
			Field field = clas.getDeclaredField("isImmuneToFire");
			field.setAccessible(true);
			field.setBoolean(target,true);
			field.setAccessible(false);
		} catch (Exception e)
    	{
		}
    }
    public static void setPlayerSpeed(EntityPlayer target,float value)
    {
    	Class clas = PlayerCapabilities.class;
    	try {
			Field field = clas.getDeclaredField("walkSpeed");
			field.setAccessible(true);
			field.setFloat(target.capabilities,value);
			field.setAccessible(false);
		} catch (Exception e)
    	{
		}
    }
    public static ForgeDirection getSide(EntityPlayer p, int x, int y, int z)
    {
    	EntityLivingBase base;
        return ForgeDirection.getOrientation(determineOrientation(p.worldObj, x, y, z, p));
    }
    public static int determineOrientation(World par0World, int par1, int par2, int par3, EntityLivingBase par4EntityLivingBase)
    {
        if (MathHelper.abs((float)par4EntityLivingBase.posX - (float)par1) < 2.0F && MathHelper.abs((float)par4EntityLivingBase.posZ - (float)par3) < 2.0F)
        {
            double d0 = par4EntityLivingBase.posY + 1.82D - (double)par4EntityLivingBase.yOffset;

            if (d0 - (double)par2 > 2.0D)
            {
                return 1;
            }

            if ((double)par2 - d0 > 0.0D)
            {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double)(par4EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }
    public static void registerFuelEMC(Item item,int value)
    {
    	if(!EMCMap.containsKey(item))
    	{
    		EMCMap.put(item, value);
    	}
    }
    public static void registerFuelEMC(Block block,int value)
    {
    	Item item = Item.getItemFromBlock(block);
    	if(!EMCMap.containsKey(item))
    	{
    		EMCMap.put(item, value);
    	}
    }

    public static boolean isClient(World var0)
    {
        return var0.isRemote;
    }

    public static boolean isServer()
    {
        return false;
    }

    public static Object getTileEntity(IBlockAccess var0, int var1, int var2, int var3, Class var4)
    {
        if (var2 < 0)
        {
            return null;
        }
        else
        {
            TileEntity var5 = var0.getTileEntity(var1, var2, var3);
            return !var4.isInstance(var5) ? null : var5;
        }
    }

    public static boolean isEntityFireImmune(Entity var0)
    {
        return var0.isImmuneToFire();
    }

    public static int getEntityHealth(EntityLivingBase var0)
    {
        return (int)var0.getHealth();
    }

    public static FoodStats getFoodStats(EntityPlayer var0)
    {
        return var0.getFoodStats();
    }

    public static WorldInfo getWorldInfo(World var0)
    {
        return var0.getWorldInfo();
    }

    public static int getMaxStackSize(Item var0)
    {
        return var0.getItemStackLimit(new ItemStack(var0));
    }

    public static int blockDamageDropped(Block var0, int var1)
    {
        return var0.damageDropped(var1);
    }

    public static void dropBlockAsItemStack(Block var0, EntityLiving var1, int var2, int var3, int var4, ItemStack var5)
    {
        var0.dropBlockAsItem(var1.worldObj, var2, var3, var4, var5.getItemDamage(), 0);
    }

    public static boolean isJumping(EntityPlayer var0)
    {
        return mc.gameSettings.keyBindJump.isPressed();
    }

    public static boolean isFuel(Object var0,int damage)
    {
        return isFuel(var0,damage);
    }

    public static boolean isFuel(int var0, int var1)
    {
        return GameRegistry.getFuelValue(ee.gs(var0, 1, var1)) != 0;
    }

    public static void playSound(String var0, float var1, float var2, float var3, float var4, float var5)
    {
       mc.theWorld.playSound(var1,var2,var3,var0, var4, var5,false);
    }

    public static void playSoundAtPlayer(String var0, EntityPlayer var1, float var2, float var3)
    {
        playSound(var0, (float)var1.posX, (float)var1.posY, (float)var1.posZ, var2, var3);
    }
    public static Entity getRandomEntity(World world, Entity toRandomize)
	{
		Class entClass = toRandomize.getClass();

		if (peacefuls.contains(entClass))
		{
			return getNewEntityInstance((Class) getRandomListEntry(peacefuls, entClass), world);
		}
		else if (mobs.contains(entClass))
		{
			return getNewEntityInstance((Class) getRandomListEntry(mobs, entClass), world);
		}
		else if (world.rand.nextInt(2) == 0)
		{
			return new EntitySlime(world);
		}
		else
		{
			return new EntitySheep(world);
		}
	}

	public static Object getRandomListEntry(List<?> list, Object toExclude)
	{
		Object obj;

		do
		{
			int random = randomIntInRange(list.size() - 1, 0);
			obj = list.get(random);
		}
		while(obj.equals(toExclude));

		return obj;
	}
	public static int randomIntInRange(int max, int min)
	{
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
	public static Entity getNewEntityInstance(Class c, World world)
	{
		try
		{
			Constructor constr = c.getConstructor(World.class);
			Entity ent = (Entity) constr.newInstance(world);

			if (ent instanceof EntitySkeleton)
			{
				if (world.rand.nextInt(2) == 0)
				{
					((EntitySkeleton) ent).setSkeletonType(1);
					ent.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
				}
				else
				{
					ent.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
				}
			}
			else if (ent instanceof EntityPigZombie)
			{
				ent.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
			}

			return ent;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
	private static void loadEntityLists()
	{
		//Peacefuls
		peacefuls.add(EntityCow.class);
		peacefuls.add(EntityMooshroom.class);
		peacefuls.add(EntitySheep.class);
		peacefuls.add(EntityPig.class);
		peacefuls.add(EntityChicken.class);
		peacefuls.add(EntityOcelot.class);
		peacefuls.add(EntityVillager.class);
		peacefuls.add(EntitySquid.class);
		peacefuls.add(EntityHorse.class);

		//Mobs
		mobs.add(EntityZombie.class);
		mobs.add(EntitySkeleton.class);
		mobs.add(EntitySpider.class);
		mobs.add(EntityCaveSpider.class);
		mobs.add(EntityCreeper.class);
		mobs.add(EntityEnderman.class);
		mobs.add(EntitySilverfish.class);
		mobs.add(EntityGhast.class);
		mobs.add(EntityPigZombie.class);
		mobs.add(EntitySlime.class);
		mobs.add(EntityWitch.class);
		mobs.add(EntityBlaze.class);
		mobs.add(EntityFireball.class);
	}
	private static void registerFuels()
	{
		registerFuelEMC(Items.redstone,4);
		registerFuelEMC(Items.glowstone_dust,16);
		registerFuelEMC(Items.gunpowder,48);
	}
}

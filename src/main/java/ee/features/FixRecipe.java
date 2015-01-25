package ee.features;

import gregtech.api.items.GT_MetaGenerated_Tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class FixRecipe extends ShapelessRecipes
{
    /** Is the ItemStack that you get when craft the recipe. */
    private ItemStack recipeOutput;
    private ItemStack GTItem = null;

    /** Is a List of ItemStack that composes the recipe. */
    public List recipeItems;
    /** Is the Enchantment of the tool*/
    public Map<Integer, Integer> enchantment;
    private NBTTagCompound tag;

    public FixRecipe(ItemStack par1ItemStack, List par2List)
    {
    	super(par1ItemStack,par2List);
        this.recipeOutput = par1ItemStack;
        this.recipeItems = par2List;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World)
    {
        ArrayList var3 = new ArrayList(this.recipeItems);

        for (int var4 = 0; var4 < 3; ++var4)
        {
            for (int var5 = 0; var5 < 3; ++var5)
            {
                ItemStack var6 = par1InventoryCrafting.getStackInRowAndColumn(var5, var4);

                if (var6 != null)
                {
                    boolean var7 = false;
                    Iterator var8 = var3.iterator();

                    while (var8.hasNext())
                    {
                        ItemStack var9 = (ItemStack)var8.next();

                        if (var9 == null)
                        {
                            return false;
                        }

                        if (var6.getItem() == var9.getItem() && ((var9.getItemDamage() == 32767 && var6.getItemDamage() != 0) || var6.getItemDamage() == var9.getItemDamage()))
                        {
                        	boolean flag = false;
                            if (var9.getItemDamage() == 32767)
                            {
                            	if(var6.isItemEnchanted())//�A�C�e�����G���`�����g����Ă��邩�ǂ���
                            	{
                            		enchantment = EnchantmentHelper.getEnchantments(var6);//�G���`�����g���擾
                            	}
                            	else
                            	{
                            		enchantment = null;//Null�ɂ��Ă���
                            	}
                            	if(var6.hasTagCompound())//NBTTag�̑��݂��m�F(NBTTag�Ɏ�ނ��i�[����MOD�����邽�� ex.TinCo)
                            	{
                            		tag = var6.getTagCompound();//NBTTag��ۑ�
                            	}
                            	else
                            	{
                            		tag = null;//Null��(ry
                            	}
                            	if(var6.getItem() instanceof GT_MetaGenerated_Tool)
                            	{
                            		flag = true;
                            		GT_MetaGenerated_Tool tool = (GT_MetaGenerated_Tool)var6.getItem();
                            		GTItem = var6.copy();
                            		GTItem.setItemDamage(var6.getItemDamage());
                            		GT_MetaGenerated_Tool.setToolDamage(GTItem, 0);
                            	}
                            	else
                            	{
                            		GTItem = null;
                            	}
                            }
                            var7 = true;
                            var3.remove(var9);
                            if(flag)
                            {
                            	var9 = null;
                            }
                            break;
                        }
                    }

                    if (!var7)
                    {
                        return false;
                    }
                }
            }
        }

        return var3.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inventory)
    {
        ItemStack is = recipeOutput.copy();

        if (enchantment != null)//�G���`�����g���ۑ�����Ă���?
        {
            Set<Integer> keyset = enchantment.keySet();
            Iterator it = keyset.iterator();

            while (it.hasNext())
            {
                int id = (Integer)it.next();
                int level = (Integer)enchantment.get(id);
                is.addEnchantment(Enchantment.enchantmentsList[id], level);
            }
        }
        if(tag != null)//NBTTag���ۑ�����Ă��邩
        {
        	is.setTagCompound(tag);//�^�O���擾�E�ݒ�
        }
        if (!is.getHasSubtypes()&&GTItem == null)
        {
            is.setItemDamage(0);
        }
        if(GTItem != null)
        {
        	for(int i = 0;i < 9;i++)
        	{
        		ItemStack is2 = inventory.getStackInSlot(i);
        		if(is2!=null&&is2.getItem() instanceof GT_MetaGenerated_Tool)
        		{
        			inventory.setInventorySlotContents(i, null);
        		}
        	}
        	return GTItem;
        }
        return is;
    }
}

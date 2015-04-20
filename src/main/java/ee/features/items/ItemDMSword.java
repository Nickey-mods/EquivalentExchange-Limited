package ee.features.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import ee.features.NameRegistry;

public class ItemDMSword extends ItemEETool
{
    public ItemDMSword()
    {
        super(NameRegistry.DMSword,6);
    }

    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        if (getChargeLevel(par1ItemStack) > 0)
        {
            par2EntityLivingBase.attackEntityFrom(DamageSource.causeMobDamage(par3EntityLivingBase), toolDamage * 2);
        }
        return super.hitEntity(par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
    }
}
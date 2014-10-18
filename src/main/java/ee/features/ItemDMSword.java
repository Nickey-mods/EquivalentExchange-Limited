package ee.features;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import cpw.mods.fml.client.FMLClientHandler;

public class ItemDMSword extends ItemEE
{
    public ItemDMSword()
    {
        super(NameRegistry.DMSword,6);
    }

    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        par2EntityLivingBase.setFire(20);
        if (par1ItemStack.getItemDamage() == 1)
        {
            par2EntityLivingBase.attackEntityFrom(DamageSource.causePlayerDamage(EEProxy.getPlayer()), toolDamage * 2);
        }
        return super.hitEntity(par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
    }
}
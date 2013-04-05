package chb.mods.mffs.common;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemManuelBook extends ItemMultitool  {

	public ItemManuelBook(int par1) {
		super(par1, 5);

	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		if(entityplayer.isSneaking())
		{
			int powerleft = this.getForceEnergy(itemstack);
			ItemStack hand = entityplayer.inventory.getCurrentItem();
			hand= new ItemStack(ModularForceFieldSystem.MFFSitemWrench, 1);
			ForceEnergyItems.charge(hand, powerleft,entityplayer);
		return hand;
		}
		
		
		if(world.isRemote)
		entityplayer.openGui(ModularForceFieldSystem.instance, 1, world,0, 0, 0);
		

		return itemstack;
	}
	


}

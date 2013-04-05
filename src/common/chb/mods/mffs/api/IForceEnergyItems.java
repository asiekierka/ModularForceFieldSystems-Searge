package chb.mods.mffs.api;

import chb.mods.mffs.common.TileEntityMachines;
import net.minecraft.src.ItemStack;

public interface IForceEnergyItems {
		
	public int getAvailablePower(ItemStack itemStack);
	
	public int getMaximumPower(ItemStack itemStack);
	
	public boolean consumePower(ItemStack itemStack, int powerAmount,boolean simulation);
	
	public void setAvailablePower(ItemStack itemStack, int amount);

	public int getPowerTransferrate();

	public int getItemDamage(ItemStack stackInSlot);
	
	
}

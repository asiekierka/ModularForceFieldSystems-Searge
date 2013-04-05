/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    Thunderdark - initial implementation
*/

package chb.mods.mffs.common;

import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import chb.mods.mffs.api.IForceEnergyItems;
import chb.mods.mffs.api.IPowerLinkItem;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class ItemForcePowerCrystal extends ForceEnergyItems implements IPowerLinkItem{
	public ItemForcePowerCrystal(int i) {
		super(i);
		setIconIndex(96);
		setMaxStackSize(1);
		setMaxDamage(100);
		
		
	}
	@Override
	public String getTextureFile() {
		return "/chb/mods/mffs/sprites/items.png";
	}
	@Override
	public boolean isRepairable() {
		return false;
	}
	
	@Override
	public boolean isDamageable()
	{
	return true;
	}
	@Override
	public int getPowerTransferrate() {
		return 100000;
	}
	
	@Override
    public int getIconFromDamage(int dmg){
    if (dmg== -1)
     return 112;
    return 112 + ((100-dmg)/20);
    }
	
	@Override
	public int getItemDamage(ItemStack itemStack) {
		
		if(getAvailablePower(itemStack) == 0)
		return -1;
		
		
		return 101-((getAvailablePower(itemStack)*100)/getMaximumPower(itemStack));
		
	}
	@Override
	public int getMaximumPower(ItemStack itemStack){
		return 5000000;
	}

	
    @Override
    public void addInformation(ItemStack itemStack,EntityPlayer player, List info,boolean b)
    {
        String tooltip = String.format( "%d FE/%d FE ",getAvailablePower(itemStack),getMaximumPower(itemStack));
        info.add(tooltip);
    }
    

	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int i, CreativeTabs tabs, List itemList)
	{
		ItemStack charged = new ItemStack(this, 1);
		charged.setItemDamage(1);
		setAvailablePower(charged, getMaximumPower(null));
		itemList.add(charged);
		
		ItemStack empty = new ItemStack(this, 1);
		empty.setItemDamage(100);
		setAvailablePower(empty, 0);
		itemList.add(empty);
		
	}
	@Override
	public boolean isPowersourceItem(ItemStack itemStack) {
		return true;
	}
	
	@Override
	public int getAvailablePower(ItemStack itemStack,
			TileEntityMachines tem,World world) {
		return getAvailablePower(itemStack);
	}
	
	@Override
	public int getMaximumPower(ItemStack itemStack,
			TileEntityMachines tem,World world) {
		return getMaximumPower(itemStack);
	}
	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem,World world) {
		return consumePower(itemStack,powerAmount,simulation);
	}
	@Override
	public int getPowersourceID(ItemStack itemStack,
			TileEntityMachines tem,World world) {
           return -1;
	}
	@Override
	public int getPercentageCapacity(ItemStack itemStack, TileEntityMachines tem,World world) {
		return ((getAvailablePower(itemStack)/1000)*100)/(getMaximumPower(itemStack)/1000);
		
	}

	
}

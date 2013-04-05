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
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public class ItemForcePowerCrystal extends ItemMFFSBase  implements IForceEnergyItems{
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
	public int getforceEnergyTransferMax() {
		return 100000;
	}
	@Override
	public int getItemDamage(ItemStack itemStack) {
		
		if(getForceEnergy(itemStack) == 0)
		return -1;
		
		
		return 101-((getForceEnergy(itemStack)*100)/getMaxForceEnergy());
		
	}
	@Override
	public int getMaxForceEnergy() {
		return 5000000;
	}
	@Override
	public void setForceEnergy(ItemStack itemStack, int ForceEnergy) {
	       
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
	       nbtTagCompound.setInteger("ForceEnergy", ForceEnergy);
		
	}
	
    @Override
    public void addInformation(ItemStack itemStack,EntityPlayer player, List info,boolean b)
    {
        String tooltip = String.format( "%d FE/%d FE ",getForceEnergy(itemStack),getMaxForceEnergy());
        info.add(tooltip);
    }
    
	@Override
	public int getForceEnergy(ItemStack itemstack) {
    	
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
    	if(nbtTagCompound != null)
    	{
    		return nbtTagCompound.getInteger("ForceEnergy");
    	}
       return 0;
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(int i, CreativeTabs tabs, List itemList)
	{
		ItemStack charged = new ItemStack(this, 1);
		charged.setItemDamage(1);
		setForceEnergy(charged, getMaxForceEnergy());
		itemList.add(charged);
		
		ItemStack empty = new ItemStack(this, 1);
		empty.setItemDamage(100);
		setForceEnergy(empty, 0);
		itemList.add(empty);
		
		
	}
	
	
}

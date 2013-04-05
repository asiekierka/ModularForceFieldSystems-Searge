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
    
    Thunderdark
    Matchlighter

 */

package chb.mods.mffs.common;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import chb.mods.mffs.api.IForceEnergyItems;

public class ForceEnergyItems {
	
	public static boolean use(ItemStack itemStack, int amount,boolean trial,EntityPlayer entityplayer)
	{
		if (itemStack.getItem() instanceof IForceEnergyItems) {
			
			IForceEnergyItems ForceEnergyItem = (IForceEnergyItems) itemStack.getItem();
			if(ForceEnergyItem.getForceEnergy(itemStack) >= amount)
			{
				if(trial)
				{
					ForceEnergyItem.setForceEnergy(itemStack, ForceEnergyItem.getForceEnergy(itemStack) - amount);
				}
				return true;
			}
			
		}
		
		return false;
	}
	
	
	public static void charge(ItemStack itemStack, int amount,EntityPlayer entityplayer)
	{
		if (itemStack.getItem() instanceof IForceEnergyItems) {
			
			IForceEnergyItems ForceEnergyItem = (IForceEnergyItems) itemStack.getItem();
			ForceEnergyItem.setForceEnergy(itemStack, amount);
		}

	}

}

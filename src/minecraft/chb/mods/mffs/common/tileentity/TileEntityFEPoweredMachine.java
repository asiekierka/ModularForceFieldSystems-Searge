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
    
    Matchlighter
    Thunderdark 

 */

package chb.mods.mffs.common.tileentity;

import net.minecraft.item.ItemStack;
import chb.mods.mffs.api.IPowerLinkItem;


public abstract class TileEntityFEPoweredMachine extends TileEntityMachines {

	public abstract ItemStack getPowerLinkStack();
	public abstract int getPowerlinkSlot();
	
	
	
	public int getPercentageCapacity(){
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()){
			return ((IPowerLinkItem)linkCard.getItem()).getPercentageCapacity(linkCard,this,worldObj);
		}
		return 0;
	}
	
	
	public boolean hasPowerSource(){
		ItemStack linkCard = getPowerLinkStack();
		if (linkCard != null && linkCard.getItem() instanceof IPowerLinkItem)
			return true;
		return false;
	}
	
	public int getAvailablePower(){
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()){
			return ((IPowerLinkItem)linkCard.getItem()).getAvailablePower(linkCard,this,worldObj);
		}
		return 0;
	}
	
	public int getPowerSourceID(){
		
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()){
			return ((IPowerLinkItem)linkCard.getItem()).getPowersourceID(linkCard,this,worldObj);
		}
		return 0;
	}
	
	public int getMaximumPower(){
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()){
			return ((IPowerLinkItem)linkCard.getItem()).getMaximumPower(linkCard,this,worldObj);
		}
		return 0;
	}
	
	public boolean consumePower(int powerAmount,boolean simulation){
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()){
			return ((IPowerLinkItem)linkCard.getItem()).consumePower(linkCard, powerAmount, simulation,this,worldObj);
		}
		return false;
	}
	
	public boolean emitPower(int powerAmount,boolean simulation)
	{
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()){
			return ((IPowerLinkItem)linkCard.getItem()).insertPower(linkCard, powerAmount, simulation, this, worldObj);
		}
		return false;
	}
	
	public boolean isPowersourceItem()
	{
		ItemStack linkCard = getPowerLinkStack();
		if (hasPowerSource()){
			return ((IPowerLinkItem)linkCard.getItem()).isPowersourceItem();
		}
		return false;
	}
	
}

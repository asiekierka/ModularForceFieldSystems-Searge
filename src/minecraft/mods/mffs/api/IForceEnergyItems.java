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

package mods.mffs.api;

import net.minecraft.item.ItemStack;

public interface IForceEnergyItems {

	public int getAvailablePower(ItemStack itemStack);

	public int getMaximumPower(ItemStack itemStack);

	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation);

	public void setAvailablePower(ItemStack itemStack, int amount);

	public int getPowerTransferrate();

	public int getItemDamage(ItemStack stackInSlot);

}

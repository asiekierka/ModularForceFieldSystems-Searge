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

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerAdvSecurityStation extends Container {
	private TileEntityAdvSecurityStation SecStation;
	private EntityPlayer player;
	private boolean rights[]= { false, false, false, false, false,false,false};

	public ContainerAdvSecurityStation(EntityPlayer player,
			TileEntityAdvSecurityStation tileentity) {
		SecStation = tileentity;
		this.player = player;

		
		addSlotToContainer(new SlotHelper(SecStation, 0, -27, 2)); // MasterCard
		addSlotToContainer(new SlotHelper(SecStation, 1, -20, 60)); //  Coder Coder

		int var3;
		int var4;
		
		
		for (var3 = 0; var3 < 9; ++var3) {
			for (var4 = 0; var4 < 4; ++var4) {
				this.addSlotToContainer(new SlotHelper(SecStation, (var4 + var3 * 4)+2,
						136 + var4 * 18, 1 + var3 * 18));
			}
		}


		for (var3 = 0; var3 < 3; ++var3) {
			for ( var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(player.inventory, var4 + var3 * 9 + 9,
						-32 + var4 * 18, 109 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(player.inventory, var3, -32 + var3 * 18, 167));
		}
	}

    public EntityPlayer getPlayer() {
    	return player;
    }

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return SecStation.isUseableByPlayer(entityplayer);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer p,int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize != itemstack.stackSize) {
				slot.onSlotChanged();
			} else {
				return null;
			}
		}
		return itemstack;
	}
	
	
	
	@Override
	public void updateCraftingResults() {
		super.updateCraftingResults();

		for (int i = 0; i < crafters.size(); i++) {
			ICrafting icrafting = (ICrafting) crafters.get(i);


			for(int a = 0; a< 7 ;a++)
			{
			if (rights[a] != SecStation.getRights(a)) {
				if(SecStation.getRights(a))
				{
					icrafting.sendProgressBarUpdate(this, a, 1);
				}else{
					icrafting.sendProgressBarUpdate(this, a, 0);
				}
			}

			}
			
		}
		
	for(int a = 0; a< 7 ;a++)
	{
		rights[a] = SecStation.getRights(a);
	}
	
	}
	
	@Override
	public void updateProgressBar(int i, int j) {
		
		if(j==1){SecStation.setRights(i,true);}
		else{SecStation.setRights(i,false);}
		
	}
	
	
	
	
	
	
	
	
	
}
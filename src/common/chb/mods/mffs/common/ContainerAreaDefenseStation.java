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

public class ContainerAreaDefenseStation extends Container {
	private TileEntityAreaDefenseStation defstation;

    private int capacity;
    private int SwitchTyp;
    private int contratyp;
    private int actionmode;
    private int scanmode;
	private EntityPlayer player;

	public ContainerAreaDefenseStation(EntityPlayer player,
			TileEntityAreaDefenseStation tileentity) {
		capacity = -1;
		SwitchTyp = -1;
		contratyp = -1;
		actionmode = -1;
		scanmode = -1;

		defstation = tileentity;
		this.player = player;

		addSlotToContainer(new SlotHelper(defstation, 0, -27, 2)); //Power Link
		addSlotToContainer(new SlotHelper(defstation, 1, 57, 2)); //Security Link

		addSlotToContainer(new SlotHelper(defstation, 2, -26, 26)); //Distance mod
		addSlotToContainer(new SlotHelper(defstation, 3, -26, 63)); //Distance mod

		int var3;
		int var4;
		
		// illegal items 5+
		for (var3 = 0; var3 < 2; ++var3) {
			for (var4 = 0; var4 < 4; ++var4) {
				this.addSlotToContainer(new SlotHelper(defstation, (var4 + var3 * 4)+5,
						136 + var4 * 18, 1 + var3 * 18));
			}
		}
		
		//itembuffer 15+
		for (var3 = 0; var3 < 5; ++var3) {
			for (var4 = 0; var4 < 4; ++var4) {
				this.addSlotToContainer(new SlotHelper(defstation, (var4 + var3 * 4)+15,
						136 + var4 * 18, 73 + var3 * 18));
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

	@Override
	public void updateCraftingResults() {
		super.updateCraftingResults();

		for (int i = 0; i < crafters.size(); i++) {
			ICrafting icrafting = (ICrafting) crafters.get(i);


            if(capacity != defstation.getCapacity())
            	icrafting.sendProgressBarUpdate(this, 0, defstation.getCapacity());
            
			if (SwitchTyp != defstation.getswitchtyp()) {
				icrafting.sendProgressBarUpdate(this, 1,
						defstation.getswitchtyp());
			}
			if (contratyp != defstation.getcontratyp()) {
				icrafting.sendProgressBarUpdate(this, 2,
						defstation.getcontratyp());
			}
			if (actionmode != defstation.getActionmode()) {
				icrafting.sendProgressBarUpdate(this, 3,
						defstation.getActionmode());
			}
			if (scanmode != defstation.getScanmode()) {
				icrafting.sendProgressBarUpdate(this, 4,
						defstation.getScanmode());
			}
		}
		scanmode = defstation.getScanmode();
		actionmode = defstation.getActionmode();
		contratyp = defstation.getcontratyp();
		capacity = defstation.getCapacity();
		SwitchTyp = defstation.getswitchtyp();
	}

	public void updateProgressBar(int i, int j) {
		switch (i) {

        case 0:
        	defstation.setCapacity(j);
            break;
		case 1:
			defstation.setswitchtyp(j);
			break;
		case 2:
			defstation.setcontratyp(j);
			break;
		case 3:
			defstation.setActionmode(j);
			break;
		case 4:
			defstation.setScanmode(j);
			break;
		}
	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return defstation.isUseableByPlayer(entityplayer);
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
}
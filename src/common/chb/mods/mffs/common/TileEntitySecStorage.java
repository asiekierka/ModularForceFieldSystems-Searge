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

package chb.mods.mffs.common;

import java.util.LinkedList;
import java.util.List;

import chb.mods.mffs.network.INetworkHandlerListener;
import chb.mods.mffs.network.NetworkHandlerClient;
import buildcraft.api.transport.IExtractionHandler;
import buildcraft.api.transport.IPipe;
import buildcraft.api.transport.PipeManager;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntitySecStorage extends TileEntityMachines implements
		ISidedInventory, INetworkHandlerListener {

	private ItemStack inventory[];
	private boolean init;

	public TileEntitySecStorage() {
		inventory = new ItemStack[60];
		init = true;
	
	}

	public void dropplugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a, this);
		}
	}
	
	
	
	public TileEntityAdvSecurityStation getLinkedSecurityStation()
	{
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 0, worldObj);
	}

	
	
	public int getSecStation_ID(){
		TileEntityAdvSecurityStation sec = getLinkedSecurityStation();
		if(sec != null)
			return sec.getSecurtyStation_ID();
		return 0;	
	}
	
	public void updateEntity() {
		if (worldObj.isRemote == false) {

			if (getTicker() >= 20) {
				setTicker((short) 0);

                if(this.getLinkedSecurityStation()!=null && !this.isActive())
                	this.setActive(true);
 
                if(this.getLinkedSecurityStation()==null && this.isActive())
                	this.setActive(false);
				

			}
			setTicker((short) (getTicker() + 1));

		} else {
			if (init) {
				NetworkHandlerClient.requestInitialData(this, true);
				init = false;
			}
		}
	}

	public void removefromgrid() {
		dropplugins();
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < inventory.length) {
				inventory[byte0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}


	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	public String getInvName() {
		return "SecStation";
	}
	

	public int getSizeInventory() {
		return inventory.length;
	}

    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.inventory[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

	public ItemStack decrStackSize(int i, int j) {
		if (inventory[i] != null) {
			if (inventory[i].stackSize <= j) {
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].stackSize == 0) {
				inventory[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}



	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 1;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		if(isActive())
			return 0;
		return 54;
	}


	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerSecStorage(inventoryplayer.player, this);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		switch (Slot) {
		case 0:
			if (!(par1ItemStack.getItem() instanceof ItemCardSecurityLink))
				return false;
			break;
		}

		return true;
	}




	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.add("active");

		return NetworkedFields;
	}

	@Override
	public void onNetworkHandlerUpdate(String field){ 
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public int getSlotStackLimit(int slt) {
		if(slt==0)
			return 1;
		return 64;
	}

}

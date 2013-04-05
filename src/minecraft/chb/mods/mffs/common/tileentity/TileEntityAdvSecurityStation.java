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

package chb.mods.mffs.common.tileentity;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import chb.mods.mffs.common.Linkgrid;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.NBTTagCompoundHelper;
import chb.mods.mffs.common.SecurityRight;
import chb.mods.mffs.common.container.ContainerAdvSecurityStation;
import chb.mods.mffs.common.item.ItemAccessCard;
import chb.mods.mffs.common.item.ItemCardEmpty;
import chb.mods.mffs.common.item.ItemCardPersonalID;
import chb.mods.mffs.common.item.ItemCardPowerLink;
import chb.mods.mffs.common.item.ItemCardSecurityLink;
import chb.mods.mffs.common.multitool.ItemDebugger;
import chb.mods.mffs.network.INetworkHandlerEventListener;
import chb.mods.mffs.network.client.NetworkHandlerClient;
import chb.mods.mffs.network.server.NetworkHandlerServer;

public class TileEntityAdvSecurityStation extends TileEntityMachines {

	private String MainUser;
	private ItemStack inventory[];

	public TileEntityAdvSecurityStation() {

		inventory = new ItemStack[40];
		MainUser = "";

	}
	

	public void dropplugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a);
		}
	}

	public String getMainUser() {
		return MainUser;
	}

	public void setMainUser(String s) {
		if(!MainUser.equals(s)){
		this.MainUser = s;
		NetworkHandlerServer.updateTileEntityField(this, "MainUser");
		}
	}


	public void dropplugins(int slot) {
		if (getStackInSlot(slot) != null) {
			if (getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink
					|| getStackInSlot(slot).getItem() instanceof ItemCardPowerLink
					|| getStackInSlot(slot).getItem() instanceof ItemCardPersonalID) {
				worldObj.spawnEntityInWorld(new EntityItem(worldObj,
						(float) this.xCoord, (float) this.yCoord,
						(float) this.zCoord, new ItemStack(
								ModularForceFieldSystem.MFFSitemcardempty, 1)));
			} else {
				worldObj.spawnEntityInWorld(new EntityItem(worldObj,
						(float) this.xCoord, (float) this.yCoord,
						(float) this.zCoord, this.getStackInSlot(slot)));
			}

			this.setInventorySlotContents(slot, null);
			this.onInventoryChanged();
		}
	}

	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerAdvSecurityStation(inventoryplayer.player, this);
	}


	@Override
	public void invalidate() {
		Linkgrid.getWorldMap(worldObj).getSecStation().remove(getDeviceID());
		super.invalidate();
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

	public void updateEntity() {
		if (worldObj.isRemote == false) {

			if (this.getTicker() == 10) {

			if (!getMainUser().equals("")) {
				if (isActive() != true) {
					setActive(true);
				}
			} else {
				if (isActive() != false) {
					setActive(false);
				}
			}
				checkslots();
				this.setTicker((short) 0);
			}
			this.setTicker((short) (this.getTicker() + 1));
		} 
		
		super.updateEntity();
	}

	public void checkslots() {
		if (getStackInSlot(0) != null) {
			if (getStackInSlot(0).getItem() == ModularForceFieldSystem.MFFSItemIDCard) {
				ItemCardPersonalID Card = (ItemCardPersonalID) getStackInSlot(0)
						.getItem();

				String name = Card.getUsername(getStackInSlot(0));
				
			

				if (!getMainUser().equals(name)) {
					setMainUser(name);
				}

				if (Card.hasRight(getStackInSlot(0), SecurityRight.CSR) != true) {
					Card.setRight(getStackInSlot(0), SecurityRight.CSR, true);
					}
			} else {

				setMainUser("");
			}
		} else {

			setMainUser("");

		}
		
		
		if (getStackInSlot(39) != null && getStackInSlot(38) != null) {
			if (getStackInSlot(38).getItem() instanceof ItemCardEmpty &&
				getStackInSlot(39).getItem() instanceof ItemCardPersonalID){
				
				this.setInventorySlotContents(38, ItemStack.copyItemStack(getStackInSlot(39)));
				
			}	
		}

	}


	public int getSizeInventory() {
		return inventory.length;
	}

	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	public int getInventoryStackLimit() {
		return 1;
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

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	public String getInvName() {
		return "Secstation";
	}


	public boolean RemoteInventory(String username, SecurityRight right) {

		for (int a = 35; a >= 1; a--) {
			if (getStackInSlot(a) != null) {
				if (getStackInSlot(a).getItem() == ModularForceFieldSystem.MFFSItemIDCard) {
					String username_invtory = NBTTagCompoundHelper.getTAGfromItemstack(getStackInSlot(a)).getString("name");
					
					ItemCardPersonalID Card = (ItemCardPersonalID) getStackInSlot(a).getItem();
					
					boolean access = ItemCardPersonalID.hasRight(getStackInSlot(a), right);
					
					if (username_invtory.equals(username)) {
						if (access) {
							return true;
						} else {
							return false;
						}
					}
				}
			}
		}

		return false;
	}
	
	public boolean RemotePlayerInventory(String username,SecurityRight right)
	{
		EntityPlayer player = worldObj.getPlayerEntityByName(username);
		if(player != null){
			List<Slot> slots = player.inventoryContainer.inventorySlots;
			for (Slot slot : slots) {
				ItemStack stack = slot.getStack();
				if (stack != null) {
					if (stack.getItem() instanceof ItemAccessCard) {	
						if(((ItemAccessCard)stack.getItem()).getvalidity(stack)>0){
							if(((ItemAccessCard)stack.getItem()).getlinkID(stack)==getDeviceID()){
							 if(((ItemAccessCard)stack.getItem()).hasRight(stack, right)){
								 
								 if(!((ItemAccessCard)stack.getItem()).getforAreaname(stack).equals(getDeviceName()))
									 ((ItemAccessCard)stack.getItem()).setforArea(stack, this);
								 
								 return true;
							 }
							}
						}else{
							player.sendChatToPlayer("[Security Station] expired validity <Access license>");
							ItemStack Card= new ItemStack(ModularForceFieldSystem.MFFSitemcardempty, 1);
							slot.putStack(Card);
							NetworkHandlerServer.syncClientPlayerinventorySlot(player,slot, Card);
						}

					}
					if (stack.getItem() instanceof ItemDebugger){
						return true;
					}
						
			    }
		    }
		}
		return false;
	}
	
	public boolean isAccessGranted(String username, SecurityRight sr) {

		if (!isActive())
			return true;

		String[] ops = ModularForceFieldSystem.Admin.split(";");
		
		for(int i = 0;i<=ops.length-1;i++){
			if (username.equalsIgnoreCase(ops[i]))
				return true;
		}

		if (this.MainUser.equals(username))
			return true;
		
		if (RemoteInventory(username, sr))
			return true;
		
		if (RemotePlayerInventory(username, sr))
			return true;
		

		return false;
	}

	public ItemStack[] getContents() {
		return inventory;
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 0;
	}


	
	@Override
	public List<String> getFieldsforUpdate() {
		
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.addAll(super.getFieldsforUpdate());
		NetworkedFields.add("MainUser");

		return NetworkedFields;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		
		switch(Slot)
		{
		case 38:
			if (!(par1ItemStack.getItem() instanceof ItemCardEmpty))return false;	
		break;
		case 39:
			if (par1ItemStack.getItem() instanceof ItemAccessCard || !(par1ItemStack.getItem() instanceof ItemCardPersonalID) )return false;
		break;
		
		}

		if (par1ItemStack.getItem() instanceof ItemCardPersonalID || par1ItemStack.getItem() instanceof ItemCardEmpty)
			return true;

		return false;
	}



	@Override
	public void onNetworkHandlerEvent(int key,String value) {
		
		switch(key){
		case 100:
			if (getStackInSlot(1) != null) {
				SecurityRight sr = SecurityRight.rights.get(value);
				if (sr != null && getStackInSlot(1).getItem() instanceof ItemCardPersonalID) {
					ItemCardPersonalID.setRight(getStackInSlot(1), sr, !ItemCardPersonalID.hasRight(getStackInSlot(1), sr));
				}
			}
		break;
		case 101:
			if (getStackInSlot(1) != null) {
				if(getStackInSlot(1).getItem() instanceof ItemAccessCard){
					if(ItemAccessCard.getvalidity(getStackInSlot(1)) <=5){
						this.setInventorySlotContents(1, new ItemStack(ModularForceFieldSystem.MFFSitemcardempty,1));
					}else{
						ItemAccessCard.setvalidity(getStackInSlot(1), ItemAccessCard.getvalidity(getStackInSlot(1))-5);	
					}
				}
			}
		break;
		case 102:
			if (getStackInSlot(1) != null) {
				if (getStackInSlot(1).getItem() instanceof ItemCardEmpty) {
					    setInventorySlotContents(1, new ItemStack(ModularForceFieldSystem.MFFSAccessCard, 1));
					    if (getStackInSlot(1).getItem() instanceof ItemAccessCard){
					    	ItemAccessCard.setforArea(getStackInSlot(1), this);
					    	ItemAccessCard.setvalidity(getStackInSlot(1), 5);	    
					    	ItemAccessCard.setlinkID(getStackInSlot(1), this);	
					    }
					 break;
					}
				if(getStackInSlot(1).getItem() instanceof ItemAccessCard){
			     
						ItemAccessCard.setvalidity(getStackInSlot(1), ItemAccessCard.getvalidity(getStackInSlot(1))+5);	
				}
			}
		break;
		
		}
		super.onNetworkHandlerEvent(key,value);
	}
	
	public ItemStack getModCardStack(){
     if (getStackInSlot(1) != null){
      return getStackInSlot(1);
     }
      return null;
    }


	@Override
	public int getSlotStackLimit(int slt) {
		return 1;
	}


	@Override
	public TileEntityAdvSecurityStation getLinkedSecurityStation() {
		return this;
	}
}

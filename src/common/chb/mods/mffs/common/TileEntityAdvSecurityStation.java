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

import buildcraft.api.transport.IExtractionHandler;
import buildcraft.api.transport.IPipe;
import buildcraft.api.transport.PipeManager;
import chb.mods.mffs.api.security.SecurityRight;
import chb.mods.mffs.network.INetworkHandlerEventListener;
import chb.mods.mffs.network.INetworkHandlerListener;
import chb.mods.mffs.network.NetworkHandlerClient;
import chb.mods.mffs.network.NetworkHandlerServer;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.Container;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityAdvSecurityStation extends TileEntityMachines implements
		INetworkHandlerListener,
		INetworkHandlerEventListener {

	private String stationname;
	private String MainUser;
	private boolean create;
	private int SecurtyStation_ID;
	private ItemStack inventory[];

	public TileEntityAdvSecurityStation() {

		inventory = new ItemStack[40];
		SecurtyStation_ID = 0;
		create = true;
		MainUser = "";
		stationname="No Name Staion";
	}

	
	
	public String getStationname() {
		return stationname;
	}



	public void setStationname(String stationname) {
		this.stationname = stationname;
		NetworkHandlerServer.updateTileEntityField(this, "stationname");
	}



	public void dropplugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a);
		}
	}

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}

	public String getMainUser() {
		return MainUser;
	}

	public void setMainUser(String s) {
		this.MainUser = s;
		NetworkHandlerServer.updateTileEntityField(this, "MainUser");
	}

	public int getSecurtyStation_ID() {
		return SecurtyStation_ID;
	}

	public void setSecurtyStation_ID(int i) {
		this.SecurtyStation_ID = i;
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

	public void addtogrid() {

		if (SecurtyStation_ID == 0) {
			SecurtyStation_ID = Linkgrid.getWorldMap(worldObj).newID(this);
		}
		Linkgrid.getWorldMap(worldObj).getSecStation()
				.put(SecurtyStation_ID, this);

		registerChunkLoading();

	}

	public void removefromgrid() {
		Linkgrid.getWorldMap(worldObj).getSecStation()
				.remove(SecurtyStation_ID);
		dropplugins();
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		SecurtyStation_ID = nbttagcompound.getInteger("Secstation_ID");
		stationname = nbttagcompound.getString("stationname");
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

		nbttagcompound.setInteger("Secstation_ID", SecurtyStation_ID);
		nbttagcompound.setString("stationname", stationname);

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
			if (this.isCreate()) {
				addtogrid();
				setCreate(false);
			}

			if (!getMainUser().equals("")) {
				if (isActive() != true) {
					setActive(true);
				}
			} else {
				if (isActive() != false) {
					setActive(false);
				}
			}

			if (this.getTicker() == 10) {
				checkslots();
				this.setTicker((short) 0);
			}
			this.setTicker((short) (this.getTicker() + 1));
		} else {
			if (SecurtyStation_ID == 0) {
				if (this.getTicker() >= 20 + random.nextInt(20)) {

					NetworkHandlerClient.requestInitialData(this, true);

					this.setTicker((short) 0);
				}

				this.setTicker((short) (this.getTicker() + 1));
			}
		}
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

		for (int a = 39; a >= 1; a--) {
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

	public boolean isAccessGranted(String username, SecurityRight sr) {
		

		if (!isActive())
			return true;

		if (username.equalsIgnoreCase(ModularForceFieldSystem.Admin))
			return true;

		if (this.MainUser.equals(username))
			return true;
		
		if (RemoteInventory(username, sr))
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
	public void onNetworkHandlerUpdate(String field){ 
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.add("active");
		NetworkedFields.add("SecurtyStation_ID");
		NetworkedFields.add("MainUser");
		NetworkedFields.add("stationname");

		return NetworkedFields;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {

		if (par1ItemStack.getItem() instanceof ItemCardPersonalID)
			return true;

		return false;
	}



	@Override
	public void onNetworkHandlerEvent(int key,String value) {
		
		switch(key){
		case 0:
			
			if (getStackInSlot(1) != null) {
				SecurityRight sr = SecurityRight.rights.get(value);
				if (sr != null && getStackInSlot(1).getItem() == ModularForceFieldSystem.MFFSItemIDCard) {
					ItemCardPersonalID.setRight(getStackInSlot(1), sr, !ItemCardPersonalID.hasRight(getStackInSlot(1), sr));

				}
			}
			
		break;
		case 1:
			setStationname("");
		break;
		case 2:
			if(stationname.length()<=20)
			setStationname(getStationname()+value);
		break;
		case 3:
			setStationname(stationname.substring(0, stationname.length()-1));
			
		break;	
		
		}
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

}

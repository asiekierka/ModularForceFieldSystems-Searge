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

import ic2.api.Direction;
import ic2.api.EnergyNet;
import ic2.api.IEnergyAcceptor;
import ic2.api.IEnergySource;
import ic2.api.Items;

import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.Block;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import chb.mods.mffs.network.INetworkHandlerEventListener;
import chb.mods.mffs.network.INetworkHandlerListener;
import chb.mods.mffs.network.NetworkHandlerClient;

public class TileEntityConverter extends TileEntityMachines implements
		INetworkHandlerListener, INetworkHandlerEventListener,ISwitchabel,
		IEnergySource {
	private ItemStack inventory[];
	private boolean create;
	private int Converter_ID;
	private int capacity;
	private boolean linkGenerator;
	private int SwitchTyp;
	private boolean OnOffSwitch;
	private int output;
	private boolean addedToEnergyNet;
	private boolean Industriecraftfound = true;
	private int linkPower;

	public TileEntityConverter() {
		inventory = new ItemStack[4];
		create = true;
		Converter_ID = 0;
		capacity = 0;
		linkGenerator = false;
		SwitchTyp = 0;
		OnOffSwitch = false;
		output = 1;
		addedToEnergyNet = false;
		linkPower = 0;
	}
	
	public int getLinkPower() {
		return linkPower;
	}

	public void setLinkPower(int linkPower) {
		this.linkPower = linkPower;
	}

	public int getOutput() {
		return output;
	}

	public void setOutput(int output) {
		this.output = output;
	}

	public boolean getOnOffSwitch() {
		return OnOffSwitch;
	}

	public void setOnOffSwitch(boolean a) {
		OnOffSwitch = a;
	}

	public int getswitchtyp() {
		return SwitchTyp;
	}

	public void setswitchtyp(int a) {
		SwitchTyp = a;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int Capacity) {
		capacity = Capacity;
	}


	public int getConverter_ID() {
		return Converter_ID;
	}

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}
	
	
	public TileEntityCapacitor getLinkedCapacitor()
	{
		return ItemCardPowerLink.getLinkedCapacitor(this, 0, worldObj);
	}
	

	
	public int getLinkCapacitor_ID(){
		TileEntityCapacitor cap = getLinkedCapacitor();
		if(cap != null)
			return cap.getCapacitor_ID();
		return 0;	
	}
	

	public void updateEntity() {
		if (worldObj.isRemote == false) {
			if (!addedToEnergyNet && Industriecraftfound) {
				try {
					EnergyNet.getForWorld(worldObj).addTileEntity(this);
					addedToEnergyNet = true;
				} catch (Exception ex) {
					Industriecraftfound = false;
				}
			}

			if (this.isCreate() && this.getLinkCapacitor_ID() != 0) {
				addtogrid();
				setCreate(false);
			}

			if (getLinkCapacitor_ID() != 0) {

				TileEntityCapacitor remotecap = getLinkedCapacitor();
				if(remotecap != null)
				{
					setCapacity(remotecap.getCapacity());
					setLinkPower(remotecap.getForcePower());
				}else{
					setCapacity(0);
					setLinkPower(0);
				}
				
			} else {
				setCapacity(0);
				setLinkPower(0);
			}

			boolean powerdirekt = worldObj.isBlockGettingPowered(xCoord,
					yCoord, zCoord);
			boolean powerindrekt = worldObj.isBlockIndirectlyGettingPowered(
					xCoord, yCoord, zCoord);

			if (getswitchtyp() == 0)
				setOnOffSwitch(powerdirekt || powerindrekt);

			if (getOnOffSwitch() &&  getLinkedCapacitor()!= null
					&& !isActive())
				setActive(true);

			if ((!getOnOffSwitch() || getLinkCapacitor_ID()==0) && isActive())
				setActive(false);

			if (isActive())
				Emitpower();

		} else {
			if (Converter_ID == 0) {
				if (this.getTicker() >= 20 + random.nextInt(20)) {
					NetworkHandlerClient.requestInitialData(this, true);

					this.setTicker((short) 0);
				}

				this.setTicker((short) (this.getTicker() + 1));
			}
		}
	}


	public void addtogrid() {
		if (Converter_ID == 0) {
			Converter_ID = Linkgrid.getWorldMap(worldObj).newID(this);
		}
		Linkgrid.getWorldMap(worldObj).getConverter().put(Converter_ID, this);
		registerChunkLoading();
	}

	public void removefromgrid() {
		Linkgrid.getWorldMap(worldObj).getConverter().remove(getConverter_ID());
		dropplugins();
	}

	public void dropplugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a, this);
		}
	}


	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		output = nbttagcompound.getInteger("output");
		Converter_ID = nbttagcompound.getInteger("Converter_ID");
		SwitchTyp = nbttagcompound.getInteger("SwitchTyp");
		OnOffSwitch = nbttagcompound.getBoolean("OnOffSwitch");

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

		nbttagcompound.setInteger("output", output);
		nbttagcompound.setInteger("Converter_ID", Converter_ID);
		nbttagcompound.setInteger("SwitchTyp", SwitchTyp);
		nbttagcompound.setBoolean("OnOffSwitch", OnOffSwitch);

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
		return "Extractor";
	}


	public int getSizeInventory() {
		return inventory.length;
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
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
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 1;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 1;
	}

	@Override
	public void onNetworkHandlerUpdate(String field){ 
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}
	@Override
	public void onNetworkHandlerEvent(String event) {
		
	
		if (Integer.parseInt(event) == 0) {
			if (this.getswitchtyp() == 0) {
				this.setswitchtyp(1);
			} else {
				this.setswitchtyp(0);
			}
		}

		if (!this.isActive()) {
			switch(Integer.parseInt(event)){
			case 1:
				if (output < 2046) {
					output++;
				}
				break;
			case 2:
				if (output > 1) {
					output--;
				}
				break;
			case 3:
				if (output + 10 > 2048) {
					output = 2048;
				} else {
					output += 10;
				}
				break;
			case 4:
				if (output - 10 < 1) {
					output = 1;
				} else {
					output -= 10;
				}
				break;
			case 5:
				if (output + 100 > 2048) {
					output = 2048;
				} else {
					output += 100;
				}
				break;
			case 6:
				if (output - 100 < 1) {
					output = 1;
				} else {
					output -= 100;
				}
				break;
			}
		}
	}

	public void Emitpower() {
		if (Industriecraftfound) {
			
			TileEntityCapacitor remotecap = getLinkedCapacitor();
			if(remotecap != null)
			{
				if (remotecap.getForcePower() >= (ModularForceFieldSystem.ExtractorPassForceEnergyGenerate/4000)* getOutput()) {
				
					int a = EnergyNet.getForWorld(worldObj).emitEnergyFrom(((IEnergySource) (this)), getOutput());
					remotecap.consumForcePower((ModularForceFieldSystem.ExtractorPassForceEnergyGenerate/4000)* (getOutput()-a));
					
				}
			}
						
		}
	}

	@Override
	public void invalidate() {
		if (addedToEnergyNet) {
			EnergyNet.getForWorld(worldObj).removeTileEntity(this);
			addedToEnergyNet = false;
		}

		super.invalidate();
	}

	public boolean isAddedToEnergyNet() {
		return addedToEnergyNet;
	}

	public int getMaxEnergyOutput() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		return receiver instanceof IEnergyAcceptor;
	}

	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.add("active");
		NetworkedFields.add("side");
		NetworkedFields.add("Converter_ID");

		return NetworkedFields;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerConverter(inventoryplayer.player, this);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		switch (Slot) {
		case 0:
			if (!(par1ItemStack.getItem() instanceof ItemCardPowerLink))
				return false;
			break;
		}

		return true;
	}

	@Override
	public int getSlotStackLimit(int Slot){
		return 1;
	}
}

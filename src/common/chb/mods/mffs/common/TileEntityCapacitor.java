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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import chb.mods.mffs.api.IForceEnergyItems;
import chb.mods.mffs.api.IForceEnergyCapacitor;
import chb.mods.mffs.network.INetworkHandlerEventListener;
import chb.mods.mffs.network.INetworkHandlerListener;
import chb.mods.mffs.network.NetworkHandlerClient;
import chb.mods.mffs.network.NetworkHandlerServer;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class TileEntityCapacitor extends TileEntityMachines implements  IForceEnergyCapacitor,
INetworkHandlerEventListener,INetworkHandlerListener,ISwitchabel{
	private ItemStack inventory[];
	private int forcePower;
	private int Capacitor_ID;
	private boolean create;
	private short linketprojektor;
	private int capacity;
	private int SwitchTyp;
	private int Powerlinkmode;
	private boolean OnOffSwitch;
	private int TransmitRange;

	public TileEntityCapacitor() {
		inventory = new ItemStack[5];
		forcePower =0;
		Capacitor_ID = 0;
		linketprojektor = 0;
		TransmitRange = 8;
		create = true;
		capacity = 0;
		SwitchTyp = 0;
		OnOffSwitch = false;
		Powerlinkmode= 0;
	}

	public void setTransmitRange(int transmitRange) {
		TransmitRange = transmitRange;
		NetworkHandlerServer.updateTileEntityField(this, "TransmitRange");
	}
	
	public int getTransmitRange() {

		return TransmitRange;
	}
	
	

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}


	public int getPowerlinkmode() {
		return Powerlinkmode;
	}

	public void setPowerlinkmode(int powerlinkmode) {
		Powerlinkmode = powerlinkmode;
	}

	public boolean getOnOffSwitch() {
		return OnOffSwitch;
	}

	public void setOnOffSwitch(boolean a) {
	   this.OnOffSwitch = a;
	}

	public int getswitchtyp() {
		return SwitchTyp;
	}

	public void setswitchtyp(int a) {
	   this.SwitchTyp = a;
	}

	@Override
	public int getCapacity(){
		return capacity;
	}

	public void setCapacity(int Capacity){
		if(this.capacity != Capacity)
		{
		this.capacity = Capacity;
		NetworkHandlerServer.updateTileEntityField(this, "capacity");
		}
	}

	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerCapacitor(inventoryplayer.player, this);
	}

	public Short getLinketProjektor() {
		return linketprojektor;
	}

	public void setLinketprojektor(Short linketprojektor) {
		if(this.linketprojektor != linketprojektor){
		this.linketprojektor = linketprojektor;
		NetworkHandlerServer.updateTileEntityField(this, "linketprojektor");
		}
	}

	@Override
	public int getForcePower() {
		return forcePower;
	}
	
	
	public void setForcePower(int f) {
		forcePower = f;
	}


	public int getCapacitor_ID() {
		return Capacitor_ID;
	}

	public int getSizeInventory() {
		return inventory.length;
	}
	
	public TileEntityAdvSecurityStation getLinkedSecurityStation()
	{
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 4, worldObj);
	}
		

	
	public int getSecStation_ID(){
		TileEntityAdvSecurityStation sec = getLinkedSecurityStation();
		if(sec != null)
			return sec.getSecurtyStation_ID();
		return 0;	
	}
	
	public TileEntityCapacitor getLinkedCapacitor()
	{
		return ItemCardPowerLink.getLinkedCapacitor(this, 2, worldObj);
	}

	
	public int getLinkCapacitor_ID(){
		TileEntityCapacitor cap = getLinkedCapacitor();
		if(cap != null)
			return cap.getCapacitor_ID();
		return 0;	
	}
	
	
	
	
	@Override
	public int getMaxForcePower() {
				
		
		if (getStackInSlot(0) != null) {
		if (getStackInSlot(0).getItem() == ModularForceFieldSystem.MFFSitemupgradecapcap) {
			return 10000000 + (2000000 * getStackInSlot(0).stackSize);
		}
	}
		
		return 10000000;
	}
	
	

	private void checkslots(boolean init) {
		
		
		if (getStackInSlot(1) != null) {
		if (getStackInSlot(1).getItem() == ModularForceFieldSystem.MFFSitemupgradecaprange) {
			
			setTransmitRange(8 * (getStackInSlot(1).stackSize+1));
			
		}
		}else{
			setTransmitRange(8);
		}

		if (getStackInSlot(2) != null) {
			if (getStackInSlot(2).getItem() instanceof IForceEnergyItems) {
				if(this.getPowerlinkmode()!=3 && this.getPowerlinkmode()!=4)this.setPowerlinkmode(3);
				IForceEnergyItems ForceEnergyItem = (IForceEnergyItems) getStackInSlot(2).getItem();

				switch(this.getPowerlinkmode())
				{
				case 3:
				
				if(ForceEnergyItem.getForceEnergy(getStackInSlot(2)) < ForceEnergyItem.getMaxForceEnergy())
				{
					int maxtransfer = ForceEnergyItem.getforceEnergyTransferMax();
					int freeeamount = ForceEnergyItem.getMaxForceEnergy() - ForceEnergyItem.getForceEnergy(getStackInSlot(2));

					if(this.getForcePower() > 0)
					{
					  if(this.getForcePower() > maxtransfer)
					  {
						    if(freeeamount > maxtransfer)
						    {
						    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))+maxtransfer);
				                this.setForcePower(this.getForcePower() - maxtransfer);
						    }else{
						    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))+freeeamount);
				                this.setForcePower(this.getForcePower() - freeeamount);
						    }
					  }else{
						    if(freeeamount > this.getForcePower())
						    {
						    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))+this.getForcePower());
				                this.setForcePower(this.getForcePower() - this.getForcePower());
						    }else{
						    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))+freeeamount);
				                this.setForcePower(this.getForcePower() - freeeamount);
						    }
					  }

					  getStackInSlot(2).setItemDamage(ForceEnergyItem.getItemDamage(getStackInSlot(2)));
					}
				}
				
				break;
				case 4:
					
					if(ForceEnergyItem.getForceEnergy(getStackInSlot(2)) > 0)
					{
						
						int maxtransfer = ForceEnergyItem.getforceEnergyTransferMax();
						int freeeamount = this.getMaxForcePower() - this.getForcePower();
						int amountleft = ForceEnergyItem.getForceEnergy(getStackInSlot(2));
						

							if(freeeamount >= amountleft)
							{
								if(amountleft >= maxtransfer)
								{
							    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))-maxtransfer);
					                this.setForcePower(this.getForcePower() + maxtransfer);	
								}else{
					
							    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))-amountleft);
					                this.setForcePower(this.getForcePower() + amountleft);
								}
								
						     }else{
						    	
							    	ForceEnergyItem.setForceEnergy(getStackInSlot(2), ForceEnergyItem.getForceEnergy(getStackInSlot(2))-freeeamount);
					                this.setForcePower(this.getForcePower() + freeeamount);
						    	 
				
						     }
							
							getStackInSlot(2).setItemDamage(ForceEnergyItem.getItemDamage(getStackInSlot(2)));
						
					}

				break;
				}
				
				}
					

			if (getStackInSlot(2).getItem() == ModularForceFieldSystem.MFFSitemfc) {
				
				if(this.getPowerlinkmode()!=0 && this.getPowerlinkmode()!=1 && this.getPowerlinkmode()!=2)this.setPowerlinkmode(0);
				
			}
		}


	}

	public void dropplugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a,this);
		}
	}

	public void addtogrid() {
		if (Capacitor_ID == 0) {
			Capacitor_ID = Linkgrid.getWorldMap(worldObj)
					.newID(this);
		}
		Linkgrid.getWorldMap(worldObj).getCapacitor().put(Capacitor_ID, this);

		registerChunkLoading();
	}

	public void removefromgrid() {
		Linkgrid.getWorldMap(worldObj).getCapacitor().remove(getCapacitor_ID());
		dropplugins();
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		SwitchTyp = nbttagcompound.getInteger("SwitchTyp");
		OnOffSwitch = nbttagcompound.getBoolean("OnOffSwitch");
		forcePower = nbttagcompound.getInteger("forcepower");
		Capacitor_ID = nbttagcompound.getInteger("Capacitor_ID");
		Powerlinkmode = nbttagcompound.getInteger("Powerlinkmode");

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

		nbttagcompound.setInteger("SwitchTyp", SwitchTyp);
		nbttagcompound.setBoolean("OnOffSwitch", OnOffSwitch);
		nbttagcompound.setInteger("forcepower", forcePower);
		nbttagcompound.setInteger("Capacitor_ID", Capacitor_ID);
		nbttagcompound.setInteger("Powerlinkmode", Powerlinkmode);

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
                checkslots(true);
                setCreate(false);
            }

			boolean powerdirekt = worldObj.isBlockGettingPowered(xCoord,
					yCoord, zCoord);
			boolean powerindrekt = worldObj.isBlockIndirectlyGettingPowered(
					xCoord, yCoord, zCoord);

			if(this.getswitchtyp()==0)
			{
		    this.setOnOffSwitch((powerdirekt || powerindrekt));
			}

			if (getOnOffSwitch()) {
				if (isActive() != true) {
					setActive(true);
				}
			} else {
				if (isActive() != false) {
					setActive(false);
				}
			}

			if (this.getTicker() == 10) {
				
				if(this.getLinketProjektor() != (short) Linkgrid.getWorldMap(worldObj).connectedtoCapacitor(this,getTransmitRange()))
				setLinketprojektor((short) Linkgrid.getWorldMap(worldObj).connectedtoCapacitor(this,getTransmitRange()));

				if(this.getCapacity() != ((getForcePower()/1000)*100)/(getMaxForcePower()/1000))
				   setCapacity(((getForcePower()/1000)*100)/(getMaxForcePower()/1000));

				checkslots(false);
				if(isActive())
				{
				powertransfer();
				}
				this.setTicker((short) 0);
			}
			this.setTicker((short) (this.getTicker() + 1));
		}else {
			if(Capacitor_ID==0)
			{
				if (this.getTicker() >= 20 + random.nextInt(20)) {
					NetworkHandlerClient.requestInitialData(this,true);
					this.setTicker((short) 0);
				}

				this.setTicker((short) (this.getTicker() + 1));
			}
		}
	}

	private void powertransfer()
	{
		if(getLinkCapacitor_ID()!= 0)
		{
	    TileEntityCapacitor RemoteCap = getLinkedCapacitor();

	    if(RemoteCap != null)
	    {
	      int maxtrasferrate = this.getMaxForcePower() / 120;
	      int forceenergyspace = RemoteCap.getMaxForcePower() - RemoteCap.getForcePower();

		switch(this.getPowerlinkmode())
		{
		case 0:
		if(getCapacity() >= 90 && RemoteCap.getCapacity() != 100)
		{
		    if(forceenergyspace > maxtrasferrate)
		    {
		    	RemoteCap.setForcePower(RemoteCap.getForcePower() + maxtrasferrate);
                this.setForcePower(this.getForcePower() - maxtrasferrate);
		    }else{
		    	RemoteCap.setForcePower(RemoteCap.getForcePower() + forceenergyspace);
                this.setForcePower(this.getForcePower() - forceenergyspace);
		    }
		}
		break;
		case 1:
		if(RemoteCap.getCapacity() < this.getCapacity())
		{
			int balancevaue = this.getForcePower()- RemoteCap.getForcePower();

		    if(balancevaue > maxtrasferrate)
		    {
		    	RemoteCap.setForcePower(RemoteCap.getForcePower() + maxtrasferrate);
                this.setForcePower(this.getForcePower() - maxtrasferrate);
		    }else{
		    	RemoteCap.setForcePower(RemoteCap.getForcePower() + balancevaue);
                this.setForcePower(this.getForcePower() - balancevaue);
		    }
		}
		break;
		case 2:
		if(getForcePower() > 0 && RemoteCap.getCapacity() != 100)
		{
		  if(this.getForcePower() > maxtrasferrate)
		  {
			    if(forceenergyspace > maxtrasferrate)
			    {
			    	RemoteCap.setForcePower(RemoteCap.getForcePower() + maxtrasferrate);
	                this.setForcePower(this.getForcePower() - maxtrasferrate);
			    }else{
			    	RemoteCap.setForcePower(RemoteCap.getForcePower() + forceenergyspace);
	                this.setForcePower(this.getForcePower() - forceenergyspace);
			    }
		  }else{
			    if(forceenergyspace > this.getForcePower())
			    {
			    	RemoteCap.setForcePower(RemoteCap.getForcePower() + this.getForcePower());
	                this.setForcePower(this.getForcePower() - this.getForcePower());
			    }else{
			    	RemoteCap.setForcePower(RemoteCap.getForcePower() + forceenergyspace);
	                this.setForcePower(this.getForcePower() - forceenergyspace);
			    }
		  }
		}
		break;
		}
		}
		}
	}

	public ItemStack getStackInSlot(int i) {
		return inventory[i];
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
		return "Generator";
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
	public void onNetworkHandlerEvent(String event) {
		
		
		switch(Integer.parseInt(event))
		{
		case 0:
			if(this.getswitchtyp() == 0)
			{
				this.setswitchtyp(1);
			}else{
				this.setswitchtyp(0);
			}
		break;

		case 1:
			if (getStackInSlot(2) != null) 
			{
				if (getStackInSlot(2).getItem() instanceof IForceEnergyItems)
				{
					if(this.getPowerlinkmode() == 4)
					{
						this.setPowerlinkmode(3);
					}else{
						this.setPowerlinkmode(4);
					}
					
					return;
				}
				if (getStackInSlot(2).getItem() == ModularForceFieldSystem.MFFSitemfc)
				{
					
					if(this.getPowerlinkmode() < 2)
					{				
						this.setPowerlinkmode(this.getPowerlinkmode() +1);
					}else{
						this.setPowerlinkmode(0);
					}
					
					return;
				}
			}
			
			if(this.getPowerlinkmode() != 4)
			{				
				this.setPowerlinkmode(this.getPowerlinkmode() +1);
			}else{
				this.setPowerlinkmode(0);
			}
		break;
		}
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
		NetworkedFields.add("side");
		NetworkedFields.add("SwitchTyp");
		NetworkedFields.add("linketprojektor");
		NetworkedFields.add("capacity");
		NetworkedFields.add("Capacitor_ID");
		NetworkedFields.add("SecStation_ID");
		NetworkedFields.add("TransmitRange");

		return NetworkedFields;
	}

	public boolean consumForcePower(int useForcePower)
	{
		if(this.getForcePower() >=useForcePower){
		    setForcePower(this.getForcePower() - useForcePower);
		    return true;
		}
		return false;
	}
	
	public void Energylost(int fpcost) {
		if (this.getForcePower() >= 0) {
			this.setForcePower(this.getForcePower() - fpcost);
		}
		if (this.getForcePower() < 0) {
			this.setForcePower(0);
		}
	}
	
	@Override
	public void onEMPPulse(int magnitude){
		 if(ModularForceFieldSystem.influencedbyothermods)
		 {
		this.setForcePower(this.getForcePower() / 100 * Math.min(Math.max(magnitude, 0), 100));
		 }
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		switch(Slot)
		{
		case 0:
			if(par1ItemStack.getItem() instanceof  ItemCapacitorUpgradeCapacity)
			return true;
		break;
		case 1:
			if(par1ItemStack.getItem() instanceof  ItemCapacitorUpgradeRange)
			return true;
		break;
		case 2:
			if(par1ItemStack.getItem() instanceof  IForceEnergyItems || par1ItemStack.getItem() instanceof  ItemCardPowerLink)
			return true;
		break;
		case 4:
			if(par1ItemStack.getItem() instanceof  ItemCardSecurityLink)
			return true;
		break;
		}

		return false;
	}

	@Override
	public int getSlotStackLimit(int Slot){
		switch(Slot){
		case 0: //Range upgrade
			return 9;
		case 1: //Cap upgrade
			return 9;
		case 2: //Link/power item
			return 64;
		}
		return 1;
	}


}

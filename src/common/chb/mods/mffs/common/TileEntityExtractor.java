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

import ic2.api.Direction;
import ic2.api.EnergyNet;
import ic2.api.IEnergySink;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import universalelectricity.core.electricity.Electricity;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;

import buildcraft.api.gates.IOverrideDefaultTriggers;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import chb.mods.mffs.network.INetworkHandlerListener;
import chb.mods.mffs.network.NetworkHandlerClient;
import chb.mods.mffs.network.NetworkHandlerServer;

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

public class TileEntityExtractor extends TileEntityMachines implements 
INetworkHandlerListener,IPowerReceptor,IOverrideDefaultTriggers,IEnergySink{
	private ItemStack inventory[];

	private int workmode = 0;
    private boolean create;
	private int Extractor_ID;
	protected int WorkEnergy;
	protected int MaxWorkEnergy;
	private int ForceEnergybuffer;
	private int MaxForceEnergyBuffer;
	private int WorkCylce;
	private int workTicker;
	private int workdone;
	private int maxworkcylce;
	private int capacity;
	private IPowerProvider powerProvider;
	private boolean addedToEnergyNet;




	public TileEntityExtractor() {
		inventory = new ItemStack[5];
		create = true;
		Extractor_ID = 0;
		WorkEnergy = 0;
		MaxWorkEnergy = 4000;
		ForceEnergybuffer = 0;
		MaxForceEnergyBuffer = 1000000;
		WorkCylce = 0;
		workTicker = 20;
		maxworkcylce = 125;
		capacity = 0;
		addedToEnergyNet = false;

		
			
		if(ModularForceFieldSystem.buildcraftfound){	
		powerProvider = PowerFramework.currentFramework.createPowerProvider();
		powerProvider.configure(10, 2, (int) (getMaxWorkEnergy() / 2.5),(int) (getMaxWorkEnergy() / 2.5),(int) (getMaxWorkEnergy() / 2.5));
		}

	}
	

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

	public int getMaxworkcylce() {
		return maxworkcylce;
	}

	public void setMaxworkcylce(int maxworkcylce) {
		this.maxworkcylce = maxworkcylce;
	}

	public int getWorkdone() {
		return workdone;
	}

	public void setWorkdone(int workdone) {
		if(this.workdone != workdone){
		this.workdone = workdone;
		NetworkHandlerServer.updateTileEntityField(this,"workdone");
		}
	}

	public int getWorkTicker() {
		return workTicker;
	}

	public void setWorkTicker(int workTicker) {
		this.workTicker = workTicker;
	}


	public int getExtractor_ID() {
		return Extractor_ID;
	}

	public int getMaxForceEnergyBuffer() {
		return MaxForceEnergyBuffer;
	}

	public void setMaxForceEnergyBuffer(int maxForceEnergyBuffer) {
		MaxForceEnergyBuffer = maxForceEnergyBuffer;
	}

	public int getForceEnergybuffer() {
		return ForceEnergybuffer;
	}

	public void setForceEnergybuffer(int forceEnergybuffer) {
		ForceEnergybuffer = forceEnergybuffer;
	}

	public void setWorkCylce(int i)
	{
		if(this.WorkCylce != i){
		this.WorkCylce = i;
		NetworkHandlerServer.updateTileEntityField(this,"WorkCylce");
		}
	}

	public int getWorkCylce(){
		return WorkCylce;
	}

	public int getWorkEnergy() {
		return WorkEnergy;
	}

	public void setWorkEnergy(int workEnergy) {
		WorkEnergy = workEnergy;
	}

	public int getMaxWorkEnergy() {
		return MaxWorkEnergy;
	}

	public void setMaxWorkEnergy(int maxWorkEnergy) {
		MaxWorkEnergy = maxWorkEnergy;
	}

	public void addtogrid() {
		
		
		if (Extractor_ID == 0) {
			Extractor_ID = Linkgrid.getWorldMap(worldObj)
					.newID(this);
		}
		Linkgrid.getWorldMap(worldObj).getExtractor().put(Extractor_ID, this);
	}

	public void removefromgrid() {
		Linkgrid.getWorldMap(worldObj).getExtractor().remove(getExtractor_ID());
		dropplugins();
	}

	public void dropplugins() {
		for (int a = 0; a < this.inventory.length; a++) {
			dropplugins(a,this);
		}
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		} else {
			return entityplayer.getDistance((double) xCoord + 0.5D,
					(double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
		}
	}

	public void checkslots(boolean init) {

		if (getStackInSlot(2) != null) {
			if (getStackInSlot(2).getItem() == ModularForceFieldSystem.MFFSitemupgradecapcap) {
				setMaxForceEnergyBuffer(1000000 + (getStackInSlot(2).stackSize * 100000));
			 }else{
				 setMaxForceEnergyBuffer(1000000);
			 }
			}else{
				setMaxForceEnergyBuffer(1000000);
			}

		if (getStackInSlot(3) != null) {
			if (getStackInSlot(3).getItem() == ModularForceFieldSystem.MFFSitemupgradeexctractorboost) {
				setWorkTicker(20 - getStackInSlot(3).stackSize);
			}else{
				setWorkTicker(20);
			}
		}else{
			setWorkTicker(20);
		}

		if (getStackInSlot(4) != null) {
			if (getStackInSlot(4).getItem() == ModularForceFieldSystem.MFFSitemForcicumCell) {
				workmode = 1;
				setMaxWorkEnergy(200000);
			}
			}else{
				workmode = 0;
				setMaxWorkEnergy(4000);
			}
	}
	
	public TileEntityCapacitor getLinkedCapacitor()
	{
		return ItemCardPowerLink.getLinkedCapacitor(this, 1, worldObj);
	}
	
	
	public int getLinkCapacitor_ID(){
		TileEntityCapacitor cap = getLinkedCapacitor();
		if(cap != null)
			return cap.getCapacitor_ID();
		return 0;	
	}

	private boolean hasPowertoConvert()
	{
		if(WorkEnergy >= MaxWorkEnergy-1)
		{
		 setWorkEnergy(0);
		 return true;
		}
		return false;
	}

	private boolean hasfreeForceEnergyStorage()
	{
		if(this.MaxForceEnergyBuffer > this.ForceEnergybuffer)
		 return true;
		return false;
	}

	private boolean hasStufftoConvert()
	{
		if (WorkCylce > 0)
		{
			return true;
		}else{
		if(ModularForceFieldSystem.adventuremap)
		{
	    	 setMaxworkcylce(ModularForceFieldSystem.ForceciumCellWorkCylce);
			 setWorkCylce(getMaxworkcylce());
			 return true;
		}

		if (getStackInSlot(0) != null) {
				if (getStackInSlot(0).getItem() == ModularForceFieldSystem.MFFSitemForcicium) {
		    	  setMaxworkcylce(ModularForceFieldSystem.ForceciumWorkCylce);
				  setWorkCylce(getMaxworkcylce());
			      decrStackSize(0, 1);
			      return true;
				}

				if (getStackInSlot(0).getItem() == ModularForceFieldSystem.MFFSitemForcicumCell) {
					if(((ItemForcicumCell)getStackInSlot(0).getItem()).useForcecium(1, getStackInSlot(0)))
					{
				    	  setMaxworkcylce(ModularForceFieldSystem.ForceciumCellWorkCylce);
						  setWorkCylce(getMaxworkcylce());
						  return true;
					}
					}
			}
		}

		return false;
	}

	public void transferForceEnergy()
	{
		if(this.getForceEnergybuffer() > 0)
		{
		if(getLinkCapacitor_ID()!=0)
		{
			TileEntityCapacitor RemoteCap =getLinkedCapacitor();
			if(RemoteCap != null)
			{
			      int maxtrasferrate = ModularForceFieldSystem.ExtractorPassForceEnergyGenerate*2;
			      int forceenergyspace = RemoteCap.getMaxForcePower() - RemoteCap.getForcePower();

				  if(this.getForceEnergybuffer() > maxtrasferrate)
				  {
					    if(forceenergyspace > maxtrasferrate)
					    {
					    	RemoteCap.setForcePower(RemoteCap.getForcePower() + maxtrasferrate);
			                this.setForceEnergybuffer(this.getForceEnergybuffer() - maxtrasferrate);
					    }else{
					    	RemoteCap.setForcePower(RemoteCap.getForcePower() + forceenergyspace);
			                this.setForceEnergybuffer(this.getForceEnergybuffer() - forceenergyspace);
					    }
				  }else{
					    if(forceenergyspace > this.getForceEnergybuffer())
					    {
					    	RemoteCap.setForcePower(RemoteCap.getForcePower() + this.getForceEnergybuffer());
			                this.setForceEnergybuffer(this.getForceEnergybuffer() - this.getForceEnergybuffer());
					    }else{
					    	RemoteCap.setForcePower(RemoteCap.getForcePower() + forceenergyspace);
			                this.setForceEnergybuffer(this.getForceEnergybuffer() - forceenergyspace);
					    }
				  }
			}
		}
		}
	}

	public void updateEntity() {
		if (worldObj.isRemote == false) {
			if (create && this.getLinkCapacitor_ID() != 0) {
				addtogrid();
				checkslots(true);
				create = false;
				
			}

			if (!addedToEnergyNet && ModularForceFieldSystem.ic2found) {
				EnergyNet.getForWorld(worldObj).addTileEntity(this);
				addedToEnergyNet = true;
				}

			if (this.getTicker() >= getWorkTicker()) {
				checkslots(false);

				if(workmode==0)
				{
				if(ModularForceFieldSystem.buildcraftfound)
				converMJtoWorkEnergy();

				if(ModularForceFieldSystem.uefound)
				converUEtoWorkEnergy();

				if(this.getWorkdone() != getWorkEnergy() * 100 / getMaxWorkEnergy())
				setWorkdone( getWorkEnergy() * 100 / getMaxWorkEnergy());

				if(getWorkdone() > 100){setWorkdone(100);}

				if(this.getCapacity() != (getForceEnergybuffer()*100)/getMaxForceEnergyBuffer())
					   setCapacity((getForceEnergybuffer()*100)/getMaxForceEnergyBuffer());

				if(this.hasfreeForceEnergyStorage() && this.hasStufftoConvert())
				{
				 if (isActive() != true) {
					 setActive(true);
					}

					if(this.hasPowertoConvert()){
						  setWorkCylce(getWorkCylce()-1);
						  setForceEnergybuffer(getForceEnergybuffer()+ ModularForceFieldSystem.ExtractorPassForceEnergyGenerate);
					}
				}else{
					if (isActive() != false) {
						setActive(false);
					}
				}

				transferForceEnergy();

				this.setTicker((short) 0);
			}

			if(workmode==1)
			{
				if(this.getWorkdone() != getWorkEnergy() * 100 / getMaxWorkEnergy())
					setWorkdone( getWorkEnergy() * 100 / getMaxWorkEnergy());

				if(ModularForceFieldSystem.buildcraftfound)
				converMJtoWorkEnergy();

				if(ModularForceFieldSystem.uefound)
				converUEtoWorkEnergy();

			   	if(((ItemForcicumCell)getStackInSlot(4).getItem()).getForceciumlevel(getStackInSlot(4)) < ((ItemForcicumCell)getStackInSlot(4).getItem()).getMaxForceciumlevel())
				{
			      if (isActive() != true) {
					setActive(true);
					}
	             if(this.hasPowertoConvert()){
	                         ((ItemForcicumCell)getStackInSlot(4).getItem()).setForceciumlevel(getStackInSlot(4),((ItemForcicumCell)getStackInSlot(4).getItem()).getForceciumlevel(getStackInSlot(4))+1);
						 }
						}else{
					if (isActive() != false) {
						setActive(false);
						}
					}

				this.setTicker((short) 0);
			}
			}

			this.setTicker((short) (this.getTicker() + 1));
		}else {
			if(Extractor_ID==0)
			{
				if (this.getTicker() >= 20+random.nextInt(20)) {
					NetworkHandlerClient.requestInitialData(this,true);

					this.setTicker((short) 0);
				}

				this.setTicker((short) (this.getTicker() + 1));
			}
		}
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerForceEnergyExtractor(inventoryplayer.player,this);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		 Extractor_ID = nbttagcompound.getInteger("Extractor_ID");
		 ForceEnergybuffer = nbttagcompound.getInteger("ForceEnergybuffer");
		 WorkEnergy = nbttagcompound.getInteger("WorkEnergy");
		 WorkCylce = nbttagcompound.getInteger("WorkCylce");

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

		nbttagcompound.setInteger("WorkCylce", WorkCylce);
		nbttagcompound.setInteger("WorkEnergy", WorkEnergy);
		nbttagcompound.setInteger("ForceEnergybuffer",ForceEnergybuffer);
		nbttagcompound.setInteger("Extractor_ID", Extractor_ID);

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
	public int getStartInventorySide(ForgeDirection side) {
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 1;
	}

	@Override
	public void onNetworkHandlerUpdate(String field){ 
		
		if(field.equalsIgnoreCase("side") && ModularForceFieldSystem.uefound)
		{

		  ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(this.getFacing()).getOpposite()));
			
		}
		
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
		
	}

	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.add("active");
		NetworkedFields.add("side");
		NetworkedFields.add("capacity");
		NetworkedFields.add("WorkCylce");
		NetworkedFields.add("WorkEnergy");
		NetworkedFields.add("workdone");
		NetworkedFields.add("Extractor_ID");

		return NetworkedFields;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		switch(Slot)
		{
		case 0:
			if((par1ItemStack.getItem() instanceof ItemForcicium || par1ItemStack.getItem() instanceof ItemForcicumCell) && getStackInSlot(4) == null)
			return true;
		break;

		case 1:
			if(par1ItemStack.getItem() instanceof  ItemCardPowerLink)
			return true;
		break;

		case 2:
			if(par1ItemStack.getItem() instanceof  ItemCapacitorUpgradeCapacity)
			return true;
		break;

		case 3:
			if(par1ItemStack.getItem() instanceof  ItemExtractorUpgradeBooster)
			return true;
		break;

		case 4:
			if(par1ItemStack.getItem() instanceof  ItemForcicumCell && getStackInSlot(0) == null)
			return true;
		break;
		}
		return false;
	}

	@Override
	public int getSlotStackLimit(int Slot){
		switch(Slot){
		case 0: //Forcicium
			return 64;
		case 1: //Powerlink
			return 1;
		case 2: //Cap upgrade
			return 9;
		case 3: //Boost upgrade
			return 19;
		case 4: //Forcicium cell
			return 1;
		}
		return 1;
	}

	@Override
	public boolean demandsEnergy() {
		if(this.getWorkEnergy()< this.MaxWorkEnergy)
		{
			return true;
		}

		return false;
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount) {
	if(this.getWorkEnergy()< this.MaxWorkEnergy)
	{
	if((MaxWorkEnergy - WorkEnergy) > amount)
	{
	WorkEnergy = WorkEnergy + amount;
	return 0;
	}else{
	WorkEnergy = WorkEnergy + (MaxWorkEnergy - WorkEnergy);
	return 0 ; //amount- (MaxWorkEnergy - WorkEnergy);
	}
	}
	return 0;
	}

	@Override
	public void invalidate() {
		if (addedToEnergyNet) {
			EnergyNet.getForWorld(worldObj).removeTileEntity(this);
			addedToEnergyNet = false;
		}

		super.invalidate();
	}

	@Override
	public boolean isAddedToEnergyNet() {
		return addedToEnergyNet;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity tileentity, Direction direction) {
		return true;
	}

	public void converMJtoWorkEnergy(){
		if(this.getWorkEnergy() < this.getMaxWorkEnergy())
		{
	      float use = powerProvider.useEnergy(1, (float) (this.getMaxWorkEnergy() - this.getWorkEnergy() / 2.5), true);

	      if(getWorkEnergy() + (use *2.5) > getMaxWorkEnergy())
	      {
	    	     setWorkEnergy(getMaxWorkEnergy());
	      }else{
	             setWorkEnergy((int) (getWorkEnergy() + (use *2.5)));
	      }
		}
	}

	@Override
	public void setPowerProvider(IPowerProvider provider) {
		this.powerProvider = provider;
	}

	@Override
	public IPowerProvider getPowerProvider() {
		return powerProvider;
	}

	@Override
	public void doWork() {}

	@Override
	public int powerRequest() {
		double workEnergyinMJ = getWorkEnergy()  / 2.5;
		double MaxWorkEnergyinMj = getMaxWorkEnergy()  / 2.5;

		return (int) Math.round(MaxWorkEnergyinMj - workEnergyinMJ) ;
	}

	@Override
	public LinkedList<ITrigger> getTriggers() {
		return null;
	}

	public void converUEtoWorkEnergy(){
		
		ForgeDirection inputDirection = ForgeDirection.getOrientation(this.getFacing()).getOpposite();
		
		TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, Vector3.get(this), inputDirection);

		if (inputTile != null)
		{
		 if (inputTile instanceof IConductor)
		 {
		  if (this.getWorkEnergy()  >= this.getMaxWorkEnergy())
		  {
		   ((IConductor) inputTile).getNetwork().stopRequesting(this);
		  }
		   else
		  {
		   ((IConductor) inputTile).getNetwork().startRequesting(this,this.getMaxWorkEnergy() - this.getWorkEnergy()/120 , 120);
		  
		   this.setWorkEnergy((int) (getWorkEnergy() + ((IConductor) inputTile).getNetwork().consumeElectricity(this).getWatts()/2));
	      }
		  }
		}	
		
		System.out.println(getWorkEnergy());
		

	}


}

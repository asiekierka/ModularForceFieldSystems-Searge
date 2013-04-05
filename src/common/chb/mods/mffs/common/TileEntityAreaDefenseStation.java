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

import chb.mods.mffs.common.IModularProjector.Slots;
import chb.mods.mffs.common.options.ItemProjectorOptionDefenseStation;
import chb.mods.mffs.common.options.ItemProjectorOptionMobDefence;
import chb.mods.mffs.network.INetworkHandlerEventListener;
import chb.mods.mffs.network.INetworkHandlerListener;
import chb.mods.mffs.network.NetworkHandlerClient;
import chb.mods.mffs.network.NetworkHandlerServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Container;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityAreaDefenseStation extends TileEntityMachines implements
ISidedInventory,INetworkHandlerListener,INetworkHandlerEventListener,ISwitchabel {
	private ItemStack Inventory[];
	private int Defstation_ID;
	private int linkPower;
	private int capacity;
	private boolean create;
	private int distance;
	private int SwitchTyp;
	private int contratyp;
	private int actionmode;
	private int scanmode;
	private boolean OnOffSwitch;
	
	protected List<EntityPlayer> warnlist = new ArrayList<EntityPlayer>();
	protected List<EntityPlayer> actionlist = new ArrayList<EntityPlayer>();
	protected List<EntityLiving> NPClist = new ArrayList<EntityLiving>();
	private ArrayList<Item> ContraList = new ArrayList();
	
	public TileEntityAreaDefenseStation() {
		Random random = new Random();

		Inventory = new ItemStack[36];
		Defstation_ID = 0;
		linkPower = 0;
		capacity = 0;
		create = true;
		SwitchTyp = 0;
		contratyp = 1;
		actionmode = 0;
		scanmode = 1;
		OnOffSwitch = false;
	}
	
	// Start Getter AND Setter

	

	public int getScanmode() {
		return scanmode;
	}

	public void setScanmode(int scanmode) {
		this.scanmode = scanmode;
	}


	public int getActionmode() {
		return actionmode;
	}


	public void setActionmode(int actionmode) {
		this.actionmode = actionmode;
	}
	
	public boolean getOnOffSwitch() {
		return OnOffSwitch;
	}

	public void setOnOffSwitch(boolean a) {
		OnOffSwitch = a;
	}
	
	public int getcontratyp() {
		return contratyp;
	}

	public void setcontratyp(int a) {
		contratyp = a;
	}
	

	public int getswitchtyp() {
		return SwitchTyp;
	}

	public void setswitchtyp(int a) {
		SwitchTyp = a;
	}
	
	public int getCapacity(){
		return capacity;
	}

	public void setCapacity(int Capacity){
		this.capacity = Capacity;
	}
	

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}


	public int getLinkPower() {
		return linkPower;
	}

	public void setLinkPower(int linkPower) {
		this.linkPower = linkPower;
	}
	


	
	
	public int getActionDistance() {
	if (getStackInSlot(3) != null) {
		if (getStackInSlot(3).getItem() == ModularForceFieldSystem.MFFSProjectorFFDistance) {
			return (getStackInSlot(3).stackSize);
		}
	  } 
	return 0;
	
	}
	
	
	
	public int getInfoDistance() {
	if (getStackInSlot(2) != null) {
		if (getStackInSlot(2).getItem() == ModularForceFieldSystem.MFFSProjectorFFDistance) {
			return getActionDistance() + (getStackInSlot(2).stackSize+3);
		}
	  } 
	return getActionDistance()+ 3;
	}
	
	
	
	public TileEntityAdvSecurityStation getLinkedSecurityStation()
	{
		return ItemCardSecurityLink.getLinkedSecurityStation(this, 1, worldObj);
	}
	

	
	
	public int getSecStation_ID(){
		TileEntityAdvSecurityStation sec = getLinkedSecurityStation();
		if(sec != null)
			return sec.getSecurtyStation_ID();
		return 0;	
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


	// End Getter AND Setter

	public void addtogrid() {
		if (Defstation_ID == 0) {
			Defstation_ID = Linkgrid.getWorldMap(worldObj)
					.newID(this);
		}
		Linkgrid.getWorldMap(worldObj).getDefStation().put(Defstation_ID, this);

		registerChunkLoading();
	}

	public void removefromgrid() {
		Linkgrid.getWorldMap(worldObj).getDefStation().remove(Defstation_ID);
		dropplugins();
	}

	// Start NBT Read/ Save
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		Defstation_ID = nbttagcompound.getInteger("Defstation_ID");
		SwitchTyp = nbttagcompound.getInteger("SwitchTyp");
		contratyp = nbttagcompound.getInteger("contratyp");
		actionmode= nbttagcompound.getInteger("actionmode");
		scanmode= nbttagcompound.getInteger("scanmode");
		OnOffSwitch = nbttagcompound.getBoolean("OnOffSwitch");
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		Inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < Inventory.length) {
				Inventory[byte0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("Defstation_ID", Defstation_ID);
		nbttagcompound.setInteger("contratyp", contratyp);
		nbttagcompound.setInteger("SwitchTyp", SwitchTyp);
		nbttagcompound.setInteger("actionmode", actionmode);
		nbttagcompound.setBoolean("OnOffSwitch", OnOffSwitch);
		nbttagcompound.setInteger("scanmode", scanmode);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < Inventory.length; i++) {
			if (Inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				Inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}


	public void dropplugins() {
		for (int a = 0; a < this.Inventory.length; a++) {
			dropplugins(a,this);
		}
	}

	

	public void scanner()
	{
		try{
			
		TileEntityAdvSecurityStation sec = 	getLinkedSecurityStation();
		
		if(sec!=null)
		{
		int xmininfo = xCoord- getInfoDistance();
		int xmaxinfo = xCoord+ getInfoDistance()+1;
		int ymininfo = yCoord- getInfoDistance();
		int ymaxinfo = yCoord+ getInfoDistance()+1;
		int zmininfo = zCoord- getInfoDistance();
		int zmaxinfo = zCoord+ getInfoDistance()+1;
		
		int xminaction = xCoord - getActionDistance();
		int xmaxaction = xCoord+ getActionDistance()+1;
		int yminaction = yCoord- getActionDistance();
		int ymaxaction = yCoord+ + getActionDistance()+1;
		int zminaction = zCoord- getActionDistance();
		int zmaxaction = zCoord+ getActionDistance()+1;
		
		List<EntityLiving> infoLivinglist = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xmininfo, ymininfo, zmininfo, xmaxinfo, ymaxinfo, zmaxinfo));
		List<EntityLiving> actionLivinglist = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xminaction, yminaction, zminaction, xmaxaction, ymaxaction, zmaxaction));
		
		
		for(EntityLiving Living : infoLivinglist)
		{
			if(Living instanceof EntityPlayer)
			{
			EntityPlayer player = (EntityPlayer) Living;
			int distance = (int) PointXYZ.distance(getMaschinePoint(), new PointXYZ((int)Living.posX,(int)Living.posY,(int)Living.posZ,worldObj));
			
			if(distance > getInfoDistance() && this.getScanmode()==1){
				continue;
			}
			
			
			if(!warnlist.contains(player))
			{
				warnlist.add(player);
				if(!sec.isAccessGranted(player.username, "SR"))
				{
				player.addChatMessage("!!! [Area Defence] Warning you now in my Scanning range !!!");
				player.attackEntityFrom(DamageSource.generic,1);
				}

			}
			
		   }		
		}
		
		
		for(EntityLiving Living : actionLivinglist)
		{
		  if(Living instanceof EntityPlayer)
		  {
		   EntityPlayer player = (EntityPlayer) Living;
		   
		   int distance = (int) Math.round(PointXYZ.distance(getMaschinePoint(), new PointXYZ((int)Living.posX,(int)Living.posY,(int)Living.posZ,worldObj)));
			if(distance > getActionDistance() && this.getScanmode()==1)
				continue;
			
			if(!actionlist.contains(player))
			{
				actionlist.add(player);
				DefenceAction(player);
			}
			
		  }else{
			  
			  int distance = (int) Math.round(PointXYZ.distance(getMaschinePoint(), new PointXYZ((int)Living.posX,(int)Living.posY,(int)Living.posZ,worldObj)));	
				if(distance > getActionDistance() && this.getScanmode()==1)
					continue;
				
				if(!NPClist.contains(Living)){
					NPClist.add(Living);
					DefenceAction(Living);
				}
			
			  
		  }
		}
		
		
		
		

		for (int i = 0; i < actionlist.size(); i++)
		{
			if(!actionLivinglist.contains(actionlist.get(i)))
				actionlist.remove(actionlist.get(i));
		}
		
	
		for (int i = 0; i < warnlist.size(); i++)
		{
	
			if(!infoLivinglist.contains(warnlist.get(i)))
				warnlist.remove(warnlist.get(i));
		}
		
		
		
		
		}
		}catch(Exception ex)
		{
			System.err.println("[ModularForceFieldSystem] catch  Crash <TileEntityAreaDefenseStation:scanner> ");
		}
		
		
	}
	
	
	public void DefenceAction(){
		
		 for (int i = 0; i < actionlist.size(); i++)
		{
			DefenceAction(actionlist.get(i));
		}

	}
	
		
	
	public boolean StacksToInventory(IInventory inventory,ItemStack itemstacks,boolean loop)
	{
	
		int count= 0;
		
		if(inventory instanceof TileEntitySecStorage )
		   count = 1;
		
		if(inventory instanceof TileEntityAreaDefenseStation )
		   count = 15;
		
		for (int a = count; a <= inventory.getSizeInventory()-1; a++) {
              if(inventory.getStackInSlot(a)==null){
            	  inventory.setInventorySlotContents(a, itemstacks); 
            	 return true;
              }else{
            	  if(inventory.getStackInSlot(a).getItem() == itemstacks.getItem()
				 && inventory.getStackInSlot(a).getItemDamage() == itemstacks.getItemDamage()
				 && inventory.getStackInSlot(a).stackSize <  inventory.getStackInSlot(a).getMaxStackSize())
            	  {
            		int free =   inventory.getStackInSlot(a).getMaxStackSize() - inventory.getStackInSlot(a).stackSize;
            		
            		if(free > itemstacks.stackSize)
            		{
            			inventory.getStackInSlot(a).stackSize +=  itemstacks.stackSize;
            			return true;
            		}else{
            			inventory.getStackInSlot(a).stackSize=inventory.getStackInSlot(a).getMaxStackSize();
            			itemstacks.stackSize = itemstacks.stackSize - free;
            			continue;
            		}
  
            	  }
              }
             
		}
		    if(loop)
		    addremoteInventory(itemstacks);
		    
			return false;
		
	}
	
	public void addremoteInventory(ItemStack itemstacks)
	{
		IInventory inv =InventoryHelper.findAttachedInventory(worldObj, xCoord, yCoord, zCoord);
		if(inv != null)
		{
		  StacksToInventory(inv,itemstacks,false);
		}
	}
	
	public void DefenceAction(EntityLiving Living){
		if(Living instanceof EntityPlayer)
			return;
		
		TileEntityCapacitor cap = this.getLinkedCapacitor();
		TileEntityAdvSecurityStation sec = 	getLinkedSecurityStation();
		
		if(cap!=null)
		{
		
		if(sec!=null)
		{
		if(cap.getForcePower() > ModularForceFieldSystem.DefenceStationKillForceEnergy)
		{
		switch(getActionmode())
		{
		case 3: //all
			cap.consumForcePower(ModularForceFieldSystem.DefenceStationKillForceEnergy);
			Living.setEntityHealth(0);
			NPClist.remove(Living);
			break;
		case 4: //Hostile
			if(Living instanceof EntityMob || Living instanceof EntitySlime || Living instanceof EntityGhast){
				Living.setEntityHealth(0);
				NPClist.remove(Living);
				cap.consumForcePower(ModularForceFieldSystem.DefenceStationKillForceEnergy);
			}
			
			break;
		case 5://no Hostie
			
			if(!(Living instanceof EntityMob) && !(Living instanceof EntitySlime) && !(Living instanceof EntityGhast)){
				Living.setEntityHealth(0);	
				NPClist.remove(Living);
				cap.consumForcePower(ModularForceFieldSystem.DefenceStationKillForceEnergy);
			}
			break;
		}
	   }
	  }
	 }
	}

	public void DefenceAction(EntityPlayer player){
		
	
		TileEntityCapacitor cap = this.getLinkedCapacitor();
		TileEntityAdvSecurityStation sec = 	getLinkedSecurityStation();
		
		if(cap!=null)
		{
		
		if(sec!=null)
		{
		
		switch(getActionmode())
		{
		case 0: // Inform
			if(!sec.isAccessGranted(player.username, "SR"))
			{
				player.addChatMessage("!!! [Area Defence]  get out immediately you have no right to be here!!!");
			}
			
		break;
		case 1: // kill
			
				if(cap.getForcePower() > ModularForceFieldSystem.DefenceStationKillForceEnergy)
				{
					if(!sec.isAccessGranted(player.username, "SR"))
					{
						player.addChatMessage("!!! [Area Defence] you have been warned BYE BYE!!!");
					
						
						for(int i=0; i<4;i++) {
							if(player.inventory.armorInventory[i] != null){
							StacksToInventory(this,player.inventory.armorInventory[i],true);
							player.inventory.armorInventory[i]=null;
							}
						}
						
						for(int i=0; i<36;i++) {
							
							if(player.inventory.mainInventory[i] != null){
								StacksToInventory(this,player.inventory.mainInventory[i],true);
								player.inventory.mainInventory[i]=null;
							}
						}
						
						actionlist.remove(player);
						player.setEntityHealth(0);
						cap.consumForcePower(ModularForceFieldSystem.DefenceStationKillForceEnergy);
					}
					
				}
				
			
			
		break;
		case 2: // search
			
			
			if(cap.getForcePower() > ModularForceFieldSystem.DefenceStationSearchForceEnergy)
			{
				if(!sec.isAccessGranted(player.username, "AAI"))
				{
					
				   player.addChatMessage("!!! [Area Defence] You  are searched for illegal goods!!!");
					
					  ContraList.clear();
					  
						for (int place = 5; place < 15; place++) {
							if (getStackInSlot(place) != null) 
							{
								ContraList.add(getStackInSlot(place).getItem());
							}
						}	
				   
				   
				  switch(this.getcontratyp())
				  {
				  case 0:
					  

					  
						for(int i=0; i<4;i++) {
							if(player.inventory.armorInventory[i] != null){
							
								if(!ContraList.contains(player.inventory.armorInventory[i].getItem()))
								{	
									StacksToInventory(this,player.inventory.armorInventory[i],true);
									player.inventory.armorInventory[i]=null;
							        cap.consumForcePower(ModularForceFieldSystem.DefenceStationSearchForceEnergy);
								}
							}
						}
					  
					  
						for(int i=0; i<36;i++) {

							if(player.inventory.mainInventory[i] != null){
							
								if(!ContraList.contains(player.inventory.mainInventory[i].getItem()))
								{	
									StacksToInventory(this,player.inventory.mainInventory[i],true);
									player.inventory.mainInventory[i]=null;
							        cap.consumForcePower(ModularForceFieldSystem.DefenceStationSearchForceEnergy);
								}
							}
						}
					  

					  break;
				  case 1:
					  
					  
						for(int i=0; i<4;i++) {
							if(player.inventory.armorInventory[i] != null){
							
								if(ContraList.contains(player.inventory.armorInventory[i].getItem()))
								{	
									StacksToInventory(this,player.inventory.armorInventory[i],true);
									player.inventory.armorInventory[i]=null;
							        cap.consumForcePower(ModularForceFieldSystem.DefenceStationSearchForceEnergy);
								}
							}
						}
					  
					  
						for(int i=0; i<36;i++) {
							if(player.inventory.mainInventory[i] != null){
							
								if(ContraList.contains(player.inventory.mainInventory[i].getItem()))
								{	
									StacksToInventory(this,player.inventory.mainInventory[i],true);
									player.inventory.mainInventory[i]=null;
							        cap.consumForcePower(ModularForceFieldSystem.DefenceStationSearchForceEnergy);
								}
							}
						}
					  

					  break;
				  
				  
				  }
				
				}
			}
		break;
		}
		}
		}
	}
	

	public void updateEntity() {
		if (worldObj.isRemote == false) {
			if (this.isCreate() && this.getLinkCapacitor_ID() != 0) {
				addtogrid();
				this.setCreate(false);
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

			if (getOnOffSwitch() &&  getLinkedCapacitor()!= null && getLinkedSecurityStation()!=null
					&& !isActive())
				setActive(true);

			if ((!getOnOffSwitch() || getLinkedCapacitor()==null || getLinkedSecurityStation()==null) && isActive())
				setActive(false);


			
			if(this.isActive())
			{
				scanner();				
			}
			

			if (this.getTicker() == 100) {
				if(this.isActive())
				{
					DefenceAction();
				}
				this.setTicker((short) 0);
			}
			this.setTicker((short) (this.getTicker() + 1));
		} else {
			if(Defstation_ID==0)
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
	public ItemStack decrStackSize(int i, int j) {
		if (Inventory[i] != null) {
			if (Inventory[i].stackSize <= j) {
				ItemStack itemstack = Inventory[i];
				Inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = Inventory[i].splitStack(j);
			if (Inventory[i].stackSize == 0) {
				Inventory[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		Inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
			
		}
	}


	@Override
	public ItemStack getStackInSlot(int i) {
		return Inventory[i];
	}
	@Override
	public String getInvName() {
		return "Defstation";
	}

	@Override
	public int getSizeInventory() {
		return Inventory.length;
	}


	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerAreaDefenseStation(inventoryplayer.player, this);
	}


	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 15;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 20;
	}

	@Override
	public void onNetworkHandlerUpdate(String field){ 
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void onNetworkHandlerEvent(String event) {
		
		if(!this.isActive()){
		
		if (Integer.parseInt(event) == 0) {
			if (this.getswitchtyp() == 0) {
				this.setswitchtyp(1);
			} else {
				this.setswitchtyp(0);
			}
		}
		
		if (Integer.parseInt(event) == 1) {
			
			if (this.getcontratyp() == 0) {
				this.setcontratyp(1);
			} else {
				this.setcontratyp(0);
			}
		}
		
		if (Integer.parseInt(event) == 2) {
			
			   if(getActionmode() == 5)
			   {
				   setActionmode(0);
		       }else{
		    	   setActionmode(getActionmode()+1);
		       }  
		}
		
		if (Integer.parseInt(event) == 3) {
			
			if (this.getScanmode() == 0) {
				this.setScanmode(1);
			} else {
				this.setScanmode(0);
			}
		} 
		}
		
	}
	
	@Override
	public List<String> getFieldsforUpdate() {
		List<String> NetworkedFields = new LinkedList<String>();
		NetworkedFields.clear();

		NetworkedFields.add("active");
		NetworkedFields.add("side");
		NetworkedFields.add("Defstation_ID");

		return NetworkedFields;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		
		switch(Slot)
		{
		case 0:
		     if(par1ItemStack.getItem() instanceof ItemCardPowerLink)
	        	return true;
		break;
		case 1:
			if(par1ItemStack.getItem() instanceof ItemCardSecurityLink)
				return true;
		break;
		case 2:
		case 3:
			if(par1ItemStack.getItem() instanceof ItemProjectorFieldModulatorDistance)
				return true;
		break;
		}
		
		if(Slot>= 5 && Slot <=14)
			return true;
		

		return false;
	}

	@Override
	public int getSlotStackLimit(int Slot){

		switch(Slot){
		
		case 0:
		case 1:
			
			return 1;
		
		case 2: //Distance mod
		case 3:
			return 64;
		}
		
		if(Slot>= 5 && Slot <=14)
			return 1;
		
		if(Slot>= 5 && Slot <=14)
			return 1;
		
		return 64;
	}
}

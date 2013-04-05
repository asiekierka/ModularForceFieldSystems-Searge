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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.Block;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

import chb.mods.mffs.common.IModularProjector.Slots;
import chb.mods.mffs.common.modules.Module3DBase;
import chb.mods.mffs.common.modules.ModuleBase;
import chb.mods.mffs.common.options.IChecksOnAll;
import chb.mods.mffs.common.options.IInteriorCheck;
import chb.mods.mffs.common.options.ItemProjectorOptionBase;
import chb.mods.mffs.common.options.ItemProjectorOptionBlockBreaker;
import chb.mods.mffs.common.options.ItemProjectorOptionCamoflage;
import chb.mods.mffs.common.options.ItemProjectorOptionDefenseStation;
import chb.mods.mffs.common.options.ItemProjectorOptionFieldFusion;
import chb.mods.mffs.common.options.ItemProjectorOptionFieldManipulator;
import chb.mods.mffs.common.options.ItemProjectorOptionForceFieldJammer;
import chb.mods.mffs.common.options.ItemProjectorOptionMobDefence;
import chb.mods.mffs.common.options.ItemProjectorOptionSponge;
import chb.mods.mffs.common.options.ItemProjectorOptionTouchDamage;
import chb.mods.mffs.network.INetworkHandlerEventListener;
import chb.mods.mffs.network.INetworkHandlerListener;
import chb.mods.mffs.network.NetworkHandlerClient;
import chb.mods.mffs.network.NetworkHandlerServer;

public class TileEntityProjector extends TileEntityMachines implements IModularProjector,
INetworkHandlerEventListener,INetworkHandlerListener,ISwitchabel{
	private ItemStack ProjektorItemStacks[];

	private int[] focusmatrix = { 0, 0, 0, 0 }; // Up 7,Down 8,Right 9,Left 10
	private String ForceFieldTexturids ="-76/-76/-76/-76/-76/-76";
	private String ForceFieldTexturfile = "/terrain.png";
	private int ForcefieldCamoblockid;
	private int ForcefieldCamoblockmeta;
	private int switchdelay;
	private short forcefieldblock_meta;
	private int ProjektorTyp;
	private int Projektor_ID;
	private int linkPower;
	private int blockcounter;
	private boolean burnout;
	private int accesstyp;
	private int SwitchTyp;
	private boolean OnOffSwitch;
	private int capacity;
	private boolean Create;

	protected Stack<Integer> field_queue = new Stack<Integer>();
	protected Set<PointXYZ> field_interior = new HashSet<PointXYZ>();
	protected Set<PointXYZ> field_def = new HashSet<PointXYZ>();
	
	public TileEntityProjector() {
		Random random = new Random();

		ProjektorItemStacks = new ItemStack[13];
		Projektor_ID = 0;
		linkPower = 0;
		forcefieldblock_meta =  (short) ForceFieldTyps.Default.ordinal();
		ProjektorTyp = 0;
		switchdelay = 0;
		burnout = false;
		accesstyp = 0;
		SwitchTyp = 0;
		OnOffSwitch = false;
		capacity = 0;
		Create = true;
	}

	// Start Getter AND Setter

	public int getCapacity(){
		return capacity;
	}

	public void setCapacity(int Capacity){
		this.capacity = Capacity;
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


	public int getaccesstyp() {
		return accesstyp;
	}

	public void setaccesstyp(int accesstyp) {
		this.accesstyp = accesstyp;
	}

	
	
	public int getForcefieldCamoblockmeta() {
		return ForcefieldCamoblockmeta;
	}

	public void setForcefieldCamoblockmeta(int forcefieldCamoblockmeta) {
		ForcefieldCamoblockmeta = forcefieldCamoblockmeta;
		NetworkHandlerServer.updateTileEntityField(this, "ForcefieldCamoblockmeta");
	}

	public int getForcefieldCamoblockid() {
		return ForcefieldCamoblockid;
	}

	public void setForcefieldCamoblockid(int forcefieldCamoblockid) {
		ForcefieldCamoblockid = forcefieldCamoblockid;
		NetworkHandlerServer.updateTileEntityField(this, "ForcefieldCamoblockid");
	}

	public String getForceFieldTexturfile() {
		return ForceFieldTexturfile;
	}

	public void setForceFieldTexturfile(String forceFieldTexturfile) {
		ForceFieldTexturfile = forceFieldTexturfile;
		NetworkHandlerServer.updateTileEntityField(this, "ForceFieldTexturfile");
	}
	
	public String getForceFieldTexturID() {
		return ForceFieldTexturids;
	}

	public void setForceFieldTexturID(String forceFieldTexturids) {
		ForceFieldTexturids = forceFieldTexturids;
		NetworkHandlerServer.updateTileEntityField(this, "ForceFieldTexturids");
	}
	

	public int getProjektor_Typ() {
		return ProjektorTyp;
	}


	public void setProjektor_Typ(int ProjektorTyp) {
		this.ProjektorTyp = ProjektorTyp;

		NetworkHandlerServer.updateTileEntityField(this, "ProjektorTyp");
	}

	
	public int getBlockcounter() {
		return blockcounter;
	}

	public boolean isCreate() {
		return Create;
	}

	public void setCreate(boolean create) {
		this.Create = create;
	}

	public int getforcefieldblock_meta() {
		return forcefieldblock_meta;
	}

	public void setforcefieldblock_meta(int ffmeta) {
		this.forcefieldblock_meta =  (short) ffmeta;
	}


	public int getLinkPower() {
		return linkPower;
	}

	public void setLinkPower(int linkPower) {
		this.linkPower = linkPower;
	}


	@Override
	public int getProjektor_ID() {
		return Projektor_ID;
	}


	public void ProjektorBurnout() {
		this.setBurnedOut(true);
		dropplugins();
	}


	public boolean isBurnout() {
		return burnout;
	}

	public void setBurnedOut(boolean b) {
		burnout = b;
		NetworkHandlerServer.updateTileEntityField(this, "burnout");
	}

	

	// End Getter AND Setter

	// Start NBT Read/ Save

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		SwitchTyp = nbttagcompound.getInteger("SwitchTyp");
		OnOffSwitch = nbttagcompound.getBoolean("OnOffSwitch");
		accesstyp = nbttagcompound.getInteger("accesstyp");
		burnout = nbttagcompound.getBoolean("burnout");
		Projektor_ID = nbttagcompound.getInteger("Projektor_ID");
		ProjektorTyp = nbttagcompound.getInteger("Projektor_Typ");
		forcefieldblock_meta = nbttagcompound.getShort("forcefieldblockmeta");

		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		ProjektorItemStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < ProjektorItemStacks.length) {
				ProjektorItemStacks[byte0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("SwitchTyp", SwitchTyp);
		nbttagcompound.setBoolean("OnOffSwitch", OnOffSwitch);
		nbttagcompound.setInteger("accesstyp", accesstyp);
		nbttagcompound.setBoolean("burnout", burnout);
		nbttagcompound.setInteger("Projektor_ID", Projektor_ID);
		nbttagcompound.setInteger("Projektor_Typ", ProjektorTyp);
		nbttagcompound.setShort("forcefieldblockmeta", forcefieldblock_meta);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < ProjektorItemStacks.length; i++) {
			if (ProjektorItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				ProjektorItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("Items", nbttaglist);
	}

	// Stop NBT Read/ Save

	// Start Slot / Upgrades System

	public void dropplugins() {
		for (int a = 0; a < this.ProjektorItemStacks.length; a++) {
			dropplugins(a,this);
		}
	}

	
	@Override
	public void onInventoryChanged()
	{
		getLinkedSecurityStation();
		checkslots();
	}
	
	
	public void checkslots() {
		
		
		if (hasValidTypeMod()){
	           
			if(getProjektor_Typ()!= ProjectorTyp.TypfromItem(get_type()).ProTyp) 
			setProjektor_Typ(ProjectorTyp.TypfromItem(get_type()).ProTyp);
			
			if(getforcefieldblock_meta() != get_type().getForceFieldTyps().ordinal())
			setforcefieldblock_meta(get_type().getForceFieldTyps().ordinal());

			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			
		}else{
			if(getProjektor_Typ()!= 0) {setProjektor_Typ(0);}
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		
		

		// Focus function

		if (hasValidTypeMod()) {
		for (int place = 7; place < 11; place++) {
		if (getStackInSlot(place) != null) {
			if (getStackInSlot(place).getItem() == ModularForceFieldSystem.MFFSitemFocusmatix) {
				switch(ProjectorTyp.TypfromItem(get_type()).ProTyp)
				{
				case 6:
					focusmatrix[place-7] = getStackInSlot(place).stackSize+1;
				break;
				case 7:
					focusmatrix[place-7] = getStackInSlot(place).stackSize+2;
				break;
				default:
					focusmatrix[place-7] = getStackInSlot(place).stackSize;
				break;
				}
			} else {
				dropplugins(place,this);
			}
		} else {
			switch(ProjectorTyp.TypfromItem(get_type()).ProTyp)
			{
			case 6:
				focusmatrix[place-7] = 1;
			break;
			case 7:
				focusmatrix[place-7] = 2;
			break;
			default:
				focusmatrix[place-7] = 0;
			break;
			}
		}
		}
		}
		

		if (getStackInSlot(11) != null) {
			
	
			
			if(getStackInSlot(11).itemID <4095){

		    String ForcefieldTexturtemp ="180/180/180/180/180/180";
			String Texturfile = "/terrain.png";

			int[] index = new int[6];
			for (int side = 0; side < 6; side++){
			  index[side] = Block.blocksList[getStackInSlot(11).itemID].getBlockTextureFromSideAndMetadata(side, getStackInSlot(11).getItemDamage());
			}
			  ForcefieldTexturtemp = index[0]+"/"+index[1]+"/"+index[2]+"/"+index[3]+"/"+index[4]+"/"+index[5];
			  Texturfile = Block.blocksList[getStackInSlot(11).itemID].getTextureFile();
			
			  
					if( !ForcefieldTexturtemp.equalsIgnoreCase(ForceFieldTexturids)  || !ForceFieldTexturfile.equalsIgnoreCase(getForceFieldTexturfile()))
					{
						if (getStackInSlot(11).getItem() == Item.bucketLava)
							this.setForceFieldTexturID("237/237/239/254/255/255");	
						
						if (getStackInSlot(11).getItem() == Item.bucketWater)
							 this.setForceFieldTexturID("205/205/207/222/223/223");
						
						if(getStackInSlot(11).getItem() != Item.bucketLava && getStackInSlot(11).getItem() != Item.bucketWater) 
							this.setForceFieldTexturID(ForcefieldTexturtemp);
							
						this.setForcefieldCamoblockmeta(getStackInSlot(11).getItemDamage());
						this.setForcefieldCamoblockid(getStackInSlot(11).itemID);
						this.setForceFieldTexturfile(Texturfile);
						UpdateForcefieldTexttur();

					}
					
		}else{
			
			this.dropplugins(11, this);
		}

		
		}else {
			if(!ForceFieldTexturids.equalsIgnoreCase("-76/-76/-76/-76/-76/-76"))
			{
				this.setForceFieldTexturID("-76/-76/-76/-76/-76/-76");
				this.setForceFieldTexturfile("/terrain.png");
				UpdateForcefieldTexttur();
		    }
		}


		if(hasOption(ModularForceFieldSystem.MFFSProjectorOptionCamouflage))
			if(getforcefieldblock_meta() !=ForceFieldTyps.Camouflage.ordinal()){
				setforcefieldblock_meta((short) ForceFieldTyps.Camouflage.ordinal());
			}
		
		
		if(hasOption(ModularForceFieldSystem.MFFSProjectorOptionZapper))
			if(getforcefieldblock_meta() !=ForceFieldTyps.Zapper.ordinal()){
				setforcefieldblock_meta((short) ForceFieldTyps.Zapper.ordinal());
			}
		
		
		if(hasOption(ModularForceFieldSystem.MFFSProjectorOptionFieldFusion)){
			if(!Linkgrid.getWorldMap(worldObj).getFieldFusion().containsKey(getProjektor_ID()))
				Linkgrid.getWorldMap(worldObj).getFieldFusion().put(getProjektor_ID(), this);
			}else{
			if(Linkgrid.getWorldMap(worldObj).getFieldFusion().containsKey(getProjektor_ID()))
				Linkgrid.getWorldMap(worldObj).getFieldFusion().remove(getProjektor_ID());
		 }
		
		if(hasOption(ModularForceFieldSystem.MFFSProjectorOptionForceFieldJammer)){
			if(!Linkgrid.getWorldMap(worldObj).getJammer().containsKey(getProjektor_ID()))
				Linkgrid.getWorldMap(worldObj).getJammer().put(getProjektor_ID(), this);
			}else{
			if(Linkgrid.getWorldMap(worldObj).getJammer().containsKey(getProjektor_ID()))
				Linkgrid.getWorldMap(worldObj).getJammer().remove(getProjektor_ID());
		 }
		

		
		if (hasValidTypeMod()) {
			ModuleBase modTyp = get_type();				
			
			if (!modTyp.supportsStrength())
				dropplugins(6,this);
			if (!modTyp.supportsDistance())
				dropplugins(5,this);
			
			if (!modTyp.supportsMatrix()){
				dropplugins(7,this);
			    dropplugins(8,this);
				dropplugins(9,this);
			    dropplugins(10,this);
			}
			
			if(!this.hasOption(ModularForceFieldSystem.MFFSProjectorOptionCamouflage))
				dropplugins(11,this);
			
		} else {
			for (int spot = 2; spot <= 10; spot++) {
				dropplugins(spot, this);
			}
		}
		
		
		

	}

	private void UpdateForcefieldTexttur() {
		if(this.isActive() && this.hasOption(ModularForceFieldSystem.MFFSProjectorOptionCamouflage))
		{
		for (PointXYZ png : field_def) {
			
		     if (worldObj.getChunkFromBlockCoords(png.X, png.Z).isChunkLoaded) {
			
				
				TileEntity tileEntity = worldObj.getBlockTileEntity(png.X, png.Y, png.Z);

				if(tileEntity != null && tileEntity instanceof TileEntityForceField )
				{
					((TileEntityForceField)tileEntity).UpdateTextur();
				}
			
		    }
	    	}
		}
	  }
	

	// Stop Slot / Upgrades System

	public void updateEntity() {
		if (worldObj.isRemote == false) {
			

			if (this.isCreate() && this.getLinkCapacitor_ID() != 0) {
				addtogrid();
				checkslots();
				if (this.isActive()) {
					calculateField(true);
				}
				this.setCreate(false);
			}
		
			
			TileEntityCapacitor cap = getLinkedCapacitor();
			
			if(cap != null){
				setLinkPower(cap.getForcePower());
                setCapacity(cap.getCapacity());
			}else{
				setLinkPower(0);
				setCapacity(0);
			}




			boolean powerdirekt = worldObj.isBlockGettingPowered(xCoord,
					yCoord, zCoord);
			boolean powerindrekt = worldObj.isBlockIndirectlyGettingPowered(
					xCoord, yCoord, zCoord);

			if(this.getswitchtyp()==0)
			{
		    this.setOnOffSwitch((powerdirekt || powerindrekt));
			}

			if ((getOnOffSwitch() && (switchdelay >= 40))
					&& hasValidTypeMod() && this.getLinkedCapacitor()  !=null
					&& this.getLinkPower() > Forcepowerneed(5)) {
				if (isActive() != true) {
					setActive(true);
					switchdelay = 0;
					if(calculateField(true))
					{
					FieldGenerate(true);
					}
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
			if ((!getOnOffSwitch() && switchdelay >= 40)
					|| !hasValidTypeMod() || this.getLinkedCapacitor() ==null || burnout
					|| this.getLinkPower() <= Forcepowerneed(1)) {
				if (isActive() != false) {
					setActive(false);
					switchdelay = 0;
					destroyField();
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}

			if (this.getTicker() == 20) {

				if (isActive()) {
					FieldGenerate(false);
					
					if(hasOption(ModularForceFieldSystem.MFFSProjectorOptionMoobEx))
						ItemProjectorOptionMobDefence.ProjectorNPCDefence(this, worldObj);
		
					
					if(hasOption(ModularForceFieldSystem.MFFSProjectorOptionDefenceStation))
						ItemProjectorOptionDefenseStation.ProjectorPlayerDefence(this, worldObj);


				}

				this.setTicker((short) 0);
			}

			this.setTicker((short) (this.getTicker() + 1));
		}else{
			
		
			if(this.getProjektor_ID()==0)
			{
				if (this.getTicker() >= 20+random.nextInt(20)) {
					NetworkHandlerClient.requestInitialData(this,true);

					this.setTicker((short) 0);
				}

				this.setTicker((short) (this.getTicker() + 1));
			}
		}

		switchdelay++;
	}
	
	private boolean calculateField(boolean addtoMap){ //Should only be called when being turned on after setting changes or on first on.
		field_def.clear();
		field_interior.clear();
		if (hasValidTypeMod()){
			Set<PointXYZ> tField = new HashSet<PointXYZ>();
			Set<PointXYZ> tFieldInt = new HashSet<PointXYZ>();
			
			if (get_type() instanceof Module3DBase){
				((Module3DBase)get_type()).calculateField(this, tField, tFieldInt);
			}else{
				get_type().calculateField(this, tField);
			}
			
			for (PointXYZ pnt : tField){
				
				if(pnt.Y+this.yCoord<255){
				PointXYZ tp  = new PointXYZ(pnt.X+this.xCoord,pnt.Y+this.yCoord,pnt.Z+this.zCoord,worldObj);
				
				if (Forcefielddefine(tp,addtoMap))
					{
					field_def.add(tp);
					}else{return false;}
				}
			}
			for (PointXYZ pnt : tFieldInt){
				
				
				if(pnt.Y+this.yCoord<255){
				PointXYZ tp  = new PointXYZ(pnt.X+this.xCoord,pnt.Y+this.yCoord,pnt.Z+this.zCoord,worldObj);
				
				if (calculateBlock(tp))
					{field_interior.add(tp);}else{return false;}
				}
			}
			
			
			return true;
		}
		return false;
	}

	
	
	
	public boolean calculateBlock(PointXYZ pnt){

		for(ItemProjectorOptionBase opt : getOptions())
		{
			if(opt instanceof IInteriorCheck)
				((IInteriorCheck)opt).checkInteriorBlock(pnt, worldObj, this);	
						
		}
		return true;
	}
	



	public boolean Forcefielddefine(PointXYZ png,boolean addtoMap)
	{
		
		for(ItemProjectorOptionBase opt : getOptions())
		{
		
				if(opt instanceof ItemProjectorOptionForceFieldJammer){
					if(((ItemProjectorOptionForceFieldJammer)opt).CheckJammerinfluence(png, worldObj, this))
			            return false;
				}
				
				
				if(opt instanceof ItemProjectorOptionFieldFusion){
					if(((ItemProjectorOptionFieldFusion)opt).checkFieldFusioninfluence(png, worldObj, this))
			            return true;
				}
	
		}
		
		
		ForceFieldBlockStack ffworldmap = WorldMap
				.getForceFieldWorld(worldObj)
				.getorcreateFFStackMap(png.X, png.Y, png.Z,worldObj);

		if(!ffworldmap.isEmpty())
		{
			if(ffworldmap.getProjectorID() != getProjektor_ID()){
			    ffworldmap.removebyProjector(getProjektor_ID());
				ffworldmap.add(getLinkCapacitor_ID(), getProjektor_ID(), getforcefieldblock_meta());
			    }
		}else{
			ffworldmap.add(getLinkCapacitor_ID(), getProjektor_ID(), getforcefieldblock_meta());
			ffworldmap.setSync(false);
		}

		field_queue.push(WorldMap.Cordhash(png.X, png.Y, png.Z));

		return true;
	}
	


	public void FieldGenerate(boolean init) {
		TileEntityCapacitor tileEntity = getLinkedCapacitor();
			if (tileEntity != null) {
				int cost = 0;

				if (init) {
					cost = ModularForceFieldSystem.forcefieldblockcost
							* ModularForceFieldSystem.forcefieldblockcreatemodifier;
				} else {
					cost = ModularForceFieldSystem.forcefieldblockcost;
				}

				if (getforcefieldblock_meta() == 1) {
					cost *= ModularForceFieldSystem.forcefieldblockzappermodifier;
				}

				((TileEntityCapacitor) tileEntity).consumForcePower(cost* field_def.size());
			}

		blockcounter = 0;
		
		

		for (PointXYZ pnt : field_def) {
			if (blockcounter == ModularForceFieldSystem.forcefieldmaxblockpeerTick) {
				break;
			}
			ForceFieldBlockStack ffb = WorldMap.getForceFieldWorld(worldObj).getForceFieldStackMap(pnt.X, pnt.Y, pnt.Z);

			
			if(ffb!=null){
				
			 PointXYZ png = ffb.getPoint();	
 
		     if (worldObj.getChunkFromBlockCoords(png.X,png.Z).isChunkLoaded) {
		    	 if(!ffb.isEmpty()){
		            	if (ffb.getProjectorID() == getProjektor_ID()){
					if (hasOption(ModularForceFieldSystem.MFFSProjectorOptionCutter)) {
						int blockid = worldObj.getBlockId(png.X,png.Y,png.Z);
						TileEntity entity = worldObj.getBlockTileEntity(png.X,png.Y,png.Z);
						
						if (blockid != ModularForceFieldSystem.MFFSFieldblock.blockID
								&& blockid != 0
								&& blockid != Block.bedrock.blockID
								&& entity == null) 
						
						{
							ArrayList<ItemStack> stacks = Functions
									.getItemStackFromBlock(worldObj,png.X,png.Y,png.Z);
							worldObj.setBlockWithNotify(png.X,png.Y,png.Z, 0);

							if (ProjectorTyp.TypfromItem(get_type()).Blockdropper && stacks != null) {
								IInventory inventory = InventoryHelper.findAttachedInventory(worldObj,this.xCoord,this.yCoord,this.zCoord);
								if (inventory != null) {
									if (inventory.getSizeInventory() > 0) {
										InventoryHelper.addStacksToInventory(inventory, stacks);
									}
								}
							}
						}
					}

						if (worldObj.getBlockMaterial(png.X,png.Y,png.Z).isLiquid() || worldObj.isAirBlock(png.X,png.Y,png.Z) || worldObj.getBlockId(png.X,png.Y,png.Z) == ModularForceFieldSystem.MFFSFieldblock.blockID) {
						if (worldObj.getBlockId(png.X,png.Y,png.Z) != ModularForceFieldSystem.MFFSFieldblock.blockID) {
							worldObj.setBlockAndMetadataWithNotify(png.X,png.Y,png.Z,
									ModularForceFieldSystem.MFFSFieldblock.blockID,
									ffb.getTyp());
							blockcounter++; // Count create blocks...
						}
						ffb.setSync(true);
					}
				}
			}
			}
		}
		}
	}



	public void destroyField() {
		while(!field_queue.isEmpty()){
			ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(
					worldObj).getForceFieldStackMap(field_queue.pop());

			if(!ffworldmap.isEmpty())
			{
				if (ffworldmap.getProjectorID() == getProjektor_ID()) {
					ffworldmap.removebyProjector(getProjektor_ID());

					if(ffworldmap.isSync())
					{
						PointXYZ png = ffworldmap.getPoint();
						worldObj.removeBlockTileEntity(png.X,png.Y,png.Z);
						worldObj.setBlockWithNotify(png.X,png.Y,png.Z, 0);
					}

					ffworldmap.setSync(false);
				}else{
					ffworldmap.removebyProjector(getProjektor_ID());
				}
		  }
		}

		Map<Integer, TileEntityProjector> FieldFusion = Linkgrid.getWorldMap(worldObj).getFieldFusion();
		for (TileEntityProjector tileentity : FieldFusion.values()) {
		 if(tileentity.getLinkCapacitor_ID() == this.getLinkCapacitor_ID())
		 {
			 if(tileentity.isActive())
			 {
				 tileentity.calculateField(false);
			 }
		 }
	  }
	}

	public void addtogrid() {
		if (Projektor_ID == 0) {
			Projektor_ID = Linkgrid.getWorldMap(worldObj)
					.newID(this);
		}
		Linkgrid.getWorldMap(worldObj).getProjektor().put(Projektor_ID, this);
	}

	public void removefromgrid() {
		Linkgrid.getWorldMap(worldObj).getProjektor().remove(getProjektor_ID());
		dropplugins();
		destroyField();
	}

	public int Forcepowerneed(int factor) {
		if (!field_def.isEmpty()) {
			return field_def.size()
					* ModularForceFieldSystem.forcefieldblockcost;
		}

		int forcepower = 0;
		int blocks = 0;

		int tmplength = 1;

		if (this.countItemsInSlot(Slots.Strength) != 0) {
			tmplength = this.countItemsInSlot(Slots.Strength);
		}

		switch (this.getProjektor_Typ()) {
		case 1:
			blocks = ((this.countItemsInSlot(Slots.FocusDown) + this.countItemsInSlot(Slots.FocusLeft)
					+ this.countItemsInSlot(Slots.FocusRight) + this.countItemsInSlot(Slots.FocusUp)) + 1)
					* tmplength;
			break;
		case 2:
			blocks = (this.countItemsInSlot(Slots.FocusDown) + this.countItemsInSlot(Slots.FocusUp) + 1)
					* (this.countItemsInSlot(Slots.FocusLeft) + this.countItemsInSlot(Slots.FocusRight) + 1);
			break;
		case 3:
			blocks = (((this.countItemsInSlot(Slots.Distance) + 2 + this.countItemsInSlot(Slots.Distance) + 2) * 4) + 4)
					* (this.countItemsInSlot(Slots.Strength) + 1);
			break;
		case 4:
		case 5:
			blocks = (this.countItemsInSlot(Slots.Distance) * this.countItemsInSlot(Slots.Distance)) * 6;
			break;
		}

		forcepower = blocks * ModularForceFieldSystem.forcefieldblockcost;
		if (factor != 1) {
			forcepower = (forcepower * ModularForceFieldSystem.forcefieldblockcreatemodifier)
					+ (forcepower * 5);
		}
		return forcepower;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (ProjektorItemStacks[i] != null) {
			if (ProjektorItemStacks[i].stackSize <= j) {
				ItemStack itemstack = ProjektorItemStacks[i];
				ProjektorItemStacks[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = ProjektorItemStacks[i].splitStack(j);
			if (ProjektorItemStacks[i].stackSize == 0) {
				ProjektorItemStacks[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		ProjektorItemStacks[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}


	@Override
	public ItemStack getStackInSlot(int i) {
		return ProjektorItemStacks[i];
	}

	@Override
	public String getInvName() {
		return "Projektor";
	}
    
    @Override
	public int getSizeInventory() {
		return ProjektorItemStacks.length;
	}



	@Override
	public Container getContainer(InventoryPlayer inventoryplayer) {
		return new ContainerProjector(inventoryplayer.player, this);
	}


	@Override
	public int getStartInventorySide(ForgeDirection side) {
		return 11;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 1;
	}

	@Override
	public void onNetworkHandlerEvent(String event) {
		
		
		switch(Integer.parseInt(event))
		   {
		   case 0:

			   if(getaccesstyp()!=3)
			   {
			   if(getaccesstyp() == 2)
			   {
				   setaccesstyp(0);
		       }else{
		    	   setaccesstyp(getaccesstyp()+1);
		       }
			   }

		   break;
		   case 1:
			if(this.getswitchtyp() == 0)
			{
				this.setswitchtyp(1);
			}else{
				this.setswitchtyp(0);
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

		NetworkedFields.add("ProjektorTyp");
		NetworkedFields.add("active");
		NetworkedFields.add("side");
		NetworkedFields.add("burnout");
		NetworkedFields.add("camoflage");
		NetworkedFields.add("Projektor_ID");
		NetworkedFields.add("ForceFieldTexturfile");
		NetworkedFields.add("ForceFieldTexturids");
		NetworkedFields.add("ForcefieldCamoblockid");
		NetworkedFields.add("ForcefieldCamoblockmeta");
	

		return NetworkedFields;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack, int Slot) {
		
		if(Slot == 1 &&  par1ItemStack.getItem() instanceof ModuleBase)return true;
		if(Slot==0 && par1ItemStack.getItem() instanceof ItemCardPowerLink)return true;
		if(Slot == 11 && par1ItemStack.itemID < 4096  && this.hasOption(ModularForceFieldSystem.MFFSProjectorOptionCamouflage))return true;
		if(Slot==12 && par1ItemStack.getItem() instanceof ItemCardSecurityLink)return true;	
		
		if(hasValidTypeMod())
		{
			ModuleBase modTyp = get_type();
			
			switch(Slot)
			{
			case 5:
				if(par1ItemStack.getItem() instanceof ItemProjectorFieldModulatorDistance )return modTyp.supportsDistance();	
			break;
			case 6:
				if(par1ItemStack.getItem() instanceof ItemProjectorFieldModulatorStrength )return modTyp.supportsStrength();	
			break;
			
			case 7:
			case 8:
			case 9:
			case 10:
				if(par1ItemStack.getItem() instanceof ItemProjectorFocusMatrix )return modTyp.supportsMatrix();
					
			break;
			
			case 2:
			case 3:
			case 4:
				if(par1ItemStack.getItem() instanceof ItemProjectorOptionBase)
					return modTyp.supportsOption(par1ItemStack.getItem());
					
			break;
			}
			
		}

		return false;
	}
	@Override
	public int getSlotStackLimit(int Slot){
		switch(Slot){
		case 5: //Distance Slot
		case 6: //Strength Slot
			return 64;

		case 7: //Focus Up
		case 8: //Focus Down
		case 9: //Focus right
		case 10: //Focus left
			return 64;
		}

		return 1;
	}
	
	public boolean hasValidTypeMod(){
		
		if (this.getStackInSlot(1) != null && getStackInSlot(1).getItem() instanceof ModuleBase)
			return true;
		return false;
	}
	
	public ModuleBase get_type(){
		if (hasValidTypeMod())
			return (ModuleBase)this.getStackInSlot(1).getItem();
		
		return null;
	}


	@Override
	public Set<PointXYZ> getInteriorPoints() {
		return field_interior;
	}
	
	public Set<PointXYZ> getfield_queue()
	{
		return field_def;
	}
	
	
	public TileEntityAdvSecurityStation getLinkedSecurityStation()
	{
		TileEntityAdvSecurityStation sec =ItemCardSecurityLink.getLinkedSecurityStation(this, 12, worldObj);
		if(sec != null)
		{
            if(this.getaccesstyp()!=3)
                this.setaccesstyp(3);
            return sec;
		}
		
        if(this.getaccesstyp()==3)
            this.setaccesstyp(0);
		return null;
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
	
	
	
	
	public boolean hasOption(Item item){
		
		for(ItemProjectorOptionBase opt : getOptions()){
			if(opt == item)
			 return true;
		}
		return false;
	}

		
	
	public List<ItemProjectorOptionBase> getOptions(){
		List<ItemProjectorOptionBase> ret = new ArrayList<ItemProjectorOptionBase>();
		for (int place = 2; place < 5; place++) {
			if (getStackInSlot(place) != null) {
				ret.add((ItemProjectorOptionBase)(getStackInSlot(place).getItem()));
			}
			
			for (ItemProjectorOptionBase opt : ItemProjectorOptionBase.get_instances()) {
				if (opt instanceof IChecksOnAll && !ret.contains(opt))
					ret.add(opt);
			}
			
		}

		return ret;
	}
	

	
}

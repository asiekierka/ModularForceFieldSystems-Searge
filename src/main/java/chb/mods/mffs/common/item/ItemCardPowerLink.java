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

package chb.mods.mffs.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chb.mods.mffs.api.IForceEnergyStorageBlock;
import chb.mods.mffs.api.IPowerLinkItem;
import chb.mods.mffs.api.PointXYZ;
import chb.mods.mffs.common.Functions;
import chb.mods.mffs.common.Linkgrid;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.SecurityHelper;
import chb.mods.mffs.common.SecurityRight;
import chb.mods.mffs.common.tileentity.TileEntityAreaDefenseStation;
import chb.mods.mffs.common.tileentity.TileEntityCapacitor;
import chb.mods.mffs.common.tileentity.TileEntityConverter;
import chb.mods.mffs.common.tileentity.TileEntityExtractor;
import chb.mods.mffs.common.tileentity.TileEntityMachines;
import chb.mods.mffs.common.tileentity.TileEntityProjector;

public class ItemCardPowerLink extends ItemCard implements IPowerLinkItem{

	public IForceEnergyStorageBlock storage;
	
	public ItemCardPowerLink(int i) {
		super(i);
		setIconIndex(17);
	}
	
	
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5)
    {
    	super.onUpdate(itemStack, world, entity, par4, par5);
    	
    	if(Tick>600)
    	{
    		int Cap_ID =this.getValuefromKey("CapacitorID",itemStack);
    		if(Cap_ID!=0)
    		{
    			TileEntityCapacitor cap = Linkgrid.getWorldMap(world).getCapacitor().get(Cap_ID);
    			if(cap !=null)
    			{
    				if(!cap.getDeviceName().equals(this.getforAreaname(itemStack)))
    				{
    				  	this.setforArea(itemStack, cap.getDeviceName());
    				}
    			}
    		}
    		
    		Tick=0;
    	}
    	Tick++;
    }
	

	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (!world.isRemote) {
			

			if (tileEntity instanceof TileEntityConverter) {
				  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,SecurityRight.EB))
				  {

					  return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0,"<Power-Link>");

				  }
	            }
			
			
			
			if (tileEntity instanceof TileEntityProjector) {
			  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,SecurityRight.EB))
			  {

				  return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0,"<Power-Link>");

			  }
            }
			
			if (tileEntity instanceof TileEntityExtractor ) {
				  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,SecurityRight.EB))
				  {
				
						if(((TileEntityExtractor)tileEntity).getStackInSlot(1)==null)
						{
							((TileEntityExtractor)tileEntity).setInventorySlotContents(1,itemstack);
							entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
							Functions.ChattoPlayer(entityplayer, "Success: <Power-Link> Card installed");
							return true;
						}
						else
						{
							if(((TileEntityExtractor)tileEntity).getStackInSlot(1).getItem()==ModularForceFieldSystem.MFFSitemcardempty)
							{
								ItemStack itemstackcopy = itemstack.copy();
								((TileEntityExtractor)tileEntity).setInventorySlotContents(1,itemstackcopy);
								Functions.ChattoPlayer(entityplayer, "Success: <Power-Link> Card data copied ");
								return true;
							}
							Functions.ChattoPlayer(entityplayer, "Fail: Slot is not empty");
							return false;
						}
				  }
	            }
			
			
			if (tileEntity instanceof TileEntityAreaDefenseStation) {
				  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,SecurityRight.EB))
				  {
		
					  return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0,"<Power-Link>");
				  }
			}
			
			if (tileEntity instanceof TileEntityCapacitor) {
				  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,SecurityRight.EB))
				  {
		
					  return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 2,"<Power-Link>");
				  }
			}
			
		}
		return false;
	}

	public IForceEnergyStorageBlock getForceEnergyStorageBlock(ItemStack itemStack,TileEntityMachines tem,World world)
	{
		if(itemStack!= null && itemStack.getItem() instanceof ItemCard)
		{	
		if(((ItemCard)itemStack.getItem()).isvalid(itemStack))
		{
		PointXYZ png = this.getCardTargetPoint(itemStack);
		if(png != null)
		{
		if(png.dimensionId != world.provider.dimensionId) return null;
			
		if(world.getBlockTileEntity(png.X, png.Y, png.Z) instanceof TileEntityCapacitor)
		{
			TileEntityCapacitor cap = (TileEntityCapacitor) world.getBlockTileEntity(png.X, png.Y, png.Z);
		if (cap != null){
			
		  if(cap.getPowerStorageID()== this.getValuefromKey("CapacitorID",itemStack)&&  this.getValuefromKey("CapacitorID",itemStack) != 0 )
		  {
			  
				if(!cap.getDeviceName().equals(this.getforAreaname(itemStack)))
				{
				  	this.setforArea(itemStack, cap.getDeviceName());
				}
			  
			  if(cap.getTransmitRange()>= PointXYZ.distance(tem.getMaschinePoint(),cap.getMaschinePoint())){
              return cap;}return null;
		   }
		}
	  }else{
		  
		  int Cap_ID =this.getValuefromKey("CapacitorID",itemStack);
		  if(Cap_ID!=0)
		  {
			  TileEntityCapacitor cap =  Linkgrid.getWorldMap(png.getPointWorld()).getCapacitor().get(Cap_ID);
			  if (cap != null){
				  
				  ((ItemCard)this).setInformation(itemStack, cap.getMaschinePoint(), "CapacitorID", Cap_ID);
				  if(cap.getTransmitRange()>= PointXYZ.distance(tem.getMaschinePoint(),cap.getMaschinePoint())){
				  return cap;}return null;
			  }
		  }
	
	  }
	  if(world.getChunkFromBlockCoords(png.X, png.Z).isChunkLoaded)
		  ((ItemCard)itemStack.getItem()).setinvalid(itemStack);
		  
	}
	}	
	}
	return null;
		
	}
	


	@Override
	public int getAvailablePower(ItemStack itemStack, TileEntityMachines tem,World world) {
		storage = getForceEnergyStorageBlock(itemStack,tem,world);
		if(storage != null)
		   return storage.getStorageAvailablePower();
		return 0;
	}

	@Override
	public int getMaximumPower(ItemStack itemStack, TileEntityMachines tem,World world) {
		storage = getForceEnergyStorageBlock(itemStack,tem,world);
		if(storage != null)
		   return storage.getStorageMaxPower();
		return 1;
	}
	
	@Override
	public int getPowersourceID(ItemStack itemStack, TileEntityMachines tem,World world) {
		storage = getForceEnergyStorageBlock(itemStack,tem,world);
		if(storage != null)
		   return storage.getPowerStorageID();
		return 0;
	}
	
	@Override
	public int getPercentageCapacity(ItemStack itemStack,TileEntityMachines tem,World world) {
		storage = getForceEnergyStorageBlock(itemStack,tem,world);
		if(storage != null)
		   return storage.getPercentageStorageCapacity();
		return 0;
	}
	
	@Override
	public boolean consumePower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem,World world) {
		storage = getForceEnergyStorageBlock(itemStack,tem,world);
		if(storage != null)
		   return storage.consumePowerfromStorage(powerAmount, simulation);
		return false;
	}
	
	@Override
	public boolean insertPower(ItemStack itemStack, int powerAmount,
			boolean simulation, TileEntityMachines tem, World world) {
		storage = getForceEnergyStorageBlock(itemStack,tem,world);
		if(storage != null)
		   return storage.insertPowertoStorage(powerAmount, simulation);
		return false;
	}

	@Override
	public int getfreeStorageAmount(ItemStack itemStack,
			TileEntityMachines tem, World world) {
		storage = getForceEnergyStorageBlock(itemStack,tem,world);
		if(storage != null)
		   return storage.getfreeStorageAmount();
		return 0;
	}
		
	@Override
	public boolean isPowersourceItem() {
		return false;
	}

}

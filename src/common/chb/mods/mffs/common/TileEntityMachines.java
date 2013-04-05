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

import ic2.api.IWrenchable;

import java.util.Random;

import buildcraft.api.transport.IExtractionHandler;
import buildcraft.api.transport.IPipe;
import buildcraft.api.transport.PipeManager;

import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.Container;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Slot;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.ISidedInventory;
import chb.mods.mffs.api.IMFFS_Wrench;
import chb.mods.mffs.common.IModularProjector.Slots;
import chb.mods.mffs.network.NetworkHandlerServer;

public abstract class TileEntityMachines extends TileEntity implements ISidedInventory,IMFFS_Wrench,IWrenchable,IExtractionHandler{
	private boolean active;
	private int side;
	private short ticker;
	protected Random random = new Random();
	protected Ticket chunkTicket;
	public TileEntityMachines()

	{
		active = false;
		side = -1;
		ticker = 0;
		
		PipeManager.registerExtractionHandler(this);
	}
	
	public PointXYZ getMaschinePoint()
	{
		return  new PointXYZ(this.xCoord,this.yCoord,this.zCoord,worldObj);
	}

	public  void dropplugins(int slot ,IInventory inventory ) {
	
		if(worldObj.isRemote)
			return;
			
		if (inventory.getStackInSlot(slot) != null) {
			if(inventory.getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink
		     || inventory.getStackInSlot(slot).getItem() instanceof ItemCardPowerLink
		     || inventory.getStackInSlot(slot).getItem() instanceof ItemCardPersonalID)
			{
				worldObj.spawnEntityInWorld(new EntityItem(worldObj,
						(float) this.xCoord, (float) this.yCoord,
						(float) this.zCoord, new ItemStack(ModularForceFieldSystem.MFFSitemcardempty,1)));
			}else
			{
				worldObj.spawnEntityInWorld(new EntityItem(worldObj,
						(float) this.xCoord, (float) this.yCoord,
						(float) this.zCoord, inventory.getStackInSlot(slot)));
			}

			inventory.setInventorySlotContents(slot, null);
			this.onInventoryChanged();
		}
	}

	public abstract Container getContainer(InventoryPlayer inventoryplayer);

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		side = nbttagcompound.getInteger("side");
		active = nbttagcompound.getBoolean("active");
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("side", side);
		nbttagcompound.setBoolean("active", active);
	}

	@Override
	public boolean wrenchCanManipulate(EntityPlayer entityPlayer, int side) {
		   if(!SecurityHelper.isAccessGranted(this, entityPlayer, worldObj,"EB"))
		   {return false;}

		return true;
	}

	public short getTicker() {
		return ticker;
	}

	public void setTicker(short ticker) {
		this.ticker = ticker;
	}
	@Override
	public void setSide(int i) {
		side = i;
		NetworkHandlerServer.updateTileEntityField(this, "side");
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean flag) {
		active = flag;
		NetworkHandlerServer.updateTileEntityField(this, "active");
	}

	@Override
	public int getSide() {
		return side;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		if(side == getFacing()){return false;}
		if(this instanceof TileEntitySecStorage){return false;}
		if(this instanceof TileEntityAdvSecurityStation){return false;}
		if(this.active){return false;}

		return wrenchCanManipulate(entityPlayer, side);
	}

	@Override
	public short getFacing() {
		return (short) getSide();
	}

	@Override
	public void setFacing(short facing) {
		setSide(facing);
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		if(this.active){return false;}
		return wrenchCanManipulate(entityPlayer, 0);
	}

	@Override
	public float getWrenchDropRate() {
		return 1;
	}

	public void forceChunkLoading(Ticket ticket) {
	    if (chunkTicket == null)
	    {
	     chunkTicket = ticket;
	    }
	     ChunkCoordIntPair Chunk = new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4);
	     ForgeChunkManager.forceChunk(ticket, Chunk);
	     }

	protected void registerChunkLoading()
	{
		if (chunkTicket == null)
		{
		chunkTicket = ForgeChunkManager.requestTicket(ModularForceFieldSystem.instance, worldObj, Type.NORMAL);
		}
		if (chunkTicket == null)
		{
			System.out.println("[ModularForceFieldSystem]no free Chunkloaders available");
			return;
		}

		chunkTicket.getModData().setInteger("MaschineX", xCoord);
		chunkTicket.getModData().setInteger("MaschineY", yCoord);
		chunkTicket.getModData().setInteger("MaschineZ", zCoord);
		ForgeChunkManager.forceChunk(chunkTicket, new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4));

		forceChunkLoading(chunkTicket);
	}

	@Override
    public void invalidate() {
    ForgeChunkManager.releaseTicket(chunkTicket);
    PipeManager.extractionHandlers.remove(this);
    super.invalidate();
    }
	
	@Override
	public  boolean canExtractItems(IPipe pipe, World world, int i, int j,int k){
		
	TileEntity tileentity =	world.getBlockTileEntity(i, j, k);
	if(tileentity != null)
	{
		if(tileentity instanceof TileEntitySecStorage)
		{
		 if(((TileEntitySecStorage)tileentity).isActive())
		 {
			 return false;
		 }
			
		}
	}
		
		
		
		return true;
	}
	
	@Override
	public boolean canExtractLiquids(IPipe pipe, World world, int i, int j,int k) {
	
		return true;
	}
	
	
	public abstract boolean isItemValid(ItemStack par1ItemStack, int Slot);

	public abstract int getSlotStackLimit(int slt);
	
	
	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}
	
	
    @Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		} else {
			return entityplayer.getDistance((double) xCoord + 0.5D,
					(double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
		}
	}

    @Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer){
    	
    	return new ItemStack(Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)]); 
    }

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}
	
    @Override
	public int getInventoryStackLimit() {
		return 64;
	}
    
	public int countItemsInSlot(Slots slt){
		if (this.getStackInSlot(slt.slot) != null)
			return this.getStackInSlot(slt.slot).stackSize;
		return 0;
	}
	
}

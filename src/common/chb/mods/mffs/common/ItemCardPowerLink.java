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
import java.util.UUID;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ISidedInventory;

public class ItemCardPowerLink extends ItemCard  {

	public ItemCardPowerLink(int i) {
		super(i);
		setIconIndex(17);
	}
	
	public static TileEntityCapacitor getLinkedCapacitor(ISidedInventory inventiory,int slot,World world)
	{
		if (inventiory.getStackInSlot(slot) != null)
		{
			if(inventiory.getStackInSlot(slot).getItem() instanceof ItemCardPowerLink)
			{
				ItemCardPowerLink card = (ItemCardPowerLink) inventiory.getStackInSlot(slot).getItem();
				PointXYZ png = card.getCardTargetPoint(inventiory.getStackInSlot(slot));
				if(png != null)
				{
					if(png.dimensionId != world.provider.dimensionId) return null;
					
				if(world.getBlockTileEntity(png.X, png.Y, png.Z) instanceof TileEntityCapacitor)
				{
					TileEntityCapacitor cap = (TileEntityCapacitor) world.getBlockTileEntity(png.X, png.Y, png.Z);
				if (cap != null){
					
				  if(cap.getCapacitor_ID()== card.getValuefromKey("CapacitorID",inventiory.getStackInSlot(slot))&&  card.getValuefromKey("CapacitorID",inventiory.getStackInSlot(slot)) != 0 )
				  {
                    return cap;
				   }
				}
			  }else{
				  
				  int Cap_ID =card.getValuefromKey("CapacitorID",inventiory.getStackInSlot(slot));
				  if(Cap_ID!=0)
				  {
					  TileEntityCapacitor cap =  Linkgrid.getWorldMap(world).getCapacitor().get(Cap_ID);
					  if (cap != null){
						  
						  ((ItemCard)card).setInformation(inventiory.getStackInSlot(slot), cap.getMaschinePoint(), "CapacitorID", Cap_ID);
						  return cap;
					  }
				  }
			
			  }
			  if(world.getChunkFromBlockCoords(png.X, png.Z).isChunkLoaded)
				  inventiory.setInventorySlotContents(slot, new ItemStack(ModularForceFieldSystem.MFFSitemcardempty));
			}
		   }
		}
		
		return null;
	}

	
	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer,
			World world, int i, int j, int k, int l) {
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if (!world.isRemote) {
			

			if (tileEntity instanceof TileEntityConverter) {
				  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,"EB"))
				  {

					  return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0,"<Power-Link>");

				  }
	            }
			
			
			
			if (tileEntity instanceof TileEntityProjector) {
			  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,"EB"))
			  {

				  return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0,"<Power-Link>");

			  }
            }
			
			if (tileEntity instanceof TileEntityExtractor ) {
				  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,"EB"))
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
				  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,"EB"))
				  {
		
					  return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 0,"<Power-Link>");
				  }
			}
			
			if (tileEntity instanceof TileEntityCapacitor) {
				  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,"EB"))
				  {
		
					  return Functions.setIteminSlot(itemstack, entityplayer, tileEntity, 2,"<Power-Link>");
				  }
			}
			
		}
		return false;
	}


	
	
			

}

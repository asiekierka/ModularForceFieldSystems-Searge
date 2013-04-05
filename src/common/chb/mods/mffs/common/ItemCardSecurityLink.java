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

import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ISidedInventory;

public class ItemCardSecurityLink extends ItemCard  {

	public ItemCardSecurityLink(int i) {
		super(i);
		setIconIndex(19);
	
	}

	public static TileEntityAdvSecurityStation getLinkedSecurityStation(ISidedInventory inventiory,int slot,World world)
	{
		if (inventiory.getStackInSlot(slot) != null)
		{
			if(inventiory.getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink)
			{
				ItemCardSecurityLink card = (ItemCardSecurityLink) inventiory.getStackInSlot(slot).getItem();
				PointXYZ png = card.getCardTargetPoint(inventiory.getStackInSlot(slot));
				if(png != null)
				{
					if(png.dimensionId != world.provider.dimensionId) return null;
					
				if(world.getBlockTileEntity(png.X, png.Y, png.Z) instanceof TileEntityAdvSecurityStation)
				{
				TileEntityAdvSecurityStation sec = (TileEntityAdvSecurityStation) world.getBlockTileEntity(png.X, png.Y, png.Z);
				if (sec != null){
					
				  if(sec.getSecurtyStation_ID()== card.getValuefromKey("Secstation_ID",inventiory.getStackInSlot(slot))&&  card.getValuefromKey("Secstation_ID",inventiory.getStackInSlot(slot)) != 0 )
				  {
                    return sec;
				   }
				}
			  }else{
				  
				  int Sec_ID =card.getValuefromKey("Secstation_ID",inventiory.getStackInSlot(slot));
				  if(Sec_ID!=0)
				  {
					  TileEntityAdvSecurityStation sec =  Linkgrid.getWorldMap(world).getSecStation().get(Sec_ID);
					  if (sec != null){
						  
						  ((ItemCard)card).setInformation(inventiory.getStackInSlot(slot), sec.getMaschinePoint(), "Secstation_ID", Sec_ID);
						  return sec;
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
	public boolean onItemUseFirst(ItemStack itemstack,
			EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if (!world.isRemote) {
			if (tileEntity instanceof TileEntityCapacitor) {

				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, "EB")) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 4, "<Security Station Link>");

				}
			}

			if (tileEntity instanceof TileEntityAreaDefenseStation) {
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, "EB")) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 1, "<Security Station Link>");
				}
			}
			
			if (tileEntity instanceof TileEntitySecStorage) {
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, "EB")) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 0, "<Security Station Link>");
				}
			}
			

			if (tileEntity instanceof TileEntityProjector) {
				if (SecurityHelper.isAccessGranted(tileEntity, entityplayer,
						world, "EB")) {

					return Functions.setIteminSlot(itemstack, entityplayer,
							tileEntity, 12, "<Security Station Link>");
				}
			}
		}

		return false;
	}

}
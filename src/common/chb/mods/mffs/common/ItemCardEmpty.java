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

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class ItemCardEmpty extends ItemMFFSBase {
	public ItemCardEmpty(int i) {
		super(i);
		setIconIndex(16);
		setMaxStackSize(1);
	}
	@Override
	public String getTextureFile() {
		return "/chb/mods/mffs/sprites/items.png";
	}
	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer,
			World world, int i, int j, int k, int l, float par8, float par9, float par10) {
		
		
		if (world.isRemote)
			return false;
		
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
		

		if (tileEntity instanceof TileEntityAdvSecurityStation) {
			
	
			if(((TileEntityAdvSecurityStation)tileEntity).isActive()){
				
			  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,"CSR")) {
	
				ItemStack newcard = new ItemStack(ModularForceFieldSystem.MFFSItemSecLinkCard);
				
				
				((ItemCardSecurityLink)newcard.getItem()).setInformation(newcard, new PointXYZ(i,j,k,world),"Secstation_ID", ((TileEntityAdvSecurityStation)tileEntity).getSecurtyStation_ID());
				
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, newcard);
				

				Functions.ChattoPlayer(entityplayer, "[Security Station] Success: <Security Station Link>  Card create");

				return true;
			  }
			 }else{
				
			
				Functions.ChattoPlayer(entityplayer, "[Security Station] Fail: Security Station must be Active  for create" );
			 }
		
	 }
		
		
		
		if (tileEntity instanceof TileEntityCapacitor) {
			
			
			  if(SecurityHelper.isAccessGranted(tileEntity, entityplayer, world,"EB")) {
				  				  
				ItemStack newcard = new ItemStack(ModularForceFieldSystem.MFFSitemfc);
				((ItemCardPowerLink)newcard.getItem()).setInformation(newcard, new PointXYZ(i,j,k,world),"CapacitorID",((TileEntityCapacitor)tileEntity).getCapacitor_ID());
				
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, newcard);
				

				entityplayer.addChatMessage("[Capacitor] Success: <Power-Link> Card create");

				return true;
			 }
		}
		
		
		
		
		
		
		
		

		return false;
	}
}

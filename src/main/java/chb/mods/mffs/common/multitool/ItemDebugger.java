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

package chb.mods.mffs.common.multitool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chb.mods.mffs.common.tileentity.TileEntityMachines;

public class ItemDebugger extends  ItemMultitool  {
	protected StringBuffer info = new StringBuffer();

	public ItemDebugger(int i) {
		super(i,3, false);
	}

	

    
    
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getBlockTileEntity(x,y,z);
		
		if (world.isRemote) {
			
			
			if(tileEntity instanceof TileEntityMachines)
				System.out.println("client"+((TileEntityMachines)tileEntity).isActive());
			

			
			
		}else{
			
			if(tileEntity instanceof TileEntityMachines)
				System.out.println("server"+((TileEntityMachines)tileEntity).isActive());
	
		}

		return false;
	}


	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		
	
		
		return itemstack;
	}
}

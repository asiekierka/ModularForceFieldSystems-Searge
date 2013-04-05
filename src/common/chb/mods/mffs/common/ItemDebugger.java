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

import java.util.Arrays;

import ic2.api.IWrenchable;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import chb.mods.mffs.api.ForcefieldProtected;
import chb.mods.mffs.api.IForceEnergyItems;

public class ItemDebugger extends  ItemMultitool  {
	protected StringBuffer info = new StringBuffer();

	public ItemDebugger(int i) {
		super(i,3);
	}

	

    
    
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getBlockTileEntity(x,y,z);
		
		if (!world.isRemote) {
			
		    
			 System.out.println(world.getBlockMetadata(x, y, z));
                
			
			if(tileEntity instanceof TileEntityForceField)
				System.out.println(((TileEntityForceField)tileEntity).getForcefieldCamoblockmeta());
			
			if(tileEntity instanceof TileEntityForceField)
				System.out.println(((TileEntityForceField)tileEntity).getForcefieldCamoblockid());
			
			if(tileEntity instanceof TileEntityForceField)
				System.out.println(((TileEntityForceField)tileEntity).getTexturid(0));
			
			if(tileEntity instanceof TileEntityForceField)
				System.out.println(((TileEntityForceField)tileEntity).getTexturfile());
			
			if(tileEntity instanceof TileEntityProjector)
				System.out.println(((TileEntityProjector)tileEntity).getForceFieldTexturfile());

			if(tileEntity instanceof TileEntityProjector)
				System.out.println(((TileEntityProjector)tileEntity).getForceFieldTexturID());
			
			if(tileEntity instanceof TileEntityProjector)
				System.out.println(((TileEntityProjector)tileEntity).getForcefieldCamoblockid());

			
		}else{
			
			if(tileEntity instanceof TileEntityForceField)
				System.out.println(((TileEntityForceField)tileEntity).getForcefieldCamoblockmeta());
			
			if(tileEntity instanceof TileEntityForceField)
				System.out.println(((TileEntityForceField)tileEntity).getForcefieldCamoblockid());
			
			if(tileEntity instanceof TileEntityForceField)
				System.out.println(((TileEntityForceField)tileEntity).getTexturid(0));
			
			if(tileEntity instanceof TileEntityForceField)
				System.out.println(((TileEntityForceField)tileEntity).getTexturfile());
			
			if(tileEntity instanceof TileEntityProjector)
				System.out.println(((TileEntityProjector)tileEntity).getForceFieldTexturfile());
			
			if(tileEntity instanceof TileEntityProjector)
				System.out.println(((TileEntityProjector)tileEntity).getForceFieldTexturID());
			
			if(tileEntity instanceof TileEntityProjector)
				System.out.println(((TileEntityProjector)tileEntity).getForcefieldCamoblockid());
		}

		return false;
	}


	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		
	
		
		return itemstack;
	}
}

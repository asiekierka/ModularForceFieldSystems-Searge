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
    
    Thunderdark 
    Matchlighter

 */

package chb.mods.mffs.common;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockSecurtyStorage extends BlockMFFSBase {

	public BlockSecurtyStorage(int i) {
		super(i);
		setRequiresSelfNotify();	
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
	   return new TileEntitySecStorage();

	}

	@Override
	public String getTextureFile() {
		return "/chb/mods/mffs/sprites/SecStorage.png";
	}

	
	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int par6, float par7, float par8,
			float par9) {
		
		if(world.isRemote)
			return true;

		TileEntitySecStorage tileentity = (TileEntitySecStorage) world.getBlockTileEntity(i, j, k);
		if(tileentity != null)
		{
			
			if(SecurityHelper.isAccessGranted(tileentity, entityplayer, world, "OSS"))
			{
				if (!world.isRemote)
					entityplayer.openGui(ModularForceFieldSystem.instance, 0, world,i, j, k);
				return true;
			}else{
				return true;
				}
			
		}else{
			
			return true;
		}
	  }
}

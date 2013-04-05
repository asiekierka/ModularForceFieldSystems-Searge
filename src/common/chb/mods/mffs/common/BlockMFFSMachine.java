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
    
    Matchlighter
    Thunderdark 

 */

package chb.mods.mffs.common;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class BlockMFFSMachine extends BlockMFFSBase {

	public BlockMFFSMachine(int i) {
		super(i);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		MFFSFutureMachines mach = MFFSFutureMachines.values()[meta];
		return null;
	}
	
	@Override
	public String getTextureFile(){
		return "/chb/mods/mffs/sprites/blocks.png";
	}

	@Override
	public int getBlockTexture(IBlockAccess IBA, int x, int y, int z, int l){
		return 1;
	}
	
	@Override
	public int damageDropped(int i){
		return i;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List){
		for (MFFSFutureMachines mach : MFFSFutureMachines.values()) {
			par3List.add(new ItemStack(this, 1, mach.ordinal()));
		}
	}

}

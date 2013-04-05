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

package chb.mods.mffs.common.options;

import java.util.List;

import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.PointXYZ;
import chb.mods.mffs.common.TileEntityProjector;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemProjectorOptionSponge extends ItemProjectorOptionBase implements IInteriorCheck {
	public ItemProjectorOptionSponge(int i) {
		super(i);
		setIconIndex(35);
	}

	
	@Override
    public void addInformation(ItemStack itemStack,EntityPlayer player,List info,boolean par4)
    {
            String tooltip = "compatible to: <Tube><Cube><Adv.Cube><Sphere>";
            info.add(tooltip);
    }
	
	public  void checkInteriorBlock(PointXYZ png ,World world,TileEntityProjector Projector) {
		if (world.getBlockMaterial(png.X, png.Y, png.Z).isLiquid()) {
			if (!ModularForceFieldSystem.forcefieldremoveonlywaterandlava) {
				world.setBlockWithNotify(png.X, png.Y, png.Z, 0);
			} else if (world.getBlockId(png.X, png.Y, png.Z) == 8
					|| world.getBlockId(png.X, png.Y, png.Z) == 9
					|| world.getBlockId(png.X, png.Y, png.Z) == 10
					|| world.getBlockId(png.X, png.Y, png.Z) == 11

			)

			{
				world.setBlockWithNotify(png.X, png.Y, png.Z, 0);
			}
		}
	}
	
	
}
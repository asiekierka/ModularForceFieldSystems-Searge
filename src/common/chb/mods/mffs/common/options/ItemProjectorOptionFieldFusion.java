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
import java.util.Map;

import chb.mods.mffs.common.ForceFieldBlockStack;
import chb.mods.mffs.common.Linkgrid;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.PointXYZ;
import chb.mods.mffs.common.TileEntityProjector;
import chb.mods.mffs.common.WorldMap;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemProjectorOptionFieldFusion extends ItemProjectorOptionBase implements IInteriorCheck {
	public ItemProjectorOptionFieldFusion(int i) {
		super(i);
		setIconIndex(43);
	}

	
	@Override
    public void addInformation(ItemStack itemStack,EntityPlayer player,List info,boolean par4)
    {
            String tooltip = "compatible to: <Tube><Cube><Sphere><Adv.Cube>";
            info.add(tooltip);
    }

	
	public boolean checkFieldFusioninfluence(PointXYZ png, World world,TileEntityProjector Proj) {
		
		Map<Integer, TileEntityProjector> InnerMap = null;
		InnerMap = Linkgrid.getWorldMap(world).getFieldFusion();
		for (TileEntityProjector tileentity : InnerMap.values()) {
			
			boolean logicswitch= false;
			logicswitch = tileentity.getLinkCapacitor_ID() == Proj.getLinkCapacitor_ID() &&
			          tileentity.getProjektor_ID() != Proj.getProjektor_ID();
		
			if (logicswitch && tileentity.isActive()) {
				for (PointXYZ tpng : tileentity.getInteriorPoints()) {
					if(tpng.X == png.X && tpng.Y == png.Y && tpng.Z == png.Z)
						return true;	
				}	
			}	
		}
		return false;
	}
	
	
	

	@Override
	public void checkInteriorBlock(PointXYZ png, World world,TileEntityProjector Proj) {

		ForceFieldBlockStack ffworldmap = WorldMap
				.getForceFieldWorld(world)
				.getorcreateFFStackMap(png.X, png.Y,png.Z,world);
		
		if(!ffworldmap.isEmpty())
		{
		 if(ffworldmap.getGenratorID()== Proj.getLinkCapacitor_ID())
		 {
			TileEntityProjector Projector =  Linkgrid.getWorldMap(world).getProjektor().get(ffworldmap.getProjectorID());
			
			if(Projector != null)
			{
			if(Projector.hasOption(ModularForceFieldSystem.MFFSProjectorOptionFieldFusion))
			{
    			Projector.getfield_queue().remove(png);       
				ffworldmap.removebyProjector(Projector.getProjektor_ID());
				
				PointXYZ ffpng = ffworldmap.getPoint();

				if(world.getBlockId(ffpng.X,ffpng.Y, ffpng.Z) == ModularForceFieldSystem.MFFSFieldblock.blockID)
				{
					world.removeBlockTileEntity(ffpng.X,ffpng.Y, ffpng.Z);
					world.setBlockWithNotify(ffpng.X,ffpng.Y, ffpng.Z, 0);
				}
			}
		 }
		 }
		}
		
	}
	
	
	
}
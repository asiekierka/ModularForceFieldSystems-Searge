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

package chb.mods.mffs.common.modules;


import java.util.Set;

import net.minecraft.src.Item;
import net.minecraft.src.TileEntity;

import chb.mods.mffs.common.IModularProjector;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.PointXYZ;
import chb.mods.mffs.common.IModularProjector.Slots;
import chb.mods.mffs.common.options.ItemProjectorOptionBlockBreaker;
import chb.mods.mffs.common.options.ItemProjectorOptionCamoflage;
import chb.mods.mffs.common.options.ItemProjectorOptionDefenseStation;
import chb.mods.mffs.common.options.ItemProjectorOptionFieldFusion;
import chb.mods.mffs.common.options.ItemProjectorOptionFieldManipulator;
import chb.mods.mffs.common.options.ItemProjectorOptionForceFieldJammer;
import chb.mods.mffs.common.options.ItemProjectorOptionMobDefence;
import chb.mods.mffs.common.options.ItemProjectorOptionSponge;
import chb.mods.mffs.common.options.ItemProjectorOptionTouchDamage;
import chb.mods.mffs.common.TileEntityProjector;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemProjectorModuleCube extends Module3DBase{
	public ItemProjectorModuleCube(int i) {
		super(i);
		setIconIndex(53);
	}
	
	
	@Override
	public boolean supportsDistance() {
		return true;
	}



	@Override
	public boolean supportsStrength() {
		return false;
	}



	@Override
	public boolean supportsMatrix() {
		return false;
	}

	
	public void calculateField(IModularProjector projector, Set<PointXYZ> ffLocs, Set<PointXYZ> ffInterior) {
		
		int radius = projector.countItemsInSlot(Slots.Distance)+4;
		TileEntity te = (TileEntity)projector;

		
		int yDown = radius;
		int yTop = radius;
		if (te.yCoord + radius > 255) {
			yTop = 255-te.yCoord;
		}
		
		if (((TileEntityProjector)te).hasOption(ModularForceFieldSystem.MFFSProjectorOptionDome)) {
			yDown = 0;
     	}
		
		
		
		for (int y1 = -yDown; y1 <= yTop; y1++) {
			for (int x1 = -radius; x1 <= radius; x1++) {
				for (int z1 = -radius; z1 <= radius; z1++) {
					
					if (x1 == -radius || x1 == radius || y1 == -radius || y1 == yTop || z1 == -radius || z1 == radius) {
					     ffLocs.add(new PointXYZ(x1, y1, z1,0));
					} else {
						ffInterior.add(new PointXYZ(x1, y1, z1,0));
					}
				}
			}
		}
		
	}
	
	@Override
	public boolean supportsOption(Item item) {
		

		if(item instanceof ItemProjectorOptionCamoflage) return true;
		if(item instanceof ItemProjectorOptionDefenseStation) return true;
		if(item instanceof ItemProjectorOptionFieldFusion) return true;
		if(item instanceof ItemProjectorOptionFieldManipulator) return true;
		if(item instanceof ItemProjectorOptionForceFieldJammer) return true;
		if(item instanceof ItemProjectorOptionMobDefence) return true;
		if(item instanceof ItemProjectorOptionSponge) return true;
		if(item instanceof ItemProjectorOptionBlockBreaker) return true;

		
		return false;
	}


	

}
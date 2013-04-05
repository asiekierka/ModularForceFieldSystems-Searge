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

import chb.mods.mffs.api.PointXYZ;
import chb.mods.mffs.common.IModularProjector;
import chb.mods.mffs.common.IModularProjector.Slots;

public class ItemProjectorModulediagonallyWall extends ItemProjectorModuleWall {
	public ItemProjectorModulediagonallyWall(int i) {
		super(i);
		setIconIndex(56);
		
	}
	
	@Override
	public void calculateField(IModularProjector projector, Set<PointXYZ> ffLocs) {	
		
		int tpx = 0;
		int tpy = 0;
		int tpz = 0;
		
        int xstart=0;
        int xend =0;
        
        int zstart=0;
        int zend =0;
		
		if(projector.countItemsInSlot(Slots.FocusUp)>0){
			
			xend = Math.max(xend,Math.max(projector.countItemsInSlot(Slots.FocusUp), projector.countItemsInSlot(Slots.FocusRight)));
			zend = Math.max(zend,Math.max(projector.countItemsInSlot(Slots.FocusUp), projector.countItemsInSlot(Slots.FocusRight)));
		}
		
		if(projector.countItemsInSlot(Slots.FocusDown)>0){
			
			xstart = Math.max(xend,Math.max(projector.countItemsInSlot(Slots.FocusDown), projector.countItemsInSlot(Slots.FocusLeft)));
			zstart = Math.max(zend,Math.max(projector.countItemsInSlot(Slots.FocusDown), projector.countItemsInSlot(Slots.FocusLeft)));
		}
		
		if(projector.countItemsInSlot(Slots.FocusLeft)>0){
			
			xend = Math.max(xend,Math.max(projector.countItemsInSlot(Slots.FocusUp), projector.countItemsInSlot(Slots.FocusLeft)));
			zstart = Math.max(zstart,Math.max(projector.countItemsInSlot(Slots.FocusUp), projector.countItemsInSlot(Slots.FocusLeft)));
		}
		
		if(projector.countItemsInSlot(Slots.FocusRight)>0){
			
			zend = Math.max(zend,Math.max(projector.countItemsInSlot(Slots.FocusDown), projector.countItemsInSlot(Slots.FocusRight)));
			xstart = Math.max(xstart,Math.max(projector.countItemsInSlot(Slots.FocusDown), projector.countItemsInSlot(Slots.FocusRight)));
		}

		for (int x1 = 0 - zstart; x1 < zend + 1; x1++) {
		for (int z1 = 0 - xstart; z1 <  xend+ 1; z1++) {
			for (int y1 = 1; y1 < projector.countItemsInSlot(Slots.Strength) + 1 + 1; y1++) {

				if (projector.getSide() == 0) {
					tpy = y1 - y1 - y1 -  projector.countItemsInSlot(Slots.Distance);
					tpx = x1;
					tpz = z1 - z1 - z1;
				}

				if (projector.getSide() == 1) {
					tpy = y1 +  projector.countItemsInSlot(Slots.Distance);
					tpx = x1;
					tpz = z1 - z1 - z1;
				}

				if (projector.getSide() == 2) {
					tpz = y1 - y1 - y1 -  projector.countItemsInSlot(Slots.Distance);
					tpx = x1 - x1 - x1;
					tpy = z1;
				}

				if (projector.getSide() == 3) {
					tpz = y1 +  projector.countItemsInSlot(Slots.Distance);
					tpx = x1;
					tpy = z1;
				}

				if (projector.getSide() == 4) {
					tpx = y1 - y1 - y1 - projector.countItemsInSlot(Slots.Distance);
					tpz = x1;
					tpy = z1;
				}
				if (projector.getSide() == 5) {
					tpx = y1 + projector.countItemsInSlot(Slots.Distance);
					tpz = x1 - x1 - x1;
					tpy = z1;
				}

				
				if ((projector.getSide() == 0 || projector.getSide() == 1) 
					&& (Math.abs(tpx) == Math.abs(tpz) &&  ((tpx !=0 && tpz!=0) || (tpx ==0 && tpz==0)) && (
					   (projector.countItemsInSlot(Slots.FocusUp)!=0 && (tpx >= 0 && tpz <= 0) && tpx<=projector.countItemsInSlot(Slots.FocusUp) && tpz<=projector.countItemsInSlot(Slots.FocusUp)) ||
					   (projector.countItemsInSlot(Slots.FocusDown)!=0 && (tpx <= 0 && tpz >= 0) && tpx<=projector.countItemsInSlot(Slots.FocusDown) && tpz<=projector.countItemsInSlot(Slots.FocusDown)) ||	
					   (projector.countItemsInSlot(Slots.FocusRight)!=0 && (tpx >= 0 && tpz >= 0) && tpx<=projector.countItemsInSlot(Slots.FocusRight) && tpz<=projector.countItemsInSlot(Slots.FocusRight)  ) ||
					   (projector.countItemsInSlot(Slots.FocusLeft)!=0 && (tpx <= 0 && tpz <= 0) && tpx<=projector.countItemsInSlot(Slots.FocusLeft) && tpz<=projector.countItemsInSlot(Slots.FocusLeft)) 
					   )) 					
                    ||
                    (projector.getSide() == 2 || projector.getSide() == 3) &&
                    (Math.abs(tpx) == Math.abs(tpy) &&  ((tpx !=0 && tpy!=0) || (tpx ==0 && tpy==0)) && (
     					   (projector.countItemsInSlot(Slots.FocusUp)!=0 && (tpx >= 0 && tpy >= 0) && tpx<=projector.countItemsInSlot(Slots.FocusUp) && tpy<=projector.countItemsInSlot(Slots.FocusUp)) ||
     					   (projector.countItemsInSlot(Slots.FocusDown)!=0 && (tpx <= 0 && tpy <= 0) && tpx<=projector.countItemsInSlot(Slots.FocusDown) && tpy<=projector.countItemsInSlot(Slots.FocusDown)) ||	
     					   (projector.countItemsInSlot(Slots.FocusRight)!=0 && (tpx >= 0 && tpy <= 0) && tpx<=projector.countItemsInSlot(Slots.FocusRight) && tpy<=projector.countItemsInSlot(Slots.FocusRight)  ) ||
     					   (projector.countItemsInSlot(Slots.FocusLeft)!=0 && (tpx <= 0 && tpy >= 0) && tpx<=projector.countItemsInSlot(Slots.FocusLeft) && tpy<=projector.countItemsInSlot(Slots.FocusLeft)) 
     					   )) 
     	            ||
     	            (projector.getSide() == 4 || projector.getSide() == 5) &&
     	           (Math.abs(tpz) == Math.abs(tpy) &&  ((tpx !=0 && tpy!=0) || (tpz ==0 && tpy==0)) && (
     	     			    (projector.countItemsInSlot(Slots.FocusUp)!=0 && (tpz >= 0 && tpy >= 0) && tpz<=projector.countItemsInSlot(Slots.FocusUp) && tpy<=projector.countItemsInSlot(Slots.FocusUp)) ||
     	     				(projector.countItemsInSlot(Slots.FocusDown)!=0 && (tpz <= 0 && tpy <= 0) && tpz<=projector.countItemsInSlot(Slots.FocusDown) && tpy<=projector.countItemsInSlot(Slots.FocusDown)) ||	
     	     				(projector.countItemsInSlot(Slots.FocusRight)!=0 && (tpz >= 0 && tpy <= 0) && tpz<=projector.countItemsInSlot(Slots.FocusRight) && tpy<=projector.countItemsInSlot(Slots.FocusRight)  ) ||
     	     				(projector.countItemsInSlot(Slots.FocusLeft)!=0 && (tpz <= 0 && tpy >= 0) && tpz<=projector.countItemsInSlot(Slots.FocusLeft) && tpy<=projector.countItemsInSlot(Slots.FocusLeft)) 
     	     			   )) 

		         )

				{

					ffLocs.add(new PointXYZ(tpx, tpy, tpz,0));
				}
			}
        }
	}
		
	}


	
}
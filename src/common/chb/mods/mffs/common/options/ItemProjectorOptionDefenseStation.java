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

import chb.mods.mffs.common.Functions;
import chb.mods.mffs.common.Linkgrid;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.PointXYZ;
import chb.mods.mffs.common.TileEntityAdvSecurityStation;
import chb.mods.mffs.common.TileEntityCapacitor;
import chb.mods.mffs.common.TileEntityProjector;
import chb.mods.mffs.common.IModularProjector.Slots;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ItemProjectorOptionDefenseStation extends ItemProjectorOptionBase  {
	public ItemProjectorOptionDefenseStation(int i) {
		super(i);
		setIconIndex(39);
	}

	
	@Override
    public void addInformation(ItemStack itemStack,EntityPlayer player,List info,boolean par4)
    {
            String tooltip = "compatible to ProjectorTyp: <Cube><Adv.Cube><Sphere>";
            info.add(tooltip);
            tooltip = "compatible to Area Defense Station";
            info.add(tooltip);
    }
	
	
	public static void ProjectorPlayerDefence(TileEntityProjector projector,World world){
		
		if(projector.isActive())
		{
			int xmin = projector.xCoord - (projector.countItemsInSlot(Slots.Distance)+4);
			int xmax = projector.xCoord + (projector.countItemsInSlot(Slots.Distance)+4);
			int ymin = projector.yCoord - (projector.countItemsInSlot(Slots.Distance)+4);
			int ymax = projector.yCoord + (projector.countItemsInSlot(Slots.Distance)+4);
			int zmin = projector.zCoord - (projector.countItemsInSlot(Slots.Distance)+4);
			int zmax = projector.zCoord + (projector.countItemsInSlot(Slots.Distance)+4);
			
			List<EntityLiving> LivingEntitylist = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xmin, ymin, zmin, xmax, ymax, zmax));

			for (int i = 0; i < LivingEntitylist.size(); i++) {
				EntityLiving entityLiving = LivingEntitylist.get(i);
				

				if(!(entityLiving instanceof EntityPlayer))
				{continue;}
				
				
				if (projector.getLinkPower() < 10000)
				{break;}	
				
				
				for (PointXYZ png : projector.getInteriorPoints()) {
					
					if(png.X == (int)entityLiving.posX && png.Y == (int)entityLiving.posY && png.Z == (int)entityLiving.posZ){
						
						
		        		if (projector.getLinkPower() > 10000) {
		        			
								boolean killswitch = false;

								if(projector.getaccesstyp()==2)
								{
									TileEntityCapacitor cap =  projector.getLinkedCapacitor();
									if(cap != null)
									{
									TileEntityAdvSecurityStation SecurityStation = cap.getLinkedSecurityStation();

									if(SecurityStation != null)
									{
									killswitch = !SecurityStation.isAccessGranted(((EntityPlayer)entityLiving).username,"SR");
									}
									}
								}
								if(projector.getaccesstyp()==3)
								{
									TileEntityAdvSecurityStation SecurityStation = projector.getLinkedSecurityStation();
									if(SecurityStation != null)
									{
									killswitch = !SecurityStation.isAccessGranted(((EntityPlayer)entityLiving).username,"SR");
									}
								}

								if (killswitch)
									{
									TileEntityCapacitor cap =  projector.getLinkedCapacitor();
									if(cap != null)
									{
									  if(cap.consumForcePower(10000))
									  {
										 ((EntityPlayer)entityLiving).inventory.dropAllItems();
										  entityLiving.attackEntityFrom(DamageSource.generic,10); 
									
									  }
									}
							
									continue;
									}


		        		}	
					}
				}
			}
		}
	}
	
}
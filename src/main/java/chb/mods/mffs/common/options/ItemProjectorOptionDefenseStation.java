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

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import chb.mods.mffs.api.PointXYZ;
import chb.mods.mffs.common.IModularProjector.Slots;
import chb.mods.mffs.common.Linkgrid;
import chb.mods.mffs.common.MFFSDamageSource;
import chb.mods.mffs.common.SecurityRight;
import chb.mods.mffs.common.modules.ItemProjectorModuleSphere;
import chb.mods.mffs.common.tileentity.TileEntityAdvSecurityStation;
import chb.mods.mffs.common.tileentity.TileEntityCapacitor;
import chb.mods.mffs.common.tileentity.TileEntityProjector;




public class ItemProjectorOptionDefenseStation extends ItemProjectorOptionBase  {
	
	public ItemProjectorOptionDefenseStation(int i) {
		super(i);
		setIconIndex(39);
	}
	
	public static void ProjectorPlayerDefence(TileEntityProjector projector,World world){
		
		if(projector.isActive())
		{
		    int fieldxmin =projector.xCoord;
		    int fieldxmax =projector.xCoord;
		    int fieldymin =projector.yCoord;
		    int fieldymax =projector.yCoord;
		    int fieldzmin =projector.zCoord;
		    int fieldzmax =projector.zCoord;
			
			for (PointXYZ png : projector.getfield_queue()) {
				fieldxmax = Math.max(fieldxmax, png.X);
				fieldxmin = Math.min(fieldxmin, png.X);
				fieldymax = Math.max(fieldymax, png.Y);
				fieldymin = Math.min(fieldymin, png.Y);
				fieldzmax = Math.max(fieldzmax, png.Z);
				fieldzmin = Math.min(fieldzmin, png.Z);
			}
			
			List<EntityLiving> LivingEntitylist = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(fieldxmin, fieldymin, fieldzmin, fieldxmax, fieldymax, fieldzmax));
			
			
			for (int i = 0; i < LivingEntitylist.size(); i++) {
				EntityLiving entityLiving = LivingEntitylist.get(i);
				

				if(!(entityLiving instanceof EntityPlayer))
				{continue;}
				
				if (projector.get_type() instanceof ItemProjectorModuleSphere)
				 if(PointXYZ.distance(new PointXYZ((int)entityLiving.posX, (int)entityLiving.posY, (int)entityLiving.posZ, world), projector.getMaschinePoint())>projector.countItemsInSlot(Slots.Distance) +4)
					continue;
					

				if (projector.getLinkPower() < 10000)
				{break;}	
										
		        		if (projector.getLinkPower() > 10000) {
		        			
								boolean killswitch = false;

								if(projector.getaccesstyp()==2)
								{
									TileEntityCapacitor cap =Linkgrid.getWorldMap(world).getCapacitor().get(projector.getPowerSourceID());
									if(cap != null)
									{
									TileEntityAdvSecurityStation SecurityStation = cap.getLinkedSecurityStation();

									if(SecurityStation != null)
									{
									killswitch = !SecurityStation.isAccessGranted(((EntityPlayer)entityLiving).username,SecurityRight.SR);
									}
									}
								}
								if(projector.getaccesstyp()==3)
								{
									TileEntityAdvSecurityStation SecurityStation = projector.getLinkedSecurityStation();
									if(SecurityStation != null)
									{
									killswitch = !SecurityStation.isAccessGranted(((EntityPlayer)entityLiving).username,SecurityRight.SR);
									}
								}

								if (killswitch)
									{
									  if(projector.consumePower(10000, true))
									  {
										 ((EntityPlayer)entityLiving).addChatMessage("!!! [Area Defence] leave or die !!!");
										 ((EntityPlayer)entityLiving).attackEntityFrom(MFFSDamageSource.fieldDefense,10);
										  projector.consumePower(10000, false);
									
									  }
							
									continue;
									}

		        		}	
			}
		}
	}
	
}
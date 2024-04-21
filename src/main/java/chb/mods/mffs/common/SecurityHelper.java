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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chb.mods.mffs.common.tileentity.TileEntityAdvSecurityStation;
import chb.mods.mffs.common.tileentity.TileEntityAreaDefenseStation;
import chb.mods.mffs.common.tileentity.TileEntityCapacitor;
import chb.mods.mffs.common.tileentity.TileEntityControlSystem;
import chb.mods.mffs.common.tileentity.TileEntityConverter;
import chb.mods.mffs.common.tileentity.TileEntityExtractor;
import chb.mods.mffs.common.tileentity.TileEntityProjector;
import chb.mods.mffs.common.tileentity.TileEntitySecStorage;


public class SecurityHelper {

	
	
	
	public static boolean isAccessGranted(TileEntity tileEntity,
			EntityPlayer entityplayer, World world, SecurityRight right) {
		
		return isAccessGranted(tileEntity,
				 entityplayer, world, right,false);
	}
	
	
	public static boolean isAccessGranted(TileEntity tileEntity,
			EntityPlayer entityplayer, World world, SecurityRight right,boolean suppresswarning) {
		
		

		if (tileEntity instanceof TileEntitySecStorage) {
			
			
			TileEntityAdvSecurityStation sec = ((TileEntitySecStorage)tileEntity).getLinkedSecurityStation();
			
			if(sec!=null){
				if(sec.isAccessGranted(entityplayer.username, right))
					return true;
				
				
				if (!suppresswarning)
					entityplayer.sendChatToPlayer("[Field Security] Fail: access denied");
				return false;
			}else{
				
				if (world.isRemote)
					return false;
				
				return true;
			}
		
		}

		
		
		if (tileEntity instanceof TileEntityControlSystem) {
			
			TileEntityAdvSecurityStation sec = ((TileEntityControlSystem)tileEntity).getLinkedSecurityStation();
			if(sec!=null){
				if(sec.isAccessGranted(entityplayer.username, right))
					return true;
				
				
				if (!suppresswarning)
					entityplayer.sendChatToPlayer("[Field Security] Fail: access denied");
				return false;
			}else{
				
				if (world.isRemote)
					return false;
				
				return true;
			}
			
		}
		if (tileEntity instanceof TileEntityAdvSecurityStation) {

			if (!(((TileEntityAdvSecurityStation) tileEntity).isAccessGranted(
					entityplayer.username, right))) {

				if (!suppresswarning)
					Functions.ChattoPlayer(entityplayer,
							"[Field Security] Fail: access denied");
				return false;
			}
		}
		
		
		

		if (tileEntity instanceof TileEntityConverter) {
			
			TileEntityAdvSecurityStation sec = ((TileEntityConverter)tileEntity).getLinkedSecurityStation();
			if(sec!=null)
				{
				  if(sec.isAccessGranted(entityplayer.username, right))
				  {
					  return true;
				  }else{
						if (!suppresswarning)
							Functions.ChattoPlayer(entityplayer,
									"[Field Security] Fail: access denied");
					  return false;
				  }
					
				}
			return true;
		}
			
	
		
		if (tileEntity instanceof TileEntityCapacitor) {
			
			TileEntityAdvSecurityStation sec = ((TileEntityCapacitor)tileEntity).getLinkedSecurityStation();
			if(sec!=null)
			{
			  if(sec.isAccessGranted(entityplayer.username, right))
			  {
				  return true;
			  }else{
					if (!suppresswarning)
						Functions.ChattoPlayer(entityplayer,
								"[Field Security] Fail: access denied");
				  return false;
			  }
				
			}
			return true;
		}
			
			

		

		if (tileEntity instanceof TileEntityExtractor) {
			
			TileEntityAdvSecurityStation sec = ((TileEntityExtractor)tileEntity).getLinkedSecurityStation();
			if(sec!=null)
				{
				  if(sec.isAccessGranted(entityplayer.username, right))
				  {
					  return true;
				  }else{
						if (!suppresswarning)
							Functions.ChattoPlayer(entityplayer,
									"[Field Security] Fail: access denied");
					  return false;
				  }
					
				}
		
			return true;
			
			
		}

		if (tileEntity instanceof TileEntityAreaDefenseStation) {
			
				TileEntityAdvSecurityStation sec = ((TileEntityAreaDefenseStation)tileEntity).getLinkedSecurityStation();
				
				if(sec!=null)
				{
				  if(sec.isAccessGranted(entityplayer.username, right))
				  {
					  return true;
				  }else{
						if (!suppresswarning)
							Functions.ChattoPlayer(entityplayer,
									"[Field Security] Fail: access denied");
					  return false;
				  }
					
				
			}
			return true;

		}
		


		if (tileEntity instanceof TileEntityProjector) {
			
		
			switch(((TileEntityProjector) tileEntity).getaccesstyp()){
			case 2:
				
				TileEntityCapacitor cap =Linkgrid.getWorldMap(world).getCapacitor().get(((TileEntityProjector)tileEntity).getPowerSourceID());
				if(cap != null)
				{
					TileEntityAdvSecurityStation sec = cap.getLinkedSecurityStation();
					if(sec!=null)
					{
					  if(sec.isAccessGranted(entityplayer.username, right))
					  {
						  return true;
					  }else{
							if (!suppresswarning)
								Functions.ChattoPlayer(entityplayer,
										"[Field Security] Fail: access denied");
						  return false;
					  }
						
					}
				}
				
				
			break;
			case 3:
				
				TileEntityAdvSecurityStation sec = ((TileEntityProjector)tileEntity).getLinkedSecurityStation();
				if(sec!=null)
				{
				  if(sec.isAccessGranted(entityplayer.username, right))
				  {
					  return true;
				  }else{
						if (!suppresswarning)
							Functions.ChattoPlayer(entityplayer,
									"[Field Security] Fail: access denied");
					  return false;
				  }
			}
				
			break;
			}
			
			return true;
		}


		return true;
	}

}

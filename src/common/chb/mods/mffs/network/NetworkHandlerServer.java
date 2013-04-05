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

package chb.mods.mffs.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;

import chb.mods.mffs.common.ForceFieldBlockStack;
import chb.mods.mffs.common.Linkgrid;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.TileEntityAdvSecurityStation;
import chb.mods.mffs.common.TileEntityAreaDefenseStation;
import chb.mods.mffs.common.TileEntityCapacitor;
import chb.mods.mffs.common.TileEntityExtractor;
import chb.mods.mffs.common.TileEntityMachines;
import chb.mods.mffs.common.TileEntityProjector;
import chb.mods.mffs.common.TileEntityForceField;
import chb.mods.mffs.common.TileEntityConverter;
import chb.mods.mffs.common.WorldMap;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.ReflectionHelper;



public class NetworkHandlerServer implements IPacketHandler{

private static final boolean DEBUG = false;

@Override
public void onPacketData(INetworkManager manager,Packet250CustomPayload packet, Player player) {

	

	ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
	int x = dat.readInt();
	int y = dat.readInt();
	int z = dat.readInt();
	int typ = dat.readInt(); 
	

	switch(typ)
	{
	case 2:
		
		int dimension = dat.readInt() ;
		String daten = dat.readUTF(); 
		World serverworld = DimensionManager.getWorld(dimension);
		if(serverworld != null)
		{
		TileEntity servertileEntity = serverworld.getBlockTileEntity(x, y, z);
		
		
		if(servertileEntity != null)
		{
		for(String varname : daten.split("/"))
		{
			updateTileEntityField(servertileEntity,  varname);
		}
		}else{
			 if(DEBUG)
			 System.out.println(x+"/"+y+"/"+z+":no Tileentity found !!");
		}
		}else{
			 if(DEBUG)
			 System.out.println("[Error]No world found !!");
		}
		
		
	break;
	
	case 3:
		int dimension2 = dat.readInt() ;
		String evt = dat.readUTF();
		
		World serverworld2 = DimensionManager.getWorld(dimension2);
		TileEntity servertileEntity2 = serverworld2.getBlockTileEntity(x, y, z);
		
		if(servertileEntity2 instanceof INetworkHandlerEventListener)
		{
			((INetworkHandlerEventListener)servertileEntity2).onNetworkHandlerEvent(evt);
			
		}
		
		
	break;		
	case 10:
		

		int Dim =dat.readInt() ;
		String Corrdinsaten = dat.readUTF(); 
		
		World worldserver = DimensionManager.getWorld(Dim);
	
		if(worldserver != null)
		{
			
		for(String varname : Corrdinsaten.split("#"))
		{

		 if(!varname.isEmpty())	
		 {
		 String[] corr =varname.split("/");
		 TileEntity servertileEntity = worldserver.getBlockTileEntity(Integer.parseInt(corr[2].trim()), Integer.parseInt(corr[1].trim()),Integer.parseInt(corr[0].trim()));
		 if(servertileEntity instanceof TileEntityForceField)
		 {
			 
			 
				ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(worldserver).getForceFieldStackMap(WorldMap.Cordhash(servertileEntity.xCoord, servertileEntity.yCoord, servertileEntity.zCoord));

				if(ffworldmap != null)
				{
					if(!ffworldmap.isEmpty())

					{
					 TileEntityProjector projector = Linkgrid.getWorldMap(worldserver).getProjektor().get(ffworldmap.getProjectorID());

						if(projector != null)
						{
							ForceFieldServerUpdatehandler.getWorldMap(worldserver).addto(servertileEntity.xCoord, servertileEntity.yCoord, servertileEntity.zCoord, Dim,projector.xCoord,projector.yCoord,projector.zCoord);
						}
					}
				}
			 
		 }
		 }
		}
		}
		
	break;
	
	}
	

	 
}




public static void updateTileEntityField(TileEntity tileEntity, String varname)
{
	
//	System.out.println(tileEntity+"Ubertragt daten"+varname);
	
	if(tileEntity != null)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = tileEntity.xCoord;
		int y = tileEntity.yCoord;
		int z = tileEntity.zCoord;
		int typ = 1; // Server -> Client

		 try {
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeInt(typ);
			dos.writeUTF(varname);
			
			} catch (Exception e) {
				if(DEBUG)
				System.out.println(e.getLocalizedMessage());
			}
		
 if(tileEntity instanceof TileEntityMachines)
  {
	 try {
	        Field f = ReflectionHelper.findField(TileEntityMachines.class, varname);
	        f.get(tileEntity);
	    	dos.writeUTF(String.valueOf(f.get(tileEntity)));
		} catch (Exception e) {
			if(DEBUG)
			System.out.println(e.getLocalizedMessage());
		}
  }
		
		
 if(tileEntity instanceof TileEntityProjector)
 {
	
	 try {	
        Field f = ReflectionHelper.findField(TileEntityProjector.class, varname);
        f.get(tileEntity);
    	dos.writeUTF(String.valueOf(f.get(tileEntity)));
	} catch (Exception e) {
		if(DEBUG)
		System.out.println(e.getLocalizedMessage());
	}
 }
 
 if(tileEntity instanceof TileEntityCapacitor)
 {
	 try {	
        Field f = ReflectionHelper.findField(TileEntityCapacitor.class, varname);
        f.get(tileEntity);
    	dos.writeUTF(String.valueOf(f.get(tileEntity)));
	} catch (Exception e) {
		if(DEBUG)
		System.out.println(e.getLocalizedMessage());
	}
 }
 
 if(tileEntity instanceof TileEntityExtractor)
 {
	 try {	
        Field f = ReflectionHelper.findField(TileEntityExtractor.class, varname);
        f.get(tileEntity);
    	dos.writeUTF(String.valueOf(f.get(tileEntity)));
	} catch (Exception e) {
		if(DEBUG)
		System.out.println(e.getLocalizedMessage());
	}
 }
 
 if(tileEntity instanceof TileEntityConverter)
 {
	 try {	
        Field f = ReflectionHelper.findField(TileEntityConverter.class, varname);
        f.get(tileEntity);
    	dos.writeUTF(String.valueOf(f.get(tileEntity)));
	} catch (Exception e) {
		if(DEBUG)
		System.out.println(e.getLocalizedMessage());
	}
 }
 
 
 if(tileEntity instanceof TileEntityAreaDefenseStation)
 {
	 try {	
        Field f = ReflectionHelper.findField(TileEntityAreaDefenseStation.class, varname);
        f.get(tileEntity);
    	dos.writeUTF(String.valueOf(f.get(tileEntity)));
	} catch (Exception e) {
		if(DEBUG)
		System.out.println(e.getLocalizedMessage());
	}
 }
 
 if(tileEntity instanceof TileEntityAdvSecurityStation)
 {
	 try {	
        Field f = ReflectionHelper.findField(TileEntityAdvSecurityStation.class, varname);
        f.get(tileEntity);
    	dos.writeUTF(String.valueOf(f.get(tileEntity)));
	} catch (Exception e) {
		if(DEBUG)
			System.out.println(e.getMessage());

	}
 }
 

 
 
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "MFFS";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;

		PacketDispatcher.sendPacketToAllAround(x, y, z, 80, tileEntity.worldObj.provider.dimensionId, pkt);
	}
	
}




}
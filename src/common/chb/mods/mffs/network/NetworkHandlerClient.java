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

import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.TileEntityAdvSecurityStation;
import chb.mods.mffs.common.TileEntityAreaDefenseStation;
import chb.mods.mffs.common.TileEntityCapacitor;
import chb.mods.mffs.common.TileEntityExtractor;
import chb.mods.mffs.common.TileEntityMachines;
import chb.mods.mffs.common.TileEntityProjector;
import chb.mods.mffs.common.TileEntityForceField;
import chb.mods.mffs.common.TileEntityConverter;

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



public class NetworkHandlerClient implements IPacketHandler{

private static final boolean DEBUG = false;

@Override
public void onPacketData(INetworkManager manager,Packet250CustomPayload packet, Player player) {
	

	ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
	int x = dat.readInt();
	int y = dat.readInt();
	int z = dat.readInt();
	int typ = dat.readInt(); 
	World world = ModularForceFieldSystem.proxy.getClientWorld();

	switch(typ)
	{
	case 100:
	
		String DataPacket = dat.readUTF();
		
		for(String blockupdate : DataPacket.split(">"))
		{
		  if(blockupdate.length() > 0)
		  {

			  String[] projector = blockupdate.split("<");
			  String[] Corrdinaten = projector[1].split("/");
			  String[] temp =projector[0].split("!");
			  String[] Dim = temp[1].split("/");
			  String[] ProjectorCorr = temp[0].split("/");
			  

			  if(Integer.parseInt(Dim[0].trim()) == world.provider.dimensionId)
			  {
				  if (world.getChunkFromBlockCoords(Integer.parseInt(Corrdinaten[0].trim()), Integer.parseInt(Corrdinaten[2].trim())).isChunkLoaded)
				  {
					  TileEntity te = world.getBlockTileEntity(Integer.parseInt(Corrdinaten[0].trim()), Integer.parseInt(Corrdinaten[1].trim()), Integer.parseInt(Corrdinaten[2].trim()));
					  if(te instanceof TileEntityForceField)
					  {
	
						  TileEntity proj = world.getBlockTileEntity(Integer.parseInt(ProjectorCorr[2].trim()), Integer.parseInt(ProjectorCorr[1].trim()), Integer.parseInt(ProjectorCorr[0].trim()));
                          if(proj instanceof TileEntityProjector)
                          {
                        	  ((TileEntityForceField)te).setTexturfile(((TileEntityProjector)proj).getForceFieldTexturfile());
                        	  ((TileEntityForceField)te).setTexturid(((TileEntityProjector)proj).getForceFieldTexturID());
                        	  ((TileEntityForceField)te).setForcefieldCamoblockid(((TileEntityProjector)proj).getForcefieldCamoblockid());
                        	  ((TileEntityForceField)te).setForcefieldCamoblockmeta(((TileEntityProjector)proj).getForcefieldCamoblockmeta());
                          }
		  
					  }
				  }
			  }
			  
			  
		  }
		}

	break;
	case 1:
		
		
		
		String fieldname = dat.readUTF();

		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		
		 if(tileEntity instanceof TileEntityMachines)
		 {
			 try{
				 Field f = ReflectionHelper.findField(TileEntityMachines.class, fieldname);
				 reflectionsetvalue(f, tileEntity,dat,fieldname);
			 }catch(Exception e)
			 {
				 if(DEBUG)
				 System.out.println(e.getLocalizedMessage());
			 } 
		 }

		 if(tileEntity instanceof TileEntityCapacitor)
		 {
			 try{
				 Field f = ReflectionHelper.findField(TileEntityCapacitor.class, fieldname);
				 reflectionsetvalue(f, tileEntity,dat,fieldname);
			 }catch(Exception e)
			 {
				 if(DEBUG)
				 System.out.println(e.getLocalizedMessage());
			 } 
		 }
		 
		 if(tileEntity instanceof TileEntityExtractor)
		 {
			 try{
				 Field f = ReflectionHelper.findField(TileEntityExtractor.class, fieldname);
				 reflectionsetvalue(f, tileEntity,dat,fieldname);
			 }catch(Exception e)
			 {
				 if(DEBUG)
				 System.out.println(e.getLocalizedMessage());
			 } 
		 }
		 
		 
		 if(tileEntity instanceof TileEntityConverter)
		 {
			 try{
				 Field f = ReflectionHelper.findField(TileEntityConverter.class, fieldname);
				 reflectionsetvalue(f, tileEntity,dat,fieldname);
			 }catch(Exception e)
			 {
				 if(DEBUG)
				 System.out.println(e.getLocalizedMessage());
			 } 
		 }

		 

		 if(tileEntity instanceof TileEntityProjector)
		 {
			 try{
				 Field f = ReflectionHelper.findField(TileEntityProjector.class, fieldname);
				 reflectionsetvalue(f, tileEntity,dat,fieldname);
			 }catch(Exception e)
			 {
				 if(DEBUG)
				 System.out.println(e.getLocalizedMessage());
			 } 
		 }
		 
		 if(tileEntity instanceof TileEntityAreaDefenseStation)
		 {
			 try{
				 Field f = ReflectionHelper.findField(TileEntityAreaDefenseStation.class, fieldname);
				 reflectionsetvalue(f, tileEntity,dat,fieldname);
			 }catch(Exception e)
			 {
				 if(DEBUG)
				 System.out.println(e.getLocalizedMessage());
			 } 
		 }
		 
		 if(tileEntity instanceof TileEntityAdvSecurityStation)
		 {
			 try{
				 Field f = ReflectionHelper.findField(TileEntityAdvSecurityStation.class, fieldname);
				 reflectionsetvalue(f, tileEntity,dat,fieldname);
			 }catch(Exception e)
			 {
				 if(DEBUG)
				 System.out.println(e.getLocalizedMessage());
			 } 
		 }
		 		
		 
	break;
 }
	

	 
}

public static void reflectionsetvalue(Field f,TileEntity tileEntity,ByteArrayDataInput dat,String fieldname) 
{
	try{
		
		 if(f.getType().equals(java.lang.Integer.TYPE)){f.setInt(tileEntity, Integer.parseInt(dat.readUTF()));}
		 if(f.getType().equals(java.lang.Boolean.TYPE)){f.setBoolean(tileEntity, Boolean.parseBoolean(dat.readUTF()));}
		 if(f.getType().equals(java.lang.Short.TYPE)){f.setShort(tileEntity, Short.parseShort(dat.readUTF()));}
		 if(f.getType().equals(java.lang.Float.TYPE)){f.setFloat(tileEntity, Float.parseFloat(dat.readUTF()));}
		 if(f.getType().equals(java.lang.String.class)){f.set(tileEntity, dat.readUTF());}
		
		 
		 if(tileEntity instanceof INetworkHandlerListener )
		 {
			 ((INetworkHandlerListener)tileEntity).onNetworkHandlerUpdate(fieldname);
		 }
	 }catch(Exception e)
	 {
		 if(DEBUG)
		 System.out.println(e.getMessage());
	 }
}



public static Packet requestInitialData(TileEntity tileEntity){
	return requestInitialData(tileEntity,false);
}


public static void requestForceFieldInitialData(int Dimension, String corridnaten){
	
	
	 try {
		 
		ByteArrayOutputStream bos = new ByteArrayOutputStream(63000);
		DataOutputStream dos = new DataOutputStream(bos);
		
		dos.writeInt(0);
		dos.writeInt(0);
		dos.writeInt(0);
		dos.writeInt(10);
		dos.writeInt(Dimension);
		dos.writeUTF(corridnaten);

		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "MFFS";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = false;
		
		PacketDispatcher.sendPacketToServer(pkt);
		
		
	
		} catch (Exception e) {
			if(true)
			System.out.println(e.getLocalizedMessage());
		}
	
	
}








public static Packet requestInitialData(TileEntity tileEntity,boolean senddirekt){
	
         
//	System.out.println(tileEntity+"Braucht InitDaten");
	
	
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = tileEntity.xCoord;
		int y = tileEntity.yCoord;
		int z = tileEntity.zCoord;
		int typ = 2; // Client -> Server
		
		int Dimension = tileEntity.worldObj.provider.dimensionId;
	   
		StringBuilder str = new StringBuilder();
		
		for(String fields : ((INetworkHandlerListener)tileEntity).getFieldsforUpdate())
		{
			str.append(fields);
			str.append("/");
		}
	
		
		 try {
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeInt(typ);
			dos.writeInt(Dimension);
			dos.writeUTF(str.toString());
	
			
			} catch (Exception e) {
				if(DEBUG)
				System.out.println(e.getLocalizedMessage());
			}
		
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "MFFS";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = false;
		
		if(senddirekt)
		PacketDispatcher.sendPacketToServer(pkt);
		
		return pkt;

}


public static void fireTileEntityEvent(TileEntity tileEntity,String event){
	
	
	if(tileEntity instanceof INetworkHandlerEventListener)
	{
		
	
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = tileEntity.xCoord;
		int y = tileEntity.yCoord;
		int z = tileEntity.zCoord;
		int typ = 3; // Client -> Server
		
		
		
		int Dimension = tileEntity.worldObj.provider.dimensionId;
		
		 try {
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeInt(typ);
			dos.writeInt(Dimension);
			dos.writeUTF(event);
		
			} catch (Exception e) {
				if(DEBUG)
				System.out.println(e.getLocalizedMessage());
			}
		 
			Packet250CustomPayload pkt = new Packet250CustomPayload();
			pkt.channel = "MFFS";
			pkt.data = bos.toByteArray();
			pkt.length = bos.size();
			pkt.isChunkDataPacket = true;
			
			PacketDispatcher.sendPacketToServer(pkt);
		
	}
}



}
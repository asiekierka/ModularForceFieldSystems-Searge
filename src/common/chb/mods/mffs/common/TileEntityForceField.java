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

package chb.mods.mffs.common;

import chb.mods.mffs.network.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;

public class TileEntityForceField extends TileEntity {
private Random random = new Random();
private int[] texturid = {-76,-76,-76,-76,-76,-76};
private String texturfile;
private int Ticker = 0 ;
private int ForcefieldCamoblockid;
private int ForcefieldCamoblockmeta;

public int getTicker() {
	return Ticker;
}

public void setTicker(int ticker) {
	Ticker = ticker;
}

	public TileEntityForceField() {
	}

	
	
	public int getForcefieldCamoblockmeta() {
		return ForcefieldCamoblockmeta;
	}

	public void setForcefieldCamoblockmeta(int forcefieldCamoblockmeta) {
		ForcefieldCamoblockmeta = forcefieldCamoblockmeta;
	}

	public int getForcefieldCamoblockid() {
		return ForcefieldCamoblockid;
	}

	public void setForcefieldCamoblockid(int forcefieldCamoblockid) {
		ForcefieldCamoblockid = forcefieldCamoblockid;
	}

	public String getTexturfile() {
		return texturfile;
	}

	public void setTexturfile(String texturfile) {
		this.texturfile = texturfile;
	}

	public int[] getTexturid()
	{
		return texturid;
	}

	public int getTexturid(int l)
	{
		return texturid[l];
	}


	public void updateEntity() {
		if (worldObj.isRemote == false) {
			if (this.getTicker() >= 20) {
				if(texturid[0] == -76 || texturfile == null)
				{
					UpdateTextur();
				}

				this.setTicker((short) 0);
			}

			this.setTicker((short) (this.getTicker() + 1));
		}else{
			if (this.getTicker() >= 20 + random.nextInt(20)) {
				if(texturid[0] == -76 || texturfile == null)
				{
					ForceFieldClientUpdatehandler.getWorldMap(ModularForceFieldSystem.proxy.getClientWorld()).addto(xCoord, yCoord, zCoord);
				}

				this.setTicker((short) 0);
			}

			this.setTicker((short) (this.getTicker() + 1));
		}
		}
	
	
	
	public void  setTexturid(String remotetextu)
	{
		
		String[] textur = remotetextu.split("/");
		
		this.texturid[0] = Integer.parseInt(textur[0].trim());
		this.texturid[1] = Integer.parseInt(textur[1].trim());
		this.texturid[2] = Integer.parseInt(textur[2].trim());
		this.texturid[3] = Integer.parseInt(textur[3].trim());
		this.texturid[4] = Integer.parseInt(textur[4].trim());
		this.texturid[5] = Integer.parseInt(textur[5].trim());

		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
		this.setTicker((short) 0);
	}

	

	public void  setTexturid(String texturid ,TileEntityProjector proj)
	{
		
		try{
		if(!texturid.equalsIgnoreCase(this.texturid[0]+"/"+this.texturid[1]+"/"+this.texturid[2]+"/"+this.texturid[3]+"/"+this.texturid[4]+"/"+this.texturid[5]))
		{
			
		    String[] textur = texturid.split("/");
			this.texturid[0] = Integer.parseInt(textur[0].trim());
			this.texturid[1] = Integer.parseInt(textur[1].trim());
			this.texturid[2] = Integer.parseInt(textur[2].trim());
			this.texturid[3] = Integer.parseInt(textur[3].trim());
			this.texturid[4] = Integer.parseInt(textur[4].trim());
			this.texturid[5] = Integer.parseInt(textur[5].trim());	

		ForceFieldServerUpdatehandler.getWorldMap(worldObj).addto(xCoord, yCoord, zCoord,  worldObj.provider.dimensionId,proj.xCoord,proj.yCoord,proj.zCoord);
		}
		}catch(Exception ex)
		{
//			System.out.println("[MFFS] Crash Catch:" + ex.getMessage());
		}
	}

	public void UpdateTextur() // Serverside
	{
		if (worldObj.isRemote == false) {
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(worldObj).getForceFieldStackMap(WorldMap.Cordhash(this.xCoord, this.yCoord, this.zCoord));

		if(ffworldmap != null)
		{
			if(!ffworldmap.isEmpty())

			{
			 TileEntityProjector projector = Linkgrid.getWorldMap(worldObj).getProjektor().get(ffworldmap.getProjectorID());

				if(projector != null)
				{
					setTexturid(projector.getForceFieldTexturID(),projector);
					setTexturfile(projector.getForceFieldTexturfile());
				    setForcefieldCamoblockid(projector.getForcefieldCamoblockid());
				    setForcefieldCamoblockmeta(projector.getForcefieldCamoblockmeta());
				}
			}
		}
	}
	}

	public ItemStack[] getContents() {
		return null;
	}

	public void setMaxStackSize(int arg0) {
	}
}

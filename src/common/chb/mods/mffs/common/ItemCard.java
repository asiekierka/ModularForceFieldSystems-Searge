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

import java.util.EnumSet;
import java.util.List;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
import net.minecraftforge.common.DimensionManager;

public class ItemCard extends Item {
	
	private StringBuffer info = new StringBuffer();

	public ItemCard(int id) {
		super(id);
		setMaxStackSize(1);
}
	
	
	@Override
	public String getTextureFile() {
		return "/chb/mods/mffs/sprites/items.png";
	}
	@Override
	public boolean isRepairable() {
		return false;
		
	}
	
	
	
	@Override
	public void addInformation(ItemStack itemStack,EntityPlayer player, List info,boolean b){
		NBTTagCompound tag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		if (tag.hasKey("worldname"))
			info.add("World: " + tag.getString("worldname"));
		if (tag.hasKey("X") && tag.hasKey("Y") && tag.hasKey("Z"))
			info.add("Coords: " + new PointXYZ(tag.getInteger("X"),tag.getInteger("Y"),tag.getInteger("Z"),0).toString());
		if (tag.hasKey("DId"))
			{info.add("valid: "+ tag.getBoolean("valid"));}else{info.add("valid: false");}
	}
	
	public void setInformation(ItemStack itemStack,PointXYZ png, String key,int value){
	
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		
		nbtTagCompound.setInteger(key, value);
		nbtTagCompound.setString("worldname", DimensionManager.getWorld(png.dimensionId).getWorldInfo().getWorldName());
		nbtTagCompound.setInteger("DId", png.dimensionId);
		nbtTagCompound.setInteger("X", png.X);
		nbtTagCompound.setInteger("Y", png.Y);
		nbtTagCompound.setInteger("Z", png.Z);
		nbtTagCompound.setBoolean("valid", true);
	}
	
	
	public int getValuefromKey(String key,ItemStack itemStack)
	{
		NBTTagCompound tag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		if (tag.hasKey(key))
			return tag.getInteger(key);
     return 0;
		
	}
	public PointXYZ getCardTargetPoint(ItemStack itemStack)
	{
		NBTTagCompound tag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		if (tag.hasKey("DId") && tag.hasKey("X") && tag.hasKey("Y") && tag.hasKey("Z")){
		    return new PointXYZ(tag.getInteger("X"),tag.getInteger("Y"),tag.getInteger("Z"),tag.getInteger("DId"));
		}else{
			tag.setBoolean("valid", false);
		}
		
		return null;
	}
	

	
	
	


	

	
}
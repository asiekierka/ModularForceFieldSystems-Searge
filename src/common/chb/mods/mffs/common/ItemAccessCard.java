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

import java.util.List;

import org.lwjgl.input.Keyboard;

import chb.mods.mffs.api.security.SecurityRight;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public class ItemAccessCard extends Item{
	public ItemAccessCard(int i) {
		super(i);
		setIconIndex(20);
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
	public boolean isDamageable()
	{
	return true;
	}
	
    public static boolean hasRight(ItemStack itemStack, SecurityRight sr){
    	NBTTagCompound itemTag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
    	NBTTagCompound rightsTag = itemTag.getCompoundTag("rights");
    	
    	if (itemTag.hasKey(sr.rightKey)){ //Update and delete old keys
    		setRight(itemStack, sr, itemTag.getBoolean(sr.rightKey));
    		itemTag.removeTag(sr.rightKey);
    	}
    	return rightsTag.getBoolean(sr.rightKey);
    }
    
    public static void setRight(ItemStack itemStack, SecurityRight sr, boolean value){
    	NBTTagCompound rightsTag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getCompoundTag("rights");
    	rightsTag.setBoolean(sr.rightKey, value);
    	NBTTagCompoundHelper.getTAGfromItemstack(itemStack).setCompoundTag("rights", rightsTag);
    }
    
	
    public static  void setforArea(ItemStack itemStack, TileEntityAdvSecurityStation sec)
    {
    	if(sec != null)
    	{
    	  NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
    	  nbtTagCompound.setString("Areaname", sec.getStationname());
    	}
    }


    public String getforAreaname(ItemStack itemstack)
    {
    	NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
    	if(nbtTagCompound != null)
    	{
    		return nbtTagCompound.getString("Areaname") ;
    	}
       return "";
    }
	

	
    @Override
    public void addInformation(ItemStack itemStack,EntityPlayer player, List info,boolean b)
    {
        String tooltip = String.format("for Area: %s ", NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getString("Areaname") );
        info.add(tooltip);
        
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            NBTTagCompound rightsTag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getCompoundTag("rights");
            info.add("Rights:");
            for (SecurityRight sr : SecurityRight.rights.values()) {
				if (rightsTag.getBoolean(sr.rightKey)){
					info.add("-" + sr.name);
				}
			}
        }else{
        	info.add("Rights: (Hold Shift)");
        }
    }

}

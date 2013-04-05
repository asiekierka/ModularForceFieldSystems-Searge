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

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public class ItemCardPersonalID extends Item{
	public ItemCardPersonalID(int i) {
		super(i);
		setIconIndex(18);
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
	
	
	public static void setlegitimac(ItemStack itemStack,String right, boolean value)
	{
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		 nbtTagCompound.setBoolean(right, value);
	}
	
	
    public boolean getlegitimac(ItemStack itemstack,String right)
    {
    	NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
    	if(nbtTagCompound != null)
    	{
    		return nbtTagCompound.getBoolean(right);
    	}
       return false;
    }
	

    public static  void setOwner(ItemStack itemStack, String username)
    {
       NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
       nbtTagCompound.setString("name", username);
    }


    public String getUsername(ItemStack itemstack)
    {
    	NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
    	if(nbtTagCompound != null)
    	{
    		return nbtTagCompound.getString("name") ;
    	}
       return "nobody";
    }

    @Override
    public void addInformation(ItemStack itemStack,EntityPlayer player, List info,boolean b)
    {
            String tooltip = String.format( "Owner: %s ", NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getString("name") );
            info.add(tooltip);

            
            
            boolean FFB = NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getBoolean("FFB");
            boolean EB = NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getBoolean("EB");
            boolean CSR = NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getBoolean("CSR");
            boolean SR = NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getBoolean("SR");
            boolean OSS = NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getBoolean("OSS");
            boolean RPB = NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getBoolean("RPB");
            boolean AAI = NBTTagCompoundHelper.getTAGfromItemstack(itemStack).getBoolean("AAI");
            
            info.add("Access Level:");
            

            if(FFB)info.add("ForceField Bypass (FFB)");
            if(EB)info.add("Edit MFFS Block (EB)");
            if(CSR)info.add("Config Security Rights (CSR)");
            if(SR)info.add("Stay Right (SR)");
            if(OSS)info.add("Open Secure Storage (OSS)");
            if(RPB)info.add("Remote Protected Blocks (RPB)");
            if(AAI)info.add("Allow have all Items (AAI)");
    }
}

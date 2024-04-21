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

package chb.mods.mffs.common.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import chb.mods.mffs.common.Linkgrid;
import chb.mods.mffs.common.NBTTagCompoundHelper;
import chb.mods.mffs.common.SecurityRight;
import chb.mods.mffs.common.tileentity.TileEntityAdvSecurityStation;


public class ItemAccessCard extends ItemCardPersonalID{
	
	private int Tick;
	
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
	
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5)
    {
    	if(Tick>1200)
    	{
    		if(getvalidity(itemStack) > 0)
    		{
    			setvalidity(itemStack,getvalidity(itemStack)-1);	
    			
        		int SEC_ID =this.getlinkID(itemStack);
        		if(SEC_ID!=0)
        		{
        			TileEntityAdvSecurityStation sec = Linkgrid.getWorldMap(world).getSecStation().get(SEC_ID);
        			if(sec !=null)
        			{
        				if(!sec.getDeviceName().equals(this.getforAreaname(itemStack)))
        				{
        				  	this.setforArea(itemStack, sec);
        				}
        			}
        		}
    			
    		}
    		Tick=0;
    	}
    	Tick++;
    }
    
    
    public static void setvalidity(ItemStack itemStack,int min)
    {
    	NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
    	 nbtTagCompound.setInteger("validity", min);
    
    }
    

    public static int getvalidity(ItemStack itemStack)
    {
    	NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
    	if(nbtTagCompound != null)
    	{
    		return nbtTagCompound.getInteger("validity") ;
    	}
       return 0;
    }
    
    public static boolean hasRight(ItemStack itemStack, SecurityRight sr){
    	NBTTagCompound itemTag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
    	NBTTagCompound rightsTag = itemTag.getCompoundTag("rights");
    	
    	if (itemTag.hasKey(sr.rightKey)){ 
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
    
    
    public static int getlinkID(ItemStack itemstack)
    {
    	NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
    	if(nbtTagCompound != null)
    	{
    		return nbtTagCompound.getInteger("linkID") ;
    	}
       return 0;
    }
    
    
    
    public static void setlinkID(ItemStack itemStack, TileEntityAdvSecurityStation sec)
    {
    	if(sec != null)
    	{
    	  NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
    	  nbtTagCompound.setInteger("linkID", sec.getDeviceID());
    	}
    }
	
    public static  void setforArea(ItemStack itemStack, TileEntityAdvSecurityStation sec)
    {
    	if(sec != null)
    	{
    	  NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
    	  nbtTagCompound.setString("Areaname", sec.getDeviceName());
    	}
    }


    public static String getforAreaname(ItemStack itemstack)
    {
    	NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
    	if(nbtTagCompound != null)
    	{
    		return nbtTagCompound.getString("Areaname") ;
    	}
       return "not set";
    }
	

	
    @Override
    public void addInformation(ItemStack itemStack,EntityPlayer player, List info,boolean b)
    {
        String SecurityArea = String.format("Security Area: %s ", getforAreaname(itemStack) );
        info.add(SecurityArea);
        
        String validity = String.format("period of validity: %s min", getvalidity(itemStack) );
        info.add(validity);
        
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

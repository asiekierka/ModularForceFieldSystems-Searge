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

import java.util.ArrayList;

import cpw.mods.fml.common.Side;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;



public class Functions {
	public static Block getBlock(int x,int y,int z,World world)
	{
		return Block.blocksList[world.getBlockId(x,y,z)];
	}


	
	public static ArrayList<ItemStack> getItemStackFromBlock(World world,
			int i, int j, int k) {
		Block block = Block.blocksList[world.getBlockId(i, j, k)];

		if (block == null)
			return null;

		int meta = world.getBlockMetadata(i, j, k);

		return block.getBlockDropped(world, i, j, k, meta, 0);
	}


	public static void ChattoPlayer(EntityPlayer player, String Message) {
		player.addChatMessage(Message);
	}


	
	
	public static boolean setIteminSlot(ItemStack itemstack, EntityPlayer entityplayer,TileEntity tileEntity,int Slot, String Cardname)
	{
		if(((IInventory)tileEntity).getStackInSlot(Slot)==null)
		{
			((IInventory)tileEntity).setInventorySlotContents(Slot,itemstack);
			entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
			Functions.ChattoPlayer(entityplayer, "Success: "+Cardname+" Card installed");
			((IInventory)tileEntity).onInventoryChanged();
			return true;
		}
		else
		{
			if(((IInventory)tileEntity).getStackInSlot(Slot).getItem()==ModularForceFieldSystem.MFFSitemcardempty)
			{
				ItemStack itemstackcopy = itemstack.copy();
				((IInventory)tileEntity).setInventorySlotContents(Slot,itemstackcopy);
				Functions.ChattoPlayer(entityplayer, "Success: "+Cardname+" Card data copied ");
				((IInventory)tileEntity).onInventoryChanged();
				return true;
			}
			Functions.ChattoPlayer(entityplayer, "Fail: Slot is not empty");
			return false;
		}
	}
		
	
}

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

package chb.mods.mffs.common.multitool;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chb.mods.mffs.api.PointXYZ;
import chb.mods.mffs.common.Functions;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.SecurityHelper;
import chb.mods.mffs.common.SecurityRight;
import chb.mods.mffs.common.item.ItemCardDataLink;
import chb.mods.mffs.common.item.ItemCardPersonalID;
import chb.mods.mffs.common.tileentity.TileEntityMachines;



public class ItemPersonalIDWriter extends ItemMultitool{
	public ItemPersonalIDWriter(int i) {
		super(i,2);
	}

    public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer entityplayer, Entity entity)
    {
    	if(entity instanceof EntityPlayer)
    	{
		List<Slot> slots = entityplayer.inventoryContainer.inventorySlots;
		for (Slot slot : slots) {
			ItemStack stack = slot.getStack();
			if (stack != null) {
				if (stack.getItem() == ModularForceFieldSystem.MFFSitemcardempty) {
					if(this.consumePower(itemstack, 1000, true))
					{
						this.consumePower(itemstack, 1000, false);
                        ItemStack IDCard= new ItemStack(ModularForceFieldSystem.MFFSItemIDCard, 1);
                        ItemCardPersonalID.setOwner(IDCard, ((EntityPlayer)entity).username);
                    
                        if (--stack.stackSize<=0) {
        					slot.putStack(IDCard);
        				} else if (!entityplayer.inventory.addItemStackToInventory(IDCard))
        					entityplayer.dropPlayerItem(IDCard);

						Functions.ChattoPlayer(entityplayer,"[MultiTool] Success: ID-Card create");
                        return true;
					}else{
						Functions.ChattoPlayer(entityplayer,"[MultiTool] Fail: not enough FP please charge");
						 return true;
					}
				}
			}
		}

		Functions.ChattoPlayer(entityplayer,"[MultiTool] Fail: need MFFS Card <blank> in  Inventory");
        return true;
    	}
    	return false;
    }

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {

		if(entityplayer.isSneaking())
		{
			return super.onItemRightClick(itemstack, world, entityplayer);
		}

			List<Slot> slots = entityplayer.inventoryContainer.inventorySlots;
			for (Slot slot : slots) {
				ItemStack stack = slot.getStack();
				if (stack != null) {
					if (stack.getItem() == ModularForceFieldSystem.MFFSitemcardempty) {
						if(this.consumePower(itemstack, 1000, true))
						{
							this.consumePower(itemstack, 1000, false);
                            ItemStack IDCard= new ItemStack(ModularForceFieldSystem.MFFSItemIDCard, 1);
                            ItemCardPersonalID.setOwner(IDCard, entityplayer.username);
   
                            if (--stack.stackSize<=0) {
            					slot.putStack(IDCard);
            				} else if (!entityplayer.inventory.addItemStackToInventory(IDCard))
            					entityplayer.dropPlayerItem(IDCard);
                            if (world.isRemote)
							Functions.ChattoPlayer(entityplayer,"[MultiTool] Success: ID-Card create");

							return itemstack;
						}else{
							if (world.isRemote)
							Functions.ChattoPlayer(entityplayer,"[MultiTool] Fail: not enough FP please charge");
							return itemstack;
						}
					}
				}
			}
			if (world.isRemote)
			Functions.ChattoPlayer(entityplayer,"[MultiTool] Fail: need MFFS Card <blank> in  Inventory");

		return itemstack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		
		if(world.isRemote)
			return true;
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(tileEntity instanceof TileEntityMachines ){
		 if(SecurityHelper.isAccessGranted(tileEntity, player, world,SecurityRight.UCS))
		 {		 
			List<Slot> slots = player.inventoryContainer.inventorySlots;
			for (Slot slot : slots) {
				ItemStack playerstack = slot.getStack();
				if (playerstack != null) {
					if (playerstack.getItem() == ModularForceFieldSystem.MFFSitemcardempty) {
						if(this.consumePower(stack, 1000, true))
						{
							this.consumePower(stack, 1000, false);
                            ItemStack IDCard= new ItemStack(ModularForceFieldSystem.MFFSitemDataLinkCard);

                            ((ItemCardDataLink)IDCard.getItem()).setforArea(IDCard, ((TileEntityMachines)tileEntity).getDeviceName());
                            ((ItemCardDataLink)IDCard.getItem()).setInformation(IDCard, new PointXYZ(x,y,z,world),"DeviceID", ((TileEntityMachines)tileEntity).getDeviceID(),tileEntity);
                                                   
                            if (--playerstack.stackSize<=0) {
                   					slot.putStack(IDCard);
                   				} else if (!player.inventory.addItemStackToInventory(IDCard))
                   					player.dropPlayerItem(IDCard);                           

                            player.inventoryContainer.detectAndSendChanges();  
							Functions.ChattoPlayer(player,"[MultiTool] Success: DataLink-Card create");

							return true;
						}else{

							Functions.ChattoPlayer(player,"[MultiTool] Fail: not enough FP please charge");
							return false;
						}
					}
				}
			}	
          }
		}
		return false;

	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		
		return false;
	}
	
}

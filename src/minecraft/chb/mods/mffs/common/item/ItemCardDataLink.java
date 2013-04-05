package chb.mods.mffs.common.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chb.mods.mffs.api.PointXYZ;
import chb.mods.mffs.common.Linkgrid;
import chb.mods.mffs.common.MFFSMaschines;
import chb.mods.mffs.common.NBTTagCompoundHelper;
import chb.mods.mffs.common.tileentity.TileEntityMachines;

public class ItemCardDataLink extends ItemCard{

	public ItemCardDataLink(int id) {
		super(id);
		setMaxStackSize(1);
		setIconIndex(22);
	}
	
	
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5)
    {
    	super.onUpdate(itemStack, world, entity, par4, par5);
    	
    	if(Tick>600)
    	{

    		int DeviceID =this.getValuefromKey("DeviceID",itemStack);
    		if(DeviceID!=0)
    		{
    		TileEntityMachines device = Linkgrid.getWorldMap(world).getTileEntityMachines(getDeviceTyp(itemStack), DeviceID);
    	    if(device!=null)
    	    {

				if(!device.getDeviceName().equals(this.getforAreaname(itemStack)))
				{
				  	this.setforArea(itemStack, device.getDeviceName());
				}
    	    	
    	    }
    		}

  
    		Tick=0;
    	}
    	Tick++;
    }
    
	

	public void setInformation(ItemStack itemStack,PointXYZ png, String key,int value,TileEntity tileentity){
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		nbtTagCompound.setString("displayName", MFFSMaschines.fromTE(tileentity).displayName);
		super.setInformation(itemStack, png, key, value);

	}
	
	@Override
	public void addInformation(ItemStack itemStack,EntityPlayer player, List info,boolean b){
		NBTTagCompound tag = NBTTagCompoundHelper.getTAGfromItemstack(itemStack);
		
		info.add("DeviceTyp: " + getDeviceTyp(itemStack));
		info.add("DeviceName: " + getforAreaname(itemStack));
	
		if (tag.hasKey("worldname"))
			info.add("World: " + tag.getString("worldname"));
		if (tag.hasKey("linkTarget"))
			info.add("Coords: " + new PointXYZ(tag.getCompoundTag("linkTarget")).toString());
		if (tag.hasKey("valid"))
		info.add((tag.getBoolean("valid")) ? "\u00a72Valid" : "\u00a74Invalid");
	}
	
    public static String getDeviceTyp(ItemStack itemstack)
    {
    	NBTTagCompound nbtTagCompound = NBTTagCompoundHelper.getTAGfromItemstack(itemstack);
    	if(nbtTagCompound != null)
    	{
    		return nbtTagCompound.getString("displayName") ;
    	}
       return "-";
    }
	
}

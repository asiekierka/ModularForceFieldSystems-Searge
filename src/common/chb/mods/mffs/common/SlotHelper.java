package chb.mods.mffs.common;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotHelper extends Slot{
	private TileEntityMachines te;
	private int Slot;

    public SlotHelper(IInventory par2IInventory, int par3, int par4, int par5)
    {
        super(par2IInventory, par3, par4, par5);
        this.te = (TileEntityMachines) par2IInventory;
        this.Slot = par3;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
         return te.isItemValid(par1ItemStack, Slot);
    }

    @Override
    public int getSlotStackLimit(){
    	return te.getSlotStackLimit(Slot);
    }
}

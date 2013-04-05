package chb.mods.mffs.common.options;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import chb.mods.mffs.common.ItemMFFSBase;
import chb.mods.mffs.common.ModularForceFieldSystem;

public abstract class ItemProjectorOptionBase extends Item{
	public ItemProjectorOptionBase(int i) {
		super(i);
		setMaxStackSize(8);
		setCreativeTab(ModularForceFieldSystem.MFFSTab);
		instances.add(this);
	}
	private static List<ItemProjectorOptionBase> instances = new ArrayList<ItemProjectorOptionBase>();
	public static List<ItemProjectorOptionBase> get_instances(){
		return instances;
	}
	
	@Override
	public String getTextureFile() {
		return "/chb/mods/mffs/sprites/items.png";
	}
	
	public boolean isRepairable() {
		return false;
	}
	
	
	public abstract void addInformation(ItemStack itemStack,EntityPlayer player,List info,boolean par4);
	
}

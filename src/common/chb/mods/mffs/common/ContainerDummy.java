package chb.mods.mffs.common;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;

public class ContainerDummy extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return false;
	}

}

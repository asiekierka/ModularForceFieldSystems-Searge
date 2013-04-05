package chb.mods.mffs.common.options;

import net.minecraft.src.World;
import chb.mods.mffs.common.PointXYZ;
import chb.mods.mffs.common.TileEntityProjector;

public interface IInteriorCheck {
	
	public void checkInteriorBlock(PointXYZ png ,World world,TileEntityProjector Projector);
	
}

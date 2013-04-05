package chb.mods.mffs.network;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import chb.mods.mffs.common.ModularForceFieldSystem;

import net.minecraft.src.World;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public final class ForceFieldClientUpdatehandler implements IScheduledTickHandler{
	
	private static Map WorldForcedield = new HashMap();
	
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		
		
		
		StringBuilder str = new StringBuilder();

		while(!this.getWorldMap(ModularForceFieldSystem.proxy.getClientWorld()).queue.isEmpty()) {
		
			str.append(this.getWorldMap(ModularForceFieldSystem.proxy.getClientWorld()).queue.pop());
			str.append("/");
			str.append(this.getWorldMap(ModularForceFieldSystem.proxy.getClientWorld()).queue.pop());
			str.append("/");
			str.append(this.getWorldMap(ModularForceFieldSystem.proxy.getClientWorld()).queue.pop());
			str.append("#");
			
			if(str.length()> 7500)
			break;	
		}
		
		
		if(str.length()>0)
		{
		NetworkHandlerClient.requestForceFieldInitialData(ModularForceFieldSystem.proxy.getClientWorld().provider.dimensionId, str.toString());
		str.setLength(0);
		}
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "ForceField Client Ticker";
	}

	@Override
	public int nextTickSpacing() {
		return 1;
	}
	
	public static ForceFieldpacket getWorldMap(World world) {
		if (world != null) {
			if (!WorldForcedield.containsKey(world)) {
				WorldForcedield.put(world, new ForceFieldpacket());
			}
			return (ForceFieldpacket) WorldForcedield.get(world);
		}

		return null;
	}


public static class ForceFieldpacket{
	
	protected Stack<Integer> queue = new Stack<Integer>();
	
	public void addto(int x,int y ,int z)
	{
		
		queue.push(x);
		queue.push(y);
		queue.push(z);
		
		
	}



}

}

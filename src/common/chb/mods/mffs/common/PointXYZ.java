
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
    
    Matchlighter
    Thunderdark 

 */

package chb.mods.mffs.common;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;

public class PointXYZ {
	
	public int X = 0;
	public int Y = 0;
	public int Z = 0;
	public int dimensionId;
	

	
	public PointXYZ(int x, int y, int z) {
		
		X=x;
		Y=y;
		Z=z;
		dimensionId = Integer.MAX_VALUE;
		
	}
	
	
	public PointXYZ(int x, int y, int z, World worldObj) {
		
		X=x;
		Y=y;
		Z=z;
		dimensionId =   worldObj.provider.dimensionId;
		
	}
	
	public PointXYZ(int x, int y, int z, int  dimensionId) {
		
		X=x;
		Y=y;
		Z=z;
		this.dimensionId = dimensionId;
		
	}
	
	public World getPointWorld()
	{
		if(dimensionId!=Integer.MAX_VALUE)
			return DimensionManager.getWorld(dimensionId);
		return null;
	}
	
	
	public static double distance(PointXYZ png1, PointXYZ png2)
	{
		if(png1.dimensionId == png2.dimensionId)
		{
			int dx = png1.X - png2.X;
			int dy = png1.Y - png2.Y;
			int dz = png1.Z - png2.Z;
			return  Math.sqrt(dx * dx + dy * dy + dz * dz);
		}
		return Integer.MAX_VALUE;
	}
	

	
	@Override
	public String toString(){
		return "X: " + X + " Y: " + Y + " Z: " + Z;
	}
	
}

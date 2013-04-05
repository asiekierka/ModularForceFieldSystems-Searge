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

package chb.mods.mffs.api;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.src.World;

public class WorldDataManager {

	private static Map<World, WorldDataCollection> WorldData = new HashMap<World, WorldDataManager.WorldDataCollection>();
	
	public static class WorldDataCollection{
		private Map<String, Object> data = new TreeMap<String, Object>();
		
		public void add(String key, Object value){
			data.put(key, value);
		}
		
		public Object get(String key){
			return data.get(key);
		}
		
	}
	
	public static WorldDataCollection getWorldData(World worldObj){
		if (worldObj != null){
			if (!WorldData.containsKey(worldObj)){
				WorldData.put(worldObj, new WorldDataCollection());
			}
			return WorldData.get(worldObj);
		}
		return null;
	}
	
}
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

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ic2.api.ExplosionWhitelist;
import ic2.api.Items;
import net.minecraft.src.Block;
import net.minecraft.src.Container;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;


public enum MFFSMaschines{
	
	Projector("MFFSProjector", "Projector", "chb.mods.mffs.common.TileEntityProjector", "chb.mods.mffs.client.GuiProjector", "chb.mods.mffs.common.ContainerProjector",ModularForceFieldSystem.MFFSProjector,0, "KyKyFyKJK","ByByKyBaB"),
	Extractor("MFFSExtractor", "Extractor", "chb.mods.mffs.common.TileEntityExtractor", "chb.mods.mffs.client.GuiExtractor", "chb.mods.mffs.common.ContainerForceEnergyExtractor",ModularForceFieldSystem.MFFSExtractor,0, " C xFx G "," E xKx J "),
	Capacitor("MFFSCapacitor", "Capacitor", "chb.mods.mffs.common.TileEntityCapacitor", "chb.mods.mffs.client.GuiCapacitor", "chb.mods.mffs.common.ContainerCapacitor",ModularForceFieldSystem.MFFSCapacitor,0, "xJxCFCxJx","xaxEKExax"),
	Converter("MFFSForceEnergyConverter", "Converter", "chb.mods.mffs.common.TileEntityConverter", "chb.mods.mffs.client.GuiConverter", "chb.mods.mffs.common.ContainerConverter",ModularForceFieldSystem.MFFSForceEnergyConverter,0,"ANAJOMAPA","AKAaJIAMA"),
	DefenceStation("MFFSDefenceStation","Defence Station","chb.mods.mffs.common.TileEntityAreaDefenseStation","chb.mods.mffs.client.GuiAreaDefenseStation","chb.mods.mffs.common.ContainerAreaDefenseStation",ModularForceFieldSystem.MFFSDefenceStation,0," J aFa E "," a EKE C "),
	SecurityStation("MFFSSecurtyStation", "Security Station", "chb.mods.mffs.common.TileEntityAdvSecurityStation", "chb.mods.mffs.client.GuiAdvSecurityStation", "chb.mods.mffs.common.ContainerAdvSecurityStation",ModularForceFieldSystem.MFFSSecurtyStation,0, "KCKCFCKJK","CECEKECaC"),
	SecurityStorage("MFFSSecurtyStorage", "Security Storage", "chb.mods.mffs.common.TileEntitySecStorage", "chb.mods.mffs.client.GuiSecStorage", "chb.mods.mffs.common.ContainerSecStorage",ModularForceFieldSystem.MFFSSecurtyStorage,0, "AAAACAAAA","AAAAEAAAA");
  
	String inCodeName;
	String displayName;
	Class<?extends TileEntity> clazz;
	String Gui;
	Class<? extends Container> Container;
	Block block;
	String recipeic;
	String recipeue;
	int baseTex;
	
	
	private MFFSMaschines(String nm, String dispNm, String cls, String gui,
			String container, Block block ,int baseTex,String recipeic,String recipeue) {

		
		this.inCodeName=nm;
		this.displayName = dispNm;
		this.Gui = gui;
		try{this.clazz =  (Class<? extends TileEntity>) Class.forName(cls);}catch(ClassNotFoundException ex){this.clazz = null;}
		try{this.Container =  (Class<?extends Container>) Class.forName(container);}catch(ClassNotFoundException ex){this.Container = null;}
		this.recipeic=recipeic;
		this.recipeue=recipeue;
		this.block = block;
		this.baseTex = baseTex;
	}
	
	public static MFFSMaschines fromTE(TileEntity tem){
		for (MFFSMaschines mach : MFFSMaschines.values()) {
			if (mach.clazz.isInstance(tem))
			{	
				return mach;
			}
			
		}
	
		return null;
	}
	
	
	public static void initialize(){
		
		for (MFFSMaschines mach : MFFSMaschines.values()){
			
			GameRegistry.registerBlock(mach.block);
	        GameRegistry.registerTileEntity(mach.clazz, mach.inCodeName);
	        
			if(ModularForceFieldSystem.ic2found) RecipesFactory.addRecipe(mach.recipeic, 1, 1, mach.block, null);
			if(ModularForceFieldSystem.uefound)  RecipesFactory.addRecipe(mach.recipeue, 1, 2, mach.block, null);
	        
			LanguageRegistry.instance().addNameForObject(mach.block, "en_US","MFFS "+ mach.displayName);
			ExplosionWhitelist.addWhitelistedBlock(mach.block);
	        

		}
	}
	
	
}
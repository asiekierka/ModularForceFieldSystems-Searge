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

import cpw.mods.fml.common.registry.LanguageRegistry;
import ic2.api.Items;
import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;



public enum ProjectorTyp {
	
	wall(1,"Wall","AA AA BB ",ModularForceFieldSystem.MFFSProjectorTypwall,true),
	deflector(2,"Deflector","AAAABAAAA",ModularForceFieldSystem.MFFSProjectorTypdeflector,true),
	tube(3,"Tube","AAA B AAA",ModularForceFieldSystem.MFFSProjectorTyptube,false),
	cube(4,"Cube","B B A B B",ModularForceFieldSystem.MFFSProjectorTypkube,false),
	sphere(5,"Sphere"," B BAB B ",ModularForceFieldSystem.MFFSProjectorTypsphere,false),
	containment(6,"Containment","BBBBABBBB",ModularForceFieldSystem.MFFSProjectorTypcontainment,false),
	AdvCube(7,"Adv.Cube","AAAACAAAA",ModularForceFieldSystem.MFFSProjectorTypAdvCube,false);
	
	public String displayName;
	String recipe;
	Item item;
	public int ProTyp;
	boolean Blockdropper;


private ProjectorTyp(int ProTyp,String dispNm,String recipe,Item item,boolean Blockdropper) {

	this.displayName = dispNm;
	this.recipe=recipe;
	this.item = item;
    this.ProTyp = ProTyp;
    this.Blockdropper = Blockdropper;
}

public static ProjectorTyp TypfromItem(Item item){
	for (ProjectorTyp mach : ProjectorTyp.values()) {
		if (mach.item ==item)
		{	
			return mach;
		}
	}
	return null;
}



public static void initialize(){
	
	for (ProjectorTyp mach : ProjectorTyp.values()){
		generateRecipesFor(mach);
		addNameForObject(mach);
	}
}

public static void addNameForObject(ProjectorTyp mach){

	LanguageRegistry.instance().addNameForObject(mach.item, "en_US"," MFFS Projector Module  "+ mach.displayName);
	
}

public static void generateRecipesFor(ProjectorTyp mach){
	String[] recipeSplit = new String[]{mach.recipe.substring(0, 3), mach.recipe.substring(3, 6), mach.recipe.substring(6, 9)};
	
	
	CraftingManager.getInstance().addRecipe(new ItemStack(mach.item, 1), recipeSplit,

			'C',ModularForceFieldSystem.MFFSProjectorTypkube,
			'B', Block.obsidian,
			'A', ModularForceFieldSystem.MFFSitemFocusmatix
				
	);
	
}

}
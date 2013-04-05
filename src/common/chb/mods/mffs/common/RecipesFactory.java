package chb.mods.mffs.common;

import basiccomponents.BasicComponents;
import ic2.api.Items;
import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

public final class RecipesFactory {
	
	// forMod 0: independent  1: IC2  2: Universal Electric 
	
	
	public static boolean addRecipe(String Recipe,int count, int forMod,Block block,Item item){
		
		if((forMod>2 && forMod < 0) || (count < 0) || (block == null && item == null) 
		   || (block != null && item != null) || (Recipe.length()!= 9))
		{
			System.out.println("[ModularForceFieldSystem] Recipes generating Fail for :" + block +"/"+item);	
			return false;
		}
		
		ItemStack itemstack = null;
		
		if(block != null && item == null)itemstack = new ItemStack(block,count);
		if(block == null && item != null)itemstack = new ItemStack(item,count);

		String[] recipeSplit = new String[]{Recipe.substring(0, 3), Recipe.substring(3, 6),Recipe.substring(6, 9)};
		
		switch(forMod)
		{
		case 0:
						
			CraftingManager.getInstance().addRecipe(itemstack, recipeSplit,	
					
					'a', Item.enderPearl,
					'b', Item.pickaxeSteel,
					'c', Item.bucketEmpty,
					'd', Item.bucketLava,
					'e', Item.bucketWater,
					'f', Item.bone,                   //Vanilla Stuff a++
					'g', Item.blazeRod,
					'h', Item.rottenFlesh,
					'i', Item.diamond,
					'j', Item.spiderEye,
					'k', Block.obsidian,
					'l', Block.glass,
					'm', Item.redstone,
					'n', Block.lever,
					'o', Item.paper,
					
					'u', ModularForceFieldSystem.MFFSitemForcicium,
					'v', ModularForceFieldSystem.MFFSitemFocusmatix,
					'w',ModularForceFieldSystem.MFFSProjectorTypkube,
					'x', new ItemStack(ModularForceFieldSystem.MFFSitemForcePowerCrystal, 1, -1),         //MFFs Stuff z--
					'y', ModularForceFieldSystem.MFFSitemFocusmatix,
					'z', ModularForceFieldSystem.MFFSItemIDCard

					);	
			 return true;

		case 1:
			if(ModularForceFieldSystem.ic2found){	
			
			CraftingManager.getInstance().addRecipe(itemstack, recipeSplit,	
					
					'a', Item.enderPearl,
					'b', Item.pickaxeSteel,
					'c', Item.bucketEmpty,
					'd', Item.bucketLava,
					'e', Item.bucketWater,
					'f', Item.bone,                   //Vanilla Stuff a++
					'g', Item.blazeRod,
					'h', Item.rottenFlesh,
					'i', Item.diamond,
					'j', Item.spiderEye,
					'k', Block.obsidian,
					'l', Block.glass,
					'm', Item.redstone,
					'n', Block.lever,
					'o', Item.paper,
					
					'u', ModularForceFieldSystem.MFFSitemForcicium,
					'v', ModularForceFieldSystem.MFFSitemFocusmatix,
					'w',ModularForceFieldSystem.MFFSProjectorTypkube,
					'x', new ItemStack(ModularForceFieldSystem.MFFSitemForcePowerCrystal, 1, -1),         //MFFs Stuff z--
					'y', ModularForceFieldSystem.MFFSitemFocusmatix,
					'z', ModularForceFieldSystem.MFFSItemIDCard,
					
					'A', Items.getItem("refinedIronIngot"),
					'B', Items.getItem("overclockerUpgrade"),
					'C', Items.getItem("electronicCircuit"),
					'D', Items.getItem("advancedCircuit"),
					'E', Items.getItem("carbonPlate"),
					'F', Items.getItem("advancedMachine"),
					'G', Items.getItem("extractor"),
					'H', Items.getItem("copperCableItem"),                          //Ic2 Stuff A++
					'I', Items.getItem("insulatedCopperCableItem"),
					'J', Items.getItem("frequencyTransmitter"),
					'K', Items.getItem("advancedAlloy"),
					'M', Items.getItem("glassFiberCableItem"),
					'N', Items.getItem("lvTransformer"),
					'O', Items.getItem("mvTransformer"),
					'P', Items.getItem("hvTransformer"),
					'Q', Items.getItem("teslaCoil"),
					'R', Items.getItem("matter"),
					'S', Items.getItem("wrench")
					
					);	
			 return true;
			}
		break;
		case 2:
			if(ModularForceFieldSystem.uefound){
			
				CraftingManager.getInstance().addRecipe(itemstack, recipeSplit,	
				
				'a', Item.enderPearl,
				'b', Item.pickaxeSteel,
				'c', Item.bucketEmpty,
				'd', Item.bucketLava,
				'e', Item.bucketWater,
				'f', Item.bone,                   //Vanilla Stuff a++
				'g', Item.blazeRod,
				'h', Item.rottenFlesh,
				'i', Item.diamond,
				'j', Item.spiderEye,
				'k', Block.obsidian,
				'l', Block.glass,
				'm', Item.redstone,
				'n', Block.lever,
				'o', Item.paper,
				
				'u', ModularForceFieldSystem.MFFSitemForcicium,
				'v', ModularForceFieldSystem.MFFSitemFocusmatix,
				'w',ModularForceFieldSystem.MFFSProjectorTypkube,
				'x', new ItemStack(ModularForceFieldSystem.MFFSitemForcePowerCrystal, 1, -1),       //MFFs Stuff z--
				'y', ModularForceFieldSystem.MFFSitemFocusmatix,
				'z', ModularForceFieldSystem.MFFSItemIDCard,
				
				
				
				'A', BasicComponents.itemSteelIngot,
				'B', BasicComponents.itemBronzePlate,
				'C', BasicComponents.itemSteelPlate,
				'D', BasicComponents.itemTinPlate,
				'E', new ItemStack(BasicComponents.itemCircuit, 1, 0),
				'F', new ItemStack(BasicComponents.itemCircuit, 1, 1),             // UE Stuff A++
				'G', new ItemStack(BasicComponents.itemCircuit, 1, 2),
				'H', BasicComponents.itemMotor,
			    'I', new ItemStack(BasicComponents.blockCopperWire, 1, 0),
                'J', BasicComponents.batteryBox,
				'K', BasicComponents.coalGenerator,
				'M', BasicComponents.electricFurnace,
				'N', BasicComponents.itemCopperIngot,
				'O', BasicComponents.itemWrench
				

				);	
				return true;	
			}	
		break;	
	    }

		
		return false;
	}

	
}

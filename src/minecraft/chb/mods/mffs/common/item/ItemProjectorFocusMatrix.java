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
    Thunderdark - initial implementation
*/

package chb.mods.mffs.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import chb.mods.mffs.common.MFFSMaschines;

public class ItemProjectorFocusMatrix extends ItemMFFSBase  {
	public ItemProjectorFocusMatrix(int i) {
		super(i);
		setIconIndex(66);
		setMaxStackSize(64);
	}

	public String getTextureFile() {
		return "/chb/mods/mffs/sprites/items.png";
	}

	public boolean isRepairable() {
		return false;
	}
	
	@Override
    public void addInformation(ItemStack itemStack,EntityPlayer player, List info,boolean b)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            info.add("compatible with:");
            info.add("MFFS "+MFFSMaschines.Projector.displayName);

        }else{
        	info.add("compatible with: (Hold Shift)");
        }
    }
	
}
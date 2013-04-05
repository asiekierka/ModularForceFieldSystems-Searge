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


package chb.mods.mffs.client;

import chb.mods.mffs.common.ContainerCapacitor;
import chb.mods.mffs.common.ContainerConverter;
import chb.mods.mffs.common.ContainerSecStorage;
import chb.mods.mffs.common.TileEntityConverter;
import chb.mods.mffs.common.TileEntitySecStorage;
import chb.mods.mffs.network.NetworkHandlerClient;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;

import org.lwjgl.opengl.GL11;


public class GuiSecStorage extends GuiContainer {

    private TileEntitySecStorage SecStorage;

    public GuiSecStorage(EntityPlayer player, TileEntitySecStorage tileentity) {
        super(new ContainerSecStorage(player, tileentity));
        SecStorage = tileentity;
        this.xSize = 185;
        this.ySize = 220;
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    	int textur = mc.renderEngine.getTexture("/chb/mods/mffs/sprites/GuiSecStorage.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(textur);
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
    }

    
	protected void actionPerformed(GuiButton guibutton) {
	}

    public void initGui() {
        super.initGui();
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {

    }
}

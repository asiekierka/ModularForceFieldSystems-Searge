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
import chb.mods.mffs.common.TileEntityConverter;
import chb.mods.mffs.network.NetworkHandlerClient;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;

import org.lwjgl.opengl.GL11;


public class GuiConverter extends GuiContainer {

    private TileEntityConverter Converter;

    public GuiConverter(EntityPlayer player, TileEntityConverter tileentity) {
        super(new ContainerConverter(player, tileentity));
        Converter = tileentity;
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    	int textur = mc.renderEngine.getTexture("/chb/mods/mffs/sprites/GuiConvertor.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(textur);
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
        int i1 = (79 * Converter.getCapacity()) / 100;
        drawTexturedModalRect(w + 8, k + 71, 176, 0, i1 + 1, 79);
    }

	protected void actionPerformed(GuiButton guibutton) {
		NetworkHandlerClient.fireTileEntityEvent(Converter, String.valueOf(guibutton.id));
	}

    public void initGui() {
    	controlList.add(new GuiGraphicButton(0, (width / 2) + 67, (height / 2) -80,Converter,1));
    	controlList.add(new GuiButton(1, (width / 2) + 12, (height / 2) -60,12,16,"1"));
    	controlList.add(new GuiButton(2, (width / 2) + 12, (height / 2) -25,12,16,"1"));
    	
    	controlList.add(new GuiButton(3, (width / 2) + 24, (height / 2) -60,16,16,"10"));
    	controlList.add(new GuiButton(4, (width / 2) + 24, (height / 2) -25,16,16,"10"));
    	
    	controlList.add(new GuiButton(5, (width / 2) + 40, (height / 2) -60,22,16,"100"));
    	controlList.add(new GuiButton(6, (width / 2) + 40, (height / 2) -25,22,16,"100"));
    	
        super.initGui();
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    	fontRenderer.drawString("MFFS FE -> EU Converter", 5, 5, 0x404040);
		if(Converter.getLinkedCapacitor()!= null){
			fontRenderer.drawString((new StringBuilder()).append("FE: ").append(Converter.getLinkPower()).toString(), 10, 60, 0x404040);
		}else{
			fontRenderer.drawString("FE: No Link/OOR",  10, 60, 0x404040);
		}
    	fontRenderer.drawString((new StringBuilder()).append("Output: ").append(Converter.getOutput()).append(" EU").toString(), 90, 45, 0x404040);
    }
}

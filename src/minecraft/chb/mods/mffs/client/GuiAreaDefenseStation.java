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

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;

import org.lwjgl.opengl.GL11;

import chb.mods.mffs.common.ContainerAreaDefenseStation;
import chb.mods.mffs.common.TileEntityAreaDefenseStation;
import chb.mods.mffs.network.NetworkHandlerClient;

public class GuiAreaDefenseStation extends GuiContainer {
	private TileEntityAreaDefenseStation DefenceStation;

	public GuiAreaDefenseStation(EntityPlayer player,
			TileEntityAreaDefenseStation tileentity) {
		super(new ContainerAreaDefenseStation(player, tileentity));
		DefenceStation = tileentity;
	}
@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int xSize = 256;
		int ySize = 216;
		int textur = mc.renderEngine.getTexture("/chb/mods/mffs/sprites/GuiDefStation.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(textur);
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
			
	}

@Override
protected void actionPerformed(GuiButton guibutton) {
	NetworkHandlerClient.fireTileEntityEvent(DefenceStation, String.valueOf(guibutton.id));
}

public void initGui() {
	controlList.add(new GuiGraphicButton(0, (width / 2) + 107, (height / 2) -104,DefenceStation,1));
	controlList.add(new GuiGraphicButton(1, (width / 2) + 47, (height / 2)- 38,DefenceStation,2));
	controlList.add(new GuiGraphicButton(2, (width / 2) - 36, (height / 2) - 58,DefenceStation,3));
	controlList.add(new GuiGraphicButton(3, (width / 2) +90, (height / 2) - 104,DefenceStation,4));
    super.initGui();
}

@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("MFFS Defence Station", -30, -17, 0x404040);
		fontRenderer.drawString("scan mode: ", 120, -17, 0x404040);
		
		switch(DefenceStation.getActionmode())
		{
		case 0:
			fontRenderer.drawString("inform", 75, 30, 0x404040);
			
			fontRenderer.drawString(" send Info", 55, 60, 0x404040);
			fontRenderer.drawString(" to player ", 55, 70, 0x404040);
			fontRenderer.drawString(" without (SR)", 55, 80, 0x404040);
			fontRenderer.drawString(" Stay Right", 55, 90, 0x404040);
			
			
		break;
		case 1:
			fontRenderer.drawString("kill", 75, 30, 0x404040);
			
			fontRenderer.drawString(" kill player", 55, 60, 0x404040);
			fontRenderer.drawString(" without (SR)", 55, 70, 0x404040);
			fontRenderer.drawString(" gathers his", 55, 80, 0x404040);
			fontRenderer.drawString(" equipment", 55, 90, 0x404040);
	
		break;
		case 2:
			fontRenderer.drawString("search", 75, 30, 0x404040);
			
			fontRenderer.drawString("scans player", 55, 60, 0x404040);
			fontRenderer.drawString("without (AAI)", 55, 70, 0x404040);
			fontRenderer.drawString("and remove", 55, 80, 0x404040);
			fontRenderer.drawString("banned items", 55, 90, 0x404040);
		break;
		
		case 3:
			fontRenderer.drawString("NPC kill", 75, 30, 0x404040);
			
			fontRenderer.drawString("kill NPC", 55, 60, 0x404040);
			fontRenderer.drawString("friendly ", 55, 70, 0x404040);
			fontRenderer.drawString("or", 55, 80, 0x404040);
			fontRenderer.drawString("hostile ", 55, 90, 0x404040);
		break;
		
		case 4:
			fontRenderer.drawString("NPC kill", 75, 30, 0x404040);
			
			fontRenderer.drawString("kill", 55, 60, 0x404040);
			fontRenderer.drawString("only  ", 55, 70, 0x404040);
			fontRenderer.drawString("hostile", 55, 80, 0x404040);
			fontRenderer.drawString("NPC ", 55, 90, 0x404040);
		break;
		
		
		case 5:
			fontRenderer.drawString("NPC kill", 75, 30, 0x404040);
			
			fontRenderer.drawString("kill", 55, 60, 0x404040);
			fontRenderer.drawString("only  ", 55, 70, 0x404040);
			fontRenderer.drawString("friendly", 55, 80, 0x404040);
			fontRenderer.drawString("NPC ", 55, 90, 0x404040);
		break;
		
		}
		
		
		fontRenderer.drawString("Action desc:", 55, 48, 0x00008B);
		
		
		fontRenderer.drawString("items", 165, 43, 0x228B22);
		
		if(DefenceStation.getcontratyp()==0)
		{
			fontRenderer.drawString("allowed", 160, 57, 0x228B22);
		}
		
		if(DefenceStation.getcontratyp()==1)
		{
			fontRenderer.drawString("banned", 160, 57, 0xFF0000);
		}
		
		
		if(DefenceStation.getLinkedCapacitor()!= null){
		fontRenderer.drawString((new StringBuilder()).append("FE: ").append(DefenceStation.getCapacity()).append(" %").toString(), -5, 6, 0x404040);
		}else{
			fontRenderer.drawString("No Link/OOR", -5, 6, 0xFF0000);
		}
		if(DefenceStation.getSecStation_ID()!=0)
		{
			fontRenderer.drawString("linked", 80, 6, 0x228B22);
		}
		
		
		fontRenderer.drawString("warning", -5, 30, 0x00008B);
		fontRenderer.drawString("perimeter: "+DefenceStation.getInfoDistance(), -28, 48, 0x404040);
		
		
		fontRenderer.drawString("action", -5, 66, 0xEE3B3B);
		fontRenderer.drawString("perimeter: "+DefenceStation.getActionDistance(), -28, 86, 0x404040);
		
		
		
		fontRenderer.drawString("int.buffer: ", 140, 170, 0x404040);
		
		
	}
}

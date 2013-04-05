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
		xSize = 256;
		ySize = 216;
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
	NetworkHandlerClient.fireTileEntityEvent(DefenceStation,0, String.valueOf(guibutton.id));
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
		fontRenderer.drawString("MFFS Defence Station", 10, 8, 0x404040);
		fontRenderer.drawString("scan mode: ", 160, 8, 0x404040);
		
		switch(DefenceStation.getActionmode())
		{
		case 0:
			fontRenderer.drawString("inform", 115, 55, 0x404040);
			
			fontRenderer.drawString(" send Info", 95, 85, 0x404040);
			fontRenderer.drawString(" to player ", 95, 95, 0x404040);
			fontRenderer.drawString(" without (SR)", 95, 105, 0x404040);
			fontRenderer.drawString(" Stay Right", 95, 115, 0x404040);
			
			
		break;
		case 1:
			fontRenderer.drawString("kill", 115, 55, 0x404040);
			
			fontRenderer.drawString(" kill player", 95, 85, 0x404040);
			fontRenderer.drawString(" without (SR)", 95, 95, 0x404040);
			fontRenderer.drawString(" gathers his", 95, 105, 0x404040);
			fontRenderer.drawString(" equipment", 95, 115, 0x404040);
	
		break;
		case 2:
			fontRenderer.drawString("search", 115, 55, 0x404040);
			
			fontRenderer.drawString("scans player", 95, 85, 0x404040);
			fontRenderer.drawString("without (AAI)", 95, 95, 0x404040);
			fontRenderer.drawString("and remove", 95, 105, 0x404040);
			fontRenderer.drawString("banned items", 95, 115, 0x404040);
		break;
		
		case 3:
			fontRenderer.drawString("NPC kill", 115, 55, 0x404040);
			
			fontRenderer.drawString("kill any NPC", 95, 85, 0x404040);
			fontRenderer.drawString("friendly or", 95, 95, 0x404040);
			fontRenderer.drawString("hostile", 95, 105, 0x404040);
		break;
		
		case 4:
			fontRenderer.drawString("NPC kill", 115, 55, 0x404040);
			
			fontRenderer.drawString("kill only", 95, 85, 0x404040);
			fontRenderer.drawString("hostile NPCs", 95, 95, 0x404040);
			
		break;
		
		
		case 5:
			fontRenderer.drawString("NPC kill", 115, 55, 0x404040);
			
			fontRenderer.drawString("kill only", 95, 85, 0x404040);
			fontRenderer.drawString("friendly NPCs", 95, 95, 0x404040);

		break;
		
		}
		
		
		fontRenderer.drawString("Action desc:", 95, 73, 0x00008B);
		
		
		fontRenderer.drawString("items", 205, 68, 0x228B22);
		
		if(DefenceStation.getcontratyp()==0)
		{
			fontRenderer.drawString("allowed", 200, 82, 0x228B22);
		}
		
		if(DefenceStation.getcontratyp()==1)
		{
			fontRenderer.drawString("banned", 200, 82, 0xFF0000);
		}
		
		
		if(DefenceStation.getPowerSourceID()!=0){
			fontRenderer.drawString((new StringBuilder()).append("FE: ").append(DefenceStation.getCapacity()).append(" %").toString(), 35, 31, 0x404040);
		}else{
			fontRenderer.drawString("No Link/OOR", 35, 31, 0xFF0000);
		}
		if(DefenceStation.getSecStation_ID()!=0)
		{
			fontRenderer.drawString("linked", 120, 31, 0x228B22);
		}
		
		
		fontRenderer.drawString("warning", 35, 55, 0x00008B);
		fontRenderer.drawString("perimeter: "+DefenceStation.getInfoDistance(), 12, 73, 0x404040);
		
		
		fontRenderer.drawString("action", 35, 91, 0xEE3B3B);
		fontRenderer.drawString("perimeter: "+DefenceStation.getActionDistance(), 12, 111, 0x404040);
		
		
		
		fontRenderer.drawString("inventory ", 180, 195, 0x404040);
		
		
	}
}

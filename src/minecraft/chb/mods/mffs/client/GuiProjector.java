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

import ic2.api.NetworkHelper;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiErrorScreen;

import org.lwjgl.opengl.GL11;

import chb.mods.mffs.common.ContainerProjector;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.ProjectorTyp;
import chb.mods.mffs.common.TileEntityProjector;
import chb.mods.mffs.network.NetworkHandlerClient;

public class GuiProjector extends GuiContainer {
	private TileEntityProjector projector;

	public GuiProjector(EntityPlayer player,
			TileEntityProjector tileentity) {
		super(new ContainerProjector(player, tileentity));
		projector = tileentity;
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int textur = mc.renderEngine
				.getTexture("/chb/mods/mffs/sprites/GuiProjector.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(textur);

		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;

		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
		int i1 = (79 * projector.getCapacity() / 100);
		drawTexturedModalRect(w + 8, k + 71, 176, 0, i1 + 1, 79);

		if (projector.hasValidTypeMod()) {
			
			if ( ProjectorTyp.TypfromItem(projector.get_type()).ProTyp != 7){
				drawTexturedModalRect(w + 119, k + 43, 177, 143, 16, 16);
			}

			if (ProjectorTyp.TypfromItem(projector.get_type()).ProTyp != 4   && ProjectorTyp.TypfromItem(projector.get_type()).ProTyp != 2 ) {
				drawTexturedModalRect(w + 155, k + 43, 177, 143, 16, 16);
			}

			if (ProjectorTyp.TypfromItem(projector.get_type()).ProTyp == 1 || ProjectorTyp.TypfromItem(projector.get_type()).ProTyp == 2 || ProjectorTyp.TypfromItem(projector.get_type()).ProTyp == 6|| ProjectorTyp.TypfromItem(projector.get_type()).ProTyp == 7) {
				drawTexturedModalRect(w + 137, k + 8, 177, 143, 16, 16);

				drawTexturedModalRect(w + 137, k + 42, 177, 143, 16, 16);

				drawTexturedModalRect(w + 154, k + 25, 177, 143, 16, 16);

				drawTexturedModalRect(w + 120, k + 25, 177, 143, 16, 16);
			}

			if (projector.hasOption(ModularForceFieldSystem.MFFSProjectorOptionCamouflage)) {
				drawTexturedModalRect(w + 137, k + 25, 177, 143, 16, 16); // center
			}
		}
	}

	protected void actionPerformed(GuiButton guibutton) {
		NetworkHandlerClient.fireTileEntityEvent(projector, String.valueOf(guibutton.id));
	}

	public void initGui() {
		controlList.add(new GuiGraphicButton(0, (width / 2) + 4, (height / 2) - 47,projector,1));
		controlList.add(new GuiGraphicButton(1, (width / 2) + 67, (height / 2) -76,projector,2));

		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("MFFS Projector", 5, 5, 0x404040);
		if(projector.getLinkedCapacitor()!=null){
		fontRenderer.drawString(String.valueOf(projector.getLinkPower()), 30, 60,0x404040);
		}else{
			fontRenderer.drawString("No Link/OOR", 30, 60,0x404040);	
		}
	}
}

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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import chb.mods.mffs.common.ContainerAdvSecurityStation;
import chb.mods.mffs.common.TileEntityAdvSecurityStation;
import chb.mods.mffs.network.NetworkHandlerClient;

public class GuiAdvSecurityStation extends GuiContainer {
	private TileEntityAdvSecurityStation tileEntity;
	private static ArrayList<String> flags = new ArrayList<String>();
	private String hovertext;
	
	
	static {
		flags.add("ForceField Bypass (FFB)");
		flags.add("Edit MFFS Block (EB)");
		flags.add("Config Security Rights (CSR)");
		flags.add("Stay Right (SR)");
		flags.add("Open Secure Storage (OSS)");
		flags.add("Remote Protected Block (RPB)");
		flags.add("Allow have all Items (AAI)");
	}

	public GuiAdvSecurityStation(EntityPlayer player,
			TileEntityAdvSecurityStation tileentity) {
		super(new ContainerAdvSecurityStation(player, tileentity));
		this.tileEntity = tileentity;
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		int xSize = 256;
		int ySize = 216;
		int textur = mc.renderEngine.getTexture("/chb/mods/mffs/sprites/GuiAdvSecstation.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(textur);
		int w = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
		
		hovertext = null;
		int x = 5, y =10;
		int scale = 18;

		int count =0;
		
		for (String flag : flags) {
			
			
			if (tileEntity.getLinkOption(flag)) {
				drawSprite(this.guiLeft+x, this.guiTop+40+y, 0,0,count);
			} else {
				drawSprite(this.guiLeft+x, this.guiTop+40+y, 0,scale,count);
			}
			if ((mouseX >= x + guiLeft && mouseX <= x + guiLeft + scale) && (mouseY >= guiTop + 40+ y && mouseY <= guiTop +40+ y + scale)) {
				hovertext = flag;
			}
			y += scale+2;
			if (y >= 40) {
				y = 10;
				x += scale+2;
			}
			
			count++;
		}
			
	}
	
	
	private void drawSprite(int par1, int par2, int par3, int par4,int count)
	{
		int var5 = this.mc.renderEngine.getTexture("/chb/mods/mffs/sprites/AdvSecStationButtons.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var5);
		
		par3+=count*18;
		
		Tessellator var10 = Tessellator.instance;
		var10.startDrawingQuads();
		var10.addVertexWithUV((double)(par1 + 0), (double)(par2 + 18), (double)this.zLevel, (double)((float)(par3 + 0) * 0.0078125F), (double)((float)(par4 + 18) * 0.0078125F));
		var10.addVertexWithUV((double)(par1 + 18), (double)(par2 + 18), (double)this.zLevel, (double)((float)(par3 + 18) * 0.0078125F), (double)((float)(par4 + 18) * 0.0078125F));
		var10.addVertexWithUV((double)(par1 + 18), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 18) * 0.0078125F), (double)((float)(par4 + 0) * 0.0078125F));
		var10.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * 0.0078125F), (double)((float)(par4 + 0) * 0.0078125F));
		var10.draw();
	}
	
	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		
		if (hovertext != null) {
			NetworkHandlerClient.fireTileEntityEvent(tileEntity, hovertext);
		}
	}
	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString("MFFS Advanced Security Station", -32, -17, 0x404040);
		fontRenderer.drawString("Master Personal ID Card", -5, 7, 0x404040);
		fontRenderer.drawString("Rights Allocation", 10, 35, 0x404040);

		if (hovertext != null) {
			List list = new ArrayList();
			list.add(hovertext);
			if (list.size() > 0) {
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				RenderHelper.disableStandardItemLighting();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);

				int j2 = 0;
				for (int k2 = 0; k2 < list.size(); k2++) {
					int i3 = fontRenderer.getStringWidth((String) list.get(k2));
					if (i3 > j2) {
						j2 = i3;
					}
				}

				int l2 = (mouseX - guiLeft) + 12;
				int j3 = mouseY - guiTop - 12;
				int k3 = j2;
				int l3 = 8;
				if (list.size() > 1) {
					l3 += 2 + (list.size() - 1) * 10;
				}

				zLevel = 300F;
				itemRenderer.zLevel = 300F;
				int i4 = 0xf0100010;
				drawGradientRect(l2 - 3, j3 - 4, l2 + k3 + 3, j3 - 3, i4, i4);
				drawGradientRect(l2 - 3, j3 + l3 + 3, l2 + k3 + 3, j3 + l3 + 4, i4, i4);
				drawGradientRect(l2 - 3, j3 - 3, l2 + k3 + 3, j3 + l3 + 3, i4, i4);
				drawGradientRect(l2 - 4, j3 - 3, l2 - 3, j3 + l3 + 3, i4, i4);
				drawGradientRect(l2 + k3 + 3, j3 - 3, l2 + k3 + 4, j3 + l3 + 3, i4, i4);
				int j4 = 0x505000ff;
				int k4 = (j4 & 0xfefefe) >> 1 | j4 & 0xff000000;
				drawGradientRect(l2 - 3, (j3 - 3) + 1, (l2 - 3) + 1, (j3 + l3 + 3) - 1, j4, k4);
				drawGradientRect(l2 + k3 + 2, (j3 - 3) + 1, l2 + k3 + 3, (j3 + l3 + 3) - 1, j4, k4);
				drawGradientRect(l2 - 3, j3 - 3, l2 + k3 + 3, (j3 - 3) + 1, j4, j4);
				drawGradientRect(l2 - 3, j3 + l3 + 2, l2 + k3 + 3, j3 + l3 + 3, k4, k4);
				for (int l4 = 0; l4 < list.size(); l4++) {
					String s = (String) list.get(l4);
					if (l4 == 0) {
						s = (new StringBuilder()).append("\247F").append(s).toString();
					} else {
						s = (new StringBuilder()).append("\2477").append(s).toString();
					}
					fontRenderer.drawStringWithShadow(s, l2, j3, -1);
					if (l4 == 0) {
						j3 += 2;
					}
					j3 += 10;
				}

				zLevel = 0.0F;
				itemRenderer.zLevel = 0.0F;
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}
		
		
		
	}
}

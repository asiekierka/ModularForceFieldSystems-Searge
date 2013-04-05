package chb.mods.mffs.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.Versioninfo;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiManuelScreen extends GuiContainer
{
	private int page = 0;
	private int maxpage;

	private List<String> pages = new ArrayList<String>();

    public GuiManuelScreen(Container par1Container) {
		super(par1Container);
		generateIndex();
		maxpage=pages.size()-1;
		
	}

    @Override
	public void initGui()
    {
    	controlList.add(new GuiButton(0, (width / 2) +90, (height / 2) +80,22,16,"-->"));
    	controlList.add(new GuiButton(1,  (width / 2) -110, (height / 2) +80,22,16,"<--"));
		super.initGui();

    }

	 @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 0)
        {
        	if(page<maxpage){page++;}
        	else{page = 0;}
        }
        if(guibutton.id == 1)
        {
        	if(page>0){page--;}
        	else{page = pages.size()-1;}
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

    	int textur = mc.renderEngine.getTexture("/chb/mods/mffs/sprites/GuiManuel.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(textur);
		int w = (width - 256) / 2;
		int k = (height - 216) / 2;
		drawTexturedModalRect(w, k, 0, 0, 256, 216);

    }
    @Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    	
    	fontRenderer.drawString("ModularForceFieldSystem Guide",   -20, -10, 0xFFFFFF);
    	fontRenderer.drawString("Version:"+ Versioninfo.version(), -20, 0, 0xFFFFFF);
    	getcontent(page);
    	fontRenderer.drawString("Page [" + this.page+"] :"+ pages.get(page) , 5, 168, 0xFFFFFF);
	}
    
    
    private void generateIndex()
    {
    	pages.clear();
    	pages.add("Table of Content");
    	pages.add("Changelog");
    	pages.add("Monazit/Forcicium/-Cell");
    }
    
    private void getcontent(int page)
    {
    	switch(page)
    	{
    	case 0:
    	 fontRenderer.drawString("Table of Content", 50, 20, 0xFFFFFF);	
    	for(int p=0;p<pages.size();p++)	
    	{
    	 fontRenderer.drawString("["+p+"]: "+ pages.get(p), -20, 40+(p*10), 0xFFFFFF);		
    	}
    	break;
    	case 1:
    	 fontRenderer.drawString("Changelog", 50, 20, 0xFFFFFF);
         fontRenderer.drawString("add MFFS MultiTool <Guide> (in progress) ", -20, 40, 0xFFFFFF);
    	 fontRenderer.drawString("Projector Camo System add Mod Blocks", -20, 60, 0xFFFFFF);
    	 fontRenderer.drawString("fix ForceField Typs bug", -20, 70, 0xFFFFFF);
       	 fontRenderer.drawString("(graphic errors possible if not supportet)", -20, 80, 0xFFFFFF);
       	 
    	 fontRenderer.drawString("update UE API to 1.2.0", -20, 100, 0xFFFFFF);
    	break;	
	case 2:
		RenderItem renderItem = new RenderItem();
        RenderHelper.enableGUIStandardItemLighting();
        renderItem.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModularForceFieldSystem.MFFSMonazitOre), -10, 20);
        renderItem.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModularForceFieldSystem.MFFSitemForcicium), -10, 40);
        renderItem.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(ModularForceFieldSystem.MFFSitemForcicumCell), -10, 60);
        RenderHelper.disableStandardItemLighting();
		
        fontRenderer.drawString("Monazit Ore (Block/WorldGen)", 20, 25, 0xFFFFFF);
        fontRenderer.drawString("Forcicium (Item/for Crafting)", 20, 45, 0xFFFFFF);
        fontRenderer.drawString("Forcicium Cell (Item/from Crafting)", 20, 65, 0xFFFFFF);
   		
        fontRenderer.drawString("Monazite can be found between 80 and 0", -20, 80, 0xFFFFFF);
        fontRenderer.drawString("Use furnace to get 4 Forcicium", -20, 90, 0xFFFFFF);
        fontRenderer.drawString("Use IC Macerator to get 8 Forcicium", -20, 100, 0xFFFFFF);
        fontRenderer.drawString("Forcicium Cell can store 1kForcicium", -20, 110, 0xFFFFFF);
        fontRenderer.drawString("if in hand right click to activate", -20, 120, 0xFFFFFF);
        fontRenderer.drawString("when active remove Forcicium from  ", -20, 130, 0xFFFFFF);
        fontRenderer.drawString("Player Inventory and stores it", -20, 140, 0xFFFFFF);
   	break;	
   	}
    }

    


}

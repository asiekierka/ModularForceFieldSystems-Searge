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

package chb.mods.mffs.common;

import buildcraft.api.transport.IExtractionHandler;
import buildcraft.api.transport.IPipe;
import buildcraft.api.transport.PipeManager;
import chb.mods.mffs.common.modules.ItemProjectorModuleAdvCube;
import chb.mods.mffs.common.modules.ItemProjectorModuleContainment;
import chb.mods.mffs.common.modules.ItemProjectorModuleCube;
import chb.mods.mffs.common.modules.ItemProjectorModuleDeflector;
import chb.mods.mffs.common.modules.ItemProjectorModuleSphere;
import chb.mods.mffs.common.modules.ItemProjectorModuleTube;
import chb.mods.mffs.common.modules.ItemProjectorModuleWall;
import chb.mods.mffs.common.options.ItemProjectorOptionBlockBreaker;
import chb.mods.mffs.common.options.ItemProjectorOptionCamoflage;
import chb.mods.mffs.common.options.ItemProjectorOptionDefenseStation;
import chb.mods.mffs.common.options.ItemProjectorOptionFieldFusion;
import chb.mods.mffs.common.options.ItemProjectorOptionFieldManipulator;
import chb.mods.mffs.common.options.ItemProjectorOptionForceFieldJammer;
import chb.mods.mffs.common.options.ItemProjectorOptionMobDefence;
import chb.mods.mffs.common.options.ItemProjectorOptionSponge;
import chb.mods.mffs.common.options.ItemProjectorOptionTouchDamage;
import chb.mods.mffs.network.ForceFieldServerUpdatehandler;
import chb.mods.mffs.network.NetworkHandlerClient;
import chb.mods.mffs.network.NetworkHandlerServer;
import chb.mods.mffs.network.ForceFieldClientUpdatehandler;

import ic2.api.ExplosionWhitelist;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.google.common.collect.Lists;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

@Mod(modid = "ModularForceFieldSystem", name = "Modular ForceField System", version = "2.2.8.1.17c")
@NetworkMod(versionBounds = "[2.2.8.1.17c]", clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { "MFFS" }, packetHandler = NetworkHandlerClient.class), serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { "MFFS" }, packetHandler = NetworkHandlerServer.class))

public class ModularForceFieldSystem {
	
	public static CreativeTabs MFFSTab;

	public static int FORCEFIELDRENDER_ID = 2908;

	public static Block MFFSCapacitor;
	public static Block MFFSProjector;
	public static Block MFFSDefenceStation;
	public static Block MFFSFieldblock;
	public static Block MFFSExtractor;
	public static Block MFFSMonazitOre;
	public static Block MFFSForceEnergyConverter;
	public static Block MFFSSecurtyStorage;
	public static Block MFFSSecurtyStation;

	public static Item MFFSitemForcicumCell;
	public static Item MFFSitemForcicium;
	public static Item MFFSitemForcePowerCrystal;

	public static Item MFFSitemdensifiedForcicium;
	public static Item MFFSitemdepletedForcicium;

	public static Item MFFSitemFocusmatix;
	public static Item MFFSitemSwitch;
	public static Item MFFSitemWrench;

	public static Item MFFSitemFieldTeleporter;
	public static Item MFFSitemMFDidtool;
	public static Item MFFSitemMFDdebugger;
	public static Item MFFSitemcardempty;
	public static Item MFFSitemfc;
	public static Item MFFSItemIDCard;
	public static Item MFFSItemSecLinkCard;
	public static Item MFFSitemManuelBook;

	public static Item MFFSitemupgradeexctractorboost;
	public static Item MFFSitemupgradecaprange;
	public static Item MFFSitemupgradecapcap;

	public static Item MFFSProjectorTypsphere;
	public static Item MFFSProjectorTypkube;
	public static Item MFFSProjectorTypwall;
	public static Item MFFSProjectorTypdeflector;
	public static Item MFFSProjectorTyptube;
	public static Item MFFSProjectorTypcontainment;
	public static Item MFFSProjectorTypAdvCube;

	public static Item MFFSProjectorOptionZapper;
	public static Item MFFSProjectorOptionSubwater;
	public static Item MFFSProjectorOptionDome;
	public static Item MFFSProjectorOptionCutter;
	public static Item MFFSProjectorOptionMoobEx;
	public static Item MFFSProjectorOptionDefenceStation;
	public static Item MFFSProjectorOptionForceFieldJammer;
	public static Item MFFSProjectorOptionCamouflage;
	public static Item MFFSProjectorOptionFieldFusion;

	public static Item MFFSProjectorFFDistance;
	public static Item MFFSProjectorFFStrenght;
	


	public static int MonazitOreworldamount = 4;

	public static int forcefieldblockcost;
	public static int forcefieldblockcreatemodifier;
	public static int forcefieldblockzappermodifier;

	public static int forcefieldtransportcost;
	public static int forcefieldmaxblockpeerTick;

	public static Boolean forcefieldremoveonlywaterandlava;

	public static Boolean influencedbyothermods;
	public static Boolean adventuremap;
	public static Boolean ic2found = false;
	public static Boolean uefound = false;
	public static Boolean buildcraftfound = false;

	public static int ForceciumWorkCylce;
	public static int ForceciumCellWorkCylce;
	public static int ExtractorPassForceEnergyGenerate;
	
	public static int DefenceStationKillForceEnergy;
	public static int DefenceStationSearchForceEnergy;

	public static Configuration MFFSconfig;

	public static String Admin;

	@SidedProxy(clientSide = "chb.mods.mffs.client.ClientProxy", serverSide = "chb.mods.mffs.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance("ModularForceFieldSystem")
	public static ModularForceFieldSystem instance;


	@PreInit
	public void preInit(FMLPreInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(proxy);

		TickRegistry.registerScheduledTickHandler(
				new ForceFieldClientUpdatehandler(), Side.CLIENT);
		TickRegistry.registerScheduledTickHandler(
				new ForceFieldServerUpdatehandler(), Side.SERVER);		
		

		MFFSconfig = new Configuration(event.getSuggestedConfigurationFile());
		event.getModMetadata().version = Versioninfo.version();
		try {
			MFFSconfig.load();

			MFFSTab = new MFFSCreativeTab(CreativeTabs.getNextID(), "MFFS");

			MonazitOreworldamount = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "MonazitOreWorldGen", 4).getInt(4);
			Admin = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "ForceFieldMaster", "nobody").value;
			influencedbyothermods = MFFSconfig.get( Configuration.CATEGORY_GENERAL, "influencedbyothermods", true).getBoolean(true);
			adventuremap = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "adventuremap", false).getBoolean(false);
			forcefieldremoveonlywaterandlava = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldremoveonlywaterandlava", false).getBoolean(false);

			forcefieldtransportcost = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldtransportcost", 10000).getInt(10000);
			forcefieldblockcost = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldblockcost", 1).getInt(1);
			forcefieldblockcreatemodifier = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldblockcreatemodifier", 10).getInt(10);
			forcefieldblockzappermodifier = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldblockzappermodifier", 2).getInt(2);
			forcefieldmaxblockpeerTick = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldmaxblockpeerTick", 5000).getInt(5000);


			ForceciumWorkCylce = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "ForceciumWorkCylce", 250).getInt(250);
			ForceciumCellWorkCylce = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "ForceciumCellWorkCylce", 230).getInt(230);
			ExtractorPassForceEnergyGenerate = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "ExtractorPassForceEnergyGenerate", 10000).getInt(10000);

			DefenceStationKillForceEnergy= MFFSconfig.get(Configuration.CATEGORY_GENERAL, "DefenceStationKillForceEnergy", 10000).getInt(10000);
			DefenceStationSearchForceEnergy = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "DefenceStationSearchForceEnergy", 1000).getInt(1000);
			
			MFFSForceEnergyConverter = new BlockConverter(MFFSconfig.getBlock("MFFSForceEnergyConverter", 687).getInt(687)).setBlockName("MFFSForceEnergyConverter");
			MFFSExtractor = new BlockExtractor(MFFSconfig.getBlock("MFFSExtractor", 682).getInt(682)).setBlockName("MFFSExtractor");
			MFFSMonazitOre = new BlockMonazitOre(MFFSconfig.getBlock("MFFSMonazitOre", 688).getInt(688)).setBlockName("MFFSMonazitOre");
			MFFSDefenceStation = new BlockAreaDefenseStation(MFFSconfig.getBlock("MFFSDefenceStation", 681).getInt(681)).setBlockName("MFFSDefenceStation");
			MFFSCapacitor = new BlockCapacitor(MFFSconfig.getBlock("MFFSCapacitor", 680).getInt(680)).setBlockName("MFFSCapacitor");
			MFFSProjector = new BlockProjector(MFFSconfig.getBlock(
					"MFFSProjector", 685).getInt(685))
					.setBlockName("MFFSProjector");
			MFFSFieldblock = new BlockForceField(MFFSconfig.getBlock(
					"MFFSFieldblock", 683).getInt(683));
			MFFSSecurtyStorage = new BlockSecurtyStorage(MFFSconfig.getBlock(
					"MFFSSecurtyStorage", 684).getInt(684))
					.setBlockName("MFFSSecurtyStorage");
			MFFSSecurtyStation = new BlockAdvSecurtyStation(MFFSconfig
					.getBlock("MFFSSecurtyStation", 686).getInt(686))
					.setBlockName("MFFSSecurtyStation");

			MFFSitemWrench = new ItemWrench(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemWrench", 11107).getInt(
					11107)).setItemName("itemWrench");
			MFFSitemSwitch = new ItemSwitch(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemSwitch", 11108).getInt(
					11108)).setItemName("itemSwitch");
			MFFSitemFieldTeleporter = new ItemFieldtransporter(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM, "itemForceFieldsync",
							11109).getInt(11109))
					.setItemName("itemForceFieldsync");
			MFFSitemMFDidtool = new ItemPersonalIDWriter(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "ItemMFDIDwriter", 11110)
					.getInt(11110)).setItemName("ItemMFDIDwriter");
			MFFSitemMFDdebugger = new ItemDebugger(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemMFDdebugger", 11111)
					.getInt(11111)).setItemName("itemMFDdebugger");
			MFFSitemManuelBook = new ItemManuelBook(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemManuelBook", 11112)
					.getInt(11112)).setItemName("itemManuelBook"); 
			
			MFFSitemcardempty = new ItemCardEmpty(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemcardempty", 11115)
					.getInt(11115)).setItemName("itemcardempty");
			MFFSitemfc = new ItemCardPowerLink(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemfc", 11116).getInt(11116))
					.setItemName("itemfc");
			MFFSitemupgradeexctractorboost = new ItemExtractorUpgradeBooster(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemextractorbooster", 11118).getInt(11118))
					.setItemName("itemextractorbooster");
			MFFSitemupgradecaprange = new ItemCapacitorUpgradeRange(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemupgradecaprange", 11119).getInt(11119))
					.setItemName("itemupgradecaprange");
			MFFSitemupgradecapcap = new ItemCapacitorUpgradeCapacity(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM, "itemupgradecapcap",
							11120).getInt(11120))
					.setItemName("itemupgradecapcap");
			MFFSProjectorTypsphere = new ItemProjectorModuleSphere(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorTypsphere", 11121).getInt(11121))
					.setItemName("itemProjectorTypsphere");
			MFFSProjectorTypkube = new ItemProjectorModuleCube(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorTypkube", 11122).getInt(11122))
					.setItemName("itemProjectorTypkube");
			MFFSProjectorTypwall = new ItemProjectorModuleWall(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorTypwall", 11124).getInt(11124))
					.setItemName("itemProjectorTypwall");
			MFFSProjectorTypdeflector = new ItemProjectorModuleDeflector(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorTypdeflector", 11125).getInt(11125))
					.setItemName("itemProjectorTypdeflector");
			MFFSProjectorTyptube = new ItemProjectorModuleTube(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorTyptube", 11126).getInt(11126))
					.setItemName("itemProjectorTyptube");
			MFFSProjectorOptionZapper = new ItemProjectorOptionTouchDamage(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemupgradeprozapper", 11127).getInt(11127))
					.setItemName("itemupgradeprozapper");
			MFFSProjectorOptionSubwater = new ItemProjectorOptionSponge(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemupgradeprosubwater", 11128).getInt(11128))
					.setItemName("itemupgradeprosubwater");
			MFFSProjectorOptionDome = new ItemProjectorOptionFieldManipulator(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemupgradeprodome", 11129).getInt(11129))
					.setItemName("itemupgradeprodome");
			MFFSProjectorOptionCutter = new ItemProjectorOptionBlockBreaker(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemUpgradeprocutter", 11130).getInt(11130))
					.setItemName("itemUpgradeprocutter");
			MFFSProjectorFFDistance = new ItemProjectorFieldModulatorDistance(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorFFDistance", 11131).getInt(11131))
					.setItemName("itemProjectorFFDistance");
			MFFSProjectorFFStrenght = new ItemProjectorFieldModulatorStrength(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorFFStrength", 11132).getInt(11132))
					.setItemName("itemProjectorFFStrength");
			MFFSitemFocusmatix = new ItemProjectorFocusMatrix(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemPorjectorFocusmatrix", 11133).getInt(11133))
					.setItemName("itemPorjectorFocusmatrix");
			MFFSItemIDCard = new ItemCardPersonalID(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemIDCard", 11134).getInt(
					11134)).setItemName("itemIDCard");
			MFFSItemSecLinkCard = new ItemCardSecurityLink(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemSecLinkCard", 11135)
					.getInt(11135)).setItemName("itemSecLinkCard");
			MFFSProjectorOptionDefenceStation = new ItemProjectorOptionDefenseStation(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorOptiondefencestation", 11137).getInt(
							11137))
					.setItemName("itemProjectorOptiondefencestation");
			MFFSProjectorOptionMoobEx = new ItemProjectorOptionMobDefence(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorOptionMoobEx", 11138).getInt(11138))
					.setItemName("itemProjectorOptionMoobEx");
			MFFSProjectorOptionForceFieldJammer = new ItemProjectorOptionForceFieldJammer(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorOptionFFJammer", 11139).getInt(11139))
					.setItemName("itemProjectorOptionFFJammer");
			MFFSProjectorTypcontainment = new ItemProjectorModuleContainment(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorModuleContainment", 11140).getInt(
							11140))
					.setItemName("itemProjectorModuleContainment");
			MFFSProjectorOptionCamouflage = new ItemProjectorOptionCamoflage(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorOptionCamoflage", 11141)
							.getInt(11141))
					.setItemName("itemProjectorOptionCamoflage");
			MFFSProjectorTypAdvCube = new ItemProjectorModuleAdvCube(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorModuleAdvCube", 11142).getInt(11142))
					.setItemName("itemProjectorModuleAdvCube");
			MFFSProjectorOptionFieldFusion = new ItemProjectorOptionFieldFusion(
					MFFSconfig.getItem(Configuration.CATEGORY_ITEM,
							"itemProjectorOptionFieldFusion", 11143).getInt(
							11143))
					.setItemName("itemProjectorOptionFieldFusion");
			MFFSitemForcePowerCrystal = new ItemForcePowerCrystal(MFFSconfig
					.getItem(Configuration.CATEGORY_ITEM,
							"itemForcePowerCrystal", 11145).getInt(11145))
					.setItemName("itemForcePowerCrystal");
			MFFSitemForcicium = new ItemForcicium(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemForcicium", 11144)
					.getInt(11144)).setItemName("itemForcicium");
			MFFSitemForcicumCell = new ItemForcicumCell(MFFSconfig.getItem(
					Configuration.CATEGORY_ITEM, "itemForcicumCell", 11146)
					.getInt(11146)).setItemName("itemForcicumCell");
			
			
			

		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e,
					"ModularForceFieldSystem has a problem loading it's configuration");
			System.out.println(e.getMessage());
		} finally {
			MFFSconfig.save();
		}
	}

	@Init
	public void load(FMLInitializationEvent evt) {

		initIC2Plugin();
		inituEPlugin();
		initbuildcraftPlugin();
		
		GameRegistry.registerBlock(MFFSMonazitOre);
		GameRegistry.registerBlock(MFFSFieldblock);
		GameRegistry.registerTileEntity(TileEntityForceField.class,"MFFSForceField");
		
		MFFSRecipes.init();
		
		MFFSMaschines.initialize();
		ProjectorTyp.initialize();
		ProjectorOptions.initialize();

		NetworkRegistry.instance().registerGuiHandler(instance, proxy);

		proxy.registerRenderInformation();
		proxy.registerTileEntitySpecialRenderer();
		
		GameRegistry.registerWorldGenerator(new MFFSWorldGenerator());


		LanguageRegistry.instance().addNameForObject(
				MFFSitemupgradeexctractorboost, "en_US",
				"MFFS Extractor Booster");
		LanguageRegistry.instance().addNameForObject(MFFSMonazitOre, "en_US",
				"Monazit Ore");
		LanguageRegistry.instance().addNameForObject(MFFSitemForcicumCell,
				"en_US", "MFFS compact Forcicium Cell");
		LanguageRegistry.instance().addNameForObject(MFFSitemForcicium,
				"en_US", "Forcicium");
		LanguageRegistry.instance().addNameForObject(MFFSitemForcePowerCrystal,
				"en_US", "MFFS Force Energy Crystal");
		LanguageRegistry.instance().addNameForObject(MFFSitemSwitch, "en_US",
				"MFFS MultiTool <Switch>");
		LanguageRegistry.instance().addNameForObject(MFFSitemWrench, "en_US",
				"MFFS MultiTool <Wrench>");
		LanguageRegistry.instance().addNameForObject(MFFSitemManuelBook, "en_US",
				"MFFS MultiTool <Guide>");
		LanguageRegistry.instance().addNameForObject(MFFSitemFocusmatix,
				"en_US", "MFFS Projector Focus Matrix");
		LanguageRegistry.instance().addNameForObject(MFFSitemFieldTeleporter,
				"en_US", "MFFS MultiTool <Field Teleporter>");
		LanguageRegistry.instance().addNameForObject(MFFSitemcardempty,
				"en_US", "MFFS Card <blank> ");
		LanguageRegistry.instance().addNameForObject(MFFSitemfc, "en_US",
				"MFFS Card <Power Link>");
		LanguageRegistry.instance().addNameForObject(MFFSItemIDCard, "en_US",
				"MFFS Card <Personal ID>");
		LanguageRegistry.instance().addNameForObject(MFFSItemSecLinkCard,
				"en_US", "MFFS Card <Security Station Link> ");
		LanguageRegistry.instance().addNameForObject(MFFSitemMFDdebugger,
				"en_US", "MFFS MultiTool <Debugger>");
		LanguageRegistry.instance().addNameForObject(MFFSitemMFDidtool,
				"en_US", "MFFS MultiTool <ID-Card Coder>");
		LanguageRegistry.instance().addNameForObject(MFFSitemupgradecaprange,
				"en_US", "MFFS Capacitor Upgrade <Range> ");
		LanguageRegistry.instance().addNameForObject(MFFSitemupgradecapcap,
				"en_US", "MFFS Capacitor Upgrade <Capacity> ");
	
		LanguageRegistry.instance().addNameForObject(MFFSProjectorFFDistance,
				"en_US", "MFFS Projector Field Modulator <distance>");
		LanguageRegistry.instance().addNameForObject(MFFSProjectorFFStrenght,
				"en_US", "MFFS Projector Field Modulator <strength>");
		
		LanguageRegistry.instance().addStringLocalization("itemGroup.MFFS",
				"en_US", "Modular Force Field System");
				
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent evt) {
		
		ForgeChunkManager.setForcedChunkLoadingCallback(instance,new MFFSChunkloadCallback());
	}
	
	

	public class MFFSChunkloadCallback implements
			ForgeChunkManager.OrderedLoadingCallback {
		@Override
		public void ticketsLoaded(List<Ticket> tickets, World world) {
			for (Ticket ticket : tickets) {
				int MaschineX = ticket.getModData().getInteger("MaschineX");
				int MaschineY = ticket.getModData().getInteger("MaschineY");
				int MaschineZ = ticket.getModData().getInteger("MaschineZ");
				TileEntityMachines Machines = (TileEntityMachines) world
						.getBlockTileEntity(MaschineX, MaschineY, MaschineZ);
				Machines.forceChunkLoading(ticket);

			}
		}

		@Override
		public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world,
				int maxTicketCount) {
			List<Ticket> validTickets = Lists.newArrayList();
			for (Ticket ticket : tickets) {
				int MaschineX = ticket.getModData().getInteger("MaschineX");
				int MaschineY = ticket.getModData().getInteger("MaschineY");
				int MaschineZ = ticket.getModData().getInteger("MaschineZ");

				TileEntity tileEntity = world.getBlockTileEntity(MaschineX,
						MaschineY, MaschineZ);
				if (tileEntity instanceof TileEntityMachines) {
					validTickets.add(ticket);
				}
			}
			return validTickets;
		}

	}


	

	
	public  void initbuildcraftPlugin() {
		
		System.out.println("[ModularForceFieldSystem] Loading module for Buildcraft");

		try {
			
			Class.forName("buildcraft.core.Version");
			buildcraftfound= true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: Buildcraft not found");
		
		}
	}
	

	
	public  void inituEPlugin() {
		
		System.out.println("[ModularForceFieldSystem] Loading module for Universal Electricity");

		try {
			
			Class.forName("basiccomponents.item.ItemBasic");
	        uefound= true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: Universal Electricity not found");
		
		}
	}
	
	
	
	public  void initIC2Plugin() {
		
		System.out.println("[ModularForceFieldSystem] Loading module for IC2");

		try {
			
			Class.forName("ic2.common.Ic2Items");
	        ic2found= true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: IC2 not found");
		
		}
	}

}
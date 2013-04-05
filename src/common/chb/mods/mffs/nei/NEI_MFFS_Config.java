package chb.mods.mffs.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.MultiItemRange;

import chb.mods.mffs.common.Versioninfo;
import chb.mods.mffs.common.ModularForceFieldSystem;

public class NEI_MFFS_Config implements IConfigureNEI {

	@Override
	public void loadConfig() {
		API.hideItem(ModularForceFieldSystem.MFFSitemSwitch.shiftedIndex);
		API.hideItem(ModularForceFieldSystem.MFFSitemFieldTeleporter.shiftedIndex);
		API.hideItem(ModularForceFieldSystem.MFFSitemMFDidtool.shiftedIndex);
		API.hideItem(ModularForceFieldSystem.MFFSitemManuelBook.shiftedIndex);

		API.hideItem(ModularForceFieldSystem.MFFSitemfc.shiftedIndex);
		API.hideItem(ModularForceFieldSystem.MFFSItemIDCard.shiftedIndex);
		API.hideItem(ModularForceFieldSystem.MFFSItemSecLinkCard.shiftedIndex);
		API.hideItem(ModularForceFieldSystem.MFFSFieldblock.blockID);

		MultiItemRange blocks = new MultiItemRange();
		MultiItemRange items = new MultiItemRange();
		MultiItemRange upgrades = new MultiItemRange();
		MultiItemRange modules = new MultiItemRange();

		blocks.add(ModularForceFieldSystem.MFFSForceEnergyConverter);
		blocks.add(ModularForceFieldSystem.MFFSExtractor);
		blocks.add(ModularForceFieldSystem.MFFSDefenceStation);
		blocks.add(ModularForceFieldSystem.MFFSSecurtyStation);
		blocks.add(ModularForceFieldSystem.MFFSCapacitor);
		blocks.add(ModularForceFieldSystem.MFFSProjector);
		blocks.add(ModularForceFieldSystem.MFFSSecurtyStorage);
		blocks.add(ModularForceFieldSystem.MFFSMonazitOre);

		items.add(ModularForceFieldSystem.MFFSitemWrench);
		items.add(ModularForceFieldSystem.MFFSitemMFDdebugger);
		items.add(ModularForceFieldSystem.MFFSitemcardempty);
		items.add(ModularForceFieldSystem.MFFSitemForcePowerCrystal);
		items.add(ModularForceFieldSystem.MFFSitemForcicium);
		items.add(ModularForceFieldSystem.MFFSitemForcicumCell);
		items.add(ModularForceFieldSystem.MFFSitemFocusmatix);

		
		upgrades.add(ModularForceFieldSystem.MFFSitemupgradeexctractorboost);
		upgrades.add(ModularForceFieldSystem.MFFSitemupgradecaprange);
		upgrades.add(ModularForceFieldSystem.MFFSitemupgradecapcap);

		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionZapper);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionSubwater);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionDome);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionCutter);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionDefenceStation);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionMoobEx);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionForceFieldJammer);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionCamouflage);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorOptionFieldFusion);

		upgrades.add(ModularForceFieldSystem.MFFSProjectorFFDistance);
		upgrades.add(ModularForceFieldSystem.MFFSProjectorFFStrenght);



		modules.add(ModularForceFieldSystem.MFFSProjectorTypsphere);
		modules.add(ModularForceFieldSystem.MFFSProjectorTypkube);
		modules.add(ModularForceFieldSystem.MFFSProjectorTypwall);
		modules.add(ModularForceFieldSystem.MFFSProjectorTypdeflector);
		modules.add(ModularForceFieldSystem.MFFSProjectorTyptube);
		modules.add(ModularForceFieldSystem.MFFSProjectorTypcontainment);
		modules.add(ModularForceFieldSystem.MFFSProjectorTypAdvCube);

		API.addSetRange("MFFS.Items.Upgrades", upgrades);
		API.addSetRange("MFFS.Items.Modules", modules);
		API.addSetRange("MFFS.Items", items);
		API.addSetRange("MFFS.Blocks", blocks);
		


	}

	@Override
	public String getName() {
		return "MFFS";
	}
	public String getVersion(){
		return Versioninfo.version();
	}

}

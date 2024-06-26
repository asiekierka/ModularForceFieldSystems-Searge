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

package mods.mffs.common.modules;

import mods.mffs.api.PointXYZ;
import mods.mffs.common.IModularProjector;
import mods.mffs.common.IModularProjector.Slots;
import mods.mffs.common.ModularForceFieldSystem;
import mods.mffs.common.options.*;
import mods.mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

import java.util.Set;

public class ItemProjectorModuleAdvCube extends Module3DBase {
	public ItemProjectorModuleAdvCube(int i) {
		super(i);
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("mffs:modules/AdvCube");
	}

	@Override
	public boolean supportsDistance() {
		return false;
	}

	@Override
	public boolean supportsStrength() {
		return true;
	}

	@Override
	public boolean supportsMatrix() {
		return true;
	}

	@Override
	public void calculateField(IModularProjector projector,
			Set<PointXYZ> ffLocs, Set<PointXYZ> ffInterior) {

		int tpx = 0;
		int tpy = 0;
		int tpz = 0;

		int xMout = projector.countItemsInSlot(Slots.FocusLeft);
		int xPout = projector.countItemsInSlot(Slots.FocusRight);
		int zMout = projector.countItemsInSlot(Slots.FocusDown);
		int zPout = projector.countItemsInSlot(Slots.FocusUp);
		int distance = projector.countItemsInSlot(Slots.Distance);
		int Strength = projector.countItemsInSlot(Slots.Strength) + 2;

		for (int y1 = 0; y1 <= Strength; y1++) {
			for (int x1 = 0 - xMout; x1 < xPout + 1; x1++) {
				for (int z1 = 0 - zPout; z1 < zMout + 1; z1++) {
					if (((TileEntityProjector) projector).getSide() == 0) {

						tpy = y1 - y1 - y1 + 1;
						tpx = x1;
						tpz = z1;
					}

					if (((TileEntityProjector) projector).getSide() == 1) {

						tpy = y1 - 1;
						tpx = x1;
						tpz = z1;
					}

					if (((TileEntityProjector) projector).getSide() == 2) {

						tpz = y1 - y1 - y1 + 1;
						tpy = z1 - z1 - z1;
						tpx = x1 - x1 - x1;
					}

					if (((TileEntityProjector) projector).getSide() == 3) {

						tpz = y1 - 1;
						tpy = z1 - z1 - z1;
						tpx = x1;
					}

					if (((TileEntityProjector) projector).getSide() == 4) {

						tpx = y1 - y1 - y1 + 1;
						tpy = z1 - z1 - z1;
						tpz = x1;
					}
					if (((TileEntityProjector) projector).getSide() == 5) {

						tpx = y1 - 1;
						tpy = z1 - z1 - z1;
						tpz = x1 - x1 - x1;
					}

					if (y1 == 0 || y1 == Strength || x1 == 0 - xMout
							|| x1 == xPout || z1 == 0 - zPout || z1 == zMout) {
						if (((TileEntityProjector) projector)
								.hasOption(
										ModularForceFieldSystem.MFFSProjectorOptionDome,
										true)) {
							switch (((TileEntityProjector) projector).getSide()) {
							case 0:
								if ((((TileEntityProjector) projector).yCoord + tpy) > ((TileEntityProjector) projector).yCoord)
									continue;
								break;
							case 1:
								if ((((TileEntityProjector) projector).yCoord + tpy) < ((TileEntityProjector) projector).yCoord)
									continue;
								break;
							case 2:
								if ((((TileEntityProjector) projector).zCoord + tpz) > ((TileEntityProjector) projector).zCoord)
									continue;
								break;
							case 3:
								if ((((TileEntityProjector) projector).zCoord + tpz) < ((TileEntityProjector) projector).zCoord)
									continue;
								break;
							case 4:
								if ((((TileEntityProjector) projector).xCoord + tpx) > ((TileEntityProjector) projector).xCoord)
									continue;
								break;
							case 5:
								if ((((TileEntityProjector) projector).xCoord + tpx) < ((TileEntityProjector) projector).xCoord)
									continue;
								break;
							}
						}

						ffLocs.add(new PointXYZ(tpx, tpy, tpz, 0));

					} else {

						//ffInterior.add(new PointXYZ(tpx, tpy, tpz, 0));
					}
				}
			}
		}

	}

	public static boolean supportsOption(ItemProjectorOptionBase item) {

		if (item instanceof ItemProjectorOptionCamoflage)
			return true;
		if (item instanceof ItemProjectorOptionDefenseStation)
			return true;
		if (item instanceof ItemProjectorOptionFieldFusion)
			return true;
		if (item instanceof ItemProjectorOptionFieldManipulator)
			return true;
		if (item instanceof ItemProjectorOptionForceFieldJammer)
			return true;
		if (item instanceof ItemProjectorOptionMobDefence)
			return true;
		if (item instanceof ItemProjectorOptionSponge)
			return true;
		if (item instanceof ItemProjectorOptionBlockBreaker)
			return true;

		return false;

	}

	@Override
	public boolean supportsOption(Item item) {

		if (item instanceof ItemProjectorOptionCamoflage)
			return true;
		if (item instanceof ItemProjectorOptionDefenseStation)
			return true;
		if (item instanceof ItemProjectorOptionFieldFusion)
			return true;
		if (item instanceof ItemProjectorOptionFieldManipulator)
			return true;
		if (item instanceof ItemProjectorOptionForceFieldJammer)
			return true;
		if (item instanceof ItemProjectorOptionMobDefence)
			return true;
		if (item instanceof ItemProjectorOptionSponge)
			return true;
		if (item instanceof ItemProjectorOptionBlockBreaker)
			return true;

		return false;
	}

}
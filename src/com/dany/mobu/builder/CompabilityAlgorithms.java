/**
MoBu - Mobile PC Builder, and android application about computer hardware.
    Copyright (C) 2011  Daniel Santiago

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
**/



package com.dany.mobu.builder;

import java.util.ArrayList;

import com.dany.mobu.databases.HardwareDBManager;

//This class handles the factoring of a string to populate a hardware select list
public class CompabilityAlgorithms {

	public static final int MB = 0;
	public static final int CPU = 1;
	public static final int RAM = 2;
	public static final int VGA = 3;
	public static final int PSU = 4;
	public static final int HDD = 5;

	public static String getConditionString(String searchingTable) {
		String conditionString = new String();

		long[] currentComponentsId = CompabilityTest.componentsListId;
		HardwareDBManager db = CompabilityTest.db;

		ArrayList<ArrayList<Object>> computer = new ArrayList<ArrayList<Object>>();

		// Get the Specs of the installed hardware
		for (int i = 0; i < currentComponentsId.length; i++) {
			computer.add(db.getRowInTable(CompabilityTest.tableNames[i],currentComponentsId[i]));
		}

		// Check which Component is being installed, format: item (row being compared to, this row)
		if (searchingTable.equals(HardwareDBManager.TABLE_NAME_CPU)) {
			// CPU depends on the MB's Socket (row2, row2)
			conditionString = "row2 = '" + computer.get(MB).get(2) + "'";

		} else if (searchingTable.equals(HardwareDBManager.TABLE_NAME_RAM)) {
			// RAM depends on the MB's Max Memory Spd (row4, row5), Memory Type (row2, row6), # of Memory Slots (row7, row7)
			conditionString = "row4 <= " + computer.get(MB).get(5) + " AND row2 = '" + computer.get(MB).get(6) + "' AND row7 <= " + computer.get(MB).get(7) + "";

		} else if (searchingTable.equals(HardwareDBManager.TABLE_NAME_VGA)) {
			// VGA Depends on the PSU
			if (currentComponentsId[PSU] != 0) {
				// If PSU, VGA depends  the PSU's Wattage (row9, row2)
				conditionString = "row9 <= " + computer.get(PSU).get(2) + "- 100";
				//Note: VGA's Minimun Wattage must be less than the installed PSU's Wattage - 100 (to be sure)
			} else {
				// If no PSU then there no VGA constrain
			}

		} else if (searchingTable.equals(HardwareDBManager.TABLE_NAME_PSU)) {
			// PSU Depends on the VGA
			if (currentComponentsId[VGA] != 0) {
				// IF VGA, PSU depends on the VGA's Minimum Wattage (row2, row9)
				conditionString = "row2 >= " + computer.get(VGA).get(9) + "+ 100";
				// Note: The PSU's wattage must be the VGA's Minimun Wattage + 100 (to be sure)
			} else {
				// If no VGA then there is no PSU constrain
			}

		} else if (searchingTable.equals(HardwareDBManager.TABLE_NAME_HDD)) {
			// HDD depends only the MB's SATA Speed (row3,row10)
			conditionString = "row3 <= '" + computer.get(MB).get(10)+ "' OR row3 = 'IDE'";
			// Note: IDE HDD are not constrains
		}

		return conditionString;
	}

}

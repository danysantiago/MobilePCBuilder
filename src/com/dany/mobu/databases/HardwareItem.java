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



package com.dany.mobu.databases;

import java.util.ArrayList;

public class HardwareItem{
	
	private long id;
	private String name;
	private ArrayList<String> rows = new ArrayList<String>();
	
	public HardwareItem(){
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addRow(String row){
		rows.add(row);
	}

	public String getRow(int i) {
		return rows.get(i);
	}

	public int getRowsSize() {
		return rows.size();
	}
	
}

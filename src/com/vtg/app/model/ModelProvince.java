package com.vtg.app.model;

import java.util.ArrayList;
import java.util.List;

public class ModelProvince {
	public String id;
	public String name;
	public List<ModelShowroom> showrooms = new ArrayList<ModelShowroom>();

	public ModelProvince(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public ModelProvince() {

	}
}

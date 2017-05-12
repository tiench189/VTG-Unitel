package com.vtg.app.model;

public class ModelShowroom {

	public String id;
	public String name;
	public String description;
	public String img;
	public double latitude;
	public double longitude;

	public ModelShowroom(String id, String name, String description, String img) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.img = img;
	}

	public ModelShowroom() {

	}

}

package com.vtg.app.model;

public class ModelPromotion {
	public String id;
	public String name;
	public String code;
	public String description;
	public String guide;
	public int status;

	public ModelPromotion(String id, String name, String code,
			String description, String guide, int status) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.guide = guide;
		this.status = status;
	}

	public ModelPromotion() {

	}
}

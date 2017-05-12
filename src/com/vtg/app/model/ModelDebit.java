package com.vtg.app.model;

public class ModelDebit {
	public String charge = "";
	public String debitStart = "";
	public String payment = "";
//	public String credit = "";

	public ModelDebit(String charge, String debitStart,
			String payment) {
		super();
		this.charge = charge;
		this.debitStart = debitStart;
		this.payment = payment;
//		this.credit = credit;
	}

	public ModelDebit() {

	}
}

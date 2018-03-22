package com.enation.app.ext.component.bankinfo.model;

import java.io.Serializable;

public class BankInfo implements Serializable
{
	private int id;
	private String bankname;
	private String bankcode;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBankcode() {
		return bankcode;
	}
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}	
}

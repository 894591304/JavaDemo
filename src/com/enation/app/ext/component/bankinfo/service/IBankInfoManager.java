package com.enation.app.ext.component.bankinfo.service;

import java.util.List;

import com.enation.app.ext.component.bankinfo.model.BankInfo;

public abstract interface IBankInfoManager {

	public abstract List<BankInfo> getAll();
	
	public abstract BankInfo getById(int id);
}

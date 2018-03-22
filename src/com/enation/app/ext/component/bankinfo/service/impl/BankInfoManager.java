package com.enation.app.ext.component.bankinfo.service.impl;

import java.util.List;

import com.enation.app.ext.component.bankinfo.model.BankInfo;
import com.enation.app.ext.component.bankinfo.service.IBankInfoManager;
import com.enation.eop.sdk.database.BaseSupport;

public class BankInfoManager extends BaseSupport<BankInfo> implements IBankInfoManager{

	public List<BankInfo> getAll() {
		String sql = "select * from bankinfo";
		List<BankInfo> bList = this.baseDaoSupport.queryForList(sql, BankInfo.class,new Object[]{});
		return bList;
	}

	public BankInfo getById(int id) {
		String sql = "select * from bankinfo where id = ?";
		BankInfo bankInfo= this.baseDaoSupport.queryForObject(sql, BankInfo.class,new Object[]{id});
		return bankInfo;
	}

}

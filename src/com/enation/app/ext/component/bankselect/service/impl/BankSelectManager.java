package com.enation.app.ext.component.bankselect.service.impl;

import java.util.List;

import com.enation.app.ext.component.bankselect.model.BankSelect;
import com.enation.app.ext.component.bankselect.service.IBankSelectManager;
import com.enation.eop.sdk.database.BaseSupport;

public class BankSelectManager extends BaseSupport<BankSelect> implements IBankSelectManager{

	public void add(BankSelect bankSelect) {
		String sql = "select count(*) from bankselect where bankInfoId = ?";
		int count = this.baseDaoSupport.queryForInt(sql,new Object[]{bankSelect.getBankInfoId()});
		if(count==0){
			this.baseDaoSupport.insert("bankselect",bankSelect);
		}else{
			update(bankSelect);
		}
	}

	public List<BankSelect> getByMemberId(int proxyMemberId) {
		String sql = "select * from bankselect where proxyMemberId = ?";
		List<BankSelect> bList = this.baseDaoSupport.queryForList(sql, BankSelect.class, new Object[]{proxyMemberId});
		return bList;
	}

	public BankSelect getDefaultByMemberId(int proxyMemberId) {
		String sql = "select * from bankselect where proxyMemberId = ? and status = 1";
		BankSelect bankSelect = this.baseDaoSupport.queryForObject(sql, BankSelect.class, new Object[]{proxyMemberId});
		return bankSelect;
	}

	public List<BankSelect> getNotDefaultByMemberId(int proxyMemberId) {
		String sql = "select * from bankselect where proxyMemberId = ? and status = 0";
		List<BankSelect> bList = this.baseDaoSupport.queryForList(sql, BankSelect.class, new Object[]{proxyMemberId});
		return bList;
	}

	public void update(BankSelect bankSelect) {
		String sql = "select count(*) from bankselect where bankInfoId = ?";
		int count = this.baseDaoSupport.queryForInt(sql,new Object[]{bankSelect.getBankInfoId()});
		if(count!=0){
			this.baseDaoSupport.update("bankselect",bankSelect,"bankInfoId="+bankSelect.getBankInfoId());
		}
	}

	public void delete(int bankInfoId) {
		String sql = "select count(*) from bankselect where bankInfoId = ?";
		int count = this.baseDaoSupport.queryForInt(sql,new Object[]{bankInfoId});
		if(count!=0){
			String sql1 = "delect from bankselect where bankInfoId = ?";
			this.baseDaoSupport.execute(sql1, new Object[]{bankInfoId});
		}
		
	}

	public void setAllStatusTo0(int proxyMemberId) {
		String sql = "update bankselect set status = 0 where proxyMemberId = ?";
		this.baseDaoSupport.execute(sql, new Object[]{proxyMemberId});		
	}

	public BankSelect getByInfoId(int infoId) {
		String sql = "select * from bankselect where bankInfoId = ?";
		BankSelect bankSelect = this.baseDaoSupport.queryForObject(sql, BankSelect.class, new Object[]{infoId});
		return bankSelect;
	}
}

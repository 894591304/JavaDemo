package com.enation.app.ext.component.useraccount.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.useraccount.model.UserAccount;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.eop.sdk.database.BaseSupport;

public class UserAccountManager extends BaseSupport<UserAccount> implements IUserAccountManager{

	@Transactional(propagation=Propagation.REQUIRED)
	public void add(UserAccount userAccount) {
		this.baseDaoSupport.insert("useraccount", userAccount);		
	}

	
	public UserAccount getByMemberId(int memberId) {
		String sql = "select * from useraccount where memberId = ?";
		List<UserAccount> userAccountList = this.baseDaoSupport.queryForList(sql,UserAccount.class,new Object[]{memberId});
		UserAccount userAccount = userAccountList.get(0);
		return userAccount;
	}

	
	public void setLevelAndUserMoney(int memberId,int level, float credit) {
		UserAccount userAccount = getByMemberId(memberId);
		userAccount.setCreditLevel(level);
		userAccount.setCredit(credit);
		update(userAccount);
	}
	
	public void update(UserAccount userAccount) {
		this.baseDaoSupport.update("useraccount", userAccount,"id="+userAccount.getId());	
	}

	public int getLevel(int memberId) {
		UserAccount userAccount = getByMemberId(memberId);
		if(userAccount==null){
			return 0;
		}
		int level = userAccount.getCreditLevel();
		return level;
	}


	@Override
	public float getRemainCredit(int memberId) {
		UserAccount userAccount = getByMemberId(memberId);
		float remainCredit = userAccount.getRemainCredit();
		return remainCredit;
	}


	@Override
	public String getAccountId(int memberId) {
		UserAccount userAccount = getByMemberId(memberId);
		String accountId = userAccount.getAccountId();
		return accountId;
	}


	@Override
	public void setRemainMoney(int memberId, float remainMoney) {
		UserAccount userAccount = getByMemberId(memberId);
		userAccount.setRemainCredit(remainMoney);
		update(userAccount);
	}


	@Override
	public String getLevelName(int memberId) {
		UserAccount userAccount = getByMemberId(memberId);
		int level = userAccount.getCreditLevel();
		if(level==1){return "金卡";}
		if(level==2){return"白金卡";}
		if(level==3){return"黑卡";}
		return null;
	}


	@Override
	public List<UserAccount> getErrorUserAccount() {
		String sql = "select * from useraccount where remainCredit+repay > credit";
		List<UserAccount> userAccountList = this.baseDaoSupport.queryForList(sql,UserAccount.class,new Object[]{});
		return userAccountList;
	}


	@Override
	public List<UserAccount> getAll() {
		String sql = "select * from useraccount";
		List<UserAccount> userAccountList = this.baseDaoSupport.queryForList(sql,UserAccount.class,new Object[]{});
		return userAccountList;
	}

}

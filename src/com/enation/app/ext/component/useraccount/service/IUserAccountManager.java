package com.enation.app.ext.component.useraccount.service;

import java.util.List;

import com.enation.app.ext.component.useraccount.model.UserAccount;

public abstract interface IUserAccountManager 
{
	public abstract void add(UserAccount userAccount);
	
	public abstract UserAccount getByMemberId(int memberId);	
	
	public abstract void setLevelAndUserMoney(int memberId,int level,float credit);
	
	public abstract void update(UserAccount userAccount);
	
	public abstract int getLevel(int memberId);
	
	public abstract float getRemainCredit(int memberId);
	
	public abstract String getAccountId(int memberId);

	public abstract void setRemainMoney(int memberId,float remainMoney);
	
	public abstract String getLevelName(int memberId);
	
	public abstract List<UserAccount> getErrorUserAccount();
	
	public abstract List<UserAccount> getAll();
}

package com.enation.app.ext.component.mobile.service;

public abstract interface IMobileMemberManager {
	
	public abstract int checkmobile(String mobile);
	
	public abstract int checkname(String username);
	
	public abstract String getUnameByMobile(String mobile);
	
	public abstract String getMobileByMemberId(int memberId);
	
	public abstract int getMemberidByMobile(String mobile);
}
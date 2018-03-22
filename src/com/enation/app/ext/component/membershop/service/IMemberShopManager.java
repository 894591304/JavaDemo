package com.enation.app.ext.component.membershop.service;

import java.util.List;

import com.enation.app.ext.component.membershop.model.MemberShop;

public abstract interface IMemberShopManager 
{
	public abstract void add(MemberShop memberShop);
	
	public abstract MemberShop getByMemberId(int memberId);
	
	public abstract List<MemberShop> OrderByLevel();
	
	public abstract void edit(MemberShop memberShop);
	
	public abstract MemberShop get(int memberId);
	
	public abstract List<MemberShop> search(String keyword);
	
	public abstract void setShowOrHide(int showhide,String memberids);
}

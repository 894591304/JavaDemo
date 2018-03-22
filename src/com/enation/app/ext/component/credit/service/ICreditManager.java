package com.enation.app.ext.component.credit.service;

import java.util.List;
import java.util.Map;

import com.enation.app.ext.component.credit.model.Credit;

public abstract interface ICreditManager 
{
	public abstract void add(Credit credit);
	
	public abstract List getByMemberId(int memberId);
	
	public abstract int checkReviewByMemberId(int memberId);
	
	public abstract void setReviewByMemberId(int memberId,int review);
	
	public abstract Credit get(int memberId);

	public abstract void edit(Credit credit);

	public abstract void importCredit(Map<Integer, List<String>> map);
}

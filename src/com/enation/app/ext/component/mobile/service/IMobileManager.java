package com.enation.app.ext.component.mobile.service;

import com.enation.app.ext.component.mobile.model.Mobile;



public abstract interface IMobileManager 
{
	public abstract int newNum(String mobile);
	
	public abstract void updateNum(Mobile mobile);
	
	public abstract int checkNum(String mobile,String checkNum);
	
	public abstract void delete(String mobile);
	
}

package com.enation.app.ext.component.usermsg.service;

import java.util.List;
import java.util.Map;

import com.enation.app.ext.component.usermsg.UserMsg;;

public abstract interface IUserMsgManager 
{
	public abstract void add(UserMsg userMsg);
	
	public abstract List getByMemberId(int memberId);
}

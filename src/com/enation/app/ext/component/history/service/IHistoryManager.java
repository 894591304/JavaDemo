package com.enation.app.ext.component.history.service;

import java.util.List;

import com.enation.app.ext.component.history.model.History;

public abstract interface IHistoryManager 
{
	public abstract List<History> getAllByMemberid(int memberid);
	
	public abstract void add(History history);
	
	public abstract void deleteByMemberid(int memberid);
	
	public abstract History getByMemberidAndProxyId(int memberid,int proxyid);
	
	public abstract void edit(History history);
}

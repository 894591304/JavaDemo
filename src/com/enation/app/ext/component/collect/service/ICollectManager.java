package com.enation.app.ext.component.collect.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.collect.model.Collect;


public abstract interface ICollectManager {

	@Transactional(propagation=Propagation.REQUIRED)
	public abstract int add(int memberid,int proxyid);
	
	public abstract int checkCollect(int memberid,int proxyid);
	
	public abstract int cancel(int memberid,int proxyid);
	
	public abstract Collect getCollect(int memberid,int proxyid);
	
	public abstract void delete(int memberid,int proxyid);
	
	public abstract int getAllCollect(int proxyid);
	
	public abstract int collect(int memberid,int proxyid);
	
	public abstract List<Collect> getAllByMemberid(int memberid);
	
	public abstract void deleteAllByMemberid(int memberid);
}

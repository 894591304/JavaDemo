package com.enation.app.ext.component.proxycount.service;

import java.util.List;

import com.enation.app.ext.component.proxycount.model.ProxyCount;

public abstract interface IProxyCountManager {
	
	public abstract ProxyCount get(int goodsid);
	
	public abstract List<ProxyCount> getAll();
	
	public abstract void update(ProxyCount proxyCount);
	
	public abstract void addProxyTime(int goodsid);
	
	public abstract void addSell(int goodsid, int num);
	
	public abstract void addProxyAll(int goodsid,int num);
	
	public abstract void addNew(int goodsid);
	
	public abstract List<ProxyCount> getHotProxyDown();
	
	public abstract List<ProxyCount> getSellHotProxyDown();
}

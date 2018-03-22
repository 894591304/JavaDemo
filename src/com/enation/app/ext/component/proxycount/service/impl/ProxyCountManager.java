package com.enation.app.ext.component.proxycount.service.impl;

import java.util.List;

import com.enation.app.ext.component.proxycount.model.ProxyCount;
import com.enation.app.ext.component.proxycount.service.IProxyCountManager;
import com.enation.eop.sdk.database.BaseSupport;

public class ProxyCountManager extends BaseSupport<ProxyCount> implements IProxyCountManager{

	
	public List<ProxyCount> getAll() {
		String sql = "select * from proxycount";
		List<ProxyCount> pList = this.baseDaoSupport.queryForList(sql, ProxyCount.class, new Object[]{});
		return pList;
	}

	
	public void update(ProxyCount proxyCount) {
		this.baseDaoSupport.update("proxycount",proxyCount, "id="+proxyCount.getId());		
	}

	
	public void addProxyTime(int goodsid) {
		checkAndAdd(goodsid);
		ProxyCount proxyCount = get(goodsid);
		proxyCount.setProxyTimeCount(proxyCount.getProxyTimeCount()+1);
		update(proxyCount);
	}

	
	public void addSell(int goodsid, int num) {
		checkAndAdd(goodsid);
		ProxyCount proxyCount = get(goodsid);
		proxyCount.setSellCount(proxyCount.getSellCount()+num);
		update(proxyCount);
	}

	
	public void addProxyAll(int goodsid, int num) {
		checkAndAdd(goodsid);
		ProxyCount proxyCount = get(goodsid);
		proxyCount.setProxyAllCount(proxyCount.getProxyAllCount()+num);
		update(proxyCount);
	}


	
	public ProxyCount get(int goodsid) {
		String sql = "select * from proxycount where goodsId = ?";
		ProxyCount proxyCount = this.baseDaoSupport.queryForObject(sql, ProxyCount.class, new Object[]{goodsid});
		return proxyCount;
	}


	
	public void addNew(int goodsid) {
		ProxyCount proxyCount = new ProxyCount();
		proxyCount.setGoodsId(goodsid);
		proxyCount.setProxyAllCount(0);
		proxyCount.setProxyTimeCount(0);
		proxyCount.setSellCount(0);
		this.baseDaoSupport.insert("proxycount",proxyCount);
	}
	
	public void checkAndAdd(int goodsid){
		String sql = "select * from proxycount where goodsId = ?";
		ProxyCount proxyCount = this.baseDaoSupport.queryForObject(sql,ProxyCount.class,new Object[]{goodsid});
		if(proxyCount==null){
			addNew(goodsid);
		}
	}


	
	public List<ProxyCount> getHotProxyDown() {
		String sql = "select * from proxycount order by proxyTimeCount desc";
		List<ProxyCount> pList = this.baseDaoSupport.queryForList(sql, ProxyCount.class,new Object[]{});
		return pList;
	}


	@Override
	public List<ProxyCount> getSellHotProxyDown() {
		String sql = "select * from proxycount order by sellCount desc";
		List<ProxyCount> pList = this.baseDaoSupport.queryForList(sql, ProxyCount.class,new Object[]{});
		return pList;
	}

}

package com.enation.app.ext.component.proxy.service;

import java.util.List;

import com.enation.app.ext.component.proxy.model.Proxy;

public abstract interface IProxyManager 
{
	public abstract void add(Proxy proxy);
	
	public abstract List<Proxy> getByGoodsIdAndMemberId(int goodsId,int memberId);
	
	public abstract int checkProxy(int goodsId,int memberId);
	
	public abstract int changeGoodsLabel(String goodsLabel,int goodsId,int memberId);
	
	public abstract int changeCategory(String goodsCategory,int goodsId,int memberId);
	
	public abstract Proxy getForOnSale(int goodsId,int memberId);
	
	public abstract void edit(Proxy proxy);
	
	public abstract Proxy get(int id);
	
	public abstract List<Proxy> getAgentMember(int goodsId);
	
	public abstract List<Proxy> getAllCanBuyByMemberid(int memberid);
	
	public abstract List<Proxy> getNewByMemberid(int memberid);
	
	public abstract List<Proxy> getHotByMemberid(int memberid);
	
	public abstract int getAllSaleByMemberid(int memberid);
	
	public abstract int getAllAgentNumByGoodsid(int goodsid);
	
	public abstract int checkProxyCanBuy(int proxyid);
	
	public abstract List<Proxy> getAllByMemberid(int memberid);
	
	public abstract List<Proxy> getAllByGoodsid(int goodsid);
	
	public abstract List<Proxy> getAll();
	
	public abstract boolean checkCanProxy(int memberid,int brokerages,int goodsid);
}

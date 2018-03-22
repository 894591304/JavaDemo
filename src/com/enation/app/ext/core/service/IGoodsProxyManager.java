package com.enation.app.ext.core.service;

import java.util.List;
import java.util.Map;

import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.shop.core.model.Goods;

public abstract interface IGoodsProxyManager {
	
	public abstract Map setGoodsInfoIntoProxyList(int goodsid,int LeftOrRight);
	
	public abstract List<Goods> getDiscountList(int tagid);
	
	public abstract Goods getGoods(int goodsid);
	
	public abstract List<Goods> getLatelyGoods();
	
	public abstract void update(Goods goods);
	
	public abstract int getAgentid(int goodsid);
	
	public abstract String getThird(int goodsid);
	
	public abstract List<Goods> search(String keyword);
	
	public abstract List<Goods> getByCatid(int catid);
}

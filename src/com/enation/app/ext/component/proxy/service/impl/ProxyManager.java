package com.enation.app.ext.component.proxy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.components.Select;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.database.BaseSupport;
import com.opensymphony.xwork2.Result;

public class ProxyManager extends BaseSupport<Proxy> implements IProxyManager{

	@Transactional(propagation=Propagation.REQUIRED)
	public void add(Proxy proxy) {
		this.baseDaoSupport.insert("proxy", proxy);
		
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<Proxy> getByGoodsIdAndMemberId(int goodsId, int memberId) {
		String sql = "select * from proxy where goodsId = ? and memberId = ? group by id desc";
		List<Proxy> proxyList = this.baseDaoSupport.queryForList(sql,Proxy.class,new Object[]{goodsId,memberId});
		return proxyList;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int changeCategory(String goodsCategory,int goodsId,int memberId){
		int check = checkProxy(goodsId, memberId);
		if(check == 1)
		{
			this.baseDaoSupport.execute("update proxy set goodsCategory = ? where goodsId = ? and memberId = ?", new Object[]{goodsCategory,goodsId,memberId});
			return 1;
		}else{
			return 0;
		}
		
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int changeGoodsLabel(String goodsLabel,int goodsId,int memberId){
		int check = checkProxy(goodsId, memberId);
		if(check == 1)
		{
			this.baseDaoSupport.execute("update proxy set goodsLabel = ? where goodsId = ? and memberId = ?", new Object[]{goodsLabel,goodsId,memberId});
			return 1;
		}else{
			return 0;
		}
	}
	
	public int checkProxy(int goodsId,int memberId){
		int result = 0;
		Long nTime = System.currentTimeMillis()/1000;
		String sql = "select * from proxy where goodsId = "+goodsId+" and memberId = "+memberId+" and proxyTestTime>"+nTime+" and goodsAmount > sale and status!=0 group by id desc";
		List proxyList = getByGoodsIdAndMemberId(goodsId, memberId);//有代理内容
		List proxyList2 = this.baseDaoSupport.queryForList(sql);//有代理内容并且代理还未结束并且未全部销售完并且未下架
		if(proxyList.isEmpty()){
			return result;
		}else if(proxyList2.isEmpty()){
			result = -1;
		}else{
			result = 1;
			}
		return result;
	}
	
	public Proxy getForOnSale(int goodsId,int memberId){
		Long nTime = System.currentTimeMillis()/1000;
		String sql = "select * from proxy where goodsId = "+goodsId+" and memberId = "+memberId+" and proxyTestTime>"+nTime+" and goodsAmount > sale and status!=0 group by id desc";
		Proxy proxy = this.baseDaoSupport.queryForObject(sql, Proxy.class);
		return proxy;
	}

	@Override
	public void edit(Proxy proxy) {
		this.baseDaoSupport.update("proxy",proxy, "id="+proxy.getId());		
	}

	@Override
	public Proxy get(int id) {
		String sql = "select * from proxy where id = ?";
		Proxy proxy = this.baseDaoSupport.queryForObject(sql, Proxy.class, new Object[]{id});
		return proxy;
	}

	@Override
	public List<Proxy> getAgentMember(int goodsId) {
		Long nTime = System.currentTimeMillis()/1000;
		String sql = "select * from proxy where goodsId = "+goodsId+" and proxyTestTime>"+nTime+" and goodsAmount > sale and status!=0 group by id desc";
		List<Proxy> agentMemberList = this.baseDaoSupport.queryForList(sql,Proxy.class);
		return agentMemberList;
	}
	
	public List<Proxy> getAllCanBuyByMemberid(int memberid){
		Long nTime = System.currentTimeMillis()/1000;
		String sql = "select * from proxy where memberId = "+memberid+" and proxyTestTime>"+nTime+" and goodsAmount > sale and status!=0 group by id desc";
		List<Proxy> agentMemberList = this.baseDaoSupport.queryForList(sql,Proxy.class);
		return agentMemberList;
	}
	
	public List<Proxy> getNewByMemberid(int memberid){
		Long nTime = System.currentTimeMillis()/1000;
		String sql = "select * from proxy where memberId = "+memberid+" and proxyTestTime>"+nTime+" and goodsAmount > sale and status=2 group by id desc";
		List<Proxy> agentMemberList = this.baseDaoSupport.queryForList(sql,Proxy.class);
		return agentMemberList;
	}
	
	public List<Proxy> getHotByMemberid(int memberid){
		Long nTime = System.currentTimeMillis()/1000;
		String sql = "select * from proxy where memberId = "+memberid+" and proxyTestTime>"+nTime+" and goodsAmount > sale and status=1 group by id desc";
		List<Proxy> agentMemberList = this.baseDaoSupport.queryForList(sql,Proxy.class);
		return agentMemberList;
	}

	@Override
	public int getAllSaleByMemberid(int memberid) {
		int AllSale = 0;
		String sql = "select * from proxy where memberId = "+memberid;
		List<Proxy> pList = this.baseDaoSupport.queryForList(sql,Proxy.class);
		int total = pList.size();
		for(int i=0;i<total;i++){
			AllSale=AllSale+pList.get(i).getSale();
		}
		return AllSale;
	}

	public int getAllAgentNumByGoodsid(int goodsid) {
		String sql = "select * from proxy where goodsId = ?";
		List<Proxy> pList = this.baseDaoSupport.queryForList(sql, Proxy.class, new Object[]{goodsid});
		int total = pList.size();
		int result = 0;
		for(int i=0;i<total;i++)
		{result = result+pList.get(i).getGoodsAmount();}
		return result;
	}

	public int checkProxyCanBuy(int proxyid) {
		Proxy proxy = get(proxyid);
		Long nTime = System.currentTimeMillis()/1000;
		if(proxy.getStatus()==0){return 0;}
		else if(nTime>Long.valueOf(proxy.getProxyTestTime())){return 0;}
		else if(proxy.getGoodsAmount()==proxy.getSale()){return 0;}
		return 1;
	}

	public List<Proxy> getAllByMemberid(int memberid) {
		String sql = "select * from proxy where memberId = "+memberid+" group by id desc";
		List<Proxy> agentMemberList = this.baseDaoSupport.queryForList(sql,Proxy.class);
		return agentMemberList;
	}

	public List<Proxy> getAllByGoodsid(int goodsid) {
		String sql = "select * from proxy where goodsId = ?";
		List<Proxy> pList = this.baseDaoSupport.queryForList(sql, Proxy.class, new Object[]{goodsid});
		return pList;
	}

	@Override
	public List<Proxy> getAll() {
		String sql = "select * from proxy";
		List<Proxy> agentMemberList = this.baseDaoSupport.queryForList(sql,Proxy.class);
		return agentMemberList;
	}

	@Override
	public boolean checkCanProxy(int memberid, int brokerages,int goodsid ) {
		String sql = "select count(*) from goods_proxymember_rules where (members like '%,"+memberid+"' or members like '%,"+memberid+",%' or members like '"+memberid+",%' or members = '"+memberid+"') and goods_id = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{goodsid});
		if(count!=0){
			return false;
		}else{
			String sql1 = "select count(*) from goods_brokerageuser_rules where (brokerages like '%,"+brokerages+"' or brokerages like '%,"+brokerages+",%' or brokerages like '"+brokerages+",%' or brokerages = '"+brokerages+"') and goods_id = ?";
			count = this.baseDaoSupport.queryForInt(sql1, new Object[]{goodsid});
			if(count !=0){
				return false;
			}
		}
		return true;
	}
	

}

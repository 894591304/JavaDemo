package com.enation.app.b2b2c.component.goods.service.impl;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.component.goods.service.IGoodsRulesManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.database.BaseSupport;

public class GoodsRulesManager extends BaseSupport<Goods> implements IGoodsRulesManager {

	@Override
	public String getProxyMembers(String goodsids) {
		String sql = "select members from es_goods_proxymember_rules where goods_id = "+goodsids;
		String proxyMembers ="";
		List<Map> result = this.baseDaoSupport.queryForList(sql);
		if(result.size()>0){
			proxyMembers = result.get(0).get("members").toString();
		}
		return proxyMembers;
	}

	@Override
	public String getBrokerageUsers(String goodsids) {
		String sql = "select brokerages from es_goods_brokerageuser_rules where goods_id = "+goodsids;
		String brokerageUsers = "";
		List<Map> result = this.baseDaoSupport.queryForList(sql);
		if(result.size()>0){
			brokerageUsers = result.get(0).get("brokerages").toString();
		}
		return brokerageUsers;
	}

	@Override
	public void setProxyMembers(String members, String goodsid) {
		String sql = "select count(0) from es_goods_proxymember_rules where goods_id=? ";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[] { goodsid });
		if(count>0){
			sql = "update es_goods_proxymember_rules set members='"+members+"' where goods_id =" +goodsid;
			this.baseDaoSupport.execute(sql, new Object[0]);
		}else{
			sql = "insert es_goods_proxymember_rules (goods_id,members) values("+goodsid+",'"+members+"')";;
			this.baseDaoSupport.execute(sql, new Object[0]);
		}
	}

	@Override
	public void setBrokerageUsers(String brokerages, String goodsid) {
		String sql = "select count(0) from es_goods_brokerageuser_rules where goods_id=? ";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[] { goodsid });
		if(count>0){
			sql = "update es_goods_brokerageuser_rules set brokerages='"+brokerages+"' where goods_id =" +goodsid;
			this.baseDaoSupport.execute(sql, new Object[0]);
		}else{
			sql = "insert es_goods_brokerageuser_rules (goods_id,brokerages) values("+goodsid+",'"+brokerages+"')";;
			this.baseDaoSupport.execute(sql, new Object[0]);
		}
	}

	@Override
	public void deleteBrokerageUsers(String goodsid) {
		this.baseDaoSupport.execute("delete from es_goods_brokerageuser_rules where goods_id=?", new Object[]{goodsid});
	}

	@Override
	public void deleteProxyMembers(String goodsid) {
		this.baseDaoSupport.execute("delete from es_goods_proxymember_rules where goods_id=?", new Object[]{goodsid});
	}
}

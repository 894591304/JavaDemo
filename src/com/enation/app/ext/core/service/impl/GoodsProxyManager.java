package com.enation.app.ext.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import com.enation.app.b2b2c.component.agent.service.IGoodsExtManager;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.database.BaseSupport;

public class GoodsProxyManager extends BaseSupport<Goods> implements IGoodsProxyManager{

	public Map setGoodsInfoIntoProxyList(int goodsid,int LeftOrRight){
		Map result = new HashMap();
		String sql = "select * from goods where goods_id = ?";
		Goods goods = this.baseDaoSupport.queryForObject(sql,Goods.class,new Object[]{goodsid});
		if(goods!=null)
		{
			if(LeftOrRight==0)
			{result.put("shang","<ul class='bd_0'>" );result.put("xia", "");}
			else if(LeftOrRight==1)
			{result.put("shang","" );result.put("xia", "</ul>");}
			else
			{result.put("shang","<ul class='bd_0'>" );result.put("xia", "</ul>");}
			result.put("goodsid", goods.getGoods_id());
			result.put("goodsname", goods.getName());
			result.put("goodsimg", goods.getOriginal());
			result.put("goodsprice", goods.getPrice());
		}
		return result;
	}

	@Override
	public List<Goods> getDiscountList(int tagid) {
		String sql = "select es_goods.*,es_tags.tag_id, es_tag_rel.tag_id,es_tag_rel.rel_id from es_goods left join es_tag_rel on es_tag_rel.rel_id = es_goods.goods_id left join es_tags on es_tags.tag_id = es_tag_rel.tag_id where es_tags.tag_id = ? and disabled = '0' group by goods_id desc;";
		List<Goods> gList = this.daoSupport.queryForList(sql, Goods.class, new Object[]{tagid});
		return gList;
	}

	@Override
	public Goods getGoods(int goodsid) {
		String sql = "select * from goods where goods_id = ? and disabled = '0'";
		Goods goods = this.baseDaoSupport.queryForObject(sql, Goods.class,new Object[]{goodsid});
		return goods;
	}
	
	public List<Goods> getLatelyGoods(){
		Long nTime =System.currentTimeMillis()/1000;            /*最近一个月上架的商品*/ 
		Long lTime = nTime-60*60*24*30;
		String outTime = String.valueOf(lTime);
		String sql = "select * from goods where create_time > ? and disabled = '0' group by goods_id desc";
		List<Goods> gList = this.baseDaoSupport.queryForList(sql, Goods.class,new Object[]{outTime});
		return gList;
	}

	@Override
	public void update(Goods goods) {
		if(goods == null){
			throw new IllegalArgumentException("goods is null");
		}
		this.baseDaoSupport.update("goods", goods, "goods_id=" + goods.getGoods_id());
	}
	public int getAgentid(int goodsid){
		String sql = "select * from goods where goods_id = ? and disabled = '0'";
		Map goods = this.baseDaoSupport.queryForMap(sql, new Object[]{goodsid});
		int agentid = -1;
		agentid = (Integer)goods.get("agentid");
		return agentid;
	}


	public String getThird(int goodsid) {
		String sql = "select * from goods where goods_id = ? and disabled = '0'";
		Map goods = this.baseDaoSupport.queryForMap(sql, new Object[]{goodsid});
		String thirdPlatform = "";
		thirdPlatform = (String)goods.get("thirdPlatform");
		return thirdPlatform;
	}
	
	public List<Goods> search(String keyword){
		String[] key = keyword.split("and|or|\\+|\\s+");
		int count = key.length;
		String sql = "select * from goods where ";
		for(int i=0;i<count;i++){
			String sqlkey = "'%"+key[i]+"%' or name like '"+key[i]+"%' or name like '%"+key[i]+"'";
			String sqlkey2 = "'%"+key[i]+"%' or meta_keywords like '"+key[i]+"%' or meta_keywords like '%"+key[i]+"'";
			sql = sql+"name like "+sqlkey+" and disabled = '0' or meta_keywords like "+sqlkey2+" and disabled = '0'";
			if(i!=count-1){
				sql = sql+" or ";
			}
		}
		List<Goods> gList = this.baseDaoSupport.queryForList(sql, Goods.class,new Object[]{});
		return gList;
	}

	
	public List<Goods> getByCatid(int catid) {
		String sql = "select * from goods where cat_id = ? and disabled = '0'";
		List<Goods> gList = this.baseDaoSupport.queryForList(sql, Goods.class, new Object[]{catid});
		return gList;
	}
}

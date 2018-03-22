package com.enation.app.b2b2c.component.agent.service.impl;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.component.agent.service.IGoodsExtManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

public class GoodsExtManager extends BaseSupport<Goods> implements IGoodsExtManager {

	@Override
	public void updateGoodsAgent(int goods_id, int userid) {
		this.baseDaoSupport.execute("update es_goods set agentid=? where goods_id=?",
				new Object[] { userid, goods_id });
	}

	@Override
	public List<Map> getGoodsAgentId(int goods_id) {
		String sql = "SELECT count(0),agentid FROM goods where goods_id=?";
		try {
			return this.baseDaoSupport.queryForList(sql, new Object[] { goods_id });
		} catch (Exception e) {
			System.out.println("垃圾方法，还报错，捕获都捕获不到！！！");
			return null;
		}
	}

	public Page listGoodsStore(Map map, int page, int pageSize, String other, String sort, String order, int agentid) {
		Integer stype = (Integer) map.get("stype");
		String keyword = (String) map.get("keyword");
		String name = (String) map.get("name");
		String sn = (String) map.get("sn");
		Integer order_store = (Integer) map.get("store");

		StringBuffer sql = new StringBuffer();
		sql.append(
				"select g.goods_id,g.sn,g.name,g.store,c.name cname from es_goods g,es_goods_cat c where g.cat_id=c.cat_id ");

		if ((stype != null) && (keyword != null) && (stype.intValue() == 0)) {
			sql.append(" and ( g.name like '%" + keyword.trim() + "%'");
			sql.append(" or g.sn like '%" + keyword.trim() + "%')");
		}

		if (!StringUtil.isEmpty(name)) {
			sql.append(" and g.name like '%" + name + "%'");
		}

		if (!StringUtil.isEmpty(sn)) {
			sql.append(" and g.sn like '%" + sn + "%'");
		}

		if ((order_store != null) && (order_store.intValue() != 0)) {
			sql.append(" and g.store=" + order_store);
		}
		sql.append(" and g.agentid=" + agentid);

		sql.append(" order by " + sort + " " + order);
		Page webPage = this.daoSupport.queryForPage(sql.toString(), page, pageSize, new Object[0]);

		List<Map> goodslist = (List) webPage.getResult();

		StringBuffer goodsidstr = new StringBuffer();
		for (Map goods : goodslist) {
			Integer goodsid = (Integer) goods.get("goods_id");
			if (goodsidstr.length() != 0) {
				goodsidstr.append(",");
			}
			goodsidstr.append(goodsid);
		}
		List<Map> storeList;
		if (goodsidstr.length() != 0) {
			String ps_sql = "select ps.* from  es_product_store ps where  ps.goodsid in(" + goodsidstr
					+ ") order by goodsid,depotid ";
			storeList = this.daoSupport.queryForList(ps_sql, new Object[0]);

			for (Map goods : goodslist) {
				Integer goodsid = (Integer) goods.get("goods_id");
				if (((Integer) map.get("depotid")).intValue() != 0) {
					for (Map store : storeList) {
						Integer store_goodsid = (Integer) store.get("goodsid");
						if ((store_goodsid.compareTo(goodsid) == 0)
								&& (map.get("depotid").equals(store.get("depotid")))) {
							Integer st = (Integer) goods.get("d_store");
							if (st != null) {
								goods.put("d_store", Integer
										.valueOf(Integer.parseInt(store.get("store").toString()) + st.intValue()));
							} else {
								goods.put("d_store", store.get("store"));
							}

						}
					}
				} else {
					goods.put("d_store", goods.get("store"));
				}
			}
		}

		return webPage;
	}

	@Override
	public void updateGoodsThirdPlatform(int goods_id, String thirdPlatform) {
		this.baseDaoSupport.execute("update es_goods set thirdPlatform=? where goods_id=?",
				new Object[] { thirdPlatform, goods_id });
	}


}

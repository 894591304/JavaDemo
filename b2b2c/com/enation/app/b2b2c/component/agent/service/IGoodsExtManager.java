package com.enation.app.b2b2c.component.agent.service;

import java.util.List;
import java.util.Map;

import com.enation.framework.database.Page;

public interface IGoodsExtManager {
	
	/**
	 * 更新商品的所属商品企业
	 * @param goodsid
	 * @param userid
	 */
	public void updateGoodsAgent(int goods_id,int userid);
	
	/**
	 * 更新商品的所属商品企业
	 * @param goodsid
	 * @param userid
	 */
	public void updateGoodsThirdPlatform(int goods_id,String thirdPlatform);
	
	/**
	 * 获取商品企业id
	 * @param goods_id
	 * @return
	 */
	public List<Map> getGoodsAgentId(int goods_id);
	
	/**
	 * 获取商品列表+库存
	 * @param map
	 * @param page
	 * @param pageSize
	 * @param other
	 * @param sort
	 * @param order
	 * @return
	 */
	public Page listGoodsStore(Map map, int page, int pageSize, String other, String sort, String order, int agentid);
}

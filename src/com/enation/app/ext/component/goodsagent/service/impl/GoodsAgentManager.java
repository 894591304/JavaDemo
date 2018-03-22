package com.enation.app.ext.component.goodsagent.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.eop.sdk.database.BaseSupport;

@Component
public class GoodsAgentManager extends BaseSupport<GoodsAgent> implements IGoodsAgentManager {

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void add(GoodsAgent goodsAgent) {
		if (goodsAgent == null) {
			throw new IllegalArgumentException("goodsagent is null");
		}

		this.baseDaoSupport.insert("goods_agent", goodsAgent);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void edit(GoodsAgent goodsAgent) {
		if (goodsAgent == null) {
			throw new IllegalArgumentException("goodsagent is null");
		}
		this.baseDaoSupport.update("goods_agent", goodsAgent, "goodsId=" + goodsAgent.getGoodsId());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public GoodsAgent get(Integer goodsId) {
		String sql = "select * from goods_agent where goodsId=?";
		GoodsAgent order = this.baseDaoSupport.queryForObject(sql, GoodsAgent.class,
				new Object[] { goodsId });
		return order;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List getByGoodsId(int goodsId){
		String sql = "select * from goods_agent where goodsId=?";
		List goodsInfo =this.baseDaoSupport.queryForList(sql, new Object[] { goodsId });
		return goodsInfo;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(Integer goodsId) {
		if (goodsId == null)
			return;
		String sql = "delete from goods_agent where goodsId = ?";
		this.baseDaoSupport.execute(sql, new Object[] { goodsId });
	}

	@Override
	public List<GoodsAgent> getAll() {
		String sql = "select * from goods_agent group by goodsId desc";
		List<GoodsAgent> gList= this.baseDaoSupport.queryForList(sql, GoodsAgent.class, new Object[] {});
		return gList;
	}

}

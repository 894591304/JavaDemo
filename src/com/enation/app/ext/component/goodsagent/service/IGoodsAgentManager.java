package com.enation.app.ext.component.goodsagent.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.goodsagent.model.GoodsAgent;

@Component
public abstract interface IGoodsAgentManager 
{
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract void add(GoodsAgent goodsAgent);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract void edit(GoodsAgent goodsAgent);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract GoodsAgent get(Integer goodsId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract List getByGoodsId(int goodsId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract void delete(Integer goodsId);
	
	public abstract List<GoodsAgent> getAll();
}

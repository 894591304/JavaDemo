package com.enation.app.ext.core.service.impl;

import java.util.List;

import com.enation.app.ext.core.service.INewGoodsCatManager;
import com.enation.app.shop.core.model.Cat;
import com.enation.eop.sdk.database.BaseSupport;

public class NewGoodsCatManager extends BaseSupport<Cat> implements INewGoodsCatManager{

	
	public List<Cat> getByParentId(int parentid) {
		String sql = "select * from goods_cat where parent_id = ?";
		List<Cat> cList = this.baseDaoSupport.queryForList(sql,Cat.class,new Object[]{parentid});
		return cList;
	}

	
	public List<Cat> getAll() {
		String sql = "select * from goods_cat";
		List<Cat> cList = this.baseDaoSupport.queryForList(sql,Cat.class,new Object[]{});
		return cList;
	}


	
	public Cat getByCatid(int catid) {
		String sql = "select * from goods_cat where cat_id = ?";
		Cat cat = this.baseDaoSupport.queryForObject(sql, Cat.class,new Object[]{catid});
		return cat;
	}

	


}

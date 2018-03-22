package com.enation.app.ext.core.service;

import com.enation.app.shop.core.model.Cat;
import java.util.List;

public abstract interface INewGoodsCatManager {
	
	public abstract List<Cat> getByParentId(int parentid);
	
	public abstract List<Cat> getAll();
	
	public abstract Cat getByCatid(int catid);

}

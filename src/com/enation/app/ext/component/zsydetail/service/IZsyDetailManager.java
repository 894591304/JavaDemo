package com.enation.app.ext.component.zsydetail.service;

import java.util.List;

import com.enation.app.ext.component.zsydetail.model.ZsyDetail;

public abstract interface IZsyDetailManager {

	public abstract void add(ZsyDetail zsyDetail);
	
	public abstract List<ZsyDetail> getZsyDetails();
	
	public abstract void delete(int id);
	
	public abstract void setStatus(int id);
}

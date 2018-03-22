package com.enation.app.ext.component.zsydetail.service.impl;

import java.util.List;

import com.enation.app.ext.component.zsydetail.model.ZsyDetail;
import com.enation.app.ext.component.zsydetail.service.IZsyDetailManager;
import com.enation.eop.sdk.database.BaseSupport;

public class ZsyDetailManager extends BaseSupport<ZsyDetail> implements IZsyDetailManager{

	
	public void add(ZsyDetail zsyDetail) {
		String sql = "select count(*) from zsydetail where outCustomerId = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{zsyDetail.getOutCustomerId()});
		if(count==0)
		{this.baseDaoSupport.insert("zsydetail",zsyDetail);}
	}

	
	public List<ZsyDetail> getZsyDetails() {
		//long ntime = System.currentTimeMillis()/1000;
		//long t1 = ntime-60*60*24*11;
		String sql = "select * from zsydetail where status = 0";
		List<ZsyDetail> zList = this.baseDaoSupport.queryForList(sql,ZsyDetail.class,new Object[]{});
		return zList;
	}

	
	public void delete(int id) {
		String sql = "delete from zsydetail where id = ?";		
		this.baseDaoSupport.execute(sql, new Object[]{id});
	}


	
	public void setStatus(int id) {
		String sql = "update zsydetail set status = 1 where id = ?";
		this.baseDaoSupport.execute(sql, new Object[]{id});
	}

	
}

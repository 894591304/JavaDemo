package com.enation.app.ext.component.vipleveldetail.service.impl;

import com.enation.app.ext.component.vipleveldetail.model.VipLevelDetail;
import com.enation.app.ext.component.vipleveldetail.service.IVipLevelDetailManager;
import com.enation.eop.sdk.database.BaseSupport;

public class VipLevelDetailManager extends BaseSupport<VipLevelDetail> implements IVipLevelDetailManager{

	
	public void add(VipLevelDetail vipLevelDetail) {
		this.baseDaoSupport.insert("vip_leveldetail",vipLevelDetail);
	}

	
	public VipLevelDetail getByMemberId(int memberId) {
		String sql = "select * from vip_leveldetail where proxyMemberId = ?";
		VipLevelDetail vipLevelDetail = this.baseDaoSupport.queryForObject(sql, VipLevelDetail.class,new Object[]{memberId});
		return vipLevelDetail;
	}

	
	public void update(VipLevelDetail vipLevelDetail) {
		String sql = "select count(*) from vip_leveldetail where proxyMemberId = ?";
		int count = this.baseDaoSupport.queryForInt(sql,new Object[]{vipLevelDetail.getProxyMemberId()});
		if(count==0){
			add(vipLevelDetail);
		}else{
			this.baseDaoSupport.update("vip_leveldetail",vipLevelDetail,"proxyMemberId ="+vipLevelDetail.getProxyMemberId());
		}
	}

}

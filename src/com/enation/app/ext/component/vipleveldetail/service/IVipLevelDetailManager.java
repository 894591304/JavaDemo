package com.enation.app.ext.component.vipleveldetail.service;

import java.util.List;

import com.enation.app.ext.component.vipleveldetail.model.VipLevelDetail;


public abstract interface IVipLevelDetailManager 
{
	public abstract void add(VipLevelDetail vipLevelDetail);
	
	public abstract VipLevelDetail getByMemberId(int memberId);
	
	public abstract void update(VipLevelDetail vipLevelDetail);
	
}

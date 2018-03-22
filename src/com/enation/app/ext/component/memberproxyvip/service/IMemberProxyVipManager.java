package com.enation.app.ext.component.memberproxyvip.service;

import java.util.List;

import com.enation.app.ext.component.memberproxyvip.model.MemberProxyVip;
import com.enation.framework.database.Page;

public abstract interface IMemberProxyVipManager 
{
	public abstract void add(MemberProxyVip memberProxyVip);
	
	public abstract MemberProxyVip get(int id);
	
	public abstract MemberProxyVip getByMemberIdAndProxyMemberId(int memberId,int proxyMemberId);
	
	public abstract void update(MemberProxyVip memberProxyVip);
	
	public abstract List<MemberProxyVip> getNotExpiredByMemberId(int memberId);
	
	public abstract List<MemberProxyVip> getByMemberId(int memberId);
	
	public abstract List<MemberProxyVip> getByProxyMemberId(int proxyMemberId);
	
	public abstract List<MemberProxyVip> getV1ByProxyMemberId(int proxyMemberId);
	
	public abstract List<MemberProxyVip> getV2ByProxyMemberId(int proxyMemberId);
	
	public abstract List<MemberProxyVip> getV3ByProxyMemberId(int proxyMemberId);
	
	public abstract List<MemberProxyVip> getVOldByProxyMemberId(int proxyMemberId);
	
	public abstract List<MemberProxyVip> getAllNotExpired();
	
	public Page searchProxyVipByMemberId(int memberId, Integer page, Integer pageSize);
}

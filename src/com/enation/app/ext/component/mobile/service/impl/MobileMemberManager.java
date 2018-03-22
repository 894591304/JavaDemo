package com.enation.app.ext.component.mobile.service.impl;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.mobile.service.IMobileMemberManager;
import com.enation.eop.sdk.database.BaseSupport;

public class MobileMemberManager extends BaseSupport<Member> implements IMobileMemberManager{
	
	private IMobileMemberManager mobileMemberManager;

	@Transactional(propagation=Propagation.REQUIRED)
	public int checkmobile(String mobile) 
	{
		System.out.println(mobile);
		String sql= "select count(0) from member where mobile=?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{mobile});
		count = count > 0 ? 1 : 0;
		return count;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int checkname(String username)
	{
		String sql = "select count(0) from member where uname=?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{username});
		count = count > 0 ? 1 : 0;
		return count;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public String getUnameByMobile(String mobile)
	{
		String sql = "select uname from member where mobile = "+mobile;
		String uname = "";
		uname = this.baseDaoSupport.queryForString(sql);
		return uname;
	}
	
	public int getMemberidByMobile(String mobile)
	{
		String sql = "select member_id from member where mobile=?";
		int mid = -1;
		mid = this.baseDaoSupport.queryForInt(sql, new Object[]{mobile});
		return mid;
	}

	public IMobileMemberManager getMobileMemberManager()
	{
		return this.mobileMemberManager;
	}

	public String getMobileByMemberId(int memberId) {
		String mId = String.valueOf(memberId);
		String sql = "select mobile from member where member_id = "+mId;
		String mobile = "";
		mobile = this.baseDaoSupport.queryForString(sql);
		return mobile;
	}
}
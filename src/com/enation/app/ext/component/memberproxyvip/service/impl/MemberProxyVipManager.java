package com.enation.app.ext.component.memberproxyvip.service.impl;

import java.util.List;
import java.util.Map;

import com.enation.app.ext.component.memberproxyvip.model.MemberProxyVip;
import com.enation.app.ext.component.memberproxyvip.service.IMemberProxyVipManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

public class MemberProxyVipManager extends BaseSupport<MemberProxyVip> implements IMemberProxyVipManager
{

	public void add(MemberProxyVip memberProxyVip) {
		String sql = "select count(*) from member_proxyvip where memberId = ? and proxyMemberId = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{memberProxyVip.getMemberId(),memberProxyVip.getProxyMemberId()});
		if(count==0){
			this.baseDaoSupport.insert("member_proxyvip",memberProxyVip);
		}
	}

	public MemberProxyVip get(int id) {
		String sql = "select * from member_proxyvip where id = ?";
		MemberProxyVip memberProxyVip = this.baseDaoSupport.queryForObject(sql, MemberProxyVip.class,new Object[]{id});
		return memberProxyVip;
	}

	public MemberProxyVip getByMemberIdAndProxyMemberId(int memberId,int proxyMemberId) {
		String sql = "select * from member_proxyvip where memberId = ? and proxyMemberId = ?";
		MemberProxyVip memberProxyVip = this.baseDaoSupport.queryForObject(sql, MemberProxyVip.class,new Object[]{memberId,proxyMemberId});
		return memberProxyVip;
	}

	public void update(MemberProxyVip memberProxyVip) {
		String sql = "select count(*) from member_proxyvip where memberId = ? and proxyMemberId = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{memberProxyVip.getMemberId(),memberProxyVip.getProxyMemberId()});
		if(count!=0){
			this.baseDaoSupport.update("member_proxyvip",memberProxyVip,"id="+memberProxyVip.getId());
		}
	}

	public List<MemberProxyVip> getNotExpiredByMemberId(int memberId) {
		String sql = "select * from member_proxyvip where memberId = ? and (v1LT != 0 or v2LT != 0 or v3LT !=0)";
		List<MemberProxyVip> mList = this.baseDaoSupport.queryForList(sql, MemberProxyVip.class,new Object[]{memberId});
		return mList;
	}
	
	public Page searchProxyVipByMemberId(int memberId, Integer page, Integer pageSize) {
		String sql = "select mv.*,m.* from es_member_proxyvip mv,es_member m where mv.memberId=m.member_id and mv.proxyMemberId = ?";
		Page webpage = this.daoSupport.queryForPage(sql, page.intValue(), pageSize.intValue(),new Object[]{memberId});
		return webpage;
	}

	public List<MemberProxyVip> getByMemberId(int memberId) {
		String sql = "select * from member_proxyvip where memberId = ? ";
		List<MemberProxyVip> mList = this.baseDaoSupport.queryForList(sql, MemberProxyVip.class,new Object[]{memberId});
		return mList;
	}

	
	public List<MemberProxyVip> getByProxyMemberId(int proxyMemberId) {
		String sql = "select * from member_proxyvip where proxyMemberId = ? order by vipEX DESC";
		List<MemberProxyVip> mList = this.baseDaoSupport.queryForList(sql, MemberProxyVip.class,new Object[]{proxyMemberId});
		return mList;
	}

	@Override
	public List<MemberProxyVip> getV1ByProxyMemberId(int proxyMemberId) {
		String sql = "select * from member_proxyvip where proxyMemberId = ? and v2LT = 0 and v1LT != 0 ";
		List<MemberProxyVip> mList = this.baseDaoSupport.queryForList(sql, MemberProxyVip.class,new Object[]{proxyMemberId});
		return mList;
	}

	@Override
	public List<MemberProxyVip> getV2ByProxyMemberId(int proxyMemberId) {
		String sql = "select * from member_proxyvip where proxyMemberId = ? and v3LT = 0 and v2LT != 0 ";
		List<MemberProxyVip> mList = this.baseDaoSupport.queryForList(sql, MemberProxyVip.class,new Object[]{proxyMemberId});
		return mList;
	}

	@Override
	public List<MemberProxyVip> getV3ByProxyMemberId(int proxyMemberId) {
		String sql = "select * from member_proxyvip where proxyMemberId = ? and v3LT != 0 ";
		List<MemberProxyVip> mList = this.baseDaoSupport.queryForList(sql, MemberProxyVip.class,new Object[]{proxyMemberId});
		return mList;
	}

	@Override
	public List<MemberProxyVip> getVOldByProxyMemberId(int proxyMemberId) {
		String sql = "select * from member_proxyvip where proxyMemberId = ? and v1LT = 0 ";
		List<MemberProxyVip> mList = this.baseDaoSupport.queryForList(sql, MemberProxyVip.class,new Object[]{proxyMemberId});
		return mList;
	}

	@Override
	public List<MemberProxyVip> getAllNotExpired() {
		String sql = "select * from member_proxyvip where v1LT != 0";
		List<MemberProxyVip> mList = this.baseDaoSupport.queryForList(sql, MemberProxyVip.class,new Object[]{});
		return mList;
	}

}

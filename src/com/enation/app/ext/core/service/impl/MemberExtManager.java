package com.enation.app.ext.core.service.impl;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.core.service.IMemberExtManager;
import com.enation.eop.sdk.database.BaseSupport;

public class MemberExtManager extends BaseSupport<Member> implements IMemberExtManager{

	
	public String getUnameByMemberid(int memberid) {
		String result = null;
		Member member = getByMemberid(memberid);
		if(member!=null)
		{result=member.getUname();}
		return result;
	}

	
	public Member getByMemberid(int memberid) {
		String sql = "select * from member where member_id = ?";
		Member member = this.baseDaoSupport.queryForObject(sql, Member.class, new Object[]{memberid});
		return member;
	}


	public void update(Member member) {
		this.baseDaoSupport.update("member", member, "member_id=" + member.getMember_id());		
	}

}

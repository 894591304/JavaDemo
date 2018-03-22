package com.enation.app.ext.core.service;

import com.enation.app.base.core.model.Member;

public abstract interface IMemberExtManager {

	public abstract String getUnameByMemberid(int memberid);
	
	public abstract Member getByMemberid(int memberid);
	
	public abstract void update(Member member);
}

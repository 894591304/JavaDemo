package com.enation.app.ext.component.follow.service;

import java.util.List;

import com.enation.app.ext.component.follow.model.Follow;

public abstract interface IFollowManager 
{
	public abstract int follow(int memberId, int followId);
	public abstract int cancelFollow(int memberId, int followId);
	public abstract List<Follow> getFollowListByMemberId(int memberId);
	public abstract int followMsgPlus(int followId);
	public abstract int getFansNum(int followid);
	public abstract int checkFollow(int memberid,int followid);
	public abstract int getFollowNum(int memberid);
	public abstract void msgUp(int followid);
	public abstract void msgClean(int memberid,int followid);
}

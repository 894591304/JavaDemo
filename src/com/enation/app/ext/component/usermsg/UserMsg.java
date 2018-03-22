package com.enation.app.ext.component.usermsg;

import java.io.Serializable;

public class UserMsg implements Serializable
{
	
	private String Id;
	private int memberId;
	private String memberMsg;
	private String goodsId;
	private String msgTime;
	private int msgCheck;
	
	public String getId()
	{
		return this.Id;
	}
	
	public void setSnId(String Id)
	{
		this.Id = Id;
	}
	
	public int getMemberId()
	{
		return this.memberId;
	}
	
	public void setMemberId(int memberId)
	{
		this.memberId = memberId;
	}
	
	public String getMemberMsg()
	{
		return this.memberMsg;
	}
	
	public void setMemberMsg(String memberMsg)
	{
		this.memberMsg = memberMsg;
	}

	public String getGoodsId()
	{
		return this.goodsId;
	}
	
	public void setGoodsId(String goodsId)
	{
		this.goodsId = goodsId;
	}
	
	public String getMsgTime()
	{
		return this.msgTime;
	}
	
	public void setMsgTime(String msgTime)
	{
		this.msgTime = msgTime;
	}
	
	public int getMsgCheck()
	{
		return this.msgCheck;
	}
	
	public void setMsgCheck(int message_check)
	{
		this.msgCheck=msgCheck;
	}
}

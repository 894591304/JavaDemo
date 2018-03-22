package com.enation.app.ext.component.mobile.model;

import java.io.Serializable;

public class Mobile implements Serializable
{
	private int Id;
	private String mobile;
	private String outTime;
	private String checkNum;
	
	public int getId()
	{
		return this.Id;
	}
	
	public void setId(int Id)
	{
		this.Id = Id;
	}
	
	public String getMobile()
	{
		return this.mobile;
	}
	
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	
	public String getOutTime()
	{
		return this.outTime;
	}
	
	public void setOutTime(String outTime)
	{
		this.outTime = outTime;
	}
	
	public String getCheckNum()
	{
		return this.checkNum;
	}
	
	public void setCheckNum(String checkNum)
	{
		this.checkNum = checkNum;
	}
}

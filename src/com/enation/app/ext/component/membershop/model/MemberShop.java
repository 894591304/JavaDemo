package com.enation.app.ext.component.membershop.model;

import java.io.Serializable;

public class MemberShop implements Serializable 
{
	private int id;
	private int memberId;
	private String shopName;
	private String label;
	private String shopIntro;
	private String memberImg;
	private String shopImg;
	private String mobile;
	private String qq;
	private int level;
	private String createTime;
	private int proxyNum;
	private int agencyId;
	private int showOrHide;
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getMemberId()
	{
		return this.memberId;
	}
	
	public void setMemberId(int memberId)
	{
		this.memberId = memberId;
	}
	
	public String getShopName()
	{
		return this.shopName;
	}
	
	public void setShopName(String shopName)
	{
		this.shopName = shopName;
	}
	
	public String getLabel()
	{
		return this.label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public String getShopIntro()
	{
		return this.shopIntro;
	}
	
	public void setShopIntro(String shopIntro)
	{
		this.shopIntro = shopIntro;
	}
	
	public String getMemberImg()
	{
		return this.memberImg;
	}
	
	public void setMemberImg(String memberImg)
	{
		this.memberImg = memberImg;
	}
	
	public String getShopImg()
	{
		return this.shopImg;
	}
	
	public void setShopImg(String shopImg)
	{
		this.shopImg = shopImg;
	}
	
	public String getMobile()
	{
		return this.mobile;
	}
	
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public String getCreateTime()
	{
		return this.createTime;
	}
	
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public int getProxyNum() {
		return proxyNum;
	}

	public void setProxyNum(int proxyNum) {
		this.proxyNum = proxyNum;
	}

	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public int getShowOrHide() {
		return showOrHide;
	}

	public void setShowOrHide(int showOrHide) {
		this.showOrHide = showOrHide;
	}
}

package com.enation.app.ext.component.memberproxyvip.model;

import java.io.Serializable;

public class MemberProxyVip implements Serializable
{
	private int id;
	private int memberId;
	private int proxyMemberId;
	private String vipBT;
	private String v1LT;
	private String v2LT;
	private String v3LT;
	private String vipEX;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProxyMemberId() {
		return proxyMemberId;
	}
	public void setProxyMemberId(int proxyMemberId) {
		this.proxyMemberId = proxyMemberId;
	}
	public String getVipBT() {
		return vipBT;
	}
	public void setVipBT(String vipBT) {
		this.vipBT = vipBT;
	}
	public String getV1LT() {
		return v1LT;
	}
	public void setV1LT(String v1lt) {
		v1LT = v1lt;
	}
	public String getV2LT() {
		return v2LT;
	}
	public void setV2LT(String v2lt) {
		v2LT = v2lt;
	}
	public String getV3LT() {
		return v3LT;
	}
	public void setV3LT(String v3lt) {
		v3LT = v3lt;
	}
	public String getVipEX() {
		return vipEX;
	}
	public void setVipEX(String vipEX) {
		this.vipEX = vipEX;
	}
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
}

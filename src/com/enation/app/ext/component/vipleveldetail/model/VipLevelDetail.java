package com.enation.app.ext.component.vipleveldetail.model;

import java.io.Serializable;

public class VipLevelDetail implements Serializable
{
	private int id;
	private int proxyMemberId;
	private String v1Name;
	private String v2Name;
	private String v3Name;
	private String v1Content;
	private String v2Content;
	private String v3Content;
	
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
	public String getV1Name() {
		return v1Name;
	}
	public void setV1Name(String v1Name) {
		this.v1Name = v1Name;
	}
	public String getV2Name() {
		return v2Name;
	}
	public void setV2Name(String v2Name) {
		this.v2Name = v2Name;
	}
	public String getV3Name() {
		return v3Name;
	}
	public void setV3Name(String v3Name) {
		this.v3Name = v3Name;
	}
	public String getV1Content() {
		return v1Content;
	}
	public void setV1Content(String v1Content) {
		this.v1Content = v1Content;
	}
	public String getV2Content() {
		return v2Content;
	}
	public void setV2Content(String v2Content) {
		this.v2Content = v2Content;
	}
	public String getV3Content() {
		return v3Content;
	}
	public void setV3Content(String v3Content) {
		this.v3Content = v3Content;
	}
	

}

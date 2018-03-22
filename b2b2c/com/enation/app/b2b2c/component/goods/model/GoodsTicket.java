package com.enation.app.b2b2c.component.goods.model;

import com.enation.framework.database.PrimaryKeyField;

public class GoodsTicket {
	private Integer goods_id;
	private String title;
	private String pre;
	private Integer num;
	private Long startdate;
	private Long enddate;
	
	@PrimaryKeyField
	public Integer getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPre() {
		return pre;
	}
	public void setPre(String pre) {
		this.pre = pre;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Long getStartdate() {
		return startdate;
	}
	public void setStartdate(Long startdate) {
		this.startdate = startdate;
	}
	public Long getEnddate() {
		return enddate;
	}
	public void setEnddate(Long enddate) {
		this.enddate = enddate;
	}
	
	
}

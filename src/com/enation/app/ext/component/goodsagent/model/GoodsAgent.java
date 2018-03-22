package com.enation.app.ext.component.goodsagent.model;

public class GoodsAgent {
	private int goodsId;
	// 优选价
	private float price;
	// 市场价
	private float mktPrice;
	// 金卡价
	private float goldPrice;
	// 白金卡价
	private float platinumPrice;
	// 黑卡价
	private float blackPrice;
	// 供货价
	private float cost;
	// 无违约代理价
	private float defaultPrice;
	//销售期限选项
	private String timeOption;
	//现金券选项
	private String ticketOption;
	//现金券说明
	private String ticketCaption;
	
	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getMktPrice() {
		return mktPrice;
	}

	public void setMktPrice(float mktPrice) {
		this.mktPrice = mktPrice;
	}

	public float getGoldPrice() {
		return goldPrice;
	}

	public void setGoldPrice(float goldPrice) {
		this.goldPrice = goldPrice;
	}

	public float getPlatinumPrice() {
		return platinumPrice;
	}

	public void setPlatinumPrice(float platinumPrice) {
		this.platinumPrice = platinumPrice;
	}

	public float getBlackPrice() {
		return blackPrice;
	}

	public void setBlackPrice(float blackPrice) {
		this.blackPrice = blackPrice;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(float defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	@Override
	public String toString() {
		return "GoodsAgent [goodsId=" + goodsId + ", price=" + price + ", mktPrice=" + mktPrice + ", goldPrice="
				+ goldPrice + ", platinumPrice=" + platinumPrice + ", blackPrice=" + blackPrice + ", cost=" + cost
				+ ", defaultPrice=" + defaultPrice + "]";
	}

	public String getTimeOption() {
		return timeOption;
	}

	public void setTimeOption(String timeOption) {
		this.timeOption = timeOption;
	}

	public String getTicketOption() {
		return ticketOption;
	}

	public void setTicketOption(String ticketOption) {
		this.ticketOption = ticketOption;
	}

	public String getTicketCaption() {
		return ticketCaption;
	}

	public void setTicketCaption(String ticketCaption) {
		this.ticketCaption = ticketCaption;
	}
	
}

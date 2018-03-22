package com.enation.app.b2b2c.component.goods.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.enation.app.b2b2c.component.goods.model.GoodsTicket;
import com.enation.app.b2b2c.component.goods.service.IGoodsTicketManager;
import com.enation.app.b2b2c.component.goods.service.ITicketDetailManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.framework.action.WWAction;

public class GoodsTicketAction extends WWAction {

	private static final long serialVersionUID = -8618054430286090133L;

	private IGoodsManager goodsManager;
	private IGoodsTicketManager goodsTicketManager;
	private ITicketDetailManager ticketDetailManager;
	private Goods goods;
	private GoodsTicket goodsTicket;
	private Map searchMap;
	private boolean add;
	private int goods_id;
	private String title;
	private String pre;
	private Integer num;
	private Date mstartdate;
	private Date menddate;

	public String goodsTicket() {
		this.goods = this.goodsManager.getGoods(this.goods_id);
		this.goodsTicket = this.goodsTicketManager.get(this.goods_id);
		if (this.goodsTicket != null) {
			this.add = false;
		} else {
			this.add = true;
		}
		return "goodsTicket";
	}

	public String ticketlistJson() {
		this.searchMap = new HashMap();
		searchMap.put("goods_id", this.goods_id);
		this.webpage = this.goodsTicketManager.searchGoodsTicket(searchMap, Integer.valueOf(getPage()),
				Integer.valueOf(getPageSize()), getSort(), getOrder());
		showGridJson(this.webpage);
		return "json_message";
	}

	public String saveEditGoodsTicket() {
		GoodsTicket _goodsTicket = this.goodsTicketManager.get(this.goods_id);
		boolean add = false;
		if (_goodsTicket == null) {
			_goodsTicket = new GoodsTicket();
			_goodsTicket.setGoods_id(this.goods_id);
			add = true;
		}
		_goodsTicket.setTitle(this.title);
		_goodsTicket.setPre(this.pre);
		_goodsTicket.setNum(this.num);
		_goodsTicket.setStartdate(Long.valueOf(this.mstartdate.getTime()));
		_goodsTicket.setEnddate(Long.valueOf(this.menddate.getTime()));
		if (add) {
			this.goodsTicketManager.add(_goodsTicket);
		} else {
			this.goodsTicketManager.edit(_goodsTicket);
		}
		this.goodsTicketManager.createTicket(this.goods_id, this.pre, this.num, Long.valueOf(this.mstartdate.getTime()),
				Long.valueOf(this.menddate.getTime()));
		showSuccessJson("生成提货券成功");
		return "json_message";
	}

	public String export() {
		try {
			String url = this.ticketDetailManager.export(this.goods_id);
			showSuccessJson(url);
		} catch (RuntimeException e) {
			this.logger.error("导出订单出错", e);
			showErrorJson(e.getMessage());
		}
		return "json_message";
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public IGoodsTicketManager getGoodsTicketManager() {
		return goodsTicketManager;
	}

	public void setGoodsTicketManager(IGoodsTicketManager goodsTicketManager) {
		this.goodsTicketManager = goodsTicketManager;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public GoodsTicket getGoodsTicket() {
		return goodsTicket;
	}

	public void setGoodsTicket(GoodsTicket goodsTicket) {
		this.goodsTicket = goodsTicket;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
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

	public Date getMstartdate() {
		return mstartdate;
	}

	public void setMstartdate(Date mstartdate) {
		this.mstartdate = mstartdate;
	}

	public Date getMenddate() {
		return menddate;
	}

	public void setMenddate(Date menddate) {
		this.menddate = menddate;
	}

	public Map getSearchMap() {
		return searchMap;
	}

	public void setSearchMap(Map searchMap) {
		this.searchMap = searchMap;
	}

	public ITicketDetailManager getTicketDetailManager() {
		return ticketDetailManager;
	}

	public void setTicketDetailManager(ITicketDetailManager ticketDetailManager) {
		this.ticketDetailManager = ticketDetailManager;
	}

}

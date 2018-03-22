package com.enation.app.ext.component.goodsdiscountticketdetail.service.impl;

import java.util.List;

import org.apache.tools.ant.taskdefs.condition.And;

import com.enation.app.ext.component.goodsdiscountticketdetail.model.GoodsDiscountTicketDetail;
import com.enation.app.ext.component.goodsdiscountticketdetail.service.IGoodsDiscountTicketDetailManager;
import com.enation.eop.sdk.database.BaseSupport;

public class GoodsDiscountTicketDetailManager extends BaseSupport<GoodsDiscountTicketDetail> implements IGoodsDiscountTicketDetailManager{

	public void add(GoodsDiscountTicketDetail goodsDiscountTicketDetail) {
		this.baseDaoSupport.insert("goods_discount_ticketdetail",goodsDiscountTicketDetail);	
	}

	public void update(GoodsDiscountTicketDetail goodsDiscountTicketDetail) {
		String sql = "select count(*) from goods_discount_ticketdetail where id = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{goodsDiscountTicketDetail.getId()});
		if(count!=0){
			this.baseDaoSupport.update("goods_discount_ticketdetail",goodsDiscountTicketDetail,"id="+goodsDiscountTicketDetail.getId());
		}
	}

	public GoodsDiscountTicketDetail get(int id) {
		String sql = "select * from goods_discount_ticketdetail where id = ?";
		GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.baseDaoSupport.queryForObject(sql, GoodsDiscountTicketDetail.class,new Object[]{id});
		return goodsDiscountTicketDetail;
	}

	public List<GoodsDiscountTicketDetail> getByProxyId(int proxyid) {
		String sql = "select * from goods_discount_ticketdetail where proxyId = ? and useStatus = 2";
		List<GoodsDiscountTicketDetail> gList = this.baseDaoSupport.queryForList(sql, GoodsDiscountTicketDetail.class,new Object[]{proxyid});
		return gList;
	}

	@Override
	public List<GoodsDiscountTicketDetail> getByProxyIdAndTicketValue(int proxyid, float value) {
		String sql = "select * from goods_discount_ticketdetail where proxyId = ? and ticketValue = ?";
		List<GoodsDiscountTicketDetail> gList = this.baseDaoSupport.queryForList(sql, GoodsDiscountTicketDetail.class,new Object[]{proxyid,value});
		return gList;
	}

	public List<GoodsDiscountTicketDetail> getNotGiveByProxyId(int proxyid){
		String sql = "select * from goods_discount_ticketdetail where proxyId = ? and giveStatus = 0";
		List<GoodsDiscountTicketDetail> gList = this.baseDaoSupport.queryForList(sql, GoodsDiscountTicketDetail.class,new Object[]{proxyid});
		return gList;
	}

	@Override
	public List<GoodsDiscountTicketDetail> getByMemberId(int memberid) {
		String sql = "select * from goods_discount_ticketdetail where belongMemberId = ?";
		List<GoodsDiscountTicketDetail> gList = this.baseDaoSupport.queryForList(sql, GoodsDiscountTicketDetail.class,new Object[]{memberid});
		return gList;
	}

	
	public GoodsDiscountTicketDetail getBySendKey(String sendkey) {
		String sql = "select * from goods_discount_ticketdetail where sendKey = ?";
		GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.baseDaoSupport.queryForObject(sql, GoodsDiscountTicketDetail.class,new Object[]{sendkey});
		return goodsDiscountTicketDetail;
	}

	@Override
	public GoodsDiscountTicketDetail getHignValueByProxyid(int proxyid,int memberid) {
		String sql = "select * from goods_discount_ticketdetail where proxyId = ? and useStatus = 0 and sendStatus!=1 and belongMemberId = ? order by ticketValue desc";
		List<GoodsDiscountTicketDetail> gList = this.baseDaoSupport.queryForList(sql, GoodsDiscountTicketDetail.class,new Object[]{proxyid,memberid});
		if(gList.size()!=0){
			return gList.get(0);
		}else{
			return null;
		}
	}

	@Override
	public GoodsDiscountTicketDetail getByItemId(int itemid) {
		String sql = "select * from goods_discount_ticketdetail where useOrderItemId = ?";
		GoodsDiscountTicketDetail goodsDiscountTicketDetail = this.baseDaoSupport.queryForObject(sql, GoodsDiscountTicketDetail.class,new Object[]{itemid});
		return goodsDiscountTicketDetail;
	}

	@Override
	public List<GoodsDiscountTicketDetail> getSaleProxy(int proxyid) {
		String sql = "select * from goods_discount_ticketdetail where proxyId = ? and useStatus != 0";
		List<GoodsDiscountTicketDetail> gList = this.baseDaoSupport.queryForList(sql, GoodsDiscountTicketDetail.class,new Object[]{proxyid});
		return gList;
	}

	@Override
	public int getTicketNum(int proxyid) {
		String sql = "select count(*) from goods_discount_ticketdetail where proxyId = ?";
		int num = this.baseDaoSupport.queryForInt(sql, new Object[]{proxyid});
		return num;
	}
	
	
	
}

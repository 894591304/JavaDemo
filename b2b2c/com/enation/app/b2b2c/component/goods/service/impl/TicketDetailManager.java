package com.enation.app.b2b2c.component.goods.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.component.goods.model.TicketDetail;
import com.enation.app.b2b2c.component.goods.service.ITicketDetailManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.util.ExcelUtil;
import com.enation.framework.util.FileUtil;

public class TicketDetailManager extends BaseSupport<TicketDetail> implements ITicketDetailManager {

	@Override
	public String export(Integer goods_id) {
		String sql = "select * from ticketdetail where goods_id=?";

		List<TicketDetail> ticketList = this.baseDaoSupport.queryForList(sql, TicketDetail.class,
				new Object[] { goods_id });

		ExcelUtil excelUtil = new ExcelUtil();

		InputStream in = FileUtil.getResourceAsStream("com/enation/app/b2b2c/component/goods/service/impl/ticket.xls");

		excelUtil.openModal(in);
		int i = 1;
		for (TicketDetail ticket : ticketList) {
			excelUtil.writeStringToCell(i, 0, ticket.getTicketCode().toUpperCase());
			excelUtil.writeStringToCell(i, 1, ticket.getStatus()==1?"已使用":"未使用");
			excelUtil.writeStringToCell(i, 2, com.enation.eop.sdk.utils.DateUtil
					.toString(new Date(ticket.getStartdate()), "yyyy-MM-dd"));
			excelUtil.writeStringToCell(i, 3,
					com.enation.eop.sdk.utils.DateUtil.toString(new Date(ticket.getEnddate()), "yyyy-MM-dd"));
			i++;
		}

		String filename = "/ticket";
		File file = new File(EopSetting.IMG_SERVER_PATH + filename);
		if (!file.exists()) {
			file.mkdirs();
		}
		filename = filename + "/ticket" + com.enation.framework.util.DateUtil.getDatelineLong() + ".xls";
		excelUtil.writeToFile(EopSetting.IMG_SERVER_PATH + filename);

		return EopSetting.IMG_SERVER_DOMAIN + filename;
	}

	public int getNewKey(int goodsid) {
		long ct = System.currentTimeMillis();
		String sql1 = "select * from ticketdetail where goods_id = ? and enddate > ? and startdate < ? and status = 0";
		List<TicketDetail> tList =  this.baseDaoSupport.queryForList(sql1, TicketDetail.class,new Object[]{goodsid,ct,ct});
		if(tList.size()==0)
		{
			String sql = "select * from ticketdetail where goods_id = ? and enddate > ? and status = 0";
			tList = this.baseDaoSupport.queryForList(sql, TicketDetail.class,new Object[]{goodsid,ct});
		}
			if(tList==null||tList.size()==0){
				return 0;
			}else{
				sendKey(tList.get(0).getId());
			return tList.get(0).getId();
		}
	}

	@Override
	public void sendKey(int id) {
		String sql = "update ticketdetail set status='1' where id = ? ";
		this.baseDaoSupport.execute(sql, new Object[]{id});
	}

	@Override
	public TicketDetail get(int id) {
		String sql = "select * from ticketdetail where id = ?";
		TicketDetail ticketDetail = this.baseDaoSupport.queryForObject(sql, TicketDetail.class, new Object[]{id});
		return ticketDetail;
	}

	@Override
	public void update(TicketDetail ticketDetail) {
		String sql = "select count(*) from ticketdetail where id = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{ticketDetail.getId()});
		if(count!=0){
			this.baseDaoSupport.update("ticketdetail",ticketDetail,"id = "+ticketDetail.getId());
		}		
	}
	
	public Map getTicketByOrderId(int id) {
		String sql = "select t.* from es_ticketdetail t,es_proxyorder p,es_order_items o where o.item_id = p.itemId and p.ticketId = t.id and o.order_id= ?";
		List<Map> ticketDetail = this.baseDaoSupport.queryForList(sql, new Object[]{id} );
		return ticketDetail.size()>0?ticketDetail.get(0):null;
	}
	
}

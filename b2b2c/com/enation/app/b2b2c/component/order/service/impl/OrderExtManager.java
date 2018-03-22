package com.enation.app.b2b2c.component.order.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.poi.util.SystemOutLogger;

import com.enation.app.b2b2c.component.order.service.IOrderExtManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

public class OrderExtManager extends BaseSupport implements IOrderExtManager {

	public Page listOrder(Map map, int page, int pageSize, String other, String order) {
		String sql = createTempSql(map, other, order);
		Page webPage = this.baseDaoSupport.queryForPage(sql, page, pageSize, new Object[0]);
		return webPage;
	}
	
	public Page listOrder2(Map map, int page, int pageSize, String other, String order) {
		String sql = createTempSql2(map, other, order);
		Page webPage = this.baseDaoSupport.queryForPage(sql, page, pageSize, new Object[0]);
		return webPage;
	}
	
	public String getMemberNameByOrderId(String orderId){
		String sql = "select m.uname from es_member m where m.member_id = (select p.memberId from es_proxyorder p where p.itemId = (select oi.item_id from es_order_items oi,es_proxyorder p where oi.order_id = "+orderId+" LIMIT 1)  LIMIT 1)";
		return this.baseDaoSupport.queryForString(sql);
	}

	private String createTempSql(Map map, String other, String order) {
		Integer agentid = (Integer) map.get("agentid");
		Integer stype = (Integer) map.get("stype");
		String keyword = (String) map.get("keyword");
		String orderstate = (String) map.get("order_state");
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		String status = (String) map.get("status");
		String sn = (String) map.get("sn");
		String ship_name = (String) map.get("ship_name");
		Integer paystatus = (Integer) map.get("paystatus");
		String shipstatus = (String) map.get("shipstatus");
		Integer shipping_type = (Integer) map.get("shipping_type");
		Integer payment_id = (Integer) map.get("payment_id");
		Integer depotid = (Integer) map.get("depotid");
		String complete = (String) map.get("complete");

		StringBuffer sql = new StringBuffer();
		sql.append("select * from (select distinct o.* from es_order o,es_order_items i,es_goods g where i.order_id=o.order_id and i.goods_id=g.goods_id and o.disabled=0 ");

		if (agentid != null) {
			sql.append(" and g.agentid=" + agentid);
		}

		if (keyword != null) {
			sql.append(" and o.sn like '%" + keyword + "%'");
		}

		if ((status != null) && (!StringUtil.isEmpty(status))) {
			sql.append("and o.status in (" + status + ")");
		}

		if ((sn != null) && (!StringUtil.isEmpty(sn))) {
			sql.append(" and o.sn like '%" + sn + "%'");
		}

		if ((ship_name != null) && (!StringUtil.isEmpty(ship_name))) {
			sql.append(" and o.ship_name like '" + ship_name + "'");
		}

		if (paystatus != null) {
			sql.append(" and o.pay_status=" + paystatus);
		}

		if ((shipstatus != null) && (!StringUtil.isEmpty(shipstatus))) {
			sql.append(" and o.ship_status in (" + shipstatus + ")");
		}

		if (shipping_type != null) {
			sql.append(" and o.shipping_id=" + shipping_type);
		}

		if (payment_id != null) {
			sql.append(" and o.payment_id=" + payment_id);
		}

		if ((depotid != null) && (depotid.intValue() > 0)) {
			sql.append(" and o.depotid=" + depotid);
		}

		if ((start_time != null) && (!StringUtil.isEmpty(start_time))) {
			String stime = dateToStamp(start_time + " 00:00:00");
			sql.append(" and o.create_time>" + stime);
		}
		if ((end_time != null) && (!StringUtil.isEmpty(end_time))) {
			String etime = dateToStamp(end_time + " 23:59:59");
			sql.append(" and o.create_time<" + etime );
		}
		if (!StringUtil.isEmpty(orderstate)) {
			if (orderstate.equals("wait_ship")) {
				sql.append(" and ( ( o.payment_type!='cod' and o.payment_id!=8  and  o.status=2) ");
				sql.append(" or ( o.payment_type='cod' and  o.status=0)) ");
			} else if (orderstate.equals("wait_pay")) {
				sql.append(" and ( ( o.payment_type!='cod' and  o.status=0) ");
				sql.append(" or ( o.payment_id=8 and o.status!=0  and  o.pay_status!=2)");
				sql.append(" or ( o.payment_type='cod' and  (o.status=5 or o.status=6 )  ) )");
			} else if (orderstate.equals("wait_rog")) {
				sql.append(" and o.status=5");
			} else {
				sql.append(" and o.status=" + orderstate);
			}
		}

		if (!StringUtil.isEmpty(complete)) {
			sql.append(" and o.status=7");
		}

		sql.append(") temp ORDER BY " + other + " " + order);
		return sql.toString();
	}
	
	private String createTempSql2(Map map, String other, String order) {
		Integer agentid = (Integer) map.get("agentid");
		Integer stype = (Integer) map.get("stype");
		String keyword = (String) map.get("keyword");
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		String status = (String) map.get("status");
		String shipstatus = (String) map.get("shipstatus");

		StringBuffer sql = new StringBuffer();
		sql.append("select * from (SELECT DISTINCT o.* FROM es_order o , es_order_items i , es_goods g WHERE i.order_id = o.order_id AND i.goods_id = g.goods_id AND o.disabled = 0 AND o.status >0 and o.status<8 ");

		if (agentid != null) {
			sql.append("and g.agentid=" + agentid);
		}

		if (keyword != null) {
			sql.append(" and o.sn like '%" + keyword + "%'");
		}

		if ((status != null) && (!StringUtil.isEmpty(status))) {
			sql.append("and o.status in (" + status + ")");
		}

		if ((shipstatus != null) && (!StringUtil.isEmpty(shipstatus))) {
			sql.append(" and o.ship_status in (" + shipstatus + ")");
		}

		if ((start_time != null) && (!StringUtil.isEmpty(start_time))) {
			String stime = dateToStamp(start_time + " 00:00:00");
			sql.append(" and o.create_time>" + stime);
		}
		if ((end_time != null) && (!StringUtil.isEmpty(end_time))) {
			String etime = dateToStamp(end_time + " 23:59:59");
			sql.append(" and o.create_time<" + etime);
		}
		sql.append(") temp");

		return sql.toString();
	}
	public static String dateToStamp(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
		try {
			date = simpleDateFormat.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        long ts = date.getTime()/1000L;
        res = String.valueOf(ts);
        return res;
    }
}

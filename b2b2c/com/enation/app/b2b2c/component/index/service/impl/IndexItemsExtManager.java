package com.enation.app.b2b2c.component.index.service.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.enation.app.b2b2c.component.agent.service.IAgentManager;
import com.enation.app.b2b2c.component.brokerage.service.IBrokerageManager;
import com.enation.app.b2b2c.component.index.service.IIndexItemsExtManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.database.BaseSupport;

public class IndexItemsExtManager extends BaseSupport implements IIndexItemsExtManager {

	private IAdminUserManager adminUserManager;
	private IAgentManager agentManager;
	private IBrokerageManager brokerageManager;
	
	public Map censusState() {
		String agentSql = "";
		boolean isAgent = false;
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			isAgent = true;
			agentSql = " and g.agentid=" + currentUser.getUserid() + " ";
		}
		Map<String, Integer> stateMap = new HashMap(7);
		String[] states = { "cancel_ship", "cancel_pay", "pay", "ship", "complete", "allocation_yes" };
		for (String s : states) {
			stateMap.put(s, Integer.valueOf(0));
		}

		String sql = "select count(distinct o.order_id) num,o.status  from es_order o,es_order_items i,es_goods g where i.order_id=o.order_id and i.goods_id=g.goods_id and o.disabled = 0";
		if(isAgent){
			sql += agentSql;
		}
		sql += " group by o.status";
		
		List<Map<String, Integer>> list = this.daoSupport.queryForList(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Integer> map = new HashMap();
				map.put("status", Integer.valueOf(rs.getInt("status")));
				map.put("num", Integer.valueOf(rs.getInt("num")));
				return map;
			}
		}, new Object[0]);

		for (Map<String, Integer> state : list) {
			stateMap.put(getStateString((Integer) state.get("status")), state.get("num"));
		}

		sql = "select count(distinct o.order_id) num  from es_order o,es_order_items i,es_goods g where i.order_id=o.order_id and i.goods_id=g.goods_id and o.disabled = 0  and o.status=0 ";
		if(isAgent){
			sql += agentSql;
		}
		int count = this.daoSupport.queryForInt(sql, new Object[0]);
		stateMap.put("wait", Integer.valueOf(0));

		sql = "select count(distinct o.order_id) num  from es_order o,es_order_items i,es_goods g where i.order_id=o.order_id and i.goods_id=g.goods_id and o.disabled = 0  and o.pay_status = 0 ";
		if(isAgent){
			sql += agentSql;
		}
		count = this.daoSupport.queryForInt(sql, new Object[0]);
		stateMap.put("not_pay", Integer.valueOf(count));

		sql = "select count(distinct o.order_id) num from es_order o,es_order_items i,es_goods g where i.order_id=o.order_id and i.goods_id=g.goods_id and o.disabled=0  and ( ( o.payment_type!='cod' and o.payment_id!=8  and  o.status=2)  or ( o.payment_type='cod' and  o.status=0))";
		if(isAgent){
			sql += agentSql;
		}
		count = this.baseDaoSupport.queryForInt(sql, new Object[0]);
		stateMap.put("allocation_yes", Integer.valueOf(count));

		return stateMap;
	}

	private String getStateString(Integer state) {
		String str = null;
		switch (state.intValue()) {
		case -2:
			str = "cancel_ship";
			break;
		case -1:
			str = "cancel_pay";
			break;
		case 1:
			str = "pay";
			break;
		case 2:
			str = "ship";
			break;
		case 4:
			str = "allocation_yes";
			break;
		case 7:
			str = "complete";
			break;
		case 0:
		case 3:
		case 5:
		case 6:
		default:
			str = null;
		}

		return str;
	}

	public Map census() {
		String agentSql = "";
		boolean isAgent = false;
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			isAgent = true;
			agentSql = " and agentid=" + currentUser.getUserid() + " ";
		}
		
		String sql = "select count(0) from goods where 1=1";
		if(isAgent){
			sql += agentSql;
		}
		int allcount = this.baseDaoSupport.queryForInt(sql, new Object[0]);

		sql = "select count(0) from goods where market_enable=1 and  disabled = 0";
		if(isAgent){
			sql += agentSql;
		}
		int salecount = this.baseDaoSupport.queryForInt(sql, new Object[0]);

		sql = "select count(0) from goods where market_enable=0 and  disabled = 0";
		if(isAgent){
			sql += agentSql;
		}
		int unsalecount = this.baseDaoSupport.queryForInt(sql, new Object[0]);

		sql = "select count(0) from goods where   disabled = 1";
		if(isAgent){
			sql += agentSql;
		}
		int disabledcount = this.baseDaoSupport.queryForInt(sql, new Object[0]);

		Map<String, Integer> map = new HashMap(2);
		map.put("salecount", Integer.valueOf(salecount));
		map.put("unsalecount", Integer.valueOf(unsalecount));
		map.put("disabledcount", Integer.valueOf(disabledcount));
		map.put("allcount", Integer.valueOf(allcount));
		return map;
	}
	
	public Map saleState() {
		Map<String, String> stateMap = new HashMap(2);
		String agentSql = "";
		boolean isAgent = false;
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		if(this.agentManager.checkAgentUser(currentUser.getUserid())>0){
			isAgent = true;
			agentSql = " and g.agentid=" + currentUser.getUserid() + " ";
		}
		String sql = "SELECT DISTINCT distinct o.order_id,o.need_pay_money money,o.paymoney price FROM es_order o , es_order_items i , es_goods g WHERE i.order_id = o.order_id AND i.goods_id = g.goods_id AND o.disabled = 0 AND o.pay_status in (1,2) ";
		if(isAgent){
			sql += agentSql;
		}
		List<Map<String, String>> list = this.daoSupport.queryForList(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap();
				map.put("order_id", String.valueOf(rs.getInt("order_id")));
				map.put("money", rs.getString("money"));
				map.put("price", rs.getString("price"));
				return map;
			}
		}, new Object[0]);
		
		Double allSale = 0d;
		Double realSale = 0d;
		for (Map<String, String> state : list) {
			Double money = Double.valueOf(state.get("money"));
			Double price = Double.valueOf(state.get("price"));
			allSale += money;
			realSale += price;
		}
		stateMap.put("allsale", new BigDecimal(allSale.toString()).toString());
		stateMap.put("realsale", new BigDecimal(realSale.toString()).toString());
		
		Calendar now = Calendar.getInstance();  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		//日销售额统计
		Double dayAllSale = 0d;
		Double dayRealSale = 0d;
		String daySql = sql;
		String today = sdf.format(now.getTime());
		String stime = dateToStamp(today + " 00:00:00");
		daySql += " and o.create_time>" + stime;
		String etime = dateToStamp(today + " 23:59:59");
		daySql += " and o.create_time<" + etime;
		List<Map<String, String>> daylist = this.daoSupport.queryForList(daySql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap();
				map.put("order_id", String.valueOf(rs.getInt("order_id")));
				map.put("money", rs.getString("money"));
				map.put("price", rs.getString("price"));
				return map;
			}
		}, new Object[0]);
		for (Map<String, String> state : daylist) {
			Double money = Double.valueOf(state.get("money"));
			Double price = Double.valueOf(state.get("price"));
			dayAllSale += money;
			dayRealSale += price;
		}
		stateMap.put("dayallsale", new BigDecimal(dayAllSale.toString()).toString());
		stateMap.put("dayrealsale", new BigDecimal(dayRealSale.toString()).toString());
		//月销售额统计
		Double MonthAllSale = 0d;
		Double MonthRealSale = 0d;
		String monthSql = sql;
		now.add(Calendar.MONTH, 0);  
        now.set(Calendar.DAY_OF_MONTH, 1);  
        String firstday = sdf.format(now.getTime());  
		
        now.add(Calendar.MONTH, 1);  
        now.set(Calendar.DAY_OF_MONTH, 0);  
        String lastday = sdf.format(now.getTime());  
        
        stime = dateToStamp(firstday + " 00:00:00");
        monthSql += " and o.create_time>" + stime;
		etime = dateToStamp(lastday + " 23:59:59");
		monthSql += " and o.create_time<" + etime;
		List<Map<String, String>>  monthlist = this.daoSupport.queryForList(monthSql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap();
				map.put("order_id", String.valueOf(rs.getInt("order_id")));
				map.put("money", rs.getString("money"));
				map.put("price", rs.getString("price"));
				return map;
			}
		}, new Object[0]);
		for (Map<String, String> state : monthlist) {
			Double money = Double.valueOf(state.get("money"));
			Double price = Double.valueOf(state.get("price"));
			MonthAllSale += money;
			MonthRealSale += price;
		}
		stateMap.put("monthallsale", new BigDecimal(MonthAllSale.toString()).toString());
		stateMap.put("monthrealsale", new BigDecimal(MonthRealSale.toString()).toString());
		
		return stateMap;
	}
	
	
	public Map proxyState() {
		Map<String, Object> proxyState = new HashMap(2);
		String brokerageSql = "";
		boolean isBrokerage = false;
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		if(this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0){
			isBrokerage = true;
			brokerageSql = " and c.brokerageId=" + currentUser.getUserid() + " ";
		}
		String sql = "select count(0) as saleNum,sum(po.price) as price,sum(po.proxyPrice) as proxyPrice ";
		sql += " from es_proxyorder po,es_credit c  where po.proxyMemberId=c.memberId";
		if(isBrokerage){
			sql += brokerageSql;
		}
		Map<Object, Object> map = this.daoSupport.queryForMap(sql, new Object[0]);
		
		for(Map.Entry<Object, Object> entry : map.entrySet()){
			if(entry.getKey().equals("salenum")){
				proxyState.put("allnum", entry.getValue());
			}
			if(entry.getKey().equals("price")){
				proxyState.put("allprice", entry.getValue());
			}
			if(entry.getKey().equals("proxyprice")){
				proxyState.put("allproxyprice", entry.getValue());
			}
		}
		
		
		Calendar now = Calendar.getInstance();  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		//日销售额统计
		String daySql = sql;
		String today = sdf.format(now.getTime());
		String stime = dateToStamp(today + " 00:00:00");
		daySql += " and po.soldTime>" + stime;
		String etime = dateToStamp(today + " 23:59:59");
		daySql += " and po.soldTime<" + etime;
		
		Map<Object, Object> dayMap = this.daoSupport.queryForMap(daySql, new Object[0]);
		
		for(Map.Entry<Object, Object> entry : dayMap.entrySet()){  
			if(entry.getKey().equals("salenum")){
				proxyState.put("daynum", entry.getValue());
			}
			if(entry.getKey().equals("price")){
				proxyState.put("dayprice", entry.getValue());
			}
			if(entry.getKey().equals("proxyprice")){
				proxyState.put("dayproxyprice", entry.getValue());
			}
		} 

		//月销售额统计
		Double MonthAllSale = 0d;
		Double MonthRealSale = 0d;
		String monthSql = sql;
		now.add(Calendar.MONTH, 0);  
        now.set(Calendar.DAY_OF_MONTH, 1);  
        String firstday = sdf.format(now.getTime());  
		
        now.add(Calendar.MONTH, 1);  
        now.set(Calendar.DAY_OF_MONTH, 0);  
        String lastday = sdf.format(now.getTime());  
        
        stime = dateToStamp(firstday + " 00:00:00");
        monthSql += " and po.soldTime>" + stime;
		etime = dateToStamp(lastday + " 23:59:59");
		monthSql += " and po.soldTime<" + etime;
		
		Map<Object, Object> monthMap = this.daoSupport.queryForMap(monthSql, new Object[0]);
		
		for(Map.Entry<Object, Object> entry : monthMap.entrySet()){  
			if(entry.getKey().equals("salenum")){
				proxyState.put("monthnum", entry.getValue());
			}
			if(entry.getKey().equals("price")){
				proxyState.put("monthprice", entry.getValue());
			}
			if(entry.getKey().equals("proxyprice")){
				proxyState.put("monthproxyprice", entry.getValue());
			}
		} 
		
		return proxyState;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

	public IAgentManager getAgentManager() {
		return agentManager;
	}

	public void setAgentManager(IAgentManager agentManager) {
		this.agentManager = agentManager;
	}

	public Map creditState() {
		String sql = "select count(0) from credit where 1 = 1";
		AdminUser currentUser = this.adminUserManager.getCurrentUser();
		if (this.brokerageManager.checkBrokerageUser(currentUser.getUserid()) > 0) {
			sql += " and brokerageId="+currentUser.getUserid() + " ";
		}
		int creditCount = this.baseDaoSupport.queryForInt(sql, new Object[0]);

		Map<String, Integer> map = new HashMap(2);
		map.put("allcount", Integer.valueOf(creditCount));
		return map;
	}

	public IBrokerageManager getBrokerageManager() {
		return brokerageManager;
	}

	public void setBrokerageManager(IBrokerageManager brokerageManager) {
		this.brokerageManager = brokerageManager;
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

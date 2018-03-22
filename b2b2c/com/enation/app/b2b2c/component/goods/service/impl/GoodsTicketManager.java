package com.enation.app.b2b2c.component.goods.service.impl;

import java.util.Map;
import java.util.Random;

import com.enation.app.b2b2c.component.goods.model.GoodsTicket;
import com.enation.app.b2b2c.component.goods.service.IGoodsTicketManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

public class GoodsTicketManager extends BaseSupport<GoodsTicket> implements IGoodsTicketManager {

	@Override
	public void add(GoodsTicket ticket) {
		if (ticket == null) {
			throw new IllegalArgumentException("goodsticket is null");
		}

		this.baseDaoSupport.insert("goodsticket", ticket);
	}

	@Override
	public void edit(GoodsTicket ticket) {
		if (ticket == null) {
			throw new IllegalArgumentException("goodsticket is null");
		}
		this.baseDaoSupport.update("goodsticket", ticket, "goods_id=" + ticket.getGoods_id());
	}

	@Override
	public GoodsTicket get(Integer id) {
		String sql = "select * from goodsticket where goods_id=?";
		GoodsTicket find = this.baseDaoSupport.queryForObject(sql, GoodsTicket.class,
				new Object[] { id });
		return find;
	}

	@Override
	public void delete(Integer id) {
		if (id == null)
			return;
		String sql = "delete from goodsticket where goods_id = ?";
		this.baseDaoSupport.execute(sql, new Object[] { id });
	}

	@Override
	public Page searchGoodsTicket(Map ticketMap, Integer page, Integer pageSize, String other, String order) {
		String sql = createTemlSql(ticketMap);
		sql = sql + " order by " + other + " " + order;
		Page webpage = this.daoSupport.queryForPage(sql, page.intValue(), pageSize.intValue(), new Object[0]);
		return webpage;
	}

	private String createTemlSql(Map ticketMap){
		String sql = "select * from es_ticketdetail where 1=1";
		Integer goods_id = (Integer)ticketMap.get("goods_id");
		if(goods_id!=null){
			sql = sql + " and goods_id=" + goods_id;
		}
		return sql;
	}

	@Override
	public void createTicket(Integer goods_id, String pre, Integer num, Long startdate, Long enddate) {
		String sql = createInsertTicketSql(goods_id,pre,num,startdate,enddate);
		this.baseDaoSupport.execute(sql, new Object[0]);
	}
	private String createInsertTicketSql(Integer goods_id,String pre, Integer num, Long startdate, Long enddate){
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO es_ticketdetail VALUES ");
		for(int i=0;i<num;i++){
			String ticketCode = pre + getStringRandom(10);
			sb.append("(null,'"+goods_id+"', '"+ticketCode+"', '0', '"+startdate+"', '"+enddate+"','','','','')");
			if(i==num-1){
				sb.append(";");
			}else{
				sb.append(",");
			}
		}
		return sb.toString();
	}
	private static String getStringRandom(int length) {  
        String val = "";  
        Random random = new Random();  
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }
	public static void main(String[] args){
		System.out.println(getStringRandom(10).toUpperCase());
	}
	
}

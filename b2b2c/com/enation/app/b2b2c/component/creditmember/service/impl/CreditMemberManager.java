package com.enation.app.b2b2c.component.creditmember.service.impl;

import java.util.Map;

import com.enation.app.b2b2c.component.creditmember.service.ICreditMemberManager;
import com.enation.app.base.core.model.Member;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

public class CreditMemberManager extends BaseSupport<Member> implements ICreditMemberManager {

	@Override
	public Page searchCreditMember(Map memberMap, Integer page, Integer pageSize, String other, String order) {
		String sql = createTemlSql(memberMap);
		sql = sql + " order by " + other + " " + order;
		Page webpage = this.daoSupport.queryForPage(sql, page.intValue(), pageSize.intValue(), new Object[0]);

		return webpage;
	}

	public Page searchCreditProxy(Map memberMap, Integer page, Integer pageSize) {
		String start_time = (String) memberMap.get("start_time");
		String end_time = (String) memberMap.get("end_time");
		Integer brokerageId = (Integer) memberMap.get("brokerageId");
		String sql = "select c.*,temp.saleNum,temp.price,temp.proxyPrice  from (select c.id,count(0) as saleNum,sum(po.price) as price,sum(po.proxyPrice) as proxyPrice  ";
				sql += "from es_proxyorder po,es_credit c  where po.proxyMemberId=c.memberId";
				if ((start_time != null) && (!StringUtil.isEmpty(start_time))) {
					long stime = DateUtil.getDateline(start_time + " 00:00:00");
					sql = sql + " and po.soldTime>" + stime;
				}
				if ((end_time != null) && (!StringUtil.isEmpty(end_time))) {
					long etime = DateUtil.getDateline(end_time + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
					sql = sql + " and po.soldTime<" + etime;
				}
				sql += " group by  c.memberId) as temp,es_credit c  ";
				sql += "where temp.id=c.id";
		if (brokerageId != null) {
			sql = sql + " and c.brokerageId=" + brokerageId;
		}
		Page webpage = this.daoSupport.queryForPage(sql, page.intValue(), pageSize.intValue(), new Object[0]);

		return webpage;
	}

	private String createTemlSql(Map memberMap) {
		Integer stype = (Integer) memberMap.get("stype");
		String keyword = (String) memberMap.get("keyword");
		String uname = (String) memberMap.get("uname");
		Integer mobile = (Integer) memberMap.get("mobile");
		Integer lv_id = (Integer) memberMap.get("lvId");
		String email = (String) memberMap.get("email");
		String start_time = (String) memberMap.get("start_time");
		String end_time = (String) memberMap.get("end_time");
		Integer sex = (Integer) memberMap.get("sex");

		Integer province_id = (Integer) memberMap.get("province_id");
		Integer city_id = (Integer) memberMap.get("city_id");
		Integer region_id = (Integer) memberMap.get("region_id");
		Integer review = (Integer) memberMap.get("review");
		Integer brokerageId = (Integer) memberMap.get("brokerageId");

		String sql = "select m.*,c.*,ms.* from " + getTableName("credit") + " c left join " + getTableName("member")
				+ " m on c.memberId = m.member_id left join " + getTableName("membershop")
				+ " ms on m.member_id = ms.memberId where 1=1 ";

		if ((stype != null) && (keyword != null) && (stype.intValue() == 0)) {
			sql = sql + " and (m.uname like '%" + keyword + "%'";
			sql = sql + " or m.mobile like '%" + keyword + "%')";
		}

		if ((uname != null) && (!uname.equals(""))) {
			sql = sql + " and m.name like '%" + uname + "%'";
			sql = sql + " or m.uname like '%" + uname + "%'";
		}
		if (mobile != null) {
			sql = sql + " and m.mobile like '%" + mobile + "%'";
		}

		if ((email != null) && (!StringUtil.isEmpty(email))) {
			sql = sql + " and m.email = '" + email + "'";
		}

		if ((sex != null) && (sex.intValue() != 2)) {
			sql = sql + " and m.sex = " + sex;
		}

		if ((start_time != null) && (!StringUtil.isEmpty(start_time))) {
			long stime = DateUtil.getDateline(start_time + " 00:00:00");
			sql = sql + " and m.regtime>" + stime;
		}
		if ((end_time != null) && (!StringUtil.isEmpty(end_time))) {
			long etime = DateUtil.getDateline(end_time + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql = sql + " and m.regtime<" + etime;
		}
		if ((province_id != null) && (province_id.intValue() != 0)) {
			sql = sql + " and province_id=" + province_id;
		}
		if ((city_id != null) && (city_id.intValue() != 0)) {
			sql = sql + " and city_id=" + city_id;
		}
		if ((region_id != null) && (region_id.intValue() != 0)) {
			sql = sql + " and region_id=" + region_id;
		}
		if (review != null) {
			sql = sql + " and c.review=" + review;
		}
		if (brokerageId != null) {
			sql = sql + " and c.brokerageId=" + brokerageId;
		}
		return sql;
	}

	@Override
	public void creditMember(int memberid, int review) {
		this.baseDaoSupport.execute("update es_credit set review=? where memberId = ?",
				new Object[] { review, memberid });
	}

}

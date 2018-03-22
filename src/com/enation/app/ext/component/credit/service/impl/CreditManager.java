package com.enation.app.ext.component.credit.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.credit.model.Credit;
import com.enation.app.ext.component.credit.service.ICreditManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.util.DateUtil;

@Component
public class CreditManager extends BaseSupport<Credit> implements ICreditManager{

	@Transactional(propagation=Propagation.REQUIRED)
	public void add(Credit credit) {
		int count = this.baseDaoSupport.queryForInt("select count(*) from credit where memberId = ?",new Object[]{credit.getMemberId()});
		if(count == 0){
			credit.setReview(0);
			this.baseDaoSupport.insert("credit", credit);
		}else{
			credit.setReview(0);
			this.baseDaoSupport.update("credit",credit,"memberId = "+credit.getMemberId());
		}
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List getByMemberId(int memberId) {
		String sql = "select * from credit where memberId = ?";
		List creditList = this.baseDaoSupport.queryForList(sql,new Object[]{memberId});
		if(creditList.size() == 0){
			return null;
		}
		return creditList;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public int checkReviewByMemberId(int memberId) {
		int count = this.baseDaoSupport.queryForInt("select count(*) from credit where memberId = ?",new Object[]{memberId});
		if(count!=0){
			String sql = "select * from credit where memberId = ?";
			List<Credit> credit = this.baseDaoSupport.queryForList(sql,Credit.class,new Object[]{memberId});
			if(credit.get(0).getReview()==1){
				return 1;										//授信通过
			}else if(credit.get(0).getReview()==-1){
				return 2;										//未通过
			}else if(credit.get(0).getReview()==0){
				return 3;										//审核中
			}
		}
		return 0;												//未申请
	}
	
	public void setReviewByMemberId(int memberId,int review){
		String rev = String.valueOf(review);
		String mid = String.valueOf(memberId);
		String sql = "update credit set review = "+rev+" where memberId = "+mid;
		this.baseDaoSupport.execute(sql);
	}

	@Override
	public Credit get(int memberId) {
		String sql = "select * from credit where memberId = ?";
		Credit credit = this.baseDaoSupport.queryForObject(sql, Credit.class, new Object[]{memberId});
		return credit;
	}

	@Override
	public void edit(Credit credit) {
		this.baseDaoSupport.update("credit", credit, "memberId=" + credit.getMemberId());
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void importCredit(Map<Integer, List<String>> map) {
		int max_memberId = this.baseDaoSupport.queryForInt("select max(member_id) from member",new Object[0]);
		StringBuffer memberSql = new StringBuffer();
		StringBuffer creditSql = new StringBuffer();
		StringBuffer accountSql = new StringBuffer();
		StringBuffer shopSql = new StringBuffer();
		memberSql.append("INSERT INTO es_member (member_id,mobile,password,uname,name,birthday,sex,address,lv_id,regtime,face,midentity) VALUES ");
		creditSql.append("INSERT INTO es_credit (memberId,mobile,name,idCard,idImg,weibo,fans,weixin,friends,live,liveId,liveFans,review) VALUES ");
		accountSql.append("INSERT INTO es_useraccount (memberId,accountId,creditLevel,credit,remainCredit,repay,repayTime,waitCash,waitMoney,addEarn) VALUES ");
		shopSql.append("INSERT INTO es_membershop (memberId,shopName,label,shopIntro,memberImg,shopImg,mobile,qq,level,createTime,proxyNum,agencyId) VALUES ");
		for (int i = 1; i <= map.size(); i++) {
			List<String> credit = map.get(i);
			max_memberId++;
			String sex = credit.get(3).equals("男")?"1":"0";
			Long birthDay = Long.valueOf(DateUtil.getDatelineLong(credit.get(2)));
			String creditLevel = "1";
			if(credit.get(15).equals("白金卡"))creditLevel = "2";
			if(credit.get(15).equals("黑卡"))creditLevel = "3";
			
			memberSql.append("("+max_memberId+",'"+credit.get(0)+"','e10adc3949ba59abbe56e057f20f883e','"+credit.get(1)+"','"
			+credit.get(1)+"','"+birthDay+"',"+sex+",'"+credit.get(4)+"',1,'"+birthDay+"','/img/face/default.jpg',0)");
			creditSql.append("("+max_memberId+",'"+credit.get(0)+"','"+credit.get(1)+"','"+credit.get(5)+"','/img/2017/"+credit.get(5)
			+".jpg','"+credit.get(6)+"','"+credit.get(7)+"','"+credit.get(8)+"','"+credit.get(9)+"','"+credit.get(10)+"','"+credit.get(11)
			+"','"+credit.get(12)+"',1)");
			accountSql.append("("+max_memberId+",'"+credit.get(13)+"','"+creditLevel+"','"+credit.get(14)+"','"+credit.get(14)
			+"','0','2017-07-21','0','0','0')");
			shopSql.append("("+max_memberId+",'"+credit.get(16)+"','"+credit.get(17)+"','"+credit.get(18)+"','/img/face/default.jpg','/img/face/default_background.jpg','"
			+credit.get(19)+"','"+credit.get(20)+"',9,'2017-07-21',0,-1)");
			if(i==map.size()){
				memberSql.append(";");
				creditSql.append(";");
				accountSql.append(";");
				shopSql.append(";");
			}else{
				memberSql.append(",");
				creditSql.append(",");
				accountSql.append(",");
				shopSql.append(",");
			}
		}
		this.baseDaoSupport.execute(memberSql.toString(), new Object[0]);
		this.baseDaoSupport.execute(creditSql.toString(), new Object[0]);
		this.baseDaoSupport.execute(accountSql.toString(), new Object[0]);
		this.baseDaoSupport.execute(shopSql.toString(), new Object[0]);
	}

}

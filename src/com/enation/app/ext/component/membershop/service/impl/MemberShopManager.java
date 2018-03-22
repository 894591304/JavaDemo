package com.enation.app.ext.component.membershop.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.membershop.model.MemberShop;
import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.database.BaseSupport;

@Component
public class MemberShopManager extends BaseSupport<MemberShop> implements IMemberShopManager{

	@Transactional(propagation=Propagation.REQUIRED)
	public void add(MemberShop memberShop) 
	{
		memberShop.setCreateTime(String.valueOf(System.currentTimeMillis()/1000));
		String sql = "select count(*) from memberShop where memberId = ?";
		int count = this.baseDaoSupport.queryForInt(sql, new Object[]{memberShop.getMemberId()});
		if(count !=0){                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
			edit(memberShop);
			}else{
				memberShop.setShowOrHide(1);
				this.baseDaoSupport.insert("membershop", memberShop);
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public MemberShop getByMemberId(int memberId) 
	{
		MemberShop m =this.baseDaoSupport.queryForObject("select * from memberShop where memberId = ?",MemberShop.class, new Object[]{memberId});
		return m;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<MemberShop> OrderByLevel()
	{
		List<MemberShop> shopList= this.baseDaoSupport.queryForList("select * from memberShop where showOrHide = 1 order by id desc",MemberShop.class, new Object[]{});
		return shopList;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void edit(MemberShop memberShop)
	{
		MemberShop m = getByMemberId(memberShop.getMemberId());
		m.setShopName(memberShop.getShopName());
		m.setShopImg(memberShop.getShopImg());
		m.setMemberImg(memberShop.getMemberImg());
		m.setLabel(memberShop.getLabel());
		m.setShopIntro(memberShop.getShopIntro());
		m.setMobile(memberShop.getMobile());
		m.setQq(memberShop.getQq());
		m.setProxyNum(memberShop.getProxyNum());
		this.baseDaoSupport.update("memberShop",m,"id="+m.getId());
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public MemberShop get(int memberId) {
		String sql="select * from memberShop where memberId = ?";
		MemberShop memberShop = this.baseDaoSupport.queryForObject(sql, MemberShop.class, new Object[]{memberId});
		return memberShop;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<MemberShop> search(String keyword) {
		String[] key = keyword.split("and|or|\\+|\\s+");
		int count = key.length;
		String sql = "select * from memberShop where ";
		for(int i=0;i<count;i++){
			String sqlkey = "%"+key[i]+"%";
			sql = sql+"shopName like '"+sqlkey+"' or label like '"+sqlkey+"'";
			if(i!=count-1){
				sql = sql+" or ";
			}
			sql = sql + " and showOrHide = 1 ";
		}
		List<MemberShop> mList = this.baseDaoSupport.queryForList(sql, MemberShop.class,new Object[]{});
		return mList;
	}

	@Override
	public void setShowOrHide(int showhide, String memberids) {
		String sql = "update es_memberShop set showOrHide="+showhide+" where memberId in (" +memberids +")";
		this.baseDaoSupport.execute(sql, new Object[0]);
	}

}

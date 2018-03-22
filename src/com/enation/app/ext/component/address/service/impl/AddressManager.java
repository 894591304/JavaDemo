package com.enation.app.ext.component.address.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.address.model.Address;
import com.enation.app.ext.component.address.service.IAddressManager;
import com.enation.eop.sdk.database.BaseSupport;

public class AddressManager extends BaseSupport<Address> implements IAddressManager{

	@Transactional(propagation=Propagation.REQUIRED)
	public void add(Address address) {
		this.baseDaoSupport.insert("address",address);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void update(Address address){
		String sql = "select count(*) from address where id = ?";
		int count = this.baseDaoSupport.queryForInt(sql,new Object[]{address.getId()});
		if(count!=0){
			this.baseDaoSupport.update("address", address, "id="+address.getId());
		}
	}

	public List<Address> getByMemberid(int memberid) {
		String sql = "select * from address where memberid = ?";
		List<Address> aList = this.baseDaoSupport.queryForList(sql, Address.class,new Object[]{memberid});
		return aList;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(int id,int memberid) {
		String sql = "select count(*) from address where id = ? and memberId = ?";
		int count = this.baseDaoSupport.queryForInt(sql,new Object[]{id,memberid});
		if(count!=0){
			String sql1 = "delete from address where id = ?";
			this.baseDaoSupport.execute(sql1, new Object[]{id});
		}
	}

}

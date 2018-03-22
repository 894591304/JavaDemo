package com.enation.app.ext.component.address.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.ext.component.address.model.Address;

public abstract interface IAddressManager {
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract void add(Address address);
	
	public abstract List<Address> getByMemberid(int memberid);
	
	public abstract void update(Address address);
	
	public abstract void delete(int id,int memberid);
}

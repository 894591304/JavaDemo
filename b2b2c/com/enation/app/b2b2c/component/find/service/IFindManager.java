package com.enation.app.b2b2c.component.find.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.component.find.model.Find;

@Component
public interface IFindManager {

	@Transactional(propagation=Propagation.REQUIRED)
	public abstract void add(Find find);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract void edit(Find find);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract Find get(Integer id);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract void delete(Integer id);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public abstract List<Find> getAll();
}

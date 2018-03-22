package com.enation.app.b2b2c.component.find.service.impl;

import java.util.List;

import com.enation.app.b2b2c.component.find.model.Find;
import com.enation.app.b2b2c.component.find.service.IFindManager;
import com.enation.eop.sdk.database.BaseSupport;

public class FindManager extends BaseSupport<Find> implements IFindManager {

	@Override
	public void add(Find find) {
		if (find == null) {
			throw new IllegalArgumentException("find is null");
		}

		this.baseDaoSupport.insert("tags", find);
	}

	@Override
	public void edit(Find find) {
		if (find == null) {
			throw new IllegalArgumentException("find is null");
		}
		this.baseDaoSupport.update("tags", find, "tag_id=" + find.getTag_id());
	}

	@Override
	public Find get(Integer id) {
		String sql = "select * from tags where tag_id=?";
		Find find = this.baseDaoSupport.queryForObject(sql, Find.class,
				new Object[] { id });
		return find;
	}

	@Override
	public void delete(Integer id) {
		if (id == null)
			return;
		String sql = "delete from tags where tag_id = ?";
		this.baseDaoSupport.execute(sql, new Object[] { id });
	}
	
	public List<Find> getAll(){
		String sql = "select * from tags";
		List<Find> fList = this.baseDaoSupport.queryForList(sql, Find.class,new Object[]{});
		return fList;
	}

}

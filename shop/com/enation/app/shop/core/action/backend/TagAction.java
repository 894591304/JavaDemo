package com.enation.app.shop.core.action.backend;

import com.enation.app.b2b2c.component.find.model.Find;
import com.enation.app.b2b2c.component.find.service.IFindManager;
import com.enation.app.shop.core.service.ITagManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.action.WWAction;

public class TagAction extends WWAction {
	private ITagManager tagManager;
	private IFindManager findManager;
	private Find tag;
	private Integer[] tag_id;
	private Integer tagId;
	private String file;

	public String checkJoinGoods() {
		if (this.tagManager.checkJoinGoods(this.tag_id)) {
			this.json = "{result:1}";
		} else {
			this.json = "{result:0}";
		}
		return "json_message";
	}

	public String checkname() {
		if (this.tagManager.checkname(this.tag.getTag_name(), this.tag.getTag_id())) {
			this.json = "{result:1}";
		} else {
			this.json = "{result:0}";
		}
		return "json_message";
	}

	public String add() {
		return "add";
	}

	public String edit() {
		this.tag = this.findManager.get(this.tagId);
		return "edit";
	}

	public String saveAdd() {
		try {
			this.findManager.add(this.tag);
			showSuccessJson("添加标签成功");
		} catch (Exception e) {
			showErrorJson("添加标签失败");
			this.logger.error("添加标签失败", e);
		}
		return "json_message";
	}

	public String saveEdit() {
		if ((EopSetting.IS_DEMO_SITE) && (this.tag.getTag_id().intValue() <= 3)) {
			showErrorJson("抱歉，当前为演示站点，以不能修改这些示例数据，请下载安装包在本地体验这些功能！");
			return "json_message";
		}

		this.findManager.edit(this.tag);
		showSuccessJson("商品修改成功");
		return "json_message";
	}

	public String delete() {
		if (EopSetting.IS_DEMO_SITE) {
			for (Integer tid : this.tag_id) {
				if (tid.intValue() <= 3) {
					showErrorJson("抱歉，当前为演示站点，以不能修改这些示例数据，请下载安装包在本地体验这些功能！");
					return "json_message";
				}
			}
		}
		try {
			this.tagManager.delete(this.tag_id);
			showSuccessJson("标签删除成功");
		} catch (Exception e) {
			showErrorJson("标签删除失败");
			this.logger.error("标签删除失败", e);
		}
		return "json_message";
	}

	public String list() {
		this.webpage = this.tagManager.list(getPage(), getPageSize());
		return "list";
	}

	public String listJson() {
		this.webpage = this.tagManager.list(getPage(), getPageSize());
		showGridJson(this.webpage);
		return "json_message";
	}

	public ITagManager getTagManager() {
		return this.tagManager;
	}

	public void setTagManager(ITagManager tagManager) {
		this.tagManager = tagManager;
	}

	public IFindManager getFindManager() {
		return findManager;
	}

	public void setFindManager(IFindManager findManager) {
		this.findManager = findManager;
	}

	public Find getTag() {
		return tag;
	}

	public void setTag(Find tag) {
		this.tag = tag;
	}

	public Integer[] getTag_id() {
		return this.tag_id;
	}

	public void setTag_id(Integer[] tag_id) {
		this.tag_id = tag_id;
	}

	public Integer getTagId() {
		return this.tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
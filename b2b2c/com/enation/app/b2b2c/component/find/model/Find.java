package com.enation.app.b2b2c.component.find.model;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

public class Find implements Serializable {
	private static final long serialVersionUID = -6686545355126495959L;
	private Integer tag_id;
	private String tag_name;
	private Integer rel_count;
	private String pic;
	private String comment;

	@PrimaryKeyField
	public Integer getTag_id() {
		return this.tag_id;
	}

	public void setTag_id(Integer tagId) {
		this.tag_id = tagId;
	}

	public String getTag_name() {
		return this.tag_name;
	}

	public void setTag_name(String tagName) {
		this.tag_name = tagName;
	}

	public Integer getRel_count() {
		return this.rel_count;
	}

	public void setRel_count(Integer relCount) {
		this.rel_count = relCount;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}

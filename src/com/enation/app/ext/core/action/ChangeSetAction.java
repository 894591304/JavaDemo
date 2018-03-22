package com.enation.app.ext.core.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.core.service.IMemberExtManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;
import com.sun.xml.messaging.saaj.packaging.mime.internet.ParseException;

public class ChangeSetAction extends WWAction{

	private static final long serialVersionUID = -3296440087570455091L;
	
	private IMemberExtManager memberExtManager;
	private String imgurl;
	private String name;
	private int sex;
	private int year;
	private int month;
	private int day;
	
	public String change(){
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			showErrorJson("未登录不能修改资料！");
			return "json_message";
		}
		Member m1 = this.memberExtManager.getByMemberid(member.getMember_id());
		if((this.name.length()<2)||(this.name.length()>20)){
			showErrorJson("用户名的长度为2-20个字符！");
			return "json_message";
		}
		if(StringUtil.isEmpty(this.imgurl)){
			showErrorJson("请上传头像！");
			return "json_message";
		}
		m1.setName(name);
		m1.setUname(name);
		m1.setFace(imgurl);
		m1.setSex(sex);
		String yyyy = String.valueOf(year);
		String mm = String.valueOf(month);
		String dd = String.valueOf(day);
		if(month<10){
			mm = "0"+month;
		}
		if(day<10){
			dd = "0"+day;
		}
		String birth = yyyy+"-"+mm+"-"+dd;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(birth);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		Long birthday = date.getTime();
		m1.setBirthday(birthday/1000);
		this.memberExtManager.update(m1);
		this.json=JsonMessageUtil.getStringJson("result", "1");
		return "json_message";
	}

	public IMemberExtManager getMemberExtManager() {
		return memberExtManager;
	}

	public void setMemberExtManager(IMemberExtManager memberExtManager) {
		this.memberExtManager = memberExtManager;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
}

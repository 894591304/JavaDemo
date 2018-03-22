package com.enation.app.ext.core.tag;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.types.resources.selectors.Date;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.core.service.IMemberExtManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class PersonSetTag extends BaseFreeMarkerTag{

	private IMemberExtManager memberExtManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[PersonSetTag]");
		}
		Map result = new HashMap();
		Member m1 = this.memberExtManager.getByMemberid(member.getMember_id());
		if(m1.getFace()==null||m1.getFace()==""){result.put("face", "/javamall/img/face/default.jpg");}
		else{result.put("face", m1.getFace());}
		result.put("name",m1.getName());
		result.put("sex",m1.getSex());
		result.put("mobile",m1.getMobile());
		if(null==m1.getBirthday()||m1.getBirthday()==0){
			result.put("birth","保密");
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy");
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM");
			SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd");
			Long timeLong = System.currentTimeMillis();
			String year = simpleDateFormat1.format(timeLong);
			String month = simpleDateFormat2.format(timeLong);
			String day = simpleDateFormat3.format(timeLong);
			result.put("year",year);
			result.put("month",month);
			result.put("day",day);
		}else{
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy");
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM");
			SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd");
			long ts = m1.getBirthday();
			String year = simpleDateFormat1.format(ts*1000);
			String month = simpleDateFormat2.format(ts*1000);
			String day = simpleDateFormat3.format(ts*1000);
			result.put("birth",year+"年"+month+"月"+day+"日");
			result.put("year",year);
			result.put("month",month);
			result.put("day",day);			
		}
		return result;
	}

	public IMemberExtManager getMemberExtManager() {
		return memberExtManager;
	}

	public void setMemberExtManager(IMemberExtManager memberExtManager) {
		this.memberExtManager = memberExtManager;
	}	
}

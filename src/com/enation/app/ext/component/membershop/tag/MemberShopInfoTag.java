package com.enation.app.ext.component.membershop.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.ext.component.membershop.service.IMemberShopManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MemberShopInfoTag extends BaseFreeMarkerTag{

	private IMemberShopManager memberShopManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Map result = new HashMap();
		return null;
	}

}

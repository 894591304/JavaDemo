package com.enation.app.ext.core.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import com.enation.app.ext.zsy.utils.*;;

@Component
@Scope("prototype")
public class ZsyTestTag implements TemplateMethodModel{
	
	public Object exec(List arg0) throws TemplateModelException {
		Map result = new HashMap();
		SignUtil signUtil = new SignUtil();
		String json = signUtil.sendBatchPayout();
		result.put("json", json);
		return result;
	}

}

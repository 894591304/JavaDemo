package com.enation.app.ext.component.collect.tag;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.collect.service.ICollectManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.ObjectNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class CheckCollectTag extends BaseFreeMarkerTag{

	private ICollectManager collectManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		Map result = new HashMap();
		try {
			Integer proxyid = getProxyId();
			int count = this.collectManager.getAllCollect(proxyid);
			result.put("num",count);
			if(member==null){
				result.put("collect",0);
				return result;
				}else{
					int re = this.collectManager.checkCollect(member.getMember_id(), proxyid);
					if(re==1){
						result.put("collect",1);
						return result;
					}else{
						result.put("collect",0);
						return result;
					}
				}
			
		}
		catch (ObjectNotFoundException e) {
			throw new UrlNotFoundException();
		}
	}

	private Integer getProxyId()
	{
		HttpServletRequest httpRequest = ThreadContextHolder.getHttpRequest();
		String url = RequestUtil.getRequestUrl(httpRequest);
		String proxyid = paseProxyId(url);
		return Integer.valueOf(proxyid);
	}
	
	private static String paseProxyId(String url){
		String pattern = "(-)(\\d+)";
		String value = null;
		Pattern p = Pattern.compile(pattern,34);
		Matcher m = p.matcher(url);
		if(m.find()){
			value = m.group(2);
		}
		return value;
	}
	
	public ICollectManager getCollectManager() {
		return collectManager;
	}

	public void setCollectManager(ICollectManager collectManager) {
		this.collectManager = collectManager;
	}

}

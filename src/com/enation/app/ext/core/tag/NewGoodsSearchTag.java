package com.enation.app.ext.core.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.credit.model.Credit;
import com.enation.app.ext.component.credit.service.ICreditManager;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.ext.component.useraccount.service.IUserAccountManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class NewGoodsSearchTag extends BaseFreeMarkerTag{

	private IGoodsProxyManager goodsProxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IUserAccountManager userAccountManager;
	private IProxyManager proxyManager;
	private ICreditManager creditManager;

	protected Object exec(Map arg0) throws TemplateModelException {
		Map result = new HashMap();
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		int level = this.userAccountManager.getLevel(member.getMember_id());
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[NewGoodsSearchTag]");
		}
		try
		{
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			Map<String, String> params = new HashMap();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String)iter.next();
				String[] values = (String[])requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			String keyword = null;
			request.setCharacterEncoding("utf-8");
			keyword = new String(request.getParameter("keyword").getBytes("utf-8"),"utf-8");
			if(keyword==null){
				result.put("search",2);
				return result;
			}
			List<Goods> gList= this.goodsProxyManager.search(keyword);
			if(gList.size()==0){
				result.put("search",0);
				return result;
			}
			int total = gList.size();
			List goodsList = new ArrayList();
			int num = 0;
			for(int i=0;i<total;i++){
				Map gMap = new HashMap();
				Goods goods = gList.get(i);
				Credit credit = this.creditManager.get(member.getMember_id());
				boolean b = this.proxyManager.checkCanProxy(member.getMember_id(),credit.getBrokerageId(),goods.getGoods_id());
				if(this.proxyManager.checkProxy(goods.getGoods_id(), member.getMember_id())!=1&&b==true)
				{
					if(goods!=null&&goods.getDisabled()!=1&&goods.getMarket_enable()==1){
					GoodsAgent goodsAgent = this.goodsAgentManager.get(goods.getGoods_id());
					if(level==1){gMap.put("agentprice",goodsAgent.getGoldPrice());}
					else if(level==2){gMap.put("agentprice",goodsAgent.getPlatinumPrice());}
					else {gMap.put("agentprice",goodsAgent.getBlackPrice());}
					gMap.put("goodsimg", goods.getOriginal());
					gMap.put("Mktprice", goods.getMktprice());
					gMap.put("goodsid", String.valueOf(goods.getGoods_id()));
					gMap.put("name", goods.getName());
					gMap.put("price", goods.getPrice());
					gMap.put("stock", this.goodsProxyManager.getGoods(goodsAgent.getGoodsId()).getStore());
					gMap.put("onsale",this.proxyManager.getAllAgentNumByGoodsid(goodsAgent.getGoodsId()));
					goodsList.add(gMap);
					num++;
					}
				}
			}
			if(num==0){
				result.put("search",0);
				return result;
			}else{
				result.put("search",1);
			}
			result.put("total",num);
			result.put("goodslist",goodsList);
		}
		catch (Exception e)
		{
			this.logger.error("查询失败！", e);
			result.put("search", 3);
		}
		return result;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

	public IGoodsAgentManager getGoodsAgentManager() {
		return goodsAgentManager;
	}

	public void setGoodsAgentManager(IGoodsAgentManager goodsAgentManager) {
		this.goodsAgentManager = goodsAgentManager;
	}

	public IUserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(IUserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public IProxyManager getProxyManager() {
		return proxyManager;
	}

	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

	public ICreditManager getCreditManager() {
		return creditManager;
	}

	public void setCreditManager(ICreditManager creditManager) {
		this.creditManager = creditManager;
	}
	
}

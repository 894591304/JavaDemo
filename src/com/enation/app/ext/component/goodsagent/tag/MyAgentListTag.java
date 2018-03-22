package com.enation.app.ext.component.goodsagent.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.ext.component.goodsagent.model.GoodsAgent;
import com.enation.app.ext.component.goodsagent.service.IGoodsAgentManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.eop.sdk.user.UserServiceFactory;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.app.ext.component.proxy.model.Proxy;
import com.enation.app.ext.component.proxy.service.IProxyManager;
import com.enation.app.shop.core.model.Goods;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MyAgentListTag extends BaseFreeMarkerTag{

	private IGoodsProxyManager goodsProxyManager;
	private IGoodsAgentManager goodsAgentManager;
	private IProxyManager proxyManager;
	
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Member member = UserServiceFactory.getUserService().getCurrentMember();
		if(member == null){
			throw new TemplateModelException("未登录不能使用此标签[MyAgentOnSaleListTag]");
		}
		Map result = new HashMap();
		int listnum = 10;
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
			int page = 1;
			request.setCharacterEncoding("utf-8");
			page = new Integer(request.getParameter("page"));
			List<Proxy> proxyList= this.proxyManager.getAllByMemberid(member.getMember_id());
			List proxyGoods = new ArrayList();
			List proxyEndGoods = new ArrayList();
			int total = proxyList.size();
			int total1 = 0;
			int total2 = 0;
			int pn1 = 0;
			int pn2 = 0;
			for(int i=0;i<total;i++){
				if(this.proxyManager.checkProxyCanBuy(proxyList.get(i).getId())!=0){
					if(total1>=listnum){total1++;}else{
					if(pn1<(page-1)*listnum){pn1++;}else{
					Map msgMap = new HashMap();
					Goods goods = this.goodsProxyManager.getGoods(proxyList.get(i).getGoodsId());
					GoodsAgent goodsAgent = this.goodsAgentManager.get(goods.getGoods_id());
					if(goodsAgent.getTicketOption()==null||goodsAgent.getTicketOption().equals("")){
						msgMap.put("ticket",0);
					}else{
						msgMap.put("ticket",1);
					}
					msgMap.put("proxyid", String.valueOf(proxyList.get(i).getId()));
					msgMap.put("goodsimg", goods.getOriginal());
					msgMap.put("goodsname", goods.getName());
					msgMap.put("goodsid", String.valueOf(proxyList.get(i).getGoodsId()));
					msgMap.put("price", goodsAgent.getPrice());
					msgMap.put("expectearn",goodsAgent.getPrice()*proxyList.get(i).getGoodsAmount()-proxyList.get(i).getFrozenCredit()*10);
					msgMap.put("sold", proxyList.get(i).getSale());
					msgMap.put("notsold",proxyList.get(i).getOnSale()-proxyList.get(i).getSale());
					msgMap.put("notonsale",proxyList.get(i).getGoodsAmount()-proxyList.get(i).getOnSale());
					msgMap.put("earn",proxyList.get(i).getFrozenEarn());
					msgMap.put("testtime",proxyList.get(i).getProxyTestTime());
					msgMap.put("endtime",proxyList.get(i).getProxyEndTime());
					Long nTime = System.currentTimeMillis()/1000;
					Long lTime =(System.currentTimeMillis()+1000*60*60*24*2)/1000;			/*当前时间之前的三天*/ 
					Long tTime =Long.valueOf(proxyList.get(i).getProxyTestTime());
					Long eTime =Long.valueOf(proxyList.get(i).getProxyEndTime());
					msgMap.put("testlosttime", (tTime-nTime)/60/60/24);
					msgMap.put("losttime", (eTime-nTime)/60/60/24);
					if(proxyList.get(i).getProxyTestTime().equals(proxyList.get(i).getProxyEndTime())){
						msgMap.put("test",2);			//不可延期
					}else if(lTime<tTime){
						msgMap.put("test",0);			//不可延期
					}else{
						msgMap.put("test",1);			//可以延期
					}
					proxyGoods.add(msgMap);
					total1++;}}
					
				}else{
					if(total2>=listnum){total2++;}else{
					if(pn2<(page-1)*listnum){pn2++;}else{					
					Map msgMap = new HashMap();
					Goods goods = this.goodsProxyManager.getGoods(proxyList.get(i).getGoodsId());
					GoodsAgent goodsAgent = this.goodsAgentManager.get(goods.getGoods_id());
					msgMap.put("proxyid", String.valueOf(proxyList.get(i).getId()));
					msgMap.put("goodsimg", goods.getOriginal());
					msgMap.put("goodsname", goods.getName());
					msgMap.put("goodsid", String.valueOf(proxyList.get(i).getGoodsId()));
					msgMap.put("price", goodsAgent.getPrice());
					msgMap.put("sold", proxyList.get(i).getSale());
					msgMap.put("notsold",proxyList.get(i).getOnSale()-proxyList.get(i).getSale());
					msgMap.put("notonsale",proxyList.get(i).getGoodsAmount()-proxyList.get(i).getOnSale());
					msgMap.put("testtime", proxyList.get(i).getProxyTestTime());
					msgMap.put("endtime", proxyList.get(i).getProxyEndTime());
					msgMap.put("earn",proxyList.get(i).getFrozenEarn());
					proxyEndGoods.add(msgMap);		
					total2++;}}
				}
			}
			result.put("total1", total1);
			result.put("onsaleallpage", String.valueOf((total1/listnum)+1));
			result.put("onsalenowpage",String.valueOf(page+1));
			result.put("total2", total2);
			result.put("endsaleallpage", String.valueOf((total2/listnum)+1));
			result.put("endsalenowpage",String.valueOf(page+1));
			result.put("proxylist",proxyGoods);
			result.put("proxyendlist",proxyEndGoods);
			return result;
		}catch (Exception e){
			List<Proxy> proxyList= this.proxyManager.getAllByMemberid(member.getMember_id());
			List proxyGoods = new ArrayList();
			List proxyEndGoods = new ArrayList();
			int total = proxyList.size();
			int total1 = 0;
			int total2 = 0;
			int pn1 = 0;
			int pn2 = 0;
			for(int i=0;i<total;i++){
				if(this.proxyManager.checkProxyCanBuy(proxyList.get(i).getId())!=0){
					if(total1>=listnum){total1++;}else{
					Map msgMap = new HashMap();
					Goods goods = this.goodsProxyManager.getGoods(proxyList.get(i).getGoodsId());
					GoodsAgent goodsAgent = this.goodsAgentManager.get(goods.getGoods_id());
					if(goodsAgent.getTicketOption()==null||goodsAgent.getTicketOption().equals("")){
						msgMap.put("ticket",0);
					}else{
						msgMap.put("ticket",1);
					}
					msgMap.put("proxyid", String.valueOf(proxyList.get(i).getId()));
					msgMap.put("goodsimg", goods.getOriginal());
					msgMap.put("goodsname", goods.getName());
					msgMap.put("goodsid",String.valueOf( proxyList.get(i).getGoodsId()));
					msgMap.put("price", goodsAgent.getPrice());
					msgMap.put("expectearn",goodsAgent.getPrice()*proxyList.get(i).getGoodsAmount()-proxyList.get(i).getFrozenCredit()*10);
					msgMap.put("sold", proxyList.get(i).getSale());
					msgMap.put("notsold",proxyList.get(i).getOnSale()-proxyList.get(i).getSale());
					msgMap.put("notonsale",proxyList.get(i).getGoodsAmount()-proxyList.get(i).getOnSale());
					msgMap.put("earn",proxyList.get(i).getFrozenEarn());
					msgMap.put("testtime",proxyList.get(i).getProxyTestTime());
					msgMap.put("endtime",proxyList.get(i).getProxyEndTime());
					Long nTime = System.currentTimeMillis()/1000;
					Long lTime =(System.currentTimeMillis()+1000*60*60*24*2)/1000;			/*当前时间之前的三天*/ 
					Long tTime =Long.valueOf(proxyList.get(i).getProxyTestTime());
					Long eTime =Long.valueOf(proxyList.get(i).getProxyEndTime());
					msgMap.put("testlosttime", (tTime-nTime)/60/60/24);
					msgMap.put("losttime", (eTime-nTime)/60/60/24);
					if(proxyList.get(i).getProxyTestTime().equals(proxyList.get(i).getProxyEndTime())){
						msgMap.put("test",2);			//不可延期
					}else if(lTime<tTime){
						msgMap.put("test",0);			//不可延期
					}else{
						msgMap.put("test",1);			//可以延期
					}
					proxyGoods.add(msgMap);
					total1++;}
					
				}else{
					if(total2>=listnum){total2++;}else{
					Map msgMap = new HashMap();
					Goods goods = this.goodsProxyManager.getGoods(proxyList.get(i).getGoodsId());
					GoodsAgent goodsAgent = this.goodsAgentManager.get(goods.getGoods_id());
					msgMap.put("proxyid", String.valueOf(proxyList.get(i).getId()));
					msgMap.put("goodsimg", goods.getOriginal());
					msgMap.put("goodsname", goods.getName());
					msgMap.put("goodsid", String.valueOf(proxyList.get(i).getGoodsId()));
					msgMap.put("price", goodsAgent.getPrice());
					msgMap.put("sold", proxyList.get(i).getSale());
					msgMap.put("notsold",proxyList.get(i).getOnSale()-proxyList.get(i).getSale());
					msgMap.put("notonsale",proxyList.get(i).getGoodsAmount()-proxyList.get(i).getOnSale());
					msgMap.put("testtime", proxyList.get(i).getProxyTestTime());
					msgMap.put("endtime", proxyList.get(i).getProxyEndTime());
					msgMap.put("earn",proxyList.get(i).getFrozenEarn());
					proxyEndGoods.add(msgMap);		
					total2++;}
				}
			}
			result.put("total1", total1);
			result.put("onsaleallpage", String.valueOf((total1/listnum)+1));
			result.put("onsalenowpage",1+1);
			result.put("total2", total2);
			result.put("endsaleallpage", String.valueOf((total2/listnum)+1));
			result.put("endsalenowpage",1+1);
			result.put("proxylist",proxyGoods);
			result.put("proxyendlist",proxyEndGoods);
			return result;
		}
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


	public IProxyManager getProxyManager() {
		return proxyManager;
	}


	public void setProxyManager(IProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}

}

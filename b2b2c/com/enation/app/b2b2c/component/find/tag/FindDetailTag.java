package com.enation.app.b2b2c.component.find.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.find.model.Find;
import com.enation.app.b2b2c.component.find.service.IFindManager;
import com.enation.app.ext.core.service.IGoodsProxyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.ObjectNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;

import freemarker.template.TemplateModelException;


@Component
@Scope("prototype")
public class FindDetailTag extends BaseFreeMarkerTag{

	private IFindManager findManager;
	private IGoodsProxyManager goodsProxyManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		try {
				Integer findId = getFindId();
				Map result = new HashMap();
				List gList = new ArrayList();
				Find find = this.findManager.get(findId);
				if(find==null){
					throw new UrlNotFoundException();
				}
				List<Goods> goodslist = this.goodsProxyManager.getDiscountList(findId);
				int total = goodslist.size();
				if(total%2==0){								//双数商品
					for(int i=0;i<total;i++){
						Map goodsMap = new HashMap();
						if((i+1)%2==1){						//左侧商品
							goodsMap.put("LorR", "l");
						}else{								//右侧商品
							goodsMap.put("LorR", "r");
						}
						goodsMap.put("goodsimg", goodslist.get(i).getOriginal());
						goodsMap.put("goodsid", String.valueOf(goodslist.get(i).getGoods_id()));
						goodsMap.put("goodsname", goodslist.get(i).getName());
						goodsMap.put("price", goodslist.get(i).getPrice());
						gList.add(goodsMap);
					}					
				}else{										//单数商品
					for(int i=0;i<total;i++){
						Map goodsMap = new HashMap();
						if(i+1==total){						//最后一个商品
							goodsMap.put("LorR", "e");
						}else{
							if((i+1)%2==1){					//左侧商品
								goodsMap.put("LorR", "l");
							}else{							//右侧商品
								goodsMap.put("LorR", "r");
							}
						}
						goodsMap.put("goodsimg", goodslist.get(i).getOriginal());
						goodsMap.put("goodsid", String.valueOf(goodslist.get(i).getGoods_id()));
						goodsMap.put("goodsname", goodslist.get(i).getName());
						goodsMap.put("price", goodslist.get(i).getPrice());
						gList.add(goodsMap);						
					}					
				}				
				result.put("goodslist",gList);
				result.put("findname", find.getTag_name());
				result.put("findimg", "/javamall"+find.getPic());
				result.put("findcomment",find.getComment());
				return result;
			}
		catch (ObjectNotFoundException e) {
			throw new UrlNotFoundException();
		}
	}
	
	private Integer getFindId()
	{
		HttpServletRequest httpRequest = ThreadContextHolder.getHttpRequest();
		String url = RequestUtil.getRequestUrl(httpRequest);
		String findId = paseFindId(url);
		return Integer.valueOf(findId);
	}
	
	private static String paseFindId(String url){
		String pattern = "(-)(\\d+)";
		String value = null;
		Pattern p = Pattern.compile(pattern,34);
		Matcher m = p.matcher(url);
		if(m.find()){
			value = m.group(2);
		}
		return value;
	}
	
	public IFindManager getFindManager() {
		return findManager;
	}

	public void setFindManager(IFindManager findManager) {
		this.findManager = findManager;
	}

	public IGoodsProxyManager getGoodsProxyManager() {
		return goodsProxyManager;
	}

	public void setGoodsProxyManager(IGoodsProxyManager goodsProxyManager) {
		this.goodsProxyManager = goodsProxyManager;
	}

}

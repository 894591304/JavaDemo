package com.enation.app.b2b2c.component.find.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.component.find.service.IFindManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.app.b2b2c.component.find.model.Find;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class FindListTag extends BaseFreeMarkerTag{

	private IFindManager findManager;
	
	protected Object exec(Map arg0) throws TemplateModelException {
		Map result = new HashMap();
		List findList = new ArrayList();
		List<Find> fList = this.findManager.getAll();
		int total = fList.size();
		if(total>0){
			if(total>=3){
				for(int i=0;i<total;i++){
					Map findMap = new HashMap();
					if(i<1){
						findMap.put("sp", "0");
					}else if(i==1){
						findMap.put("sp", "1");
					}else if(i==2){
						findMap.put("sp", "2");
					}else{
						findMap.put("sp", "3");
					}
					findMap.put("findid", String.valueOf(fList.get(i).getTag_id()));
					findMap.put("findname",fList.get(i).getTag_name());
					findMap.put("findimg", "/javamall"+fList.get(i).getPic());
					findList.add(findMap);
					}
			}
			else{
				for(int i=0;i<total;i++){
					Map findMap = new HashMap();
					findMap.put("sp", "0");
					findMap.put("findid", String.valueOf(fList.get(i).getTag_id()));
					findMap.put("findname",fList.get(i).getTag_name());
					findMap.put("findimg", "/javamall"+fList.get(i).getPic());
					findList.add(findMap);
					}				
			}
		}	
		result.put("total", total);
		result.put("findlist",findList);
		return result;
	}

	public IFindManager getFindManager() {
		return findManager;
	}

	public void setFindManager(IFindManager findManager) {
		this.findManager = findManager;
	}

}

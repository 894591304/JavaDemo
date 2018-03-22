 package com.enation.app.base.core.action;
 
 import com.enation.app.base.core.model.AuthAction;
 import com.enation.app.base.core.service.auth.IAdminUserManager;
 import com.enation.app.base.core.service.auth.IAuthActionManager;
 import com.enation.app.base.core.service.auth.IPermissionManager;
 import com.enation.app.base.core.service.auth.impl.PermissionConfig;
 import com.enation.eop.resource.IAdminThemeManager;
 import com.enation.eop.resource.IMenuManager;
 import com.enation.eop.resource.model.AdminTheme;
 import com.enation.eop.resource.model.AdminUser;
 import com.enation.eop.resource.model.EopSite;
 import com.enation.eop.resource.model.Menu;
 import com.enation.eop.sdk.context.EopContext;
 import com.enation.eop.sdk.context.EopSetting;
 import com.enation.framework.action.WWAction;
 import com.enation.framework.context.webcontext.ThreadContextHolder;
 import java.util.ArrayList;
 import java.util.List;
 import javax.servlet.http.HttpServletRequest;
 import org.apache.commons.lang3.StringUtils;
 
 
 
 
 
 
 
 
 
 public class BackendUiAction
   extends WWAction
 {
   private IAdminThemeManager adminThemeManager;
   private IAdminUserManager adminUserManager;
   private IMenuManager menuManager;
   private IPermissionManager permissionManager;
   private IAuthActionManager authActionManager;
   private String theme;
   private EopSite site;
   private String username;
   private String version;
   private AdminUser user;
   private String timeout;
   private String referer;
   private int type;
   private List menuList;
   private String ctx;
   
   public String login()
   {
     putCommonData();
     return "login_page";
   }
   
   public String main() {
     this.user = this.adminUserManager.getCurrentUser();
     putCommonData();
     this.version = EopSetting.VERSION;
     
     String bklogo = this.site.getBklogofile() == null ? this.site.getLogofile() : this.site.getBklogofile();
     this.site.setBklogofile(bklogo);
     
     if (this.user.getFounder() != 1) {
       this.menuList = this.menuManager.newMenutree(Integer.valueOf(0), this.user);
     } else {
       this.menuList = this.menuManager.getMenuTree(Integer.valueOf(0));
     }
     this.ctx = getRequest().getContextPath();
     if ("/".equals(this.ctx)) {
       this.ctx = "";
     }
     
     return "main_page";
   }
   
   private void putCommonData() {
     this.site = EopContext.getContext().getCurrentSite();
     
 
 
 
     AdminTheme theTheme = this.adminThemeManager.get(this.site.getAdminthemeid());
     this.theme = "default";
     if (theTheme != null) {
       this.theme = theTheme.getPath();
     }
     this.ctx = getRequest().getContextPath();
     if ("/".equals(this.ctx)) {
       this.ctx = "";
     }
   }
   
   public String menuJson()
   {
     this.json = getMenuJson();
     return "json_message";
   }
   
   public String getMenuJson() {
     HttpServletRequest request = ThreadContextHolder.getHttpRequest();
     String showall = request.getParameter("showall");
     StringBuffer json = new StringBuffer();
     
 
 
 
 
 
     List<Menu> tempMenuList = this.menuManager.getMenuList();
     List<Menu> menuList = new ArrayList();
     
     AdminUser user = this.adminUserManager.getCurrentUser();
     user = this.adminUserManager.get(user.getUserid());
     List<AuthAction> authList = this.permissionManager.getUesrAct(user.getUserid().intValue(), "menu");
     
     for (Menu menu : tempMenuList) {
       if ((menu.getMenutype().intValue() == 2) && 
         (!"yes".equals(showall))) {
         if (user.getFounder() != 1) {
           if (checkPermssion(menu, authList)) {}
         }
         else
         {
           int superAdminAuthId = PermissionConfig.getAuthId("super_admin");
           AuthAction superAuth = this.authActionManager.get(superAdminAuthId);
           if ((superAuth != null) && (!checkPermssion(menu, superAuth))) {
             continue;
           }
         }
       }
       
       menuList.add(menu);
     }
     List<Menu> syslist = getMenuList(1, menuList);
     List<Menu> applist = getMenuList(2, menuList);
     List<Menu> extlist = getMenuList(3, menuList);
     
     json.append("var menu ={");
     json.append("'sys':[");
     json.append(toJson(syslist, menuList));
     json.append("]");
     
     json.append(",'app':[");
     json.append(toJson(applist, menuList));
     json.append("]");
     
     json.append(",'ext':[");
     json.append(toJson(extlist, menuList));
     json.append("]");
     json.append("};");
     
     json.append("var mainpage=true;");
     json.append("var domain='" + request.getServerName() + "';");
     json.append("var runmode=" + EopSetting.RUNMODE + ";");
     json.append("var app_path='" + request.getContextPath() + "';");
     
     return json.toString();
   }
   
 
 
 
 
 
   public String toJson(List<Menu> menuList, List<Menu> allList)
   {
     StringBuffer menuItem = new StringBuffer();
     int i = 0;
     for (Menu menu : menuList) {
       if (i != 0)
         menuItem.append(",");
       menuItem.append(toJson(menu, allList));
       i++;
     }
     return menuItem.toString();
   }
   
   private boolean checkPermssion(Menu menu, List<AuthAction> authList) {
     for (AuthAction auth : authList) {
       if (checkPermssion(menu, auth)) {
         return true;
       }
     }
     return false;
   }
   
   private boolean checkPermssion(Menu menu, AuthAction auth) {
     String values = auth.getObjvalue();
     if (values != null) {
       String[] value_ar = StringUtils.split(values, ",");
       for (String v : value_ar) {
         if (v.equals("" + menu.getId().intValue())) {
           return true;
         }
       }
     }
     return false;
   }
   
 
 
 
 
 
   private String toJson(Menu menu, List<Menu> menuList)
   {
     String defaulticon = "default.png";
     
     String title = menu.getTitle();
     String url = menu.getUrl();
     Integer selected = menu.getSelected();
     String type = menu.getDatatype();
     String target = menu.getTarget();
     String icon = menu.getIcon();
     String iconhover = menu.getIcon_hover();
     
     if (StringUtils.isEmpty(icon)) {
       icon = defaulticon;
     }
     
 
     if (!"_blank".equals(target)) {
       HttpServletRequest httpRequest = ThreadContextHolder.getHttpRequest();
       String ctx = httpRequest.getContextPath();
       ctx = ctx.equals("/") ? "" : ctx;
       url = ctx + url;
     }
     StringBuffer menuItem = new StringBuffer();
     
     menuItem.append("{");
     menuItem.append("\"id\":");
     menuItem.append(menu.getId());
     
     menuItem.append(",\"text\":\"");
     menuItem.append(title);
     menuItem.append("\"");
     
     menuItem.append(",\"url\":\"");
     menuItem.append(url);
     menuItem.append("\"");
     
     menuItem.append(",\"default\":");
     menuItem.append(selected);
     
     menuItem.append(",\"children\":");
     menuItem.append(getChildrenJson(menu.getId(), menuList));
     
     menuItem.append(",\"type\":\"");
     menuItem.append(type);
     menuItem.append("\"");
     
     menuItem.append(",\"target\":\"");
     menuItem.append(target);
     menuItem.append("\"");
     
     menuItem.append(",\"icon\":\"");
     menuItem.append(icon);
     menuItem.append("\"");
     
     menuItem.append(",\"icon_hover\":\"");
     menuItem.append(iconhover);
     menuItem.append("\"");
     
     menuItem.append("}");
     
     return menuItem.toString();
   }
   
 
 
 
 
 
 
   public List<Menu> getMenuList(int menuType, List<Menu> menuList)
   {
     List<Menu> mlist = new ArrayList();
     
     for (Menu menu : menuList) {
       if ((menu.getMenutype().intValue() == menuType) && (menu.getPid().intValue() == 0))
       {
         mlist.add(menu);
       }
     }
     return mlist;
   }
   
 
 
 
 
 
 
   private String getChildrenJson(Integer menuId, List<Menu> menuList)
   {
     StringBuffer json = new StringBuffer();
     json.append("[");
     int i = 0;
     for (Menu menu : menuList) {
       if (menuId.intValue() == menu.getPid().intValue()) {
         if (i != 0)
           json.append(",");
         json.append(toJson(menu, menuList));
         i++;
       }
     }
     json.append("]");
     return json.toString();
   }
   
 
 
   public IAdminThemeManager getAdminThemeManager()
   {
     return this.adminThemeManager;
   }
   
   public void setAdminThemeManager(IAdminThemeManager adminThemeManager) { this.adminThemeManager = adminThemeManager; }
   
   public IAdminUserManager getAdminUserManager() {
     return this.adminUserManager;
   }
   
   public void setAdminUserManager(IAdminUserManager adminUserManager) { this.adminUserManager = adminUserManager; }
   
   public String getTheme() {
     return this.theme;
   }
   
   public void setTheme(String theme) { this.theme = theme; }
   
 
 
   public EopSite getSite()
   {
     return this.site;
   }
   
 
   public void setSite(EopSite site)
   {
     this.site = site;
   }
   
 
   public String getUsername()
   {
     return this.username;
   }
   
 
   public void setUsername(String username)
   {
     this.username = username;
   }
   
 
   public String getVersion()
   {
     return this.version;
   }
   
 
   public void setVersion(String version)
   {
     this.version = version;
   }
   
   public AdminUser getUser() {
     return this.user;
   }
   
   public void setUser(AdminUser user) {
     this.user = user;
   }
   
   public IMenuManager getMenuManager() {
     return this.menuManager;
   }
   
   public void setMenuManager(IMenuManager menuManager) {
     this.menuManager = menuManager;
   }
   
   public IPermissionManager getPermissionManager() {
     return this.permissionManager;
   }
   
   public void setPermissionManager(IPermissionManager permissionManager) {
     this.permissionManager = permissionManager;
   }
   
   public IAuthActionManager getAuthActionManager() {
     return this.authActionManager;
   }
   
   public void setAuthActionManager(IAuthActionManager authActionManager) {
     this.authActionManager = authActionManager;
   }
   
   public String getTimeout() {
     return this.timeout;
   }
   
   public void setTimeout(String timeout) {
     this.timeout = timeout;
   }
   
   public String getReferer() {
     return this.referer;
   }
   
   public void setReferer(String referer) {
     this.referer = referer;
   }
   
   public int getType() {
     return this.type;
   }
   
   public void setType(int type) {
     this.type = type;
   }
   
   public List getMenuList() {
     return this.menuList;
   }
   
   public void setMenuList(List menuList) {
     this.menuList = menuList;
   }
   
   public String getCtx() {
     return this.ctx;
   }
   
   public void setCtx(String ctx) {
     this.ctx = ctx;
   }
 }
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<c:if test="${userType=='admin' || userType=='broker' }">
<table cellspacing="0" cellpadding="0" border="0" width="100%">
      <tbody>   
      	<tr>
            <th><span>总销量：</span></th>
            <td><span ><a onclick="parent.addTab1('网红销量','${ctx }/shop/b2b2c/creditMember!creditProxyList.do?type=1')" class="imp" target="ajax" >${ proxysss.allnum}</a></td>
          </tr>   
          <tr>
            <th><span>总销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('网红销量','${ctx }/shop/b2b2c/creditMember!creditProxyList.do?type=1')" class="imp" target="ajax" >${ proxysss.allprice}</a> 元</td>
          </tr>
          <tr>
            <th><span>实际结算销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('网红销量','${ctx }/shop/b2b2c/creditMember!creditProxyList.do?type=1')" class="imp" target="ajax" >${ proxysss.allproxyprice}</a> 元</span></td>
          </tr>
          <tr>
            <th><span>日销量：</span></th>
            <td><span ><a onclick="parent.addTab1('网红销量','${ctx }/shop/b2b2c/creditMember!creditProxyList.do?type=2')" class="imp" target="ajax" >${ proxysss.daynum}</a> </td>
          </tr>
          <tr>
            <th><span>日销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('网红销量','${ctx }/shop/b2b2c/creditMember!creditProxyList.do?type=2')" class="imp" target="ajax" >${ proxysss.dayprice}</a> 元</td>
          </tr>
          <tr>
            <th><span>实际日结算销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('网红销量','${ctx }/shop/b2b2c/creditMember!creditProxyList.do?type=2')" class="imp" target="ajax" >${ proxysss.dayproxyprice}</a> 元</span></td>
          </tr>
          <tr>
            <th><span>月销量：</span></th>
            <td><span ><a onclick="parent.addTab1('网红销量','${ctx }/shop/b2b2c/creditMember!creditProxyList.do?type=3')" class="imp" target="ajax" >${ proxysss.monthnum}</a> 元</td>
          </tr>
          <tr>
            <th><span>月销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('网红销量','${ctx }/shop/b2b2c/creditMember!creditProxyList.do?type=3')" class="imp" target="ajax" >${ proxysss.monthprice}</a> 元</td>
          </tr>
          <tr>
            <th><span>实际月结算销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('网红销量','${ctx }/shop/b2b2c/creditMember!creditProxyList.do?type=3')" class="imp" target="ajax" >${ proxysss.monthproxyprice}</a> 元</span></td>
          </tr>                
        </tbody>            
 </table>
 </c:if>
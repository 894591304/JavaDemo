<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<c:if test="${userType=='admin' || userType=='agent' }">
<table cellspacing="0" cellpadding="0" border="0" width="100%">
      <tbody>
          <tr>
            <th><span>总销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('销售统计','${ctx }/shop/b2b2c/indexItemsExt!order_detail.do?type=1')" class="imp" target="ajax" >${ sqlesss.allsale}</a> 元</td>
          </tr>
          <tr>
            <th><span>实际结算销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('销售统计','${ctx }/shop/b2b2c/indexItemsExt!order_detail.do?type=1')" class="imp" target="ajax" >${ sqlesss.realsale}</a> 元</span></td>
          </tr>
          <tr>
            <th><span>日销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('日销售统计','${ctx }/shop/b2b2c/indexItemsExt!order_detail.do?type=2')" class="imp" target="ajax" >${ sqlesss.dayallsale}</a> 元</td>
          </tr>
          <tr>
            <th><span>实际日结算销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('日销售统计','${ctx }/shop/b2b2c/indexItemsExt!order_detail.do?type=2')" class="imp" target="ajax" >${ sqlesss.dayrealsale}</a> 元</span></td>
          </tr>
          <tr>
            <th><span>月销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('月销售额统计','${ctx }/shop/b2b2c/indexItemsExt!order_detail.do?type=3')" class="imp" target="ajax" >${ sqlesss.monthallsale}</a> 元</td>
          </tr>
          <tr>
            <th><span>实际月结算销售额：</span></th>
            <td><span ><a onclick="parent.addTab1('月销售额统计','${ctx }/shop/b2b2c/indexItemsExt!order_detail.do?type=3')" class="imp" target="ajax" >${ sqlesss.monthrealsale}</a> 元</span></td>
          </tr>           
        </tbody>            
 </table>
 </c:if>
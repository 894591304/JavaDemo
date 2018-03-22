<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<c:if test="${userType=='admin' || userType=='broker' }">
<table cellspacing="0" cellpadding="0" border="0" width="100%">
      <tbody>      
          <tr>
            <th><span>网红数量：</span></th>
            <td><span ><a href="${ctx}/shop/b2b2c/creditMember!creditMemberList.do"  class="imp">${ creditsss.allcount}</a>个</span></td>
          </tr>     
        </tbody>            
 </table>
 </c:if>
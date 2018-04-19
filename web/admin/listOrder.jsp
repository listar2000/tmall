<%--
  Created by IntelliJ IDEA.
  User: liyx
  Date: 2018/4/19
  Time: 22:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<title>订单管理</title>

<div class="workingArea">
    <h1 class="label label-info">订单管理</h1>
    <br>
    <br>
    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
            <tr class="success">
                <th>ID</th>
                <th>状态</th>
                <th>金额</th>
                <th width="100px">商品数量</th>
                <th width="100px">买家名称</th>
                <th>创建时间</th>
                <th>支付时间</th>
                <th>发货时间</th>
                <th>确认收货时间</th>
                <th width="120px">操作</th>
            </tr>
            </thead>
            <tbody>
                <c:forEach items = "${os}" var = "o">
                    <tr>
                        <td>${o.id}</td>
                        <td>${o.statusDesc}</td>
                        <td>￥<fmt:formatNumber value="${o.total}" minFractionDigits="2" type="number"/></td>
                        <td align="center">${o.totalNumber}</td>
                        <td align="center">${o.user.username}</td>
                        <td><fmt:formatDate value="${o.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${o.payDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${o.deliveryDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${o.confirmDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <button oid="${o.id}" class="orderPageCheckOrderItems btn btn-primary btn-xs">查看详情</button>
                            <c:if test="${o.status == 'waitDelivery'}">
                                <a href="admin_order_delivery?id=${o.id}">
                                    <button class="btn btn-primary btn-xs">发货</button>
                                </a>
                            </c:if>
                        </td>
                    </tr>
                    <tr class="orderPageOrderItemTR" oid="${o.id}">
                        <td colspan="10" align="center">
                            <div class="orderPageOrderItem">
                                <table align="center" width="800px" class="orderListItemTable">
                                    <tbody>
                                        <c:forEach items="${o.orderItems}" var="oi">
                                            <tr>
                                                <td align="left">
                                                    <img src="../img/productSingle/${oi.product.firstProductImage.id}"
                                                    width="40px" height="40px">
                                                </td>

                                                <td>
                                                    <a href="foreProduct?pid=${oi.product.id}">
                                                        <span>${oi.product.name}</span>
                                                    </a>
                                                </td>

                                                <td align="right">
                                                    <span class="text-muted">${oi.product.stock}个</span>
                                                </td>

                                                <td align="right">
                                                    <span class="text-muted">单价：￥${oi.product.promotePrice}</span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="pageDiv">
        <%@include file="../include/admin/adminPage.jsp"%>
    </div>

</div>
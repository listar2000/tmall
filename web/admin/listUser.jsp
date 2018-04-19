<%--
  Created by IntelliJ IDEA.
  User: liyx
  Date: 2018/4/18
  Time: 19:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<title>用户管理</title>

<div class="workingArea">
    <h1 class="label label-info">用户管理</h1>
    <br>
    <br>
    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
            <tr class="success">
                <th>ID</th>
                <th>用户名称</th>
            </tr>
            </thead>
            <tbody>
                <c:forEach items = "${us}" var = "u">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.username}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="pageDiv">
        <%@include file="../include/admin/adminPage.jsp"%>
    </div>

</div>

<%@include file="../include/admin/adminFooter.jsp"%>
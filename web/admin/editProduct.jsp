<%--
  Created by IntelliJ IDEA.
  User: liyx
  Date: 2018/4/12
  Time: 22:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<div class="workingArea">
    <oi class="breadcrumb">
        <li>
            <a href="admin_category_list">所有分类</a>
        </li>
        <li>
            <a href="admin_product_list?cid=${p.category.id}">${p.category.name}</a>
        </li>
        <li>${p.name}</li>
        <li class="active">编辑产品</li>
    </oi>

    <div class="panel panel-info editDiv">
        <div class="panel-heading">新增产品</div>
        <div class="panel-body">
            <form method="post" id="addForm" action="admin_product_update">
                <table class="addTable">
                    <tbody>
                        <tr>
                            <td>产品名称</td>
                            <td>
                                <input id="name" name="name" value="${p.name}" type="text" class="form-control">
                            </td>
                        </tr>
                        <tr>
                            <td>产品小标题</td>
                            <td>
                                <input id="subTitle" name="subTitle" value="${p.subTitle}" type="text" class="form-control">
                            </td>
                        </tr>
                        <tr>
                            <td>原价格</td>
                            <td>
                                <input id="originalPrice" name="originalPrice" value="${p.originalPrice}" type="text" class="form-control">
                            </td>
                        </tr>
                        <tr>
                            <td>促销价格</td>
                            <td>
                                <input id="promotePrice" name="promotePrice" value="${p.promotePrice}" type="text" class="form-control">
                            </td>
                        </tr>
                        <tr>
                            <td>库存</td>
                            <td>
                                <input id="stock" name="stock" value="${p.stock}" type="text" class="form-control">
                            </td>
                        </tr>
                        <tr class="submitTR">
                            <td colspan="2" align="center">
                                <input type="hidden" name="cid" value="${p.category.id}">
                                <input type="hidden" name="id" value="${p.id}">
                                <button type="submit" class="btn btn-success">提 交</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
</div>
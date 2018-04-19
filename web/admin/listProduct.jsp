<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<script>
    $(function(){
        $("#addForm").submit(function(){
            if(!checkEmpty("name","产品名称"))
                return false;
            return true;
        });
    });
</script>

<title>产品管理</title>

<div class="workingArea">

    <ol class="breadcrumb">
        <li>
            <a href="admin_category_list">所有分类</a>
        </li>
        <li>
            <a href="admin_property_list?cid=${c.id}">${c.name}</a>
        </li>
        <li class="active">产品管理</li>
    </ol>

    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
            <tr class="success">
                <th>ID</th>
                <th>图片</th>
                <th>产品名称</th>
                <th>产品小标题</th>
                <th>原价格</th>
                <th>优惠价格</th>
                <th>库存数量</th>
                <th>图片管理</th>
                <th>设置属性</th>
                <th>编辑</th>
                <th>删除</th>
            </tr>
            </thead>
            <tbody>
                <c:forEach items = "${ps}" var="p">
                    <tr>
                        <td>${p.id}</td>
                        <td>
                            <c:if test="${!empty p.firstProductImage}">
                                <img width="40px" src="../img/productSingle/${p.firstProductImage.id}.jpg">
                            </c:if>
                        </td>
                        <td>${p.name}</td>
                        <td>${p.subTitle}</td>
                        <td>${p.originalPrice}</td>
                        <td>${p.promotePrice}</td>
                        <td>${p.stock}</td>
                        <td>
                            <a href="admin_productImage_list?pid=${p.id}">
                                <span class="glyphicon glyphicon-camera"></span>
                            </a>
                        </td>
                        <td>
                            <a href="admin_product_editPropertyValue?id=${p.id}">
                                <span class="glyphicon glyphicon-th"></span>
                            </a>
                        </td>
                        <td>
                            <a deleteLink="true" href="admin_product_edit?id=${p.id}">
                                <span class="glyphicon glyphicon-pencil"></span>
                            </a>
                        </td>
                        <td>
                            <a href="admin_product_delete?id=${p.id}">
                                <span class="glyphicon glyphicon-remove-circle"></span>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="pageDiv">
        <%@include file="../include/admin/adminPage.jsp"%>
    </div>

    <div class="panel panel-info addDiv">
        <div class="panel-heading">新增产品</div>
        <div class="panel-body">
            <form method="post" id="addForm" action="admin_product_add">
                <table class="addTable">
                    <tbody>
                        <tr>
                            <td>产品名称</td>
                            <td>
                                <input id="name" name="name" type="text" class="form-control">
                            </td>
                        </tr>
                        <tr>
                            <td>产品小标题</td>
                            <td>
                                <input id="subTitle" name="subTitle" type="text" class="form-control">
                            </td>
                        </tr>
                        <tr>
                            <td>原价格</td>
                            <td>
                                <input id="originalPrice" name="originalPrice" value="99.98" type="text" class="form-control">
                            </td>
                        </tr>
                        <tr>
                            <td>促销价格</td>
                            <td>
                                <input id="promotePrice" name="promotePrice" value="19.98" type="text" class="form-control">
                            </td>
                        </tr>
                        <tr>
                            <td>库存</td>
                            <td>
                                <input id="stock" name="stock" value="99" type="text" class="form-control">
                            </td>
                        </tr>
                        <tr class="submitTR">
                            <td colspan="2" align="center">
                                <input type="hidden" name="cid" value="${c.id}">
                                <button type="submit" class="btn btn-success">提 交</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
</div>

<%@include file="../include/admin/adminFooter.jsp"%>
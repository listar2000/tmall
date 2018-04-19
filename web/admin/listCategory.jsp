<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="java.util.*"%>
 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>


<!--作者：listar2000@163.com
	时间：2018-04-03
	描述：list category-->

<script>
 	$(function(){
		$("#addForm").submit(function(){
			if(!checkEmpty("name","分类名称"))
				return false;
			if(!checkEmpty("categoryPic","分类图片"))
				return false;
			return true;
		});
	});
</script>

<title>分类管理</title>

<div class="workingArea">
	<h1 class="label label-warning">分类管理</h1>
	<br>
	<br>
	<div class="listDataTableDiv">
		<table class="table table-striped table-bordered table-hover table-condensed">
			<thead>
				<tr class="success">
					<th>ID</th>
	                <th>图片</th>
	                <th>分类名称</th>
	            	<th>属性管理</th> 
	           	 	<th>产品管理</th> 
	                <th>编辑</th>
	                <th>删除</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items = "${thecs}" var = "c">
					<tr>
						<td>${c.id}</td>
						<td><img height="40px" src="img/category/${c.id}.jpg"></td>
						<td>${c.name}</td>
						<td>
							<a href="admin_property_list?cid=${c.id}">
								<span class="glyphicon glyphicon-list-alt"></span>
							</a>
						</td>
						<td>
							<a href="admin_product_list?cid=${c.id}">
								<span class="glyphicon glyphicon-lock"></span>
							</a>
						</td>
						<td>
							<a deleteLink="true" href="admin_category_edit?id=${c.id}">
								<span class="glyphicon glyphicon-pencil"></span>
							</a>
						</td>
						<td>
							<a href="admin_category_delete?id=${c.id}">
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

	<!--增加分类-->
	<div class="panel panel-info addDiv"> 
		<div class="panel-heading">添加分类</div>
		<div class="panel-body">
			<form method="post" id="addForm" action="admin_category_add" enctype="multipart/form-data">
				<table class="addTable">
					<tbody>
						<tr>
							<td>分类名称</td>
							<td><input id="name" name="name" type="text" placeholder="输入名称" class="form-control"></td>
						</tr>
						<tr>
							<td>分类图片</td>
							<td>
								<input id="categoryPic" accept="image/*" type="file" name="filepath"/>
							</td>
						</tr>
						<tr class="submitTR">
							<td colspan="2" align="center">
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
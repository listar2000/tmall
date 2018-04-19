<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
   
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<title>编辑分类</title>

<div class="workingArea">
	<ol class="breadcrumb">
		<li>
			<a href="admin_category_list">所有分类</a>
		</li>
		<li class="active">编辑分类</li>
	</ol>
</div>

<div class="panel panel-info editDiv"> 
	<div class="panel-heading">添加分类</div>
	<div class="panel-body">
		<form method="post" id="editForm" action="admin_category_update" enctype="multipart/form-data">
			<table class="editTable">
				<tbody>
					<tr>
						<td>分类名称</td>
						<td>
							<input id="name" name="name" type="text" value="${c.name}" class="form-control">
							<input type="hidden" name="id" value="${c.id}">
						</td>
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"
isELIgnored="false" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
	$(function() {
		$("ul.pagination li.disabled a").click(function(){
			return false;
		});
	});
</script>

<nav>
	<ul class="pagination">
		<li <c:if test="${!page.hasPrevious}">class="disabled"</c:if>>
			<a href="?page.start=0${page.param}" aria-label="Previous">
				<span aria-hidden="true" class="glyphicon glyphicon-backward"></span>
			</a>
		</li>
		
		<li <c:if test="${!page.hasPrevious}">class="disabled"</c:if>>
			<a href="?page.start=${page.start-page.end}${page.param}" aria-label="Previous">
				<span aria-hidden="true" class="glyphicon glyphicon-chevron-left"></span>
			</a>
		</li>
		
		<c:forEach begin="0" end="${page.totalPage-1}" varStatus="status">

				<li <c:if test="${status.index*page.end==page.start}">class="disabled"</c:if>>
					<a 
                	href="?page.start=${status.index*page.end}${page.param}"
                	<c:if test="${status.index*page.end==page.start}">class="current"</c:if>>
                	${status.count}
					</a>
				</li>

		</c:forEach>
		
		<li <c:if test="${page.hasNext}">class="disabled"</c:if>>
			<a href="?page.start=${page.start+page.end}${page.param}" aria-label="Next">
				<span aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span>
			</a>
		</li>
		
		<li <c:if test="${page.hasNext}">class="disabled"</c:if>>
			<a href="?page.start=${page.last}${page.param}" aria-label="Next">
				<span aria-hidden="true" class="glyphicon glyphicon-forward"></span>
			</a>
		</li>
	</ul>
</nav>
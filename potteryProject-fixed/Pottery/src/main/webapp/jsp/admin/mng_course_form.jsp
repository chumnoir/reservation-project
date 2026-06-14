<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="active" value="course" />
<c:set var="isEdit" value="${mode == 'edit'}" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>コース${isEdit ? '編集' : '追加'} - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">コース${isEdit ? '編集' : '追加'}</h1>

		<c:if test="${not empty error}">
			<div class="alert alert-error"><c:out value="${error}" /></div>
		</c:if>

		<div class="admin-card">
			<form method="post"
				action="${pageContext.request.contextPath}/admin/course/${isEdit ? 'edit' : 'add'}/confirm">
				<c:if test="${isEdit}">
					<input type="hidden" name="id" value="${course.course_id}">
				</c:if>

				<div class="form-row">
					<label for="course_name">コース名</label>
					<input type="text" id="course_name" name="course_name"
						class="form-control" value="${fn:escapeXml(course.course_name)}">
				</div>

				<div class="form-row">
					<label for="price">参加費（円）</label>
					<input type="number" id="price" name="price" min="0"
						class="form-control"
						value="${course.course_id != null || not empty course.course_name ? course.price : ''}">
				</div>

				<div class="form-row">
					<label for="required_time">所要時間（分）</label>
					<input type="number" id="required_time" name="required_time" min="0"
						class="form-control"
						value="${course.course_id != null || not empty course.course_name ? course.required_time : ''}">
				</div>

				<div class="form-row">
					<label for="description">詳細内容</label>
					<textarea id="description" name="description" class="form-control"><c:out
							value="${course.description}" /></textarea>
				</div>

				<div class="btn-row">
					<button type="submit" class="btn btn-primary">確認</button>
					<a class="btn btn-secondary"
						href="${pageContext.request.contextPath}/admin/course">戻る</a>
				</div>
			</form>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

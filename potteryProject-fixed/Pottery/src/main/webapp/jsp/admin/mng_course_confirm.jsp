<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="active" value="course" />
<c:set var="isEdit" value="${mode == 'edit'}" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>コース${isEdit ? '編集' : '追加'}確認 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">コース${isEdit ? '編集' : '追加'}確認</h1>
		<p class="admin-page-desc">以下の内容で${isEdit ? '更新' : '登録'}します。よろしいですか？</p>

		<div class="admin-card">
			<div class="confirm-list">
				<div class="row">
					<div class="key">コース名</div>
					<div class="val"><c:out value="${course_name}" /></div>
				</div>
				<div class="row">
					<div class="key">参加費</div>
					<div class="val"><c:out value="${price}" /> 円</div>
				</div>
				<div class="row">
					<div class="key">所要時間</div>
					<div class="val"><c:out value="${required_time}" /> 分</div>
				</div>
				<div class="row">
					<div class="key">詳細内容</div>
					<div class="val"><c:out value="${description}" /></div>
				</div>
			</div>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/course/${isEdit ? 'edit' : 'add'}/complete">
				<c:if test="${isEdit}">
					<input type="hidden" name="id" value="${id}">
				</c:if>
				<input type="hidden" name="course_name" value="${fn:escapeXml(course_name)}">
				<input type="hidden" name="price" value="${price}">
				<input type="hidden" name="required_time" value="${required_time}">
				<input type="hidden" name="description" value="${fn:escapeXml(description)}">
				<div class="btn-row">
					<button type="submit" class="btn btn-primary">この内容で${isEdit ? '更新' : '登録'}する</button>
					<button type="button" class="btn btn-secondary"
						onclick="history.back()">戻る</button>
				</div>
			</form>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

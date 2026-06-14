<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="active" value="course" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>コース削除確認 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">コース削除確認</h1>
		<p class="admin-page-desc">以下のコースを削除しますか？このコースを使っているワークショップがある場合は削除できません。</p>

		<div class="admin-card">
			<div class="confirm-list">
				<div class="row">
					<div class="key">コース名</div>
					<div class="val"><c:out value="${course.course_name}" /></div>
				</div>
				<div class="row">
					<div class="key">参加費</div>
					<div class="val"><c:out value="${course.price}" /> 円</div>
				</div>
				<div class="row">
					<div class="key">所要時間</div>
					<div class="val"><c:out value="${course.required_time}" /> 分</div>
				</div>
			</div>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/course/delete/complete">
				<input type="hidden" name="id" value="${course.course_id}">
				<div class="btn-row">
					<button type="submit" class="btn btn-danger">削除する</button>
					<a class="btn btn-secondary"
						href="${pageContext.request.contextPath}/admin/course">戻る</a>
				</div>
			</form>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

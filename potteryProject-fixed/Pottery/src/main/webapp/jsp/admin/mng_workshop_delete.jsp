<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="active" value="workshop" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>ワークショップ削除確認 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">ワークショップ削除確認</h1>
		<p class="admin-page-desc">以下のワークショップを削除しますか？この枠への予約も併せて削除されます。</p>

		<div class="admin-card">
			<div class="confirm-list">
				<div class="row">
					<div class="key">コース</div>
					<div class="val"><c:out value="${workshop.course_name}" /></div>
				</div>
				<div class="row">
					<div class="key">開催日</div>
					<div class="val"><c:out value="${workshop.workshop_date}" /></div>
				</div>
				<div class="row">
					<div class="key">開始時刻</div>
					<div class="val"><c:out value="${fn:substring(workshop.start_time,0,5)}" /></div>
				</div>
				<div class="row">
					<div class="key">残席数</div>
					<div class="val"><c:out value="${workshop.capacity}" /></div>
				</div>
			</div>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/workshop/delete/complete">
				<input type="hidden" name="id" value="${workshop.workshop_id}">
				<div class="btn-row">
					<button type="submit" class="btn btn-danger">削除する</button>
					<a class="btn btn-secondary"
						href="${pageContext.request.contextPath}/admin/workshop">戻る</a>
				</div>
			</form>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

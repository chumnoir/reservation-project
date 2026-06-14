<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="active" value="reservation" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>予約削除確認 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">予約削除確認</h1>
		<p class="admin-page-desc">以下の予約を削除しますか？削除すると、その人数分の残席が戻ります。</p>

		<div class="admin-card">
			<div class="confirm-list">
				<div class="row">
					<div class="key">コース</div>
					<div class="val"><c:out value="${history.course_name}" /></div>
				</div>
				<div class="row">
					<div class="key">開催日</div>
					<div class="val"><c:out value="${history.workshop_date}" /></div>
				</div>
				<div class="row">
					<div class="key">時間</div>
					<div class="val"><c:out value="${fn:substring(history.start_time, 0, 5)}" /></div>
				</div>
				<div class="row">
					<div class="key">代表者名</div>
					<div class="val"><c:out value="${history.guest_name}" /></div>
				</div>
				<div class="row">
					<div class="key">参加人数</div>
					<div class="val"><c:out value="${history.num_people}" /> 名</div>
				</div>
			</div>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/reservation/delete/complete">
				<input type="hidden" name="id" value="${history.book_id}">
				<div class="btn-row">
					<button type="submit" class="btn btn-danger">削除する</button>
					<a class="btn btn-secondary"
						href="${pageContext.request.contextPath}/admin/reservation">戻る</a>
				</div>
			</form>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

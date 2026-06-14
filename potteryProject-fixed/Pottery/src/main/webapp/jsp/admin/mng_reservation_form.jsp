<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="active" value="reservation" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>予約編集 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">予約編集</h1>
		<p class="admin-page-desc">代表者名と参加人数を編集できます。人数を変更すると残席数も自動で調整されます。</p>

		<c:if test="${not empty error}">
			<div class="alert alert-error"><c:out value="${error}" /></div>
		</c:if>

		<div class="admin-card">
			<div class="confirm-list" style="margin-bottom: 22px;">
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
			</div>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/reservation/edit/confirm">
				<input type="hidden" name="id" value="${reservation.book_id}">

				<div class="form-row">
					<label for="guest_name">代表者名</label>
					<input type="text" id="guest_name" name="guest_name"
						class="form-control" value="${fn:escapeXml(reservation.guest_id)}">
				</div>

				<div class="form-row">
					<label for="num_people">参加人数</label>
					<input type="number" id="num_people" name="num_people" min="1"
						class="form-control" value="${reservation.num_people}">
				</div>

				<div class="btn-row">
					<button type="submit" class="btn btn-primary">確認</button>
					<a class="btn btn-secondary"
						href="${pageContext.request.contextPath}/admin/reservation">戻る</a>
				</div>
			</form>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="active" value="workshop" />
<c:set var="isEdit" value="${mode == 'edit'}" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>ワークショップ${isEdit ? '編集' : '追加'}確認 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">ワークショップ${isEdit ? '編集' : '追加'}確認</h1>
		<p class="admin-page-desc">以下の内容で${isEdit ? '更新' : '登録'}します。よろしいですか？</p>

		<div class="admin-card">
			<div class="confirm-list">
				<div class="row">
					<div class="key">コース</div>
					<div class="val"><c:out value="${courseName}" /></div>
				</div>
				<div class="row">
					<div class="key">開催日</div>
					<div class="val"><c:out value="${workshop_date}" /></div>
				</div>
				<div class="row">
					<div class="key">開始時刻</div>
					<div class="val"><c:out value="${start_time}" /></div>
				</div>
				<div class="row">
					<div class="key">定員（残席数）</div>
					<div class="val"><c:out value="${capacity}" /></div>
				</div>
			</div>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/workshop/${isEdit ? 'edit' : 'add'}/complete">
				<c:if test="${isEdit}">
					<input type="hidden" name="id" value="${id}">
				</c:if>
				<input type="hidden" name="courseId" value="${courseId}">
				<input type="hidden" name="workshop_date" value="${workshop_date}">
				<input type="hidden" name="start_time" value="${start_time}">
				<input type="hidden" name="capacity" value="${capacity}">
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

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="active" value="info" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>お知らせ削除確認 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">お知らせ削除確認</h1>
		<p class="admin-page-desc">以下のお知らせを削除しますか？</p>

		<div class="admin-card">
			<div class="confirm-list">
				<div class="row">
					<div class="key">タイトル</div>
					<div class="val"><c:out value="${info.title}" /></div>
				</div>
				<div class="row">
					<div class="key">メッセージ</div>
					<div class="val"><c:out value="${info.content}" /></div>
				</div>
				<div class="row">
					<div class="key">投稿日</div>
					<div class="val"><c:out value="${info.post_date}" /></div>
				</div>
			</div>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/info/delete/complete">
				<input type="hidden" name="id" value="${info.info_id}">
				<div class="btn-row">
					<button type="submit" class="btn btn-danger">削除する</button>
					<a class="btn btn-secondary"
						href="${pageContext.request.contextPath}/admin/info">戻る</a>
				</div>
			</form>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

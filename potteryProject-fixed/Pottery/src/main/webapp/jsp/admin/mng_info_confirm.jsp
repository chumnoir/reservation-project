<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="active" value="info" />
<c:set var="isEdit" value="${mode == 'edit'}" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>お知らせ${isEdit ? '編集' : '追加'}確認 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">お知らせ${isEdit ? '編集' : '追加'}確認</h1>
		<p class="admin-page-desc">以下の内容で${isEdit ? '更新' : '登録'}します。よろしいですか？</p>

		<div class="admin-card">
			<div class="confirm-list">
				<div class="row">
					<div class="key">タイトル</div>
					<div class="val"><c:out value="${title}" /></div>
				</div>
				<div class="row">
					<div class="key">メッセージ</div>
					<div class="val"><c:out value="${content}" /></div>
				</div>
				<div class="row">
					<div class="key">投稿日</div>
					<div class="val"><c:out value="${empty post_date ? '（今日の日付）' : post_date}" /></div>
				</div>
			</div>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/info/${isEdit ? 'edit' : 'add'}/complete">
				<c:if test="${isEdit}">
					<input type="hidden" name="id" value="${id}">
				</c:if>
				<input type="hidden" name="title" value="${fn:escapeXml(title)}">
				<input type="hidden" name="content" value="${fn:escapeXml(content)}">
				<input type="hidden" name="post_date" value="${post_date}">
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

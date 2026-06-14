<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="active" value="member" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>会員削除確認 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">会員情報削除確認</h1>
		<p class="admin-page-desc">以下の会員情報を削除しますか？この会員の予約も併せて削除されます。</p>

		<div class="admin-card">
			<div class="confirm-list">
				<div class="row">
					<div class="key">氏名</div>
					<div class="val"><c:out value="${member.user_name}" /></div>
				</div>
				<div class="row">
					<div class="key">メールアドレス</div>
					<div class="val"><c:out value="${member.email}" /></div>
				</div>
				<div class="row">
					<div class="key">電話番号</div>
					<div class="val"><c:out value="${member.phone_number}" /></div>
				</div>
				<div class="row">
					<div class="key">住所</div>
					<div class="val"><c:out value="${member.address}" /></div>
				</div>
			</div>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/member/delete/complete">
				<input type="hidden" name="id" value="${member.user_id}">
				<div class="btn-row">
					<button type="submit" class="btn btn-danger">削除する</button>
					<a class="btn btn-secondary"
						href="${pageContext.request.contextPath}/admin/member">戻る</a>
				</div>
			</form>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

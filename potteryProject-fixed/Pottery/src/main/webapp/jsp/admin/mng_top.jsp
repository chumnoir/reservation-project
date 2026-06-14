<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="active" value="top" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>管理者トップ - 陶筋工房</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">管理者トップ</h1>
		<p class="admin-page-desc">
			ようこそ、<c:out value="${sessionScope.adminUser.admin_name}" /> さん。管理する項目を選んでください。
		</p>

		<div class="admin-menu-grid">
			<a class="admin-menu-card"
				href="${pageContext.request.contextPath}/admin/workshop">
				<div class="menu-title">ワークショップ管理</div>
				<div class="menu-desc">開催枠の追加・編集・削除を行います。</div>
			</a>
			<a class="admin-menu-card"
				href="${pageContext.request.contextPath}/admin/reservation">
				<div class="menu-title">予約管理</div>
				<div class="menu-desc">予約の確認・編集・削除を行います。</div>
			</a>
			<a class="admin-menu-card"
				href="${pageContext.request.contextPath}/admin/info">
				<div class="menu-title">お知らせ管理</div>
				<div class="menu-desc">お知らせの追加・編集・削除を行います。</div>
			</a>
			<a class="admin-menu-card"
				href="${pageContext.request.contextPath}/admin/course">
				<div class="menu-title">コース管理</div>
				<div class="menu-desc">コースの追加・編集・削除を行います。</div>
			</a>
			<a class="admin-menu-card"
				href="${pageContext.request.contextPath}/admin/member">
				<div class="menu-title">会員管理</div>
				<div class="menu-desc">会員情報の確認・削除を行います。</div>
			</a>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>

</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>ログイン - 陶筋工房</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">

<style>
/* ログイン画面専用の追加スタイル */
.login-container {
	max-width: 460px;
	margin: 60px auto;
	padding: 40px;
	background-color: #fffdf8;
	border: 1px solid #ead7bd;
	border-radius: 12px;
	box-shadow: 0 4px 20px rgba(111, 73, 38, 0.06);
	box-sizing: border-box;
}

.login-title {
	font-size: 24px;
	font-weight: bold;
	color: #6b4423;
	text-align: center;
	margin-bottom: 25px;
	letter-spacing: 0.05em;
}

.form-group {
	margin-bottom: 20px;
}

.form-label {
	display: block;
	font-size: 15px;
	font-weight: bold;
	color: #5f3f25;
	margin-bottom: 8px;
}

.form-input {
	width: 100%;
	padding: 12px;
	font-size: 16px;
	border: 1px solid #d9b98f;
	border-radius: 6px;
	background-color: #fffaf3;
	color: #5f3f25;
	box-sizing: border-box;
	transition: border-color 0.2s, box-shadow 0.2s;
}

.form-input:focus {
	outline: none;
	border-color: #b96b32;
	box-shadow: 0 0 0 3px rgba(185, 107, 50, 0.15);
}

/* エラーメッセージ */
.error-box {
	background-color: #fff0f0;
	border: 1px solid #ffc0c0;
	color: #c02020;
	padding: 12px 16px;
	border-radius: 6px;
	font-size: 14px;
	margin-bottom: 20px;
	line-height: 1.5;
}

/* 下部リンクエリア */
.login-footer-links {
	margin-top: 25px;
	padding-top: 15px;
	border-top: 1px dashed #ead7bd;
	text-align: center;
	display: flex;
	flex-direction: column;
	gap: 12px;
}

.link-accent {
	color: #b96b32;
	font-weight: bold;
	text-decoration: none;
	font-size: 15px;
}

.link-accent:hover {
	text-decoration: underline;
}

.link-sub {
	color: #7a5738;
	text-decoration: none;
	font-size: 14px;
	cursor: pointer;
}

.link-sub:hover {
	text-decoration: underline;
}
</style>
</head>

<body>

	<header class="system-header">
		<div class="header-left">
			<c:choose>
				<c:when test="${not empty redirect}">
					<a onclick="history.back();" class="header-logo"
						style="cursor: pointer;">陶筋工房</a>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/user/main"
						class="header-logo">陶筋工房</a>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${not empty redirect}">
					<a onclick="history.back();" class="header-link-btn"
						style="cursor: pointer;">ワークショップ予約</a>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/user/workshops"
						class="header-link-btn">ワークショップ予約</a>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="header-right">
			<a
				href="${pageContext.request.contextPath}/user/register?redirect=${redirect}"
				class="header-text-link">新規登録</a>
		</div>
	</header>

	<div class="page"
		style="background: none; border: none; box-shadow: none; width: auto; margin: 0;">

		<div class="login-container">
			<h1 class="login-title">ログイン</h1>

			<c:if test="${not empty error}">
				<div class="error-box">
					<c:out value="${error}" />
				</div>
			</c:if>

			<form action="${pageContext.request.contextPath}/user/login"
				method="post">
				<input type="hidden" name="redirect" value="${redirect}">

				<div class="form-group">
					<label for="email" class="form-label">メールアドレス</label> <input
						type="email" id="email" name="email" value="${email}"
						class="form-input" placeholder="example@pottery.com"
						[cite_start]required>
				</div>

				<div class="form-group" style="margin-bottom: 28px;">
					<label for="password" class="form-label">パスワード</label> <input
						type="password" id="password" name="password" class="form-input"
						placeholder="••••••••" [cite_start]required> 
				</div>

				<button type="submit" class="btn-calendar-link"
					style="width: 100%; border: none; cursor: pointer; font-size: 16px;">
					ログインする</button>
			</form>

			<div class="login-footer-links">
				<div>
					<a
						href="${pageContext.request.contextPath}/user/register?redirect=${redirect}"
						class="link-accent"> 新規登録はこちら </a>
				</div>
				<div>
					<c:choose>
						<c:when test="${not empty redirect}">
							<a onclick="history.back();" class="link-sub"> 戻る </a>
						</c:when>
						<c:otherwise>
							<a href="${pageContext.request.contextPath}/user/main"
								class="link-sub">トップへ戻る </a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>

	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
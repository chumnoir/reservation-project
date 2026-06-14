<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>会員登録完了 - 陶筋工房</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">

<style>
/* 登録完了画面専用のコンテナ */
.complete-container {
	max-width: 520px;
	margin: 60px auto;
	padding: 50px 40px;
	background-color: #fffdf8;
	border: 1px solid #ead7bd;
	border-radius: 12px;
	box-shadow: 0 4px 20px rgba(111, 73, 38, 0.06);
	box-sizing: border-box;
	text-align: center;
}

/* 成功を祝う和モダンのアクセント */
.complete-icon {
	font-size: 48px;
	color: #b96b32;
	margin-bottom: 20px;
}

.complete-title {
	font-size: 24px;
	font-weight: bold;
	color: #6b4423;
	margin-bottom: 12px;
	letter-spacing: 0.05em;
}

.complete-message {
	font-size: 15px;
	color: #5f3f25;
	line-height: 1.8;
	margin-bottom: 35px;
}

/* 💡 ボタンが大きすぎないようにサイズを制限し、カードと同じ綺麗な緩い丸角にします */
.btn-complete-submit {
	display: inline-block;
	background: linear-gradient(135deg, #d88942, #b96b32);
	color: #fffaf3 !important;
	border: 1px solid #a85f2e;
	padding: 12px 32px;
	font-size: 16px;
	font-weight: bold;
	text-decoration: none;
	border-radius: 6px; /* 💡 カプセル型をやめて、6pxの綺麗な和モダン角丸に */
	box-shadow: 0 4px 12px rgba(185, 107, 50, 0.15);
	transition: background-color 0.2s, transform 0.1s;
	min-width: 200px; /* 💡 程よい存在感の横幅に制限 */
}

.btn-complete-submit:hover {
	background: linear-gradient(135deg, #e1984f, #c77739);
	transform: translateY(-1px);
}
</style>
</head>

<body>

	<header class="system-header">
		<div class="header-left">
			<a href="${pageContext.request.contextPath}/user/main"
				class="header-logo">陶筋工房</a> <a
				href="${pageContext.request.contextPath}/user/workshops"
				class="header-link-btn">ワークショップ予約</a>
		</div>
		<div class="header-right">
			<a href="${pageContext.request.contextPath}/user/login"
				class="header-text-link">ログイン</a>
		</div>
	</header>

	<div class="page"
		style="background: none; border: none; box-shadow: none; width: auto; margin: 0;">

		<div class="complete-container">
			<div class="complete-icon">🏺</div>

			<h1 class="complete-title">会員登録完了</h1>

			<p class="complete-message">
				会員登録が完了いたしました！ <br> 下のボタンよりログインし、予約画面へお進みください。</br>
			</p>

			<c:choose>
				<c:when test="${not empty param.redirect}">
					<a
						href="${pageContext.request.contextPath}/user/login?redirect=${param.redirect}"
						class="btn-complete-submit"> ログイン画面へ進む </a>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/user/login"
						class="btn-complete-submit"> ログイン画面へ進む </a>
				</c:otherwise>
			</c:choose>
		</div>

	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
<%@page import="dto.User"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
User user = (User) session.getAttribute("loginUser");
// 万が一セッションが切れていた場合の安全対策
if (user == null) {
	response.sendRedirect(request.getContextPath() + "/user/login");
	return;
}
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>マイページ - 陶筋工房</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">

<style>
/* マイページ専用コンテナ */
.mypage-container {
	max-width: 700px;
	margin: 40px auto;
	padding: 40px;
	background-color: #fffdf8;
	border: 1px solid #ead7bd;
	border-radius: 12px;
	box-shadow: 0 4px 20px rgba(111, 73, 38, 0.06);
	box-sizing: border-box;
}

.mypage-title {
	font-size: 24px;
	font-weight: bold;
	color: #6b4423;
	text-align: center;
	margin-bottom: 30px;
	letter-spacing: 0.05em;
	border-bottom: 2px solid #f4e3cf;
	padding-bottom: 12px;
}

/* ようこそカード */
.welcome-box {
	background-color: #fff4e5;
	border: 1px solid #f5e2cb;
	border-radius: 8px;
	padding: 20px;
	margin-bottom: 35px;
}

.welcome-text {
	font-size: 16px;
	color: #5f3f25;
	margin: 0;
	text-align: center;
}

.welcome-text strong {
	font-size: 20px;
	color: #b96b32;
	margin-right: 4px;
}

/* メニュー一覧（縦並びのカードリンク風） */
.mypage-menu-list {
	display: flex;
	flex-direction: column;
	gap: 16px;
	margin-bottom: 30px;
}

.menu-item-form {
	margin: 0;
	padding: 0;
}

/* メニューボタン */
.btn-mypage-menu {
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 20px 24px;
	background-color: #ffffff;
	border: 1px solid #ead7bd;
	border-radius: 8px;
	color: #6b4423;
	font-size: 16px;
	font-weight: bold;
	cursor: pointer;
	text-align: left;
	transition: all 0.2s ease;
	box-shadow: 0 2px 6px rgba(111, 73, 38, 0.02);
}

.btn-mypage-menu:hover {
	background-color: #fffbf5;
	border-color: #d88942;
	transform: translateY(-1.5px);
	box-shadow: 0 4px 12px rgba(111, 73, 38, 0.08);
}

.menu-label {
	display: flex;
	align-items: center;
}

.menu-arrow {
	color: #b96b32;
	font-size: 14px;
	transition: transform 0.2s;
}

.btn-mypage-menu:hover .menu-arrow {
	transform: translateX(4px);
}

/* 下部リンクエリア */
.mypage-footer-links {
	text-align: center;
	margin-top: 35px;
	padding-top: 25px;
	border-top: 1px dashed #ead7bd;
}

/* 戻るボタン */
.btn-back-to-main {
	display: inline-block;
	min-width: 200px;
	padding: 12px 24px;
	font-size: 15px;
	font-weight: bold;
	text-decoration: none;
	box-sizing: border-box;
	border-radius: 6px;
	transition: background-color 0.2s, transform 0.1s;
	color: #7a5738;
	border: 1px solid #d9b98f;
	background-color: #fff8ee;
}

.btn-back-to-main:hover {
	background-color: #f3e2cf;
	transform: translateY(-1px);
}

.btn-back-to-main:active {
	transform: scale(0.98);
}

/* 💡 ヘッダーのログアウトボタン用の微調整 */
.header-logout-link {
	color: #f4e3cf !important;
	opacity: 0.8;
}

.header-logout-link:hover {
	opacity: 1;
	color: #ffebd2 !important;
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
			<span class="header-user-welcome">ようこそ、<%=user.getUser_name()%>
				様
			</span> 
			 <a
				href="${pageContext.request.contextPath}/user/logout"
				class="header-text-link header-logout-link">ログアウト</a>
		</div>
	</header>

	<div class="page"
		style="background: none; border: none; box-shadow: none; width: auto; margin: 0;">

		<div class="mypage-container">
			<h1 class="mypage-title">マイページ</h1>

			<div class="welcome-box">
				<p class="welcome-text">
					ようこそ、<strong><%=user.getUser_name()%></strong> さん。
				</p>
			</div>

			<div class="mypage-menu-list">

				<form action="<%=request.getContextPath()%>/user/reservation/list"
					method="get" class="menu-item-form">
					<button type="submit" class="btn-mypage-menu">
						<span class="menu-label"> ワークショップ申し込み履歴・キャンセル </span> 
					</button>
				</form>

				<form action="<%=request.getContextPath()%>/user/info/change"
					method="get" class="menu-item-form">
					<button type="submit" class="btn-mypage-menu">
						<span class="menu-label"> 会員情報の編集 </span>
					</button>
				</form>

			</div>

			<div class="mypage-footer-links">
				<a href="<%=request.getContextPath()%>/user/main"
					class="btn-back-to-main"> トップへ戻る </a>
			</div>
		</div>

	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
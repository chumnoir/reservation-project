<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
// セッションからユーザー情報を取得（ヘッダー表示用）
dto.User user = (dto.User) session.getAttribute("loginUser");
if (user == null) {
	response.sendRedirect(request.getContextPath() + "/user/login");
	return;
}
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>会員情報変更完了 - 陶筋工房</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">

<style>
/* 💡 キャンセル完了画面（max-width: 680px, #fffdf8）とデザインを完全同期 */
.complete-container {
	max-width: 680px;
	margin: 60px auto;
	padding: 50px 40px;
	background-color: #fffdf8; /* 統一された優しく暖かみのある白 */
	border: 1px solid #ead7bd; /* 完全に同じ色の茶色枠線 */
	border-radius: 12px;
	box-shadow: 0 4px 20px rgba(111, 73, 38, 0.06);
	box-sizing: border-box;
	text-align: center;
}

/* 和モダンのアクセントアイコン */
.complete-icon {
	font-size: 48px;
	color: #b96b32;
	margin-bottom: 20px;
}

.complete-title {
	font-size: 24px;
	font-weight: bold;
	color: #6b4423;
	margin-bottom: 16px;
	letter-spacing: 0.05em;
}

.complete-message {
	font-size: 16px;
	color: #5f3f25;
	line-height: 1.8;
	margin-bottom: 35px;
}
</style>
</head>

<body>

	<header class="system-header">
		<div class="header-left">
			<a href="${pageContext.request.contextPath}/user/main"
				class="header-logo">陶筋工房</a> <a
				href="${pageContext.request.contextPath}/user/workshops"
				class="header-link-btn">ワークショップ一覧</a>
		</div>
		<div class="header-right">
			<span class="header-user-welcome">ようこそ、<%=user.getUser_name()%>
				様
			</span> <a href="${pageContext.request.contextPath}/user/logout"
				class="header-text-link"
				style="text-decoration: underline !important;">ログアウト</a>
		</div>
	</header>

	<div class="page"
		style="background: none; border: none; box-shadow: none; width: auto; margin: 0;">

		<div class="complete-container">
			<div class="complete-icon">✓</div>

			<h1 class="complete-title">会員情報変更完了</h1>

			<p class="complete-message">会員情報の変更手続きが完了いたしました。</p>

			<div class="button-row" style="margin-top: 35px;">

				<a class="btn-link btn-negative"
					href="${pageContext.request.contextPath}/user/mypage"> マイページへ戻る
				</a>

			</div>
		</div>

	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
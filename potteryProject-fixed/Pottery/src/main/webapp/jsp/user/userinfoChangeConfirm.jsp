<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page import="dto.User"%>
<%
// セッションからユーザー情報を取得（ヘッダー表示用）
dto.User user = (dto.User) session.getAttribute("loginUser");
if (user == null) {
	response.sendRedirect(request.getContextPath() + "/user/login");
	return;
}

// セッションから変更対象のユーザー情報を取得
User changeUser = (User) session.getAttribute("changeUser");
if (changeUser == null) {
	response.sendRedirect(request.getContextPath() + "/user/info/change");
	return;
}
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>会員情報変更確認 - 陶筋工房</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/info-shared.css">

<style>
/* 💡 他の確認画面（680px）と色・サイズ・影・角丸を100%完全に同期 */
.confirm-main-box {
	max-width: 680px;
	margin: 40px auto;
	padding: 40px;
	background-color: #fffdf8; /* 統一された暖かみのある白 */
	border: 1px solid #ead7bd; /* 土壁カラーの枠線 */
	border-radius: 12px;
	box-shadow: 0 4px 20px rgba(111, 73, 38, 0.06);
	box-sizing: border-box;
}

.confirm-title {
	font-size: 24px;
	font-weight: bold;
	color: #6b4423;
	text-align: center;
	margin-bottom: 8px;
	letter-spacing: 0.05em;
}

.confirm-subtitle {
	font-size: 14px;
	color: #8a6a4e;
	text-align: center;
	margin-bottom: 30px;
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

		<div class="confirm-main-box">
			<h1 class="confirm-title">会員情報変更確認</h1>
			<p class="confirm-subtitle">以下の内容で変更を確定します。よろしければ「この情報で登録する」ボタンを押してください。</p>

			<table class="common-table">
				<tr>
					<th>氏名</th>
					<td style="font-weight: bold; color: #6b4423;"><%=changeUser.getUser_name()%></td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td><%=changeUser.getEmail()%></td>
				</tr>
				<tr>
					<th>パスワード</th>
					<td
						style="font-family: monospace; letter-spacing: 0.1em; color: #8a6a4e;">********</td>
				</tr>
				<tr>
					<th>電話番号</th>
					<td><%=changeUser.getPhone_number()%></td>
				</tr>
				<tr>
					<th>住所</th>
					<td><%=changeUser.getAddress()%></td>
				</tr>
			</table>

			<div class="button-row" style="margin-top: 35px;">

				<form
					action="<%=request.getContextPath()%>/user/info/change/complete"
					method="post">
					<button type="submit" class="btn-positive">この情報で登録する</button>
				</form>

				<a class="btn-link btn-negative"
					href="<%=request.getContextPath()%>/user/info/change"> 前画面に戻る
				</a>

			</div>
		</div>

	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>登録内容確認 - 陶筋工房</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/info-shared.css">

<style>
/* 確認画面全体のコンテナ微調整 */
.confirm-container {
	max-width: 680px;
	margin: 40px auto;
	padding: 40px;
	background-color: #fffdf8;
	border: 1px solid #ead7bd;
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
	color: #7a5738;
	text-align: center;
	margin-bottom: 25px;
}

/* ボタンエリアを横並びにするスタイリング */
.action-button-group {
	display: flex;
	gap: 20px;
	max-width: 650px;
	margin: 30px auto 0 auto;
}

.action-button-group form {
	flex: 1;
	margin: 0;
}

.action-button-group button {
	width: 100%;
	padding: 14px;
	font-size: 16px;
	font-weight: bold;
	border-radius: 6px;
	cursor: pointer;
	box-sizing: border-box;
	transition: background-color 0.2s, transform 0.1s;
}

.action-button-group button:active {
	transform: scale(0.98);
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
			<a href="${pageContext.request.contextPath}/user/login"
				class="header-text-link">ログイン</a>
		</div>
	</header>

	<div class="page"
		style="background: none; border: none; box-shadow: none; width: auto; margin: 0;">

		<div class="confirm-container">
			<h1 class="confirm-title">登録内容確認</h1>
			<p class="confirm-subtitle">入力された内容に間違いがないか、ご確認ください。</p>

			<table class="common-table">
				<tr>
					<th>名前</th>
					<td><c:out value="${name}" /></td>
				</tr>
				<tr>
					<th>住所</th>
					<td><c:out value="${address}" /></td>
				</tr>
				<tr>
					<th>電話番号</th>
					<td><c:out value="${phone}" /></td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td><c:out value="${email}" /></td>
				</tr>
				<tr>
					<th>パスワード</th>
					<td
						style="font-family: monospace; letter-spacing: 0.1em; color: #8a6a4e;">********</td>
				</tr>
			</table>

			<div class="action-button-group">

				<form
					action="${pageContext.request.contextPath}/user/register/complete"
					method="post">
					<input type="hidden" name="name" value="<c:out value='${name}' />">
					<input type="hidden" name="address"
						value="<c:out value='${address}' />"> <input type="hidden"
						name="phone_number" value="<c:out value='${phone}' />"> <input
						type="hidden" name="email" value="<c:out value='${email}' />">
					<input type="hidden" name="password"
						value="<c:out value='${password}' />"> <input
						type="hidden" name="redirect"
						value="<c:out value='${redirect}' />">

					<button type="submit" class="btn-positive">この内容で登録する</button>
				</form>
				<form
					action="${pageContext.request.contextPath}/user/register/confirm"
					method="get">
					<button type="submit" class="btn-negative">入力内容を修正する</button>
				</form>
			</div>
		</div>

	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
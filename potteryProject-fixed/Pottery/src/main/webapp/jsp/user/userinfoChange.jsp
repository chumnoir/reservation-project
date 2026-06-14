<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.User"%>

<%
User user = (User) request.getAttribute("loginUser");
if (user == null) {
	user = (User) session.getAttribute("loginUser");
}

if (user == null) {
	response.sendRedirect(request.getContextPath() + "/user/login");
	return;
}

String error = (String) request.getAttribute("error");
String userName = (String) request.getAttribute("user_name");
String email = (String) request.getAttribute("user_email");
String password = (String) request.getAttribute("password");
String telephone = (String) request.getAttribute("telephone");
String address = (String) request.getAttribute("address");

if (userName == null) {
	userName = user.getUser_name();
}
if (email == null) {
	email = user.getEmail();
}
if (password == null) {
	password = user.getPassword();
}
if (telephone == null) {
	telephone = user.getPhone_number();
}
if (address == null) {
	address = user.getAddress();
}

if (userName == null)
	userName = "";
if (email == null)
	email = "";
if (password == null)
	password = "";
if (telephone == null)
	telephone = "";
if (address == null)
	address = "";
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>会員情報編集</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/info-shared.css">
<style>
.common-table input[type="text"], .common-table input[type="email"],
	.common-table input[type="password"] {
	width: 100%;
	box-sizing: border-box;
	padding: 10px 14px;
	border: 1px solid #d9b98f;
	background-color: #fffdf8;
	border-radius: 6px;
	color: #5f3f25;
	font-size: 16px;
	transition: all 0.2s ease;
}

.common-table input:focus {
	outline: none;
	border-color: #d88942;
	box-shadow: 0 0 0 3px rgba(216, 137, 66, 0.15);
}
</style>
</head>
<body>

	<div class="header">陶芸教室 予約システム</div>

	<div class="page">
		<h2 class="page-title">会員情報編集</h2>

		<div class="content">
			<%
			if (error != null && !error.isEmpty()) {
			%>
			<p class="confirm-message" style="color: #c85f50;"><%=error%></p>
			<%
			}
			%>

			<form
				action="<%=request.getContextPath()%>/user/info/change/confirm"
				method="post" style="width: 100%; max-width: 650px;">
				<table class="common-table">
					<tr>
						<th>氏名</th>
						<td><input type="text" name="user_name"
							value="<%=userName%>" required></td>
					</tr>
					<tr>
						<th>メールアドレス</th>
						<td><input type="email" name="user_email"
							value="<%=email%>" required></td>
					</tr>
					<tr>
						<th>パスワード</th>
						<td><input type="password" name="password"
							value="<%=password%>" required></td>
					</tr>
					<tr>
						<th>電話番号</th>
						<td><input type="text" name="telephone"
							value="<%=telephone%>" required></td>
					</tr>
					<tr>
						<th>住所</th>
						<td><input type="text" name="address" value="<%=address%>"
							required></td>
					</tr>
				</table>

				<div class="button-row">
					<button type="submit" class="btn-positive">確認画面へ</button>
					<a href="<%=request.getContextPath()%>/user/mypage"
						class="btn-negative btn-link">マイページへ戻る</a>
				</div>
			</form>
		</div>
	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
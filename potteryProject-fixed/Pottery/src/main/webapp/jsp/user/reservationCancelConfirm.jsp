<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page import="dto.ReservationHistory"%>
<%
// セッションからユーザー情報を取得（ヘッダー表示用）
dto.User user = (dto.User) session.getAttribute("loginUser");
if (user == null) {
	response.sendRedirect(request.getContextPath() + "/user/login");
	return;
}

// リクエストから予約詳細データを取得
ReservationHistory r = (ReservationHistory) request.getAttribute("reservation");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>予約キャンセル確認 - 陶筋工房</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/info-shared.css">

<style>
/* 💡 確認画面専用：中央の確認コンテナのサイズと余白のみを定義 */
.confirm-main-box {
	max-width: 680px;
	margin: 40px auto;
	padding: 40px;
	background-color: #fffdf8;
	border: 1px solid #ead7bd;
	border-radius: 12px;
	box-shadow: 0 4px 20px rgba(111, 73, 38, 0.06);
	box-sizing: border-box;
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
			<h1 class="page-title" style="margin-bottom: 10px;">ご予約のキャンセル確認</h1>
			<p class="confirm-message"
				style="text-align: center; color: #7a5738; margin-bottom: 30px;">
				選択した内容をキャンセルしますか？</p>

			<%
			if (r != null) {
			%>
			<table class="common-table">
				<tr>
					<th>コース名</th>
					<td style="font-weight: bold; color: #6b4423;"><%=r.getCourse_name()%></td>
				</tr>
				<tr>
					<th>開催日</th>
					<td><%=r.getWorkshop_date()%></td>
				</tr>
				<tr>
					<th>開始時間</th>
					<td><%=r.getStart_time()%></td>
				</tr>
				<tr>
					<th>代表者名</th>
					<td><%=r.getGuest_name()%></td>
				</tr>
				<tr>
					<th>人数</th>
					<td><%=r.getNum_people()%>名</td>
				</tr>
				<tr>
					<th>合計金額</th>
					<td style="font-weight: bold; color: #b96b32;"><%=r.getTotal_price()%>円</td>
				</tr>
			</table>

			<div class="button-row" style="margin-top: 35px;">

				<form
					action="<%=request.getContextPath()%>/user/reservation/cancel/complete"
					method="post">
					<input type="hidden" name="bookId" value="<%=r.getBook_id()%>">
					<button type="submit" class="btn-positive">キャンセル確定</button>
				</form>

				<a class="btn-link btn-negative"
					href="<%=request.getContextPath()%>/user/reservation/list"> 戻る
				</a>

			</div>
			<%
			} else {
			%>
			<p class="error-message"
				style="text-align: center; color: #c0392b; padding: 40px 0;">
				対象の予約データが見つかりませんでした。</p>
			<div class="button-row">
				<a class="btn-link btn-negative"
					href="<%=request.getContextPath()%>/user/reservation/list">戻る</a>
			</div>
			<%
			}
			%>
		</div>

	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
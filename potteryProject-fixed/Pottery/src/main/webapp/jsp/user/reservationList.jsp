<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%
// セッションからユーザー情報を取得
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
<title>ワークショップ申し込み状況 - 陶筋工房</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">

<style>
/* 予約履歴画面専用：テーブルの枠組み配置のみをスマートに定義 */
.reservation-table {
	width: 100%;
	border-collapse: separate;
	border-spacing: 0;
	border: 1px solid #ead7bd;
	border-radius: 8px;
	overflow: hidden;
	margin-bottom: 24px;
}

.reservation-table th, .reservation-table td {
	padding: 16px 12px;
	font-size: 15px;
	border-bottom: 1px solid #f4e3cf;
	border-right: 1px solid #f4e3cf;
	text-align: center;
	vertical-align: middle;
}

.reservation-table th:last-child, .reservation-table td:last-child {
	border-right: none;
}

.reservation-table tr:last-child th, .reservation-table tr:last-child td
	{
	border-bottom: none;
}

.reservation-table th {
	background-color: #fff4e5;
	color: #6b4423;
	font-weight: bold;
}

.reservation-table td {
	background-color: #ffffff;
	color: #5f3f25;
}

.reservation-table input[type="radio"] {
	transform: scale(1.2);
	cursor: pointer;
}

.empty-message {
	text-align: center;
	color: #7a5738;
	padding: 40px 0;
	font-size: 16px;
}

/* 💡 原則3：目立たせないボタン（枠なし・下線のみ）の定義 */
.header-utility-link {
	color: #f4e3cf !important;
	font-size: 14px;
	text-decoration: underline !important;
	background: none !important;
	border: none !important;
	padding: 6px 12px;
	cursor: pointer;
}

.header-utility-link:hover {
	color: #ffebd2 !important;
	text-decoration: underline !important;
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
			</span>
			 <a href="${pageContext.request.contextPath}/user/logout"
				class="header-utility-link">ログアウト</a>
		</div>
	</header>

	<div class="page">

		<h1 class="page-title">ワークショップお申し込み状況</h1>

		<div class="content" style="padding-top: 40px;">
			<c:choose>
				<%-- 予約がない場合の表示 --%>
				<c:when test="${empty reservationList}">
					<p class="empty-message">現在、お申し込み中のワークショップはありません。</p>
					<div class="button-row">
						<a class="btn-link btn-negative"
							href="${pageContext.request.contextPath}/user/mypage">戻る</a>
					</div>
				</c:when>

				<%-- 予約がある場合の表示 --%>
				<c:otherwise>
					<form
						action="${pageContext.request.contextPath}/user/reservation/cancel/confirm"
						method="post" style="width: 100%;">

						<table class="reservation-table">
							<thead>
								<tr>
									<th style="width: 70px;">選択</th>
									<th>コース名</th>
									<th>開催日</th>
									<th>開始時間</th>
									<th>代表者名</th>
									<th>人数</th>
									<th>合計金額</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="r" items="${reservationList}">
									<tr>
										<td><input type="radio" name="bookId"
											value="${r.book_id}" required></td>
										<td
											style="text-align: left; font-weight: bold; color: #6b4423;">${r.course_name}</td>
										<td>${r.workshop_date}</td>
										<td>${r.start_time}</td>
										<td>${r.guest_name}</td>
										<td>${r.num_people}名</td>
										<td style="font-weight: bold; color: #b96b32;">${r.total_price}円</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

						<div class="button-row">
							<button type="submit" class="btn-positive">予約をキャンセル</button>

							<a class="btn-link btn-negative"
								href="${pageContext.request.contextPath}/user/mypage">戻る</a>
						</div>

					</form>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
String date = (String) request.getAttribute("date");
String time = (String) request.getAttribute("time");
String courseId = (String) request.getAttribute("courseId");
String courseName = (String) request.getAttribute("courseName");
String name = (String) request.getAttribute("name");
String num = (String) request.getAttribute("num");

if (date == null)
	date = "";
if (time == null)
	time = "";
if (courseId == null)
	courseId = "";
if (courseName == null)
	courseName = "";
if (name == null)
	name = "";
if (num == null)
	num = "";
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>予約確認</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/info-shared.css">
</head>

<body>

	<div class="header">陶芸ワークショップ</div>

	<div class="page">
		<h2 class="page-title">予約内容確認</h2>

		<div class="content">

			<p class="confirm-message">選択した内容でよろしいですか？</p>

			<table class="common-table">
				<tr>
					<th>開催日</th>
					<td><%=date%></td>
				</tr>
				<tr>
					<th>時間</th>
					<td><%=time%></td>
				</tr>
				<tr>
					<th>コース</th>
					<td><%=courseName%></td>
				</tr>
				<tr>
					<th>予約代表者名</th>
					<td><%=name%></td>
				</tr>
				<tr>
					<th>人数</th>
					<td><%=num%>名</td>
				</tr>
			</table>

			<form
				action="${pageContext.request.contextPath}/user/reservation/complete"
				method="post">

				<input type="hidden" name="date" value="<%=date%>"> <input
					type="hidden" name="time" value="<%=time%>"> <input
					type="hidden" name="courseId" value="<%=courseId%>"> <input
					type="hidden" name="name" value="<%=name%>"> <input
					type="hidden" name="num" value="<%=num%>">

				<div class="button-row">
					<button type="submit" class="btn-positive">予約を確定する</button>

					<button type="button" class="btn-negative" onclick="history.back()">戻る</button>

				</div>
			</form>

		</div>
	</div>

	<footer class="footer">陶芸工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
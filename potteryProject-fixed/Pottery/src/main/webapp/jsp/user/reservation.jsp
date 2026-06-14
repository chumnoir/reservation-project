<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page import="java.util.List"%>
<%@ page import="java.time.LocalTime"%>

<%
String errorMessage = (String) request.getAttribute("error");

List<LocalTime> timeList = (List<LocalTime>) request.getAttribute("timeList");
if (timeList == null) {
	timeList = new java.util.ArrayList<>();
}

List<?> courseList = (List<?>) request.getAttribute("courseList");
if (courseList == null) {
	courseList = new java.util.ArrayList<>();
}

// コースごとの残席数（course_id → 残席数）。コース選択画面で赤文字表示に使う
java.util.Map<Long, Integer> capacityMap =
		(java.util.Map<Long, Integer>) request.getAttribute("capacityMap");
if (capacityMap == null) {
	capacityMap = new java.util.HashMap<>();
}

// 選択状態保持
String selectedDate = (String) request.getAttribute("selectedDate");
if (selectedDate == null) {
	selectedDate = request.getParameter("date");
}
if (selectedDate == null) {
	selectedDate = "";
}

String selectedTime = (String) request.getAttribute("selectedTime");
if (selectedTime == null) {
	selectedTime = request.getParameter("time");
}
if (selectedTime == null) {
	selectedTime = "";
}

String selectedCourseId = (String) request.getAttribute("selectedCourseId");
if (selectedCourseId == null) {
	selectedCourseId = request.getParameter("courseId");
}
if (selectedCourseId == null) {
	selectedCourseId = "";
}

String nameValue = request.getParameter("name");
if (nameValue == null) {
	nameValue = "";
}

String numValue = request.getParameter("num");
if (numValue == null) {
	numValue = "";
}
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>コース選択 - 陶筋工房</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/reservation.css">

</head>
<body>

	<header class="system-header">
		<div class="header-left">
			<a href="${pageContext.request.contextPath}/user/main"
				class="header-logo">陶筋工房</a>
		</div>

		<div class="header-right">
			<c:choose>
				<c:when test="${empty sessionScope.loginUser}">
					<a href="${pageContext.request.contextPath}/user/register"
						class="header-text-link">新規登録</a>
					<a href="${pageContext.request.contextPath}/user/login"
						class="btn-header-login">ログイン</a>
				</c:when>
				<c:otherwise>
					<span class="header-user-welcome"> ようこそ、<strong><c:out
								value="${sessionScope.loginUser.user_name}" /></strong> 様
					</span>
					<a href="${pageContext.request.contextPath}/user/mypage"
						class="btn-header-mypage">マイページ</a>
					<a href="${pageContext.request.contextPath}/user/logout"
						class="header-text-link"
						style="text-decoration: underline !important;">ログアウト</a>
				</c:otherwise>
			</c:choose>
		</div>
	</header>

	<div class="page" style="width: 1140px; max-width: 95%;">

		<div class="page-title">コース選択</div>

		<div class="content">

			<%
			if (errorMessage != null && !errorMessage.isEmpty()) {
			%>
			<div class="error-message">
				<%=errorMessage%>
			</div>
			<%
			}
			%>

			<form action="${pageContext.request.contextPath}/user/reservation"
				method="get">
				<div class="main-layout">

					<div class="left-area">
						<div class="section-title">ワークショップ開催日程</div>
						<div class="calendar-area">
							<jsp:include page="/jsp/user/calendar.jsp" />
						</div>
					</div>

					<div class="right-area">

						<div class="choice-frame">
							<div class="choice-title">時間</div>

							<select name="time" class="form-control"
								onchange="this.form.submit()">
								<option value="">選択してください</option>
								<%
								for (LocalTime time : timeList) {
									String timeValue = time.toString();
								%>
								<option value="<%=timeValue%>"
									<%=timeValue.equals(selectedTime) ? "selected" : ""%>>
									<%=timeValue.substring(0, 5)%>
								</option>
								<%
								}
								%>
							</select>

							<%
							if (timeList.isEmpty()) {
							%>
							<div class="note">日付を選択すると時間候補が表示されます。</div>
							<%
							}
							%>
						</div>

						<div class="choice-frame">
							<div class="choice-title">コース</div>

							<%
							if (courseList.isEmpty()) {
							%>
							<div class="note">日付と時間を選択するとコース候補が表示されます。</div>
							<%
							} else {
							for (Object obj : courseList) {
								String courseIdValue = "";
								String courseName = "";
								int price = 0;

								Integer remain = null;

								try {
									courseIdValue = String.valueOf(obj.getClass().getMethod("getCourse_id").invoke(obj));
									courseName = String.valueOf(obj.getClass().getMethod("getCourse_name").invoke(obj));
									price = ((Integer) obj.getClass().getMethod("getPrice").invoke(obj)).intValue();
									remain = capacityMap.get(Long.valueOf(courseIdValue));
								} catch (Exception ex) {
									ex.printStackTrace();
								}

								String remainHtml;
								if (remain == null) {
									remainHtml = "";
								} else if (remain <= 0) {
									remainHtml = "<span style=\"color:#d00000; font-weight:bold; margin-left:8px;\">（満席）</span>";
								} else {
									remainHtml = "<span style=\"color:#d00000; font-weight:bold; margin-left:8px;\">（残り " + remain + " 席）</span>";
								}
							%>
							<label class="choice-option"> <input type="radio"
								name="courseId" value="<%=courseIdValue%>"
								<%=courseIdValue.equals(selectedCourseId) ? "checked" : ""%>>
								<%=courseName%> <%=price%>円<%=remainHtml%>
							</label>
							<%
							}
							}
							%>
						</div>

						<div class="form-block">
							<label for="name">予約代表者名</label> <input type="text" id="name"
								name="name" class="form-control" placeholder="代表者名を入力"
								value="<%=nameValue%>">
						</div>

						<div class="form-block">
							<label for="participantCount">人数</label> <input type="number"
								id="participantCount" name="num" class="form-control" min="1"
								placeholder="人数を入力" value="<%=numValue%>">
						</div>

						<div class="button-row"
							style="margin-top: 40px; justify-content: flex-start;">
							<button type="submit" formmethod="post" class="btn-positive">確認</button>

							<a class="btn-link btn-negative"
								href="${pageContext.request.contextPath}/user/workshops">戻る</a>
						</div>

					</div>

				</div>
			</form>
		</div>

	</div>

	<div class="footer">陶筋工房（Fukuoka） | © 2026 All Rights Reserved.</div>

</body>
</html>
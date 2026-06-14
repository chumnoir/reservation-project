<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>開講ワークショップ一覧 - 陶筋工房</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">
<style>
.workshop-container {
	display: flex;
	flex-direction: column;
	gap: 28px; /* cite: 45 */
	width: 100%;
	max-width: 850px;
	margin: 20px auto 40px auto;
}

.workshop-card {
	background-color: #fffdf8;
	border: 1px solid #ead7bd;
	border-radius: 12px;
	padding: 28px; /* cite: 46 */
	box-shadow: 0 4px 12px rgba(111, 73, 38, 0.05);
	text-align: left;
	width: 100%;
	box-sizing: border-box;
	transition: transform 0.2s; /* cite: 47 */
}

.workshop-card:hover {
	transform: translateY(-2px);
}

.course-title {
	font-size: 22px;
	font-weight: bold;
	color: #6b4423;
	border-bottom: 2px solid #f4e3cf;
	padding-bottom: 10px;
	margin-bottom: 14px;
	letter-spacing: 0.03em; /* cite: 48 */
}

/* 🧱 追加：開催スケジュールエリアの和モダン装飾 */
.schedule-section {
	margin: 20px 0;
	background-color: #fffaf3; /* やわらかい粘土色の背景 */
	border: 1px dashed #dfbd92;
	border-radius: 8px;
	padding: 16px;
}

.schedule-title {
	font-size: 14px;
	font-weight: bold;
	color: #8a6a4e;
	margin-bottom: 10px;
	display: flex;
	align-items: center;
	gap: 6px;
}

.schedule-grid {
	display: flex;
	flex-wrap: wrap;
	gap: 8px;
}

/* スケジュールをボタン/バッジ風に見せるリンク */
.schedule-badge {
	display: inline-flex;
	align-items: center;
	padding: 6px 12px;
	background-color: #ffffff;
	border: 1px solid #ead7bd;
	border-radius: 6px;
	color: #b96b32;
	font-size: 13px;
	font-weight: bold;
	text-decoration: none;
	transition: all 0.15s ease;
}

.schedule-badge:hover {
	background-color: #b96b32;
	color: #ffffff;
	border-color: #a85f2e;
	transform: translateY(-1px);
	box-shadow: 0 2px 6px rgba(185, 107, 50, 0.15);
}
</style>
</head>
<body>

	<header class="system-header">
		<div class="header-left">
			<a href="${pageContext.request.contextPath}/user/main"
				class="header-logo"> 陶筋工房 </a> <a
				href="${pageContext.request.contextPath}/user/reservation"
				class="header-link-btn"> ワークショップ予約 </a>
		</div>

		<div class="header-right">
			<c:choose>
				<c:when test="${empty sessionScope.loginUser}">
					<a href="${pageContext.request.contextPath}/user/register"
						class="header-text-link"> 新規登録 </a>
					<a href="${pageContext.request.contextPath}/user/login"
						class="btn-header-login"> ログイン </a>
				</c:when>
				<c:otherwise>
					<span class="header-user-welcome"> ようこそ、<strong><c:out
								value="${sessionScope.loginUser.user_name}" /></strong> 様
					</span>
					<a href="${pageContext.request.contextPath}/user/mypage"
						class="btn-header-mypage"> マイページ </a>
					<a href="${pageContext.request.contextPath}/user/logout"
						class="header-text-link"
						style="text-decoration: underline !important;"> ログアウト </a>
				</c:otherwise>
			</c:choose>
		</div>
	</header>

	<div class="page">
		<h2 class="page-title">開講ワークショップ一覧</h2>

		<div class="content">
			<div class="calendar-btn-container">
				<a href="${pageContext.request.contextPath}/user/reservation"
					class="btn-calendar-link">予約カレンダーから選ぶ</a>
			</div>

			<c:choose>
				<c:when test="${empty workshops}">
					<p class="confirm-message" style="color: #7a5738;">現在表示できるワークショップはありません。</p>
				</c:when>
				<c:otherwise>
					<div class="workshop-container">

						<c:set var="processedCourseIds" value="," />

						<c:forEach var="w" items="${workshops}">
							<c:set var="currentCourseIdIdStr" value=",${w.course_id}," />

							<c:if
								test="${!fn:contains(processedCourseIds, currentCourseIdIdStr)}">
								<c:set var="processedCourseIds"
									value="${processedCourseIds}${w.course_id}," />

								<div class="workshop-card">

									<div class="course-title">
										<c:out value="${w.course_name}" />
									</div>

									<p
										style="color: #5f3f25; line-height: 1.7; white-space: pre-line; margin-bottom: 16px; font-size: 15px;">
										<c:out value="${w.description}" />
									</p>

									<div class="schedule-section">
										<div class="schedule-title">今月の開講スケジュール（タップしてこの日時で予約）</div>
										<div class="schedule-grid">
											<%-- 💡 判定用に、現在のJavaの「今日の日付(LocalDate)」を取得してリクエスト属性にセット --%>
											<%
											if (request.getAttribute("today") == null) {
												request.setAttribute("today", java.time.LocalDate.now());
											}
											%>

											<c:forEach var="subW" items="${workshops}">
												<c:if test="${subW.course_id == w.course_id}">

													<c:choose>
														<%-- パターン①：すでに開催を終えた過去の日程の場合（loopDate.isBefore(today)） --%>
														<c:when test="${subW.workshop_date.isBefore(today)}">
															<span class="schedule-badge"
																style="background-color: #f0eae1; color: #bfaea0; border-color: #e3d4c5; cursor: not-allowed; opacity: 0.5;">
																<c:out value="${subW.workshop_date}" /> <span
																style="margin-left: 6px; font-weight: normal;"> <c:out
																		value="${fn:substring(subW.start_time, 0, 5)}" />
															</span> <span style="margin-left: 6px; font-size: 11px;">(終了)</span>
															</span>
														</c:when>

														<%-- パターン②：未来の日程だけど、予約枠が埋まっている場合（満席） --%>
														<c:when test="${subW.capacity <= 0}">
															<span class="schedule-badge"
																style="background-color: #eadfd1; color: #a99a8a; border-color: #d9b98f; cursor: not-allowed; opacity: 0.7;">
																<c:out value="${subW.workshop_date}" /> <span
																style="margin-left: 6px; font-weight: normal;"> <c:out
																		value="${fn:substring(subW.start_time, 0, 5)}" />
															</span> <span
																style="margin-left: 6px; color: #c85f50; font-weight: bold;">(満席)</span>
															</span>
														</c:when>

														<%-- パターン③：未来の日程で、通常受付中の場合（残り枠数を表示） --%>
														<c:otherwise>
															<a
																href="${pageContext.request.contextPath}/user/reservation?date=${subW.workshop_date}&time=${subW.start_time}&courseId=${subW.course_id}"
																class="schedule-badge"> <c:out
																	value="${subW.workshop_date}" /> <span
																style="margin-left: 6px; font-weight: normal; opacity: 0.85;">
																	<c:out value="${fn:substring(subW.start_time, 0, 5)}" />〜
															</span> <%-- 残り枠数を小さめのバッジ風に追加 --%> <span
																style="margin-left: 8px; font-size: 11px; background-color: #fff4e5; color: #b96b32; padding: 1px 5px; border-radius: 4px; border: 1px solid #dfbd92;">
																	残り<c:out value="${subW.capacity}" />枠
															</span>
															</a>
														</c:otherwise>
													</c:choose>

												</c:if>
											</c:forEach>
										</div>
									</div>

									<div
										style="display: flex; justify-content: flex-end; gap: 20px; font-size: 15px; color: #7a5738; font-weight: bold; border-top: 1px dashed #ead7bd; padding-top: 12px;">
										<div>
											目安時間：<span style="color: #5f3f25;"><c:out
													value="${w.required_time}" /> 分</span>
										</div>
										<div style="color: #b96b32; font-size: 16px;">
											費用：<span><c:out value="${w.price}" /> 円</span>
										</div>
									</div>

								</div>
							</c:if>
						</c:forEach>

					</div>
				</c:otherwise>
			</c:choose>

			<div class="button-row">
				<a href="${pageContext.request.contextPath}/user/main"
					class="btn-negative btn-link">トップへ戻る</a>
			</div>
		</div>
	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
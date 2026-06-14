<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="active" value="workshop" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>ワークショップ管理 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">ワークショップ管理</h1>
		<p class="admin-page-desc">編集・削除したい開催枠をラジオボタンで選択し、ボタンを押してください。</p>

		<c:if test="${not empty error}">
			<div class="alert alert-error"><c:out value="${error}" /></div>
		</c:if>

		<form method="post"
			action="${pageContext.request.contextPath}/admin/workshop">
			<div class="admin-card">
				<c:choose>
					<c:when test="${empty workshops}">
						<div class="admin-empty">登録されているワークショップはありません。</div>
					</c:when>
					<c:otherwise>
						<table class="admin-table">
							<thead>
								<tr>
									<th class="col-select">選択</th>
									<th>コース</th>
									<th>開催日</th>
									<th>開始時刻</th>
									<th class="num">残席</th>
									<th class="num">参加費</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="w" items="${workshops}">
									<tr class="selectable"
										onclick="this.querySelector('input[type=radio]').checked=true;">
										<td class="col-select"><input type="radio" name="workshopId"
											value="${w.workshop_id}"></td>
										<td><c:out value="${w.course_name}" /></td>
										<td><c:out value="${w.workshop_date}" /></td>
										<td><c:out value="${fn:substring(w.start_time, 0, 5)}" /></td>
										<td class="num"><c:out value="${w.capacity}" /></td>
										<td class="num"><c:out value="${w.price}" /> 円</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="btn-row">
				<a class="btn btn-secondary"
					href="${pageContext.request.contextPath}/admin/workshop/add">＋ 新規追加</a>
				<button type="submit" name="action" value="edit" class="btn btn-primary">編集</button>
				<button type="submit" name="action" value="delete" class="btn btn-danger">削除</button>
			</div>
		</form>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

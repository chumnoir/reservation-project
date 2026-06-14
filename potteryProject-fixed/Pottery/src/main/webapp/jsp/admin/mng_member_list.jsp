<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="active" value="member" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>会員管理 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">会員管理</h1>
		<p class="admin-page-desc">削除したい会員をラジオボタンで選択し、削除ボタンを押してください。</p>

		<c:if test="${not empty error}">
			<div class="alert alert-error"><c:out value="${error}" /></div>
		</c:if>

		<form method="post"
			action="${pageContext.request.contextPath}/admin/member">
			<div class="admin-card">
				<c:choose>
					<c:when test="${empty memberList}">
						<div class="admin-empty">登録されている会員はありません。</div>
					</c:when>
					<c:otherwise>
						<table class="admin-table">
							<thead>
								<tr>
									<th class="col-select">選択</th>
									<th>氏名</th>
									<th>メールアドレス</th>
									<th>電話番号</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="m" items="${memberList}">
									<tr class="selectable"
										onclick="this.querySelector('input[type=radio]').checked=true;">
										<td class="col-select"><input type="radio" name="userId"
											value="${m.user_id}"></td>
										<td><c:out value="${m.user_name}" /></td>
										<td><c:out value="${m.email}" /></td>
										<td><c:out value="${m.phone_number}" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="btn-row">
				<button type="submit" class="btn btn-danger">削除</button>
			</div>
		</form>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

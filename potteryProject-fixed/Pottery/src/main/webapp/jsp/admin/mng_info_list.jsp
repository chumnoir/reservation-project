<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="active" value="info" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>お知らせ管理 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">お知らせ管理</h1>
		<p class="admin-page-desc">編集・削除したいお知らせをラジオボタンで選択し、ボタンを押してください。</p>

		<c:if test="${not empty error}">
			<div class="alert alert-error"><c:out value="${error}" /></div>
		</c:if>

		<form method="post"
			action="${pageContext.request.contextPath}/admin/info">
			<div class="admin-card">
				<c:choose>
					<c:when test="${empty infoList}">
						<div class="admin-empty">登録されているお知らせはありません。</div>
					</c:when>
					<c:otherwise>
						<table class="admin-table">
							<thead>
								<tr>
									<th class="col-select">選択</th>
									<th>タイトル</th>
									<th>投稿日</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="i" items="${infoList}">
									<tr class="selectable"
										onclick="this.querySelector('input[type=radio]').checked=true;">
										<td class="col-select"><input type="radio" name="infoId"
											value="${i.info_id}"></td>
										<td><c:out value="${i.title}" /></td>
										<td><c:out value="${i.post_date}" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="btn-row">
				<a class="btn btn-secondary"
					href="${pageContext.request.contextPath}/admin/info/add">＋ 新規追加</a>
				<button type="submit" name="action" value="edit" class="btn btn-primary">編集</button>
				<button type="submit" name="action" value="delete" class="btn btn-danger">削除</button>
			</div>
		</form>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

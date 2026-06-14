<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>処理完了 - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<div class="admin-card">
			<div class="complete-box">
				<c:choose>
					<c:when test="${success}">
						<div class="icon">✓</div>
						<div class="msg"><c:out value="${msg}" /></div>
					</c:when>
					<c:otherwise>
						<div class="icon" style="color: #b4232a;">✕</div>
						<div class="msg" style="color: #b4232a;"><c:out value="${msg}" /></div>
					</c:otherwise>
				</c:choose>
				<div class="btn-row" style="justify-content: center;">
					<a class="btn btn-secondary" href="${backUrl}">
						<c:out value="${backLabel}" />
					</a>
					<a class="btn btn-primary"
						href="${pageContext.request.contextPath}/admin/top">管理者トップへ</a>
				</div>
			</div>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

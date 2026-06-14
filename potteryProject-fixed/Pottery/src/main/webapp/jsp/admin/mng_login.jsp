<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>管理者ログイン - 陶筋工房</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<header class="admin-header">
		<span class="brand">陶筋工房 <span class="brand-badge">ADMIN</span></span>
	</header>

	<div class="admin-login-wrap">
		<div class="admin-login-card">
			<div class="login-title">管理者ログイン</div>
			<div class="login-sub">Administrator Sign in</div>

			<c:if test="${not empty error}">
				<div class="alert alert-error"><c:out value="${error}" /></div>
			</c:if>

			<form action="${pageContext.request.contextPath}/admin/login"
				method="post">
				<div class="form-row">
					<label for="email">メールアドレス</label>
					<input type="email" id="email" name="email" class="form-control"
						value="${fn:escapeXml(email)}" placeholder="admin@example.com"
						autofocus>
				</div>
				<div class="form-row">
					<label for="password">パスワード</label>
					<input type="password" id="password" name="password"
						class="form-control" placeholder="パスワード">
				</div>
				<div class="btn-row">
					<button type="submit" class="btn btn-primary"
						style="width: 100%;">ログイン</button>
				</div>
			</form>
		</div>
	</div>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>

</body>
</html>

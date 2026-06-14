<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="active" value="info" />
<c:set var="isEdit" value="${mode == 'edit'}" />
<c:set var="valDate"
	value="${not empty formDate ? formDate : (isEdit and not empty info.post_date ? info.post_date : '')}" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>お知らせ${isEdit ? '編集' : '追加'} - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">お知らせ${isEdit ? '編集' : '追加'}</h1>

		<c:if test="${not empty error}">
			<div class="alert alert-error"><c:out value="${error}" /></div>
		</c:if>

		<div class="admin-card">
			<form method="post"
				action="${pageContext.request.contextPath}/admin/info/${isEdit ? 'edit' : 'add'}/confirm">
				<c:if test="${isEdit}">
					<input type="hidden" name="id" value="${info.info_id}">
				</c:if>

				<div class="form-row">
					<label for="title">タイトル</label>
					<input type="text" id="title" name="title" class="form-control"
						value="${fn:escapeXml(info.title)}">
				</div>

				<div class="form-row">
					<label for="content">メッセージ</label>
					<textarea id="content" name="content" class="form-control"><c:out
							value="${info.content}" /></textarea>
				</div>

				<div class="form-row">
					<label for="post_date">投稿日 <span class="hint">未指定の場合は今日の日付</span></label>
					<input type="date" id="post_date" name="post_date"
						class="form-control" value="${valDate}">
				</div>

				<div class="btn-row">
					<button type="submit" class="btn btn-primary">確認</button>
					<a class="btn btn-secondary"
						href="${pageContext.request.contextPath}/admin/info">戻る</a>
				</div>
			</form>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="active" value="workshop" />
<c:set var="isEdit" value="${mode == 'edit'}" />
<c:set var="selCourse"
	value="${not empty formCourseId ? formCourseId : (isEdit ? workshop.course_id : '')}" />
<c:set var="valDate"
	value="${not empty formDate ? formDate : (isEdit ? workshop.workshop_date : '')}" />
<c:set var="valTime"
	value="${not empty formTime ? formTime : (isEdit ? fn:substring(workshop.start_time,0,5) : '')}" />
<c:set var="valCap"
	value="${not empty formCapacity ? formCapacity : (isEdit ? workshop.capacity : '')}" />
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>ワークショップ${isEdit ? '編集' : '追加'} - 管理画面</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-layout.css">
</head>
<body class="admin">

	<%@ include file="adminHeader.jspf"%>

	<main class="admin-main">
		<h1 class="admin-page-title">ワークショップ${isEdit ? '編集' : '追加'}</h1>
		<p class="admin-page-desc">開催するコースと日時、定員を設定します。</p>

		<c:if test="${not empty error}">
			<div class="alert alert-error"><c:out value="${error}" /></div>
		</c:if>

		<div class="admin-card">
			<form method="post"
				action="${pageContext.request.contextPath}/admin/workshop/${isEdit ? 'edit' : 'add'}/confirm">
				<c:if test="${isEdit}">
					<input type="hidden" name="id" value="${workshop.workshop_id}">
				</c:if>

				<div class="form-row">
					<label for="courseId">コース</label>
					<select id="courseId" name="courseId" class="form-control">
						<option value="">選択してください</option>
						<c:forEach var="c" items="${courses}">
							<option value="${c.course_id}"
								${selCourse == c.course_id ? 'selected' : ''}>
								<c:out value="${c.course_name}" />（<c:out value="${c.price}" />円 /
								<c:out value="${c.required_time}" />分）
							</option>
						</c:forEach>
					</select>
				</div>

				<div class="form-row">
					<label for="workshop_date">開催日</label>
					<input type="date" id="workshop_date" name="workshop_date"
						class="form-control" value="${valDate}">
				</div>

				<div class="form-row">
					<label for="start_time">開始時刻</label>
					<input type="time" id="start_time" name="start_time"
						class="form-control" value="${valTime}">
				</div>

				<div class="form-row">
					<label for="capacity">定員（残席数）</label>
					<input type="number" id="capacity" name="capacity" min="0"
						class="form-control" value="${valCap}">
				</div>

				<div class="btn-row">
					<button type="submit" class="btn btn-primary">確認</button>
					<a class="btn btn-secondary"
						href="${pageContext.request.contextPath}/admin/workshop">戻る</a>
				</div>
			</form>
		</div>
	</main>

	<footer class="admin-footer">陶筋工房 管理システム | © 2026</footer>
</body>
</html>

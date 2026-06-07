<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>予約確認 | 陶芸体験ワークショップ〔統合版〕</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="site-header">
    <div class="site-header-inner">
        <h1 class="site-title"><a href="${pageContext.request.contextPath}/combined/user/main">陶芸体験ワークショップ〔統合版〕</a></h1>
        <nav class="site-nav">
            <a href="${pageContext.request.contextPath}/combined/user/main">メインへ戻る</a>
        </nav>
    </div>
</header>

<main class="container narrow">
    <section class="card">
        <h2 class="section-title">予約内容の確認</h2>

        <c:if test="${not empty error}">
            <p class="alert alert-error">${error}</p>
        </c:if>

        <dl class="detail-list">
            <dt>ワークショップ</dt><dd>${workshop.title}</dd>
            <dt>開催日</dt><dd><fmt:formatDate value="${workshop.eventDate}" pattern="yyyy年MM月dd日"/></dd>
            <dt>開始時刻</dt><dd><fmt:formatDate value="${workshop.startTime}" pattern="HH:mm"/></dd>
            <dt>講師</dt><dd>${workshop.instructor}</dd>
            <dt>参加費</dt><dd><fmt:formatNumber value="${workshop.price}" type="number"/>円（1名あたり）</dd>
            <dt>残席</dt><dd>${workshop.remainingSeats} 名</dd>
        </dl>

        <form action="${pageContext.request.contextPath}/combined/user/reserve" method="post" class="reserve-form">
            <input type="hidden" name="workshopId" value="${workshop.workshopId}">
            <%-- コース選択(必須)。サーバ側でも存在チェックを行う --%>
            <label>コース
                <select name="courseId" required>
                    <option value="" disabled selected>選択してください</option>
                    <c:forEach var="course" items="${courses}">
                        <option value="${course.courseId}">
                            ${course.name}<c:if test="${course.price > 0}">（＋<fmt:formatNumber value="${course.price}" type="number"/>円）</c:if>
                        </option>
                    </c:forEach>
                </select>
            </label>
            <label>予約人数
                <select name="numberOfPeople">
                    <c:forEach begin="1" end="${workshop.remainingSeats}" var="i">
                        <option value="${i}">${i} 名</option>
                    </c:forEach>
                </select>
            </label>
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/combined/user/main" class="btn btn-outline">キャンセル</a>
                <button type="submit" class="btn btn-primary">予約を確定する</button>
            </div>
        </form>
    </section>
</main>
</body>
</html>

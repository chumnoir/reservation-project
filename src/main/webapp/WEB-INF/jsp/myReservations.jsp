<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>マイ予約 | 陶芸体験ワークショップ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="site-header">
    <div class="site-header-inner">
        <h1 class="site-title"><a href="${pageContext.request.contextPath}/user/main">陶芸体験ワークショップ</a></h1>
        <nav class="site-nav">
            <a href="${pageContext.request.contextPath}/user/main">メインへ戻る</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline">ログアウト</a>
        </nav>
    </div>
</header>

<main class="container">
    <c:if test="${not empty sessionScope.flash}">
        <p class="alert alert-success">${sessionScope.flash}</p>
        <c:remove var="flash" scope="session"/>
    </c:if>

    <section class="card">
        <h2 class="section-title">マイ予約一覧</h2>
        <c:choose>
            <c:when test="${empty reservations}">
                <p class="empty">予約はまだありません。</p>
            </c:when>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ワークショップ</th><th>コース</th><th>開催日</th><th>時刻</th><th>人数</th><th>状態</th><th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${reservations}">
                            <tr>
                                <td>${r.workshopTitle}</td>
                                <td>${r.courseName}</td>
                                <td><fmt:formatDate value="${r.eventDate}" pattern="yyyy/MM/dd"/></td>
                                <td><fmt:formatDate value="${r.startTime}" pattern="HH:mm"/></td>
                                <td>${r.numberOfPeople} 名</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${r.status == 'CONFIRMED'}"><span class="badge badge-ok">予約確定</span></c:when>
                                        <c:otherwise><span class="badge badge-cancel">キャンセル済</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="actions">
                                    <%-- 確定状態の予約のみキャンセル可能。POST+確認ダイアログで誤操作を防止 --%>
                                    <c:if test="${r.status == 'CONFIRMED'}">
                                        <form action="${pageContext.request.contextPath}/user/reserve" method="post"
                                              onsubmit="return confirm('この予約をキャンセルしてよろしいですか？');" style="display:inline;">
                                            <input type="hidden" name="action" value="cancel">
                                            <input type="hidden" name="reservationId" value="${r.reservationId}">
                                            <button type="submit" class="btn btn-sm btn-danger">キャンセル</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>

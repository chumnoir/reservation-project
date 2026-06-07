<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>メインページ | 陶芸体験ワークショップ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="site-header">
    <div class="site-header-inner">
        <h1 class="site-title">陶芸体験ワークショップ</h1>
        <nav class="site-nav">
            <span class="welcome">${sessionScope.loginMember.name} さん</span>
            <a href="${pageContext.request.contextPath}/user/reserve?action=mylist">マイ予約</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline">ログアウト</a>
        </nav>
    </div>
</header>

<main class="container">

    <c:if test="${not empty sessionScope.flash}">
        <p class="alert alert-success">${sessionScope.flash}</p>
        <c:remove var="flash" scope="session"/>
    </c:if>

    <!-- お知らせ -->
    <section class="card">
        <h2 class="section-title">お知らせ</h2>
        <c:choose>
            <c:when test="${empty notices}">
                <p class="empty">現在お知らせはありません。</p>
            </c:when>
            <c:otherwise>
                <ul class="notice-list">
                    <c:forEach var="n" items="${notices}">
                        <li>
                            <span class="notice-date">
                                <fmt:formatDate value="${n.createdAt}" pattern="yyyy/MM/dd"/>
                            </span>
                            <span class="notice-title">${n.title}</span>
                            <p class="notice-content">${n.content}</p>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- カレンダー -->
    <section class="card">
        <div class="calendar-header">
            <h2 class="section-title">開催カレンダー</h2>
            <div class="calendar-nav">
                <a class="btn btn-sm btn-outline"
                   href="${pageContext.request.contextPath}/user/main?year=${prevYear}&month=${prevMonth}">‹ 前月</a>
                <span class="calendar-current">${calYear}年 ${calMonth}月</span>
                <a class="btn btn-sm btn-outline"
                   href="${pageContext.request.contextPath}/user/main?year=${nextYear}&month=${nextMonth}">翌月 ›</a>
            </div>
        </div>

        <table class="calendar">
            <thead>
                <tr>
                    <th class="sun">日</th><th>月</th><th>火</th><th>水</th>
                    <th>木</th><th>金</th><th class="sat">土</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <%-- 月初までの空白セル --%>
                    <c:forEach begin="1" end="${firstDayOffset}" var="blank">
                        <td class="empty-cell"></td>
                    </c:forEach>

                    <c:set var="cellCount" value="${firstDayOffset}"/>
                    <c:forEach begin="1" end="${daysInMonth}" var="day">

                        <%-- 当日の日付文字列 (yyyy-M-d) --%>
                        <c:set var="target" value="${calYear}-${calMonth}-${day}"/>

                        <td class="day-cell">
                            <div class="day-number">${day}</div>
                            <c:forEach var="w" items="${workshops}">
                                <fmt:formatDate value="${w.eventDate}" pattern="yyyy-M-d" var="wDate"/>
                                <c:if test="${wDate == target}">
                                    <a class="cal-event ${w.full ? 'full' : ''}"
                                       href="${pageContext.request.contextPath}/user/reserve?id=${w.workshopId}"
                                       title="${w.title}">${w.title}</a>
                                </c:if>
                            </c:forEach>
                        </td>

                        <c:set var="cellCount" value="${cellCount + 1}"/>
                        <%-- 週末(土曜)で改行 --%>
                        <c:if test="${cellCount % 7 == 0 and day < daysInMonth}">
                            </tr><tr>
                        </c:if>
                    </c:forEach>

                    <%-- 月末以降の空白セル --%>
                    <c:forEach begin="${cellCount % 7}" end="6" var="tail">
                        <c:if test="${cellCount % 7 != 0}">
                            <td class="empty-cell"></td>
                        </c:if>
                    </c:forEach>
                </tr>
            </tbody>
        </table>
        <p class="cal-legend"><span class="cal-event sample">通常</span> <span class="cal-event full sample">満席</span></p>
    </section>

    <!-- ワークショップ一覧 -->
    <section class="card">
        <h2 class="section-title">ワークショップ一覧</h2>
        <c:choose>
            <c:when test="${empty workshops}">
                <p class="empty">現在予約可能なワークショップはありません。</p>
            </c:when>
            <c:otherwise>
                <div class="workshop-grid">
                    <c:forEach var="w" items="${workshops}">
                        <article class="workshop-card">
                            <h3>${w.title}</h3>
                            <p class="workshop-meta">
                                <fmt:formatDate value="${w.eventDate}" pattern="yyyy年MM月dd日"/>
                                <fmt:formatDate value="${w.startTime}" pattern="HH:mm"/>〜
                            </p>
                            <p class="workshop-desc">${w.description}</p>
                            <ul class="workshop-detail">
                                <li>講師：${w.instructor}</li>
                                <li>参加費：<fmt:formatNumber value="${w.price}" type="number"/>円</li>
                                <li>残席：
                                    <c:choose>
                                        <c:when test="${w.full}"><span class="badge-full">満席</span></c:when>
                                        <c:otherwise>${w.remainingSeats} / ${w.capacity} 名</c:otherwise>
                                    </c:choose>
                                </li>
                            </ul>
                            <c:choose>
                                <c:when test="${w.full}">
                                    <button class="btn btn-block" disabled>満席</button>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-primary btn-block"
                                       href="${pageContext.request.contextPath}/user/reserve?id=${w.workshopId}">予約する</a>
                                </c:otherwise>
                            </c:choose>
                        </article>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

</main>

<footer class="site-footer">
    <p>&copy; 2026 Pottery Workshop Reservation System</p>
</footer>
</body>
</html>

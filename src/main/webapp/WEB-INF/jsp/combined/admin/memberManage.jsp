<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>会員管理 | 管理画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="admin">
<%@ include file="_adminHeader.jspf" %>

<main class="admin-container">
    <h2 class="admin-title">会員管理</h2>
    <p class="admin-lead">会員情報の確認と削除ができます。</p>

    <c:choose>
        <c:when test="${empty members}">
            <p class="empty">会員はいません。</p>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr><th>ID</th><th>氏名</th><th>メール</th><th>電話番号</th><th>住所</th><th>区分</th><th>登録日</th><th>操作</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${members}">
                        <tr>
                            <td>${m.memberId}</td>
                            <td>${m.name}</td>
                            <td>${m.email}</td>
                            <td>${m.phone}</td>
                            <td>${m.address}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${m.admin}"><span class="badge badge-admin">管理者</span></c:when>
                                    <c:otherwise><span class="badge">会員</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${m.createdAt}" pattern="yyyy/MM/dd"/></td>
                            <td class="actions">
                                <c:choose>
                                    <c:when test="${m.memberId == sessionScope.cbLoginAdmin.memberId}">
                                        <span class="sub">—</span>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="${pageContext.request.contextPath}/combined/admin/member" method="post"
                                              onsubmit="return confirm('この会員を削除してよろしいですか？関連する予約も削除されます。');" style="display:inline;">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="memberId" value="${m.memberId}">
                                            <button type="submit" class="btn btn-sm btn-danger">削除</button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>

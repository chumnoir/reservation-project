<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>エラー</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/info-shared.css">
</head>

<body>

	<div class="header">陶芸教室 予約システム</div>

	<div class="page">
		<h2 class="page-title">システムエラー</h2>

		<div class="content">

			<div class="message-block">
				<span class="message-icon alert">⚠</span>
				<p class="confirm-message" style="color: #c85f50; font-size: 22px;">エラーが発生しました。</p>
				<p class="message-text">
					申し訳ございません。処理の途中で問題が発生しました。<br>
					しばらく時間をおいてから、もう一度最初からお手続きをお願いいたします。
				</p>
			</div>

			<div class="button-row">
				<form action="<%= request.getContextPath() %>/user/main" method="get">
					<button type="submit" class="btn-positive">トップへ戻る</button>
				</form>
			</div>

		</div>
	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights Reserved.</footer>

</body>
</html>
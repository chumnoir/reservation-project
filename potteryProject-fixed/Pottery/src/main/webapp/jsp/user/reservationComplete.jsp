<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>予約完了</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/info-shared.css">
</head>

<body>

	<div class="header">陶芸ワークショップ</div>

	<div class="page">
		<h2 class="page-title">予約完了</h2>

		<div class="content">

			<div class="message-block">
				<span class="message-icon success">✓</span><!-- 記号を入れて表示してもいいかも？ -->
				<p class="confirm-message" style="color: #d88942; font-size: 22px;">予約が完了しました。</p>
				<p>ワークショップへのご参加を心よりお待ちしております。</p>
			</div>

			<div class="button-row">
				<a href="${pageContext.request.contextPath}/user/main"
					class="btn-positive btn-link">TOPへ戻る</a>
			</div>

		</div>
	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
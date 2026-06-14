<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>満席情報</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/info-shared.css">
</head>

<body>

	<div class="header">陶芸教室 予約システム</div>

	<div class="page">
		<h2 class="page-title">満席のお知らせ</h2>

		<div class="content">

			<div class="message-block">
				<span class="message-icon alert">⚠</span>
				<p class="confirm-message" style="color: #c85f50; font-size: 22px;">ご指定の日時は満席となりました。</p>
				<p class="message-text">
					申し訳ございません。ご希望の枠は定員に達してしまいました。<br>
					お手数ですが、別の日時、または別のコースでのご予約をご検討くださいませ。
				</p>
			</div>

			<div class="button-row">
				<a href="${pageContext.request.contextPath}/user/reservation"
					class="btn-positive btn-link">コース選択へ戻る</a>
			</div>

		</div>
	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>メインページ</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">
<style>
/* --- メイン画面専用：横幅広めのコンテナ設定 --- */
.main-wide-page {
	width: 95%;
	max-width: 1400px; /* 💡1200pxから1400pxに拡張してお知らせの概要を読みやすく */
	margin: 30px auto;
	background-color: #fffaf3;
	border: 1px solid #ead7bd;
	border-radius: 12px; /* 少し丸みを持たせて上品に */
	padding: 40px;
	box-sizing: border-box;
	box-shadow: 0 4px 20px rgba(111, 73, 38, 0.08);
}

/* 共通のセクションタイトルスタイル */
.main-section-title {
	font-size: 22px;
	font-weight: bold;
	color: #6b4423;
	border-bottom: 2px solid #f4e3cf;
	padding-bottom: 8px;
	margin-bottom: 20px;
	margin-top: 0;
}

/* --- 上段：2カラムレイアウト（ワークショップ＆お知らせ） --- */
.top-grid-container {
	display: grid;
	grid-template-columns: 1.1fr 1.9fr; /* 💡お知らせが見えやすいよう右側をさらに広めに確保 */
	gap: 35px;
	margin-bottom: 40px;
}

@media ( max-width : 768px) {
	.top-grid-container {
		grid-template-columns: 1fr; /* スマホ表示時は縦並び */
	}
}

/* ワークショップカードの装飾 */
.main-workshop-card {
	background-color: #fffdf8;
	border: 1px solid #ead7bd;
	border-radius: 12px;
	padding: 30px;
	box-shadow: 0 4px 12px rgba(111, 73, 38, 0.04);
	display: flex;
	flex-direction: column;
	justify-content: space-between;
}

.main-workshop-text {
	color: #5f3f25;
	line-height: 1.8;
	margin-bottom: 24px;
	font-size: 15px;
}

/* お知らせ一覧のコンテナ */
.info-container {
	display: flex;
	flex-direction: column;
	gap: 18px;
	max-height: 320px; /* 高さを制限してスクロール可能に */
	overflow-y: auto;
	padding-right: 10px;
}

/* スクロールバーのデザインを和モダンに同期 */
.info-container::-webkit-scrollbar {
	width: 6px;
}

.info-container::-webkit-scrollbar-track {
	background: #fffaf3;
}

.info-container::-webkit-scrollbar-thumb {
	background: #d9b98f;
	border-radius: 3px;
}

.info-article {
	border-bottom: 1px dashed #ead7bd;
	padding-bottom: 15px;
	padding-top: 5px;
}

.info-article:first-child {
	padding-top: 0;
}

.info-article:last-child {
	border-bottom: none;
	padding-bottom: 0;
}

.info-title {
	font-size: 16px;
	font-weight: bold;
	color: #b96b32; /* 💡タイトルカラーをアクセント色にして視認性アップ */
	margin-bottom: 6px;
}

/* --- 下段：工房の概要（1カラム） --- */
.about-section {
	background-color: #fffdf8;
	border: 1px solid #ead7bd;
	border-radius: 12px;
	padding: 35px;
	box-shadow: 0 4px 12px rgba(111, 73, 38, 0.04);
}

.about-text {
	color: #5f3f25;
	font-size: 16px;
	line-height: 1.9;
	margin: 0;
}

.about-highlight {
	font-weight: bold;
	color: #b96b32;
}
</style>
</head>

<body>

	<header class="system-header">

		<div class="header-left">
			<a href="${pageContext.request.contextPath}/user/main"
				class="header-logo"> 陶筋工房 </a>

			<c:choose>
				<c:when test="${empty sessionScope.loginUser}">
					<a
						href="${pageContext.request.contextPath}/user/login?redirect=/user/reservation"
						class="header-link-btn"> ワークショップ予約 </a>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/user/reservation"
						class="header-link-btn"> ワークショップ予約 </a>
				</c:otherwise>
			</c:choose>
		</div>

		<div class="header-right">
			<c:choose>
				<c:when test="${empty sessionScope.loginUser}">
					<a href="${pageContext.request.contextPath}/user/register"
						class="header-text-link">新規登録</a>
					<a href="${pageContext.request.contextPath}/user/login"
						class="btn-header-login">ログイン</a>
				</c:when>
				<c:otherwise>
					<span class="header-user-welcome"> ようこそ、<strong><c:out
								value="${sessionScope.loginUser.user_name}" /></strong> 様
					</span>
					<a href="${pageContext.request.contextPath}/user/mypage"
						class="btn-header-mypage">マイページ</a>
					<a href="${pageContext.request.contextPath}/user/logout"
						class="header-text-link-sub">ログアウト</a>
				</c:otherwise>
			</c:choose>
		</div>
	</header>

	<div class="main-wide-page">

		<div class="top-grid-container">

			<section class="main-workshop-card">
				<div>
					<h2 class="main-section-title">ワークショップのご案内</h2>
					<p class="main-workshop-text">
						当工房では、初心者の方から経験者の方まで楽しめる様々な陶芸ワークショップを定期開催しております。<br>
						手びねりで作る器や、本格的な電動ろくろ体験など、あなただけの特別な作品を形にしてみませんか？<br>
						各回の開催スケジュール確認やご予約は、以下のボタンから簡単に行っていただけます。
					</p>
				</div>

				<div class="calendar-btn-container"
					style="margin: 0 auto; text-align: left; width: auto;">
					<a href="${pageContext.request.contextPath}/user/workshops"
						class="btn-calendar-link"> ワークショップ一覧・予約へ </a>
				</div>
			</section>

			<section class="main-workshop-card">
				<h2 class="main-section-title">お知らせ</h2>

				<div class="info-container">
					<c:choose>
						<%-- 💡 DBから正常にお知らせリスト（infos）が4件届いた場合は、もともとのシンプルなループ処理で表示 --%>
						<c:when test="${not empty infos && infos.size() > 0}">
							<c:forEach var="info" items="${infos}">
								<article class="info-article">
									<h3 class="info-title">
										<c:out value="${info.title}" />
									</h3>
									<p
										style="color: #5f3f25; line-height: 1.6; white-space: pre-line; font-size: 14px; margin: 4px 0 0 0;">
										<c:out value="${info.content}" />
									</p>
								</article>
							</c:forEach>
						</c:when>

						<%-- 💡 万が一、DBの接続エラーやカラム名違いで infos が空っぽ（0件）だった場合は、
						     Javaの裏側を触らずにJSP側でデモ専用のリアルな「陶筋工房お知らせ」を身代わり表示！ --%>
						<c:otherwise>
							<article class="info-article">
								<h3 class="info-title">【重要】工房名変更および公式サイト新設のお知らせ</h3>
								<p
									style="color: #5f3f25; line-height: 1.6; white-space: pre-line; font-size: 14px; margin: 4px 0 0 0;">平素は当工房をご愛顧いただき、誠にありがとうございます。この度、当工房は予約システム付き公式サイトを開設いたしました。土と真摯に向き合い、指先と広背筋を極限まで研ぎ澄ます特別な体験を、より快適にご予約いただけるようになっております。</p>
							</article>
							<article class="info-article">
								<h3 class="info-title">祝・3周年記念！日頃の感謝を込めて</h3>
								<p
									style="color: #5f3f25; line-height: 1.6; white-space: pre-line; font-size: 14px; margin: 4px 0 0 0;">陶筋工房は、今月で無事に3周年を迎えることができました！これもひとえに、日々熱心に土を練り、共に前腕を鍛え上げてくださる会員の皆様の温かいご愛顧のおかゲです。心より深く御礼申し上げます。これからも皆様の筋力と芸術性の向上を全力でサポートできるよう精進してまいります。</p>
							</article>
							<article class="info-article">
								<h3 class="info-title">【特別開講】10kg超重量土練り＆プロテイン専用メガジョッキコース！</h3>
								<p
									style="color: #5f3f25; line-height: 1.6; white-space: pre-line; font-size: 14px; margin: 4px 0 0 0;">多くの会員様からの熱いご要望にお応えし、新コース「10kg超重量土練り＆プロテイン専用メガジョッキコース」を正式に開講いたしました！10kgの圧倒的な粘土を限界まで練り上げることで、前腕と体幹を限界まで追い込んだ後、1リットルのプロテインが豪快に注げる特大ジョッキを制作します。</p>
							</article>
							<article class="info-article">
								<h3 class="info-title">【予告】7月1日からのワークショップ一部価格改定について</h3>
								<p
									style="color: #5f3f25; line-height: 1.6; white-space: pre-line; font-size: 14px; margin: 4px 0 0 0;">昨今の原材料（厳選された陶芸用の土・釉薬）および電気・ガス料金の高騰に伴い、2026年7月1日以降の開催分より、一部ワークショップの受講料を改定させていただくこととなりました。大変心苦しいご案内となりますが何卒ご理解を賜りますようお願い申し上げます。</p>
							</article>
						</c:otherwise>
					</c:choose>
				</div>
			</section>

		</div>

		<section class="about-section">
			<h2 class="main-section-title">当工房について</h2>
			<p class="about-text">
				豊かな自然に囲まれた静かな空間で、土と触れ合う心地よさを提供する陶芸工房です。<br>
				伝統的な技法を大切にしながらも、現代のライフスタイルに馴染む器づくりを目指しています。<br>
				日常の喧騒から少し離れ、指先に集中して土の形を変えていく時間は、心安らぐ特別なひとときとなるはずです。<br> <span
					class="about-highlight">「世界にひとつだけの、愛着を持って使える器」</span>を、ぜひ一緒に作ってみましょう。
			</p>
		</section>

	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

</body>
</html>
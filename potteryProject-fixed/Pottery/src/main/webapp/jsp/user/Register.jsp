<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>新規会員登録 - 陶筋工房</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common-layout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header-layout.css">

<style>
/* 新規登録画面専用のスタイル */
.register-container {
	max-width: 560px;
	margin: 40px auto;
	padding: 40px;
	background-color: #fffdf8;
	border: 1px solid #ead7bd;
	border-radius: 12px;
	box-shadow: 0 4px 20px rgba(111, 73, 38, 0.06);
	box-sizing: border-box;
}

.register-title {
	font-size: 24px;
	font-weight: bold;
	color: #6b4423;
	text-align: center;
	margin-bottom: 8px;
	letter-spacing: 0.05em;
}

.register-subtitle {
	font-size: 14px;
	color: #7a5738;
	text-align: center;
	margin-bottom: 30px;
}

.form-grid {
	display: flex;
	flex-direction: column;
	gap: 20px;
}

.form-group {
	display: flex;
	flex-direction: column;
	gap: 8px;
}

.form-label {
	font-size: 15px;
	font-weight: bold;
	color: #5f3f25;
}

.form-input {
	width: 100%;
	padding: 12px;
	font-size: 16px;
	border: 1px solid #d9b98f;
	border-radius: 6px;
	background-color: #fffaf3;
	color: #5f3f25;
	box-sizing: border-box;
	transition: border-color 0.2s, box-shadow 0.2s;
}

.form-input:focus {
	outline: none;
	border-color: #b96b32;
	box-shadow: 0 0 0 3px rgba(185, 107, 50, 0.15);
}

/* パスワード不一致エラーメッセージ */
#errorMsg {
	color: #c02020;
	font-size: 13px;
	font-weight: bold;
	margin-top: 4px;
	display: none; /* デフォルト非表示 */
}

/* サーバーエラー */
.server-error {
	background-color: #fff0f0;
	border: 1px solid #ffc0c0;
	color: #c02020;
	padding: 10px;
	border-radius: 6px;
	font-size: 14px;
}

.checkbox-group {
	display: flex;
	align-items: center;
	gap: 8px;
	font-size: 14px;
	color: #5f3f25;
	cursor: pointer;
	user-select: none;
}

.checkbox-group input {
	cursor: pointer;
	accent-color: #b96b32;
}

.register-footer-links {
	margin-top: 30px;
	padding-top: 20px;
	border-top: 1px dashed #ead7bd;
	text-align: center;
}

.link-sub {
	color: #7a5738;
	text-decoration: none;
	font-size: 14px;
	cursor: pointer;
}

.link-sub:hover {
	text-decoration: underline;
}

/* 送信ボタンの無効化スタイル */
#submitBtn:disabled {
	background: #ccc !important;
	border-color: #bbb !important;
	cursor: not-allowed;
	opacity: 0.7;
}
</style>
</head>

<body>

	<header class="system-header">
		<div class="header-left">
			<c:choose>
				<c:when test="${not empty redirect}">
					<a onclick="history.back();" class="header-logo"
						style="cursor: pointer;">陶筋工房</a>
				</c:when>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/user/main"
						class="header-logo">陶筋工房</a>
				</c:otherwise>
			</c:choose>
			<a href="${pageContext.request.contextPath}/user/workshops"
				class="header-link-btn">ワークショップ一覧</a>
		</div>
		<div class="header-right">
			<a href="${pageContext.request.contextPath}/user/login"
				class="header-text-link">ログイン</a>
		</div>
	</header>

	<div class="page"
		style="background: none; border: none; box-shadow: none; width: auto; margin: 0;">

		<div class="register-container">
			<h1 class="register-title">新規会員登録</h1>
			<p class="register-subtitle">必要事項を入力してください</p>

			<form
				action="${pageContext.request.contextPath}/user/register/confirm"
				method="post" id="registerForm">
				<input type="hidden" name="redirect" value="${redirect}">

				<div class="form-grid">
					<div class="form-group">
						<label class="form-label">名前</label> <input type="text"
							name="name" required value="${name}" class="form-input"
							placeholder="山田 太郎">
					</div>

					<div class="form-group">
						<label class="form-label">住所</label> <input type="text"
							name="address" required value="${address}" class="form-input"
							placeholder="福岡県糸島市...">
					</div>

					<div class="form-group">
						<label class="form-label">電話番号</label> <input type="tel"
							name="phone_number" required value="${phone}" class="form-input"
							placeholder="09012345678">
					</div>

					<div class="form-group">
						<label class="form-label">メールアドレス</label> <input type="email"
							name="email" required value="${email}" class="form-input"
							placeholder="example@pottery.com">
					</div>

					<div class="form-group">
						<label class="form-label">パスワード</label> <input type="password"
							id="password" name="password" required class="form-input"
							placeholder="8文字以上の半角英数">
					</div>

					<div class="form-group">
						<label class="form-label">パスワード（確認用）</label> <input
							type="password" id="confirmPassword" name="confirmPassword"
							required class="form-input">
						<p id="errorMsg">⚠️ パスワードが一致していません</p>
					</div>

					<c:if test="${not empty error}">
						<div class="server-error">${error}</div>
					</c:if>

					<label class="checkbox-group"> <input type="checkbox"
						id="showPass"> パスワードを表示
					</label>

					<button type="submit" id="submitBtn" class="btn-calendar-link"
						style="width: 100%; border: none; cursor: pointer; font-size: 16px; margin-top: 10px;">
						入力内容を確認する</button>
				</div>
			</form>

			<div class="register-footer-links">
				<p style="font-size: 14px; color: #5f3f25; margin-bottom: 10px;">すでにアカウントをお持ちですか？</p>
				<a
					href="${pageContext.request.contextPath}/user/login?redirect=${redirect}"
					class="link-sub" style="font-weight: bold; color: #b96b32;">
					ログインはこちら </a>
				<div style="margin-top: 15px;">
					<a onclick="history.back();" class="link-sub">戻る</a>
				</div>
			</div>
		</div>

	</div>

	<footer class="footer">陶筋工房（Fukuoka） | © 2026 All Rights
		Reserved.</footer>

	<script>
    window.addEventListener("DOMContentLoaded", () => {
        const password = document.getElementById("password");
        const confirmPassword = document.getElementById("confirmPassword");
        const errorMsg = document.getElementById("errorMsg");
        const submitBtn = document.getElementById("submitBtn");
        const chk = document.getElementById("showPass");

        function validate() {
            let valid = true;

            // パスワード一致チェック
            if (confirmPassword.value && password.value !== confirmPassword.value) {
                errorMsg.style.display = "block";
                valid = false;
            } else {
                errorMsg.style.display = "none";
            }

            // 必須入力チェック
            const requiredInputs = document.querySelectorAll("input[required]");
            requiredInputs.forEach(input => {
                if (!input.value.trim()) valid = false;
            });

            // ボタンの状態制御
            submitBtn.disabled = !valid;
        }

        // 全入力フィールドの監視
        const allInputs = document.querySelectorAll("input");
        allInputs.forEach(input => {
            input.addEventListener("input", validate);
        });

        // 初回ロード時にもバリデーションを実行（リダイレクト時などの入力保持対応）
        validate();

        // パスワード表示切り替え
        chk.addEventListener("change", () => {
            const type = chk.checked ? "text" : "password";
            password.type = type;
            confirmPassword.type = type;
        });
    });
    </script>
</body>
</html>
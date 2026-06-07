# 陶芸体験ワークショップ Web予約システム

HTML / CSS / Servlet / JSP を用いた、陶芸体験ワークショップの予約システムです。
**DAO / DTO / Service / Servlet / JSP** によるMVCモデルで構成しています。

- 実行環境: **Tomcat 11**（Jakarta EE 11 / `jakarta.servlet`）・**Java 17+**
- データベース: **PostgreSQL 14+**
- ビルド: **Maven**

---

## 主な機能

### 一般会員（ユーザー）
- 会員登録（氏名・メールアドレス・電話番号・住所・パスワード）＋**入力バリデーション**
- ログイン（メールアドレス＋パスワード）／ログアウト
- ログイン状態はセッションで保持（ログアウトするまで維持）
- メインページ
  - ワークショップ詳細一覧
  - 開催カレンダー（開催日にワークショップを表示）
  - 管理者からのお知らせ一覧
  - **一覧・カレンダーの双方から予約可能**
- 予約時に**コース（プラン）を選択**
- マイ予約一覧 ＋ **予約キャンセル機能**

### 管理者（別URL `/admin/login` からアクセス）
- ダッシュボード
- **お知らせ管理**：追加・編集・削除
- **ワークショップ管理**：追加・編集・削除
- **予約管理**：編集・削除
- **会員管理**：削除のみ

---

## アーキテクチャ（MVCモデル）

```
ブラウザ
  │  HTTP（Filterで認証・文字コードを制御）
  ▼
[ Servlet ]  ── 入力受取・画面遷移の制御（Controller）
  │
  ▼
[ Service ]  ── 業務ロジック・バリデーション（ValidationUtil）
  │
  ▼
[ DAO ]      ── SQL実行・DBアクセス（DBConnection）
  │
  ▼
[ Database ] PostgreSQL
  ▲
  │ データの受け渡しは [ DTO ] で行う
  ▼
[ JSP ]      ── 画面表示（View）
```

- **ER図**: [`docs/ER-diagram.md`](docs/ER-diagram.md)
- **クラス図・シーケンス図**: [`docs/class-diagram.md`](docs/class-diagram.md)
- **処理別クラス図**（ログイン/登録/予約/キャンセル/管理）: [`docs/process-class-diagrams.md`](docs/process-class-diagrams.md)

### 2つの構成（学習用に同梱）
同一アプリ内に、設計の異なる2つの実装を**並行**で同梱しています（DAO/DTO/CSSは共通）。

| 構成 | 入口URL | 説明 |
|------|---------|------|
| **MVC分離版**（標準） | `/` | Servlet→**Service**→DAO と層を分離。`servlet` / `service` / `dao` / `dto` パッケージ |
| **Service統合版** | `/combined/` | Serviceを設けず、業務ロジックを**Servletに直接記述**。`combined.servlet` / `combined.filter` パッケージ |

- 両者は別URL・別セッション属性（`loginMember` ↔ `cbLoginMember` 等）で完全独立。一方のログインは他方に影響しません。
- 機能（登録/ログイン/予約/コース選択/キャンセル/管理CRUD/IP制限）は同等です。

### パッケージ構成
```
com.pottery.reservation
├── dto/        MemberDTO, WorkshopDTO, ReservationDTO, NoticeDTO
├── dao/        MemberDAO, WorkshopDAO, ReservationDAO, NoticeDAO
├── service/    MemberService, WorkshopService, ReservationService, NoticeService
├── servlet/    Register/Login/Logout/Main/Reservation Servlet
│   └── admin/  AdminLogin/Logout/Dashboard + Notice/Workshop/Reservation/Member Admin
├── filter/     EncodingFilter, AuthFilter, AdminAuthFilter
└── util/       DBConnection, PasswordUtil, ValidationUtil
```

---

## データベース設計（PostgreSQL）

| テーブル | 概要 | 主なカラム |
|----------|------|-----------|
| `members`      | 会員（一般・管理者） | name, email(UNIQUE), phone, address, password(平文/演習用), role |
| `workshops`    | ワークショップ      | title, description, instructor, event_date, start_time, capacity, price |
| `courses`      | コース（予約時に選択するプランのマスタ） | name, description, price |
| `reservations` | 予約               | member_id(FK), workshop_id(FK), course_id(FK), number_of_people, status |
| `notices`      | お知らせ           | title, content, created_at, updated_at |

- 主キーは `GENERATED ALWAYS AS IDENTITY`（PostgreSQLの自動採番）
- `reservations` は **部分一意インデックス**で「確定状態のみ」会員×WSを一意化
  → キャンセル後の再予約が可能
- 外部キーは `ON DELETE CASCADE`、`notices.updated_at` はトリガで自動更新
- 詳細は [`database/schema.sql`](database/schema.sql) を参照

---

## セットアップ手順

### 1. 必要環境
| ソフトウェア | バージョン | 確認コマンド |
|--------------|-----------|--------------|
| JDK | 17 以上 | `java -version` |
| Apache Tomcat | 11.x | — |
| PostgreSQL | 14 以上 | `psql --version` |
| Maven | 3.9 以上 | `mvn -v` |

> macOS での導入例: `brew install openjdk@17 maven postgresql@16 tomcat`

### 2. データベース構築
```bash
# DB作成
psql -U postgres -c "CREATE DATABASE pottery_reservation ENCODING 'UTF8';"
# テーブル＋初期データ投入
psql -U postgres -d pottery_reservation -f database/schema.sql
```

### 3. DB接続情報の設定
`src/main/java/com/pottery/reservation/util/DBConnection.java` の
`URL` / `USER` / `PASSWORD` を環境に合わせて変更してください。

```java
private static final String URL = "jdbc:postgresql://localhost:5432/pottery_reservation";
private static final String USER = "postgres";
private static final String PASSWORD = "password";
```

---

## コンパイル確認とビルド

```bash
# 文法・依存解決のチェックのみ（クラスファイル生成）
mvn clean compile

# テスト含むパッケージング（target/reservation.war を生成）
mvn clean package
```

- `mvn compile` が **BUILD SUCCESS** になればコンパイルは通っています。
- 生成物 `target/reservation.war` に PostgreSQL ドライバや JSTL 実装が
  `WEB-INF/lib` として同梱されます（Servlet/JSP API は Tomcat 提供のため除外）。

---

## 実行方法（Tomcat 11 へデプロイ）

### 方法A: warを直接配置（最も簡単）
```bash
mvn clean package
cp target/reservation.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh      # Windows は startup.bat
```
起動後、Tomcat が war を `webapps/reservation/` に自動展開します。

### 方法B: Tomcat Maven プラグインを使う場合
`pom.xml` にプラグインを追加すれば `mvn tomcat:run` 相当の起動も可能ですが、
公式に Tomcat 11 対応のプラグインは限定的なため、**方法A（war配置）を推奨**します。

### アクセスURL
- 利用者入口： `http://localhost:8080/reservation/`
- 管理者入口： `http://localhost:8080/reservation/admin/login`

### 停止
```bash
$CATALINA_HOME/bin/shutdown.sh
```

---

## 初期アカウント

| 区分 | メールアドレス | パスワード |
|------|----------------|-----------|
| 管理者 | `admin@pottery.com` | `admin123` |
| 一般会員 | `taro@example.com` | `user123` |

---

## バリデーション仕様

| 項目 | クライアント側（HTML5） | サーバ側（ValidationUtil / Service） |
|------|------------------------|--------------------------------------|
| 氏名 | required, maxlength=100 | 必須・100文字以内 |
| メール | type=email, required | 必須・正規表現で形式チェック |
| 電話番号 | pattern, required | 数字＋ハイフン 10〜13文字 |
| 住所 | required, maxlength=255 | 必須・255文字以内 |
| パスワード | minlength=6＋確認欄一致チェック | 6文字以上 |
| 予約人数 | select（残席数まで） | 1名以上・残席超過/重複/満席チェック |

> クライアント側は利便性のための一次チェックで、最終判断は必ずサーバ側で行います（多層防御）。

---

## 管理画面のアクセス制限（店舗端末のみ）

管理画面 `/admin/*` は一般利用者ページから導線を一切張らず、**許可したIPアドレス
（店舗端末）からのみ**アクセスできます。許可外のIPには **404** を返し、管理画面の
存在自体を隠します（`AdminAuthFilter`）。

許可IPは再コンパイル不要で、システムプロパティ／環境変数 `ADMIN_ALLOWED_IPS`
（カンマ区切り・末尾 `*` で前方一致）で指定します。Tomcat では
`$CATALINA_HOME/bin/setenv.sh` に次のように記述します。

```sh
# 店舗端末の実IPのみを許可（ループバックは含めない）
export CATALINA_OPTS="$CATALINA_OPTS -DADMIN_ALLOWED_IPS=192.168.11.50"
# 例) サブネット全体を許可: -DADMIN_ALLOWED_IPS=192.168.11.*
```

未指定時の既定はループバック（`127.0.0.1` / `::1`）のみです。

> ⚠️ **トンネル公開との併用に注意**: cloudflared などのトンネル経由のアクセスは
> Tomcat からは `127.0.0.1` に見えます。既定（ループバック許可）のままトンネルを
> 公開すると**管理画面が外部から到達可能**になります。店舗端末のみに限定したい場合は
> `ADMIN_ALLOWED_IPS` に店舗端末の実IPを設定し、ループバックを含めないでください。

---

## 補足
- パスワードは**演習用の仕様により平文で保存**しています（`MemberService`）。
  実運用では必ず bcrypt 等でハッシュ化してください。
- 文字コードは全リクエストで UTF-8 に統一（`EncodingFilter`）。
- 認証は `AuthFilter`（`/user/*`）と `AdminAuthFilter`（`/admin/*`）で制御。
- JSPは `WEB-INF` 配下に置き、必ず Servlet 経由で表示（直接アクセス防止）。

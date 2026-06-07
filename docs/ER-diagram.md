# ER図（Entity Relationship Diagram）

陶芸体験ワークショップ予約システムのデータベース構造です。
（GitHub 上では Mermaid が図として描画されます）

```mermaid
erDiagram
    MEMBERS ||--o{ RESERVATIONS : "予約する"
    WORKSHOPS ||--o{ RESERVATIONS : "予約される"

    MEMBERS {
        int         member_id  PK "会員ID(IDENTITY)"
        varchar     name           "氏名"
        varchar     email      UK  "メールアドレス(一意)"
        varchar     phone          "電話番号"
        varchar     address        "住所"
        varchar     password       "パスワード(SHA-256)"
        varchar     role           "USER / ADMIN"
        timestamp   created_at     "登録日時"
    }

    WORKSHOPS {
        int         workshop_id PK "ワークショップID(IDENTITY)"
        varchar     title          "タイトル"
        text        description    "説明"
        varchar     instructor     "講師名"
        date        event_date     "開催日"
        time        start_time     "開始時刻"
        int         capacity       "定員"
        int         price          "参加費(円)"
        timestamp   created_at     "登録日時"
    }

    RESERVATIONS {
        int         reservation_id   PK "予約ID(IDENTITY)"
        int         member_id        FK "会員ID"
        int         workshop_id      FK "ワークショップID"
        int         number_of_people    "予約人数"
        varchar     status              "CONFIRMED / CANCELED"
        timestamp   reserved_at         "予約日時"
    }

    NOTICES {
        int         notice_id  PK "お知らせID(IDENTITY)"
        varchar     title          "タイトル"
        text        content        "本文"
        timestamp   created_at     "作成日時"
        timestamp   updated_at     "更新日時(トリガで自動更新)"
    }
```

## リレーションと制約

| 関係 | 内容 |
|------|------|
| `members` 1 — * `reservations` | 1人の会員は複数の予約を持てる。会員削除時は予約も削除（`ON DELETE CASCADE`） |
| `workshops` 1 — * `reservations` | 1つのWSは複数の予約を持てる。WS削除時は予約も削除（`ON DELETE CASCADE`） |
| `notices` | 他テーブルと関連を持たない独立テーブル |

### 主な制約
- `members.email` … **UNIQUE**（重複登録の防止）
- `members.role` / `reservations.status` … **CHECK制約**で許可値を限定
- `reservations` … **部分一意インデックス** `uq_active_reservation`
  `(member_id, workshop_id) WHERE status='CONFIRMED'`
  → 「確定」状態でのみ重複を禁止し、**キャンセル後の再予約を許容**する
- `notices.updated_at` … **トリガ** `trg_notices_updated_at` で更新時に自動更新

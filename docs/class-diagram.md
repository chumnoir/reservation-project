# クラス図（Class Diagram）

MVCモデル（DAO / DTO / Service / Servlet / JSP）の構造を示します。
矢印は依存方向（呼び出し方向）です。Servlet → Service → DAO → DB の流れで、
データは DTO に詰めて各層を受け渡します。

## 全体レイヤ構成

```mermaid
flowchart TD
    subgraph View["View 層 (JSP)"]
        JSP["*.jsp / *.jspf"]
    end
    subgraph Controller["Controller 層 (Servlet + Filter)"]
        SV["各種 Servlet"]
        FL["EncodingFilter / AuthFilter / AdminAuthFilter"]
    end
    subgraph Logic["Service 層"]
        SVC["MemberService / WorkshopService<br/>ReservationService / NoticeService"]
    end
    subgraph Data["DAO 層"]
        DAO["MemberDAO / WorkshopDAO<br/>ReservationDAO / NoticeDAO"]
    end
    subgraph Util["util"]
        U["DBConnection / PasswordUtil / ValidationUtil"]
    end
    DB[("PostgreSQL")]
    DTO["DTO (Member/Workshop/Reservation/Notice)"]

    JSP <-->|"画面表示 / 入力"| SV
    FL -.->|"認証・文字コード"| SV
    SV --> SVC
    SVC --> DAO
    DAO --> U
    U --> DB
    SV -. "受け渡し" .-> DTO
    SVC -. DTO .-> DAO
```

## 主要クラスの関係（予約まわりを例に）

```mermaid
classDiagram
    class ReservationDTO {
        -int reservationId
        -int memberId
        -int workshopId
        -int numberOfPeople
        -String status
        -Timestamp reservedAt
        +getters/setters()
    }

    class ReservationServlet {
        -ReservationService reservationService
        -WorkshopService workshopService
        +doGet(req, resp)
        +doPost(req, resp)
    }

    class ReservationService {
        -ReservationDAO reservationDAO
        -WorkshopDAO workshopDAO
        +reserve(memberId, workshopId, people) boolean
        +cancel(reservationId, memberId) void
        +findByMemberId(memberId) List~ReservationDTO~
        +findAll() List~ReservationDTO~
        +update(ReservationDTO) boolean
        +delete(reservationId) boolean
    }

    class ReservationDAO {
        +insert(ReservationDTO) boolean
        +findByMemberId(memberId) List~ReservationDTO~
        +existsReservation(memberId, workshopId) boolean
        +cancelByMember(reservationId, memberId) boolean
        +update(ReservationDTO) boolean
        +delete(reservationId) boolean
    }

    class DBConnection {
        +getConnection() Connection$
    }

    ReservationServlet --> ReservationService : 利用
    ReservationService --> ReservationDAO : 利用
    ReservationDAO --> DBConnection : 接続取得
    ReservationServlet ..> ReservationDTO : 生成/参照
    ReservationService ..> ReservationDTO : 生成/参照
    ReservationDAO ..> ReservationDTO : マッピング
```

## 予約キャンセルのシーケンス

```mermaid
sequenceDiagram
    participant U as 会員(ブラウザ)
    participant S as ReservationServlet
    participant SV as ReservationService
    participant D as ReservationDAO
    participant DB as PostgreSQL

    U->>S: POST /user/reserve (action=cancel, reservationId)
    S->>SV: cancel(reservationId, memberId)
    SV->>D: cancelByMember(reservationId, memberId)
    D->>DB: UPDATE ... SET status='CANCELED'<br/>WHERE id=? AND member_id=? AND status='CONFIRMED'
    DB-->>D: 更新件数
    alt 1件更新
        D-->>SV: true
        SV-->>S: 正常終了
        S-->>U: マイ予約へリダイレクト(「キャンセルしました」)
    else 0件(他人の予約/既にキャンセル済み)
        D-->>SV: false
        SV-->>S: IllegalStateException
        S-->>U: マイ予約へリダイレクト(エラーメッセージ)
    end
```

# Java - 住所録 アプリ（crud学習用）

 <img alt="Static Badge" src="https://img.shields.io/badge/wsl2-w?style=plastic&logo=linux&logoColor=000000&labelColor=%23FCC624&color=%23FCC624"> <img alt="Static Badge" src="https://img.shields.io/badge/ubuntu-u?style=plastic&logo=ubuntu&logoColor=%23ffffff&labelColor=%23E95420&color=%23E95420">
 <img alt="Static Badge" src="https://img.shields.io/badge/Docker-d?style=plastic&logo=docker&logoColor=%23ffffff&labelColor=%232496ED&color=%232496ED">
 <img alt="Static Badge" src="https://img.shields.io/badge/MySQL-m?style=plastic&logo=mysql&logoColor=%23ffffff&labelColor=%234479A1&color=%234479A1">  
 <img alt="Static Badge" src="https://img.shields.io/badge/Apache%20Maven-%20?style=plastic&logo=Apache%20Maven&logoColor=%23C71A36&color=000000">
 <img alt="Static Badge" src="https://img.shields.io/badge/Spring%20Boot-s?style=plastic&logo=springboot&logoColor=%23005F0F">
 <img alt="Static Badge" src="https://img.shields.io/badge/OpenJDK-j?style=plastic&logo=openjdk&logoColor=%23000000">  

## プロジェクト概要
このリポジトリは、Java と Spring Boot を使って作成したシンプルな Todo リストのサンプルアプリです。PWA（プログレッシブウェブアプリ）化されており、ブラウザでオフラインキャッシュやホーム画面追加が試せます。学習・デモ用を想定しています。
参考サイトをベースに学習目的で作成しましたが、Docker / Gradle / PWA 化などの環境構築や設定は自分で検討し、自作しています。

## 学習・検証目的
- Kotlin + Spring Boot を使ったサーバーサイド
- Thymeleaf を使ったテンプレートレンダリング（サーバーサイド描画）
- PostgreSQL をデータ永続化に使用（Docker Compose で簡単に立ち上げ可能）
- PWA 化（`manifest.json`, `service-worker.js` を配置）

---

## 主な機能
- Todo の一覧表示（読み取り）
- Todo の追加（作成）
- Todo の編集（更新）
- Todo の完了 / 未完了切り替え（更新）
- Todo の削除（削除）
- 初回起動時に簡単なサンプルデータを自動投入
- PWA の登録（Service Worker によるキャッシュ登録）

## 使用技術
| カテゴリ | 使用技術 |
| :--- | :--- |
| **language** | Java21 |
| **Framework** | Spring Boot |
| **Infrastructure** | Docker Compose ,Maven |
| **OS Environment** | WSL2 (Ubuntu / Alpine Linux) |
| **Database** | MySQL |



## セットアップ手順

### 1. SpringBootの設定
[Spring Initializr](https://start.spring.io/)
下記の選択肢で設定
```
Project: Maven
Language: Java
Spring Boot: 4.1.1
Project Metadata、Group、Artifact、Package nameは任意の内容
Packaging:Jar
Configuration: Properties
Java: 21
Dependencies:
Spring Web Web
Spring Data JPA SQL
MySQL Driver SQL
```

### 2. インフラのビルドと起動
```
docker-compose up --build
```

### 3. Spring Bootだけ再起動
```
docker compose restart app
```

## ディレクトリ構成（主要ファイル）
ルートから見た主なファイル/フォルダの説明です。

- `build.gradle.kts`, `gradlew`, `gradlew.bat` - ビルド設定と Gradle ラッパー
- `docker-compose.yml`, `Dockerfile` - Docker / Compose の設定（開発用）
- `src/main/kotlin/com/example/todo/` - Kotlin のソースコード
  - `TodoApplication.kt` - Spring Boot のエントリポイント
  - `controller/` - HTTP リクエストとビューをつなぐコントローラー (`TodoController.kt`)
  - `service/` - ビジネスロジック（`TodoService.kt`）
  - `repository/` - データアクセス（Spring Data JPA の `TodoRepository.kt`）
  - `entity/` - JPA エンティティ（`Todo.kt`）
  - `initializer/` - アプリ起動時の初期データ投入（`TodoInitializer.kt`）
- `src/main/resources/` - リソース
  - `application.yaml` - アプリケーションの設定（ポート、DB など）
  - `static/` - 静的ファイル（`manifest.json`, `service-worker.js`, CSS など）
  - `templates/todo/index.html` - Thymeleaf の HTML テンプレート（UI）

具体的なファイル例（このリポジトリに含まれる代表例）:
- `src/main/resources/static/manifest.json` - PWA のマニフェスト
- `src/main/resources/static/service-worker.js` - Service Worker（簡易キャッシュ）

## 設計・実装の特徴
- レイヤード構成: コントローラー（HTTP） → サービス（ビジネス） → リポジトリ（DB）というシンプルな分離を採用しています。初心者が役割を理解しやすい構造です。
- Entity（`Todo.kt`）は JPA アノテーションでテーブルマッピングされています。
- `TodoInitializer` により、DB にデータがない場合はサンプルデータを自動で投入します。学習やデバッグが楽になります。
- テンプレートエンジン（Thymeleaf）でサーバーサイドにレンダリングした HTML を返します。JavaScript と組み合わせて PWA のサービスワーカーを登録しています。
- `application.yaml` で DB の接続や Thymeleaf のキャッシュ無効化（開発時にテンプレート変更を即反映）などの設定を行っています。
- Docker Compose を使うことで、アプリ本体と PostgreSQL、pgAdmin を一括で立ち上げられるため、環境構築が簡単です（学習に便利）。

## 次のステップ（提案）
- フロントエンドを少し強化して API 化（REST）し、フロントを SPA（React/Vue/Angular）に差し替えてみる
- テスト（ユニット/統合）を追加して CI を組む
- Service Worker を改良してより高度なキャッシュ戦略を試す

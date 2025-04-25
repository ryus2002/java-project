# 電商系統使用說明

## 系統簡介

這是一個基於 Spring Boot 的電商系統，實現了會員管理、商品管理、購物車管理、訂單管理和支付管理等功能。系統使用 MySQL 作為數據庫，並支持使用 Docker 進行部署。

## 技術架構

- JDK 21
- Spring Boot 3.2.0
- Spring Security + JWT 身份驗證
- Spring Data JPA
- MySQL 8.0
- Thymeleaf 前端模板引擎
- Swagger/OpenAPI 文檔
- Docker 容器化部署

## 系統功能

1. **會員管理**
   - 會員註冊和登入
   - JWT 身份驗證
   - 會員資料修改
   - 會員公開資料查看

2. **商品管理**
   - 商品分類管理
   - 商品增刪改查
   - 商品搜索功能

3. **購物車管理**
   - 添加商品到購物車
   - 修改購物車商品數量
   - 刪除購物車商品
   - 清空購物車

4. **訂單管理**
   - 創建訂單
   - 訂單狀態管理
   - 訂單歷史查詢

5. **支付管理**
   - 訂單支付處理
   - 支付記錄查詢

## 部署指南

### 使用 Docker 部署

1. **安裝 Docker 和 Docker Compose**

   請確保您的系統已安裝 Docker 和 Docker Compose。如果尚未安裝，請參考以下官方文檔進行安裝：
   - Docker: https://docs.docker.com/get-docker/
   - Docker Compose: https://docs.docker.com/compose/install/

2. **克隆專案**

   ```bash
   git clone https://github.com/yourusername/ecommerce.git
   cd ecommerce
   ```

3. **修改配置**

   在 `docker-compose.yml` 文件中，您可以根據需要修改以下配置：
   - MySQL 數據庫的用戶名和密碼
   - Jasypt 加密密鑰（用於加密數據庫密碼等敏感信息）

4. **構建和啟動容器**

   ```bash
   docker-compose up -d
   ```

   這將會構建應用程式映像並啟動所有服務。首次啟動可能需要幾分鐘時間。

5. **訪問系統**

   系統啟動後，您可以通過以下方式訪問：
   - Web 應用程式：http://localhost:8080
   - API 文檔：http://localhost:8080/swagger-ui.html

### 使用 JAR 檔案部署

1. **安裝 JDK 21 和 MySQL 8.0**

   請確保您的系統已安裝 JDK 21 和 MySQL 8.0。

2. **創建數據庫**

   ```sql
   CREATE DATABASE ecommerce;
   ```

3. **構建專案**

   ```bash
   ./mvnw clean package -DskipTests
   ```

4. **運行應用程式**

   ```bash
   java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
   ```

   您可以通過設置環境變數來自定義配置，例如：

   ```bash
   java -jar target/ecommerce-0.0.1-SNAPSHOT.jar \
     --spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce \
     --spring.datasource.username=root \
     --spring.datasource.password=your_password \
     --jasypt.encryptor.password=your_jasypt_password
   ```

## 系統使用

### 管理員帳號

系統初始化時會自動創建一個管理員帳號：
- 用戶名：admin
- 密碼：admin123

請在首次登入後修改密碼。

### API 使用

系統提供了完整的 RESTful API，您可以通過 Swagger 文檔查看和測試這些 API：
http://localhost:8080/swagger-ui.html

主要 API 端點包括：
- `/api/auth/*` - 身份驗證相關 API
- `/api/users/*` - 用戶管理相關 API
- `/api/categories/*` - 商品分類相關 API
- `/api/products/*` - 商品管理相關 API
- `/api/cart/*` - 購物車相關 API
- `/api/orders/*` - 訂單相關 API
- `/api/payments/*` - 支付相關 API

## 數據庫結構

系統使用以下主要數據表：
- `users` - 存儲用戶資料
- `roles` - 存儲角色資料
- `user_roles` - 用戶和角色的關聯表
- `categories` - 存儲商品分類
- `products` - 存儲商品資料
- `carts` - 存儲購物車資料
- `cart_items` - 存儲購物車項目
- `orders` - 存儲訂單資料
- `order_items` - 存儲訂單項目
- `payments` - 存儲支付記錄

完整的數據庫結構可以通過 JPA 自動生成，或者參考 `src/main/resources/schema.sql`（如果有的話）。

## 開發指南

如果您想要進一步開發或修改系統，請參考以下步驟：

1. **克隆專案**

   ```bash
   git clone https://github.com/yourusername/ecommerce.git
   cd ecommerce
   ```

2. **導入到 IDE**

   您可以使用 IntelliJ IDEA、Eclipse 或 VS Code 等 IDE 導入專案。

3. **運行開發環境**

   您可以使用 Docker Compose 啟動只包含 MySQL 的開發環境：

   ```bash
   docker-compose up -d mysql
   ```

   然後在 IDE 中運行應用程式。

4. **修改代碼**

   根據需要修改代碼，主要的代碼結構如下：
   - `src/main/java/com/example/ecommerce/model` - 實體類
   - `src/main/java/com/example/ecommerce/repository` - 數據訪問層
   - `src/main/java/com/example/ecommerce/controller` - 控制器層
   - `src/main/java/com/example/ecommerce/security` - 安全相關代碼
   - `src/main/java/com/example/ecommerce/config` - 配置類
   - `src/main/resources` - 配置文件和靜態資源

5. **運行測試**

   ```bash
   ./mvnw test
   ```

6. **構建專案**

   ```bash
   ./mvnw clean package
   ```

## 故障排除

如果您在使用系統時遇到問題，請嘗試以下步驟：

1. **檢查日誌**

   查看應用程式日誌以獲取錯誤信息：

   ```bash
   docker-compose logs app
   ```

2. **檢查數據庫連接**

   確保應用程式可以連接到 MySQL 數據庫：

   ```bash
   docker-compose logs mysql
   ```

3. **重新啟動服務**

   如果服務無法正常工作，嘗試重新啟動：

   ```bash
   docker-compose restart
   ```

4. **重新構建應用程式**

   如果您修改了代碼，需要重新構建應用程式：

   ```bash
   docker-compose build app
   docker-compose up -d
   ```

## 聯繫支持

如果您有任何問題或建議，請聯繫我們：support@example.com
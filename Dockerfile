# 使用 JDK 21 作為基礎映像
FROM eclipse-temurin:21-jdk-alpine

# 設置工作目錄
WORKDIR /app

# 複製 Maven 配置文件
COPY pom.xml .

# 複製源代碼
COPY src ./src

# 安裝 Maven 並構建應用程式
RUN apk add --no-cache maven && \
    mvn package -DskipTests && \
    mv target/*.jar app.jar && \
    rm -rf target && \
    apk del maven

# 暴露應用程式端口
EXPOSE 8080

# 設置 Jasypt 加密密鑰環境變數
ENV JASYPT_ENCRYPTOR_PASSWORD=your_jasypt_password

# 啟動應用程式
ENTRYPOINT ["java", "-jar", "app.jar"]
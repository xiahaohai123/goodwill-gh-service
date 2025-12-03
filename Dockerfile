# === 只构建运行镜像 ===
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app

# 拷贝本地构建好的 JAR 包
COPY build/libs/GoodwillGhService.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar", "--spring.profiles.active=pro"]

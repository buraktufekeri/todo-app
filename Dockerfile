# 1. Base image olarak OpenJDK 17
FROM openjdk:17-jdk-slim

# 2. Uygulama dizini olarak /app belirleniyor
WORKDIR /app

# 3. Tüm proje dosyaları container'a kopyalanıyor
COPY . .

# 4. Maven ile JAR dosyası build ediliyor (testler atlanıyor)
RUN ./mvnw clean package -DskipTests

# 5. Uygulama portu dışarıya açılıyor
EXPOSE 8080

# 6. Spring Boot JAR dosyası çalıştırılıyor
CMD ["java", "-jar", "target/todo-app-0.0.1-SNAPSHOT.jar"]
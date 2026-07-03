FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/atletry-1.0.0.jar app.jar

# Firebase service account key — replace src/main/resources/firebase-service-account.json
# with the real file from Firebase Console before building the image, or mount a volume:
#   docker run -v /host/path/service-account.json:/app/firebase-service-account.json ...
COPY --from=build /app/src/main/resources/firebase-service-account.json firebase-service-account.json

EXPOSE 8080

# Override at runtime: -e FIREBASE_ENABLED=true -e FIREBASE_SERVICE_ACCOUNT_PATH=/app/firebase-service-account.json
ENV FIREBASE_SERVICE_ACCOUNT_PATH=/app/firebase-service-account.json

ENTRYPOINT ["java", "-jar", "app.jar"]

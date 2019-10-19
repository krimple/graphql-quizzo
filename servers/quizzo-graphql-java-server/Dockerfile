# see https://medium.com/@sairamkrish/docker-for-spring-boot-gradle-java-micro-service-done-the-right-way-2f46231dbc06

FROM openjdk:11.0-jdk-slim AS BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
COPY . .
RUN ./gradlew build --no-daemon || return 0
ENTRYPOINT ["./gradlew", "bootRun", "--no-daemon", "-Dspring.config.location=/etc/quizzo/spring/application.properties,classpath:application.properties"]


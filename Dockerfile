FROM openjdk:13-jdk-alpine
RUN apk add maven

COPY . /ProxyHTTPS

WORKDIR ProxyHTTPS
RUN mvn package -DskipTests -f /ProxyHTTPS

ENTRYPOINT ["java", "-jar", "target/ProxyHTTPS-0.0.1.jar"]

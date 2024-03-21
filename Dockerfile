FROM openjdk:17-alpine AS builder
RUN java --version
RUN mkdir /application && cd /application && ls -alh
WORKDIR /application
ADD . .
RUN ./gradlew clean build --no-daemon
RUN ls -al build/libs/

FROM alpine:latest
RUN apk add --no-cache wget tar && mkdir /application
WORKDIR /application
RUN wget https://cdn.azul.com/zulu/bin/zulu17.48.15-ca-jre17.0.10-linux_musl_x64.tar.gz && tar -xvf *.tar.gz
COPY --from=builder /application/build/libs/app.jar .
CMD ["/application/zulu17.48.15-ca-jre17.0.10-linux_musl_x64/bin/java", "-jar", "/application/app.jar"]

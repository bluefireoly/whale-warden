FROM openjdk:11-slim as builder

WORKDIR /gradlebuild
COPY . .

RUN chmod -R g+w ./gradlew
RUN ./gradlew clean installShadowDist


FROM openjdk:11-jre-slim

COPY --from=builder /gradlebuild/build/install/whale-warden-shadow/ .

CMD ["./bin/whale-warden"]

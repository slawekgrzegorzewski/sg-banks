FROM azul/zulu-openjdk-debian:21

RUN mkdir -p /app/logs && apt-get update

COPY currency.properties /usr/lib/jvm/zulu21/lib
COPY sg-banks-app.jar /app/banks-app.jar

CMD ["java", "-jar", "/app/banks-app.jar", "--spring.config.location=file:///app/application.yml", "--logging.config=/app/logback.xml"]
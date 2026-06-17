FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY out/production/sistema-bancario/ /app/bin/
COPY lib/ /app/lib/

ENTRYPOINT ["java", "-cp", "/app/bin/:/app/lib/*", "br.sistema.bancario.App"]
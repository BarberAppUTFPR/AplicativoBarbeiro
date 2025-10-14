FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia o pom primeiro para aproveitar cache de dependências
COPY pom.xml ./

# Baixa dependências em cache (BuildKit habilitado para cache do .m2)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -DskipTests dependency:go-offline

# Copia o código e compila
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -T 1C -DskipTests package

########## STAGE 2: runtime (JRE 17 em Alpine)
FROM eclipse-temurin:17-jre-alpine AS runtime

# Usuário não-root
RUN addgroup -S app && adduser -S app -G app
USER app

WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+HeapDumpOnOutOfMemoryError"

# Copia o jar gerado do stage de build
# se o nome do jar variar, use o wildcard:
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8088
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]

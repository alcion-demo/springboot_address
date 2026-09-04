FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN apt-get update \
    && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

RUN chmod +x mvnw
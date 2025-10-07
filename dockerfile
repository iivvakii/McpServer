FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/McpServer-0.0.1-SNAPSHOT.jar /app/mcp-server.jar

EXPOSE 8080

CMD ["java", "-jar", "mcp-server.jar"]
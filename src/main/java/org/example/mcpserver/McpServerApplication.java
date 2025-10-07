package org.example.mcpserver;

import org.example.mcpserver.tools.AuthorTools;
import org.example.mcpserver.tools.BookTools;
import org.example.mcpserver.tools.GraphQLTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider getToolCallbackProvider(
                                                        GraphQLTools graphQLTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(graphQLTools)
                .build();
    }
}

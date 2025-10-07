package org.example.mcpserver.tools;

import lombok.RequiredArgsConstructor;
import org.example.mcpserver.dto.authorDtos.AuthorIdsRequestDto;
import org.example.mcpserver.dto.authorDtos.CreateAuthorRequestDto;
import org.example.mcpserver.dto.authorDtos.UpdateAuthorRequestDto;
import org.example.mcpserver.dto.bookDtos.BooksIdsRequestDto;
import org.example.mcpserver.dto.bookDtos.CreateBookRequestDto;
import org.example.mcpserver.dto.bookDtos.UpdateBookRequestDto;
import org.example.mcpserver.model.Author;
import org.example.mcpserver.model.Book;
import org.example.mcpserver.service.AuthorService;
import org.example.mcpserver.service.BookService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GraphQLTools {
    private final BookService bookService;
    private final AuthorService authorService;


    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/graphql")
            .defaultHeader("Content-Type", "application/json")
            .build();

    @Tool(description = "Fetch book by ID with selected fields using GraphQL internally")
    public Map<String, Object> getBookFields(@ToolParam(description = "Book ID") Long bookId,
                                             @ToolParam(description = "List of fields to include, e.g. ['title', 'authors{name}']") List<String> fields) {
        if (bookId == null || fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Both bookId and fields[] are required");
        }
        String fieldList = String.join(" ", fields);
        String query = String.format("{ getBookById(id:%d) { %s } }", bookId, fieldList);
        return executeGraphQLQuery(query, null);
    }

    @Tool(description = "Fetch all books with selected fields using GraphQL internally")
    public Map<String, Object> getAllBooksFields(@ToolParam List<String> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Fields[] cannot be empty");
        }
        String fieldList = String.join(" ", fields);
        String query = String.format("{ getAllBooks { %s } }", fieldList);
        return executeGraphQLQuery(query, null);
    }

    @Tool(description = "Execute GraphQL query with optional variables and return the result as JSON")
    public Map<String, Object> executeGraphQLQuery(
            @ToolParam(description = "GraphQL query string, e.g. '{ getAllBooks { id title } }'") String query,
            @ToolParam(description = "Optional variables for the GraphQL query") Map<String, Object> variables) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("GraphQL query must not be empty");
        }
        Map<String, Object> requestBody = Map.of("query", query, "variables", variables != null ? variables : Map.of());
        Map<String, Object> response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorResume(e -> Mono.just(Map.of("error", e.getMessage()))).block();
         if (response != null && response.containsKey("errors")) {
             throw new RuntimeException("GraphQL errors: " + response.get("errors"));
         }
         return Map.of( "tool", "executeGraphQLQuery", "query", query, "result", response != null ? response.get("data") : null );
    }

    @Tool(description = "Fetch all authors with selected fields using GraphQL internally")
    public Map<String, Object> getAllAuthorsFields(@ToolParam List<String> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Fields[] cannot be empty");
        }
        String fieldList = String.join(" ", fields);
        String query = String.format("{ getAllAuthors { %s } }", fieldList);
        return executeGraphQLQuery(query, null);
    }

    @Tool(description = "Fetch an author by ID with selected fields using GraphQL internally")
    public Map<String, Object> getAuthorByIdFields(@ToolParam Long authorId,
                                                   @ToolParam List<String> fields) {
        if (authorId == null || fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("authorId and fields[] are required");
        }
        String fieldList = String.join(" ", fields);
        String query = String.format("{ getAuthorById(id:%d) { %s } }", authorId, fieldList);
        return executeGraphQLQuery(query, null);
    }



    @Tool(description = "Create a new book with a title. Optionally associate the book with a list of existing author IDs.")
    public Book createBook(
            @ToolParam(description = "Title of the book to be created") String title,
            @ToolParam(description = "Optional list of author IDs to associate with this book") List<Long> authorIds
    ) {
        return bookService.createBook(new CreateBookRequestDto(authorIds, title));
    }

    @Tool(description = "Add existing authors to a book by specifying the book ID and a list of author IDs.")
    public Book addAuthorsToBook(
            @ToolParam(description = "ID of the book to which authors will be added") Long bookId,
            @ToolParam(description = "List of author IDs to associate with the book") List<Long> authorIds
    ) {
        return bookService.addAuthorsToBook(bookId, new AuthorIdsRequestDto(authorIds));
    }

    @Tool(description = "Update the title of an existing book by its ID.")
    public Book updateBook(
            @ToolParam(description = "ID of the book to update") Long bookId,
            @ToolParam(description = "New title for the book") String newTitle
    ) {
        return bookService.updateBook(bookId, new UpdateBookRequestDto(newTitle));
    }

    @Tool(description = "Delete a book by its ID. The book will be removed from the database.")
    public void deleteBook(
            @ToolParam(description = "ID of the book to delete") Long bookId
    ) {
        bookService.deleteBook(bookId);
    }

    @Tool(description = "Create a new author with a given name. Optionally associate the author with a list of existing book IDs.")
    public Author createAuthor(
            @ToolParam(description = "Full name of the author to be created") String name,
            @ToolParam(description = "Optional list of book IDs to associate with this author") List<Long> bookIds
    ) {
        return authorService.createAuthor(new CreateAuthorRequestDto(bookIds, name));
    }

    @Tool(description = "Update the name of an existing author by their ID.")
    public Author updateAuthor(
            @ToolParam(description = "ID of the author to update") Long authorId,
            @ToolParam(description = "New full name for the author") String newName
    ) {
        return authorService.updateAuthor(authorId, new UpdateAuthorRequestDto(newName));
    }

    @Tool(description = "Add existing books to an author by specifying the author ID and a list of book IDs.")
    public Author addBooksToAuthor(
            @ToolParam(description = "ID of the author to which books will be added") Long authorId,
            @ToolParam(description = "List of book IDs to associate with the author") List<Long> bookIds
    ) {
        return authorService.addBooksToAuthor(authorId, new BooksIdsRequestDto(bookIds));
    }

    @Tool(description = "Delete an author by their ID. Returns nothing, but the author will be removed from the database.")
    public void deleteAuthor(
            @ToolParam(description = "ID of the author to delete") Long authorId
    ) {
        authorService.deleteAuthor(authorId);
    }
}

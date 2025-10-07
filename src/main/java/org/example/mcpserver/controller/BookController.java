package org.example.mcpserver.controller;

import lombok.RequiredArgsConstructor;
import org.example.mcpserver.dto.authorDtos.AuthorIdsRequestDto;
import org.example.mcpserver.dto.bookDtos.CreateBookRequestDto;
import org.example.mcpserver.dto.bookDtos.UpdateBookRequestDto;
import org.example.mcpserver.model.Book;
import org.example.mcpserver.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @QueryMapping
    public Book getBookById(@Argument Long id) {
        return bookService.getBookById(id);
    }

    @QueryMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @MutationMapping
    public Book createBook(@Argument CreateBookRequestDto request) {
        return bookService.createBook(request);
    }

    @MutationMapping
    public Book addAuthorsToBook(@Argument Long id, @Argument AuthorIdsRequestDto request) {
        return bookService.addAuthorsToBook(id, request);
    }

    @MutationMapping
    public Book updateBook(@Argument Long id, @Argument UpdateBookRequestDto request) {
        return bookService.updateBook(id, request);
    }

    @MutationMapping
    public Long deleteBook(@Argument Long id) {
        bookService.deleteBook(id);
        return id;
    }
}

package org.example.mcpserver.controller;

import lombok.RequiredArgsConstructor;
import org.example.mcpserver.dto.authorDtos.CreateAuthorRequestDto;
import org.example.mcpserver.dto.authorDtos.UpdateAuthorRequestDto;
import org.example.mcpserver.dto.bookDtos.BooksIdsRequestDto;
import org.example.mcpserver.model.Author;
import org.example.mcpserver.service.AuthorService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @QueryMapping
    public Author getAuthorById(@Argument Long id) {
        return authorService.findById(id);
    }

    @QueryMapping
    public List<Author> getAllAuthors() {
        return authorService.findAll();
    }

    @MutationMapping
    public Author createAuthor(@Argument CreateAuthorRequestDto request) {
        return authorService.createAuthor(request);
    }

    @MutationMapping
    public Author updateAuthor(@Argument Long id, @Argument UpdateAuthorRequestDto  request) {
        return authorService.updateAuthor(id, request);
    }

    @MutationMapping
    public Author addBooksToAuthor(@Argument Long id, @Argument BooksIdsRequestDto request) {
        return authorService.addBooksToAuthor(id, request);
    }

    @MutationMapping
    public Long deleteAuthor(@Argument Long id) {
        authorService.deleteAuthor(id);
        return id;
    }
}

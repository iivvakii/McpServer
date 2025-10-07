package org.example.mcpserver.service;

import org.example.mcpserver.dto.authorDtos.CreateAuthorRequestDto;
import org.example.mcpserver.dto.authorDtos.UpdateAuthorRequestDto;
import org.example.mcpserver.dto.bookDtos.BooksIdsRequestDto;
import org.example.mcpserver.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAllByIds(List<Long> authorIds);

    Author createAuthor(CreateAuthorRequestDto request);

    Author findById(Long id);

    List<Author> findAll();

    Author updateAuthor(Long id, UpdateAuthorRequestDto request);

    Author addBooksToAuthor(Long id, BooksIdsRequestDto request);

    void deleteAuthor(Long id);
}

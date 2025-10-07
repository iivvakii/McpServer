package org.example.mcpserver.service;

import org.example.mcpserver.dto.authorDtos.AuthorIdsRequestDto;
import org.example.mcpserver.dto.bookDtos.CreateBookRequestDto;
import org.example.mcpserver.dto.bookDtos.UpdateBookRequestDto;
import org.example.mcpserver.model.Book;

import java.util.List;

public interface BookService {

    Book getBookById(Long id);

    Book createBook(CreateBookRequestDto bookRequestDto);

    List<Book> getAllBooks();

    Book addAuthorsToBook(Long id, AuthorIdsRequestDto request);

    Book updateBook(Long id, UpdateBookRequestDto bookRequestDto);

    void deleteBook(Long id);

    List<Book> findAllByIds(List<Long> ids);
}

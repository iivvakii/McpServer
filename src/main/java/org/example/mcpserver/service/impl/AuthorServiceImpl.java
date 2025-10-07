package org.example.mcpserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mcpserver.dto.authorDtos.CreateAuthorRequestDto;
import org.example.mcpserver.dto.authorDtos.UpdateAuthorRequestDto;
import org.example.mcpserver.dto.bookDtos.BooksIdsRequestDto;
import org.example.mcpserver.exception.AuthorNotFoundException;
import org.example.mcpserver.model.Author;
import org.example.mcpserver.model.Book;
import org.example.mcpserver.repository.AuthorRepository;
import org.example.mcpserver.service.AuthorService;
import org.example.mcpserver.service.BookService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service

public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookService bookService;

    public AuthorServiceImpl(AuthorRepository authorRepository,
                             @Lazy BookService bookService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
    }

    @Override
    public List<Author> findAllByIds(List<Long> authorIds) {
        return authorRepository.findAllById(authorIds);
    }

    @Override
    public Author createAuthor(CreateAuthorRequestDto request) {
        Author author = new Author();
        author.setName(request.getName());
        List<Book> books = bookService.findAllByIds(request.getBookIds());
        addAuthorToBooks(books, author);
        author.getBooks().addAll(books);
        return authorRepository.save(author);
    }

    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }



    @Override
    @Transactional
    public Author updateAuthor(Long id, UpdateAuthorRequestDto request) {
        Author author = findById(id);
        author.setName(request.getName());
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public Author addBooksToAuthor(Long id, BooksIdsRequestDto request) {
        Author author = findById(id);
        List<Book> books = bookService.findAllByIds(request.getBookIds());
        author.getBooks().addAll(books);
        addAuthorToBooks(books, author);
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        Author author = findById(id);
        Set<Book> books = author.getBooks();
        books.forEach(book -> book.getAuthors().remove(author));
        author.getBooks().clear();
        authorRepository.delete(author);
    }

    private void addAuthorToBooks(List<Book> books, Author author) {
        books.forEach(book -> book.getAuthors().add(author));
    }


}

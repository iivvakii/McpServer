package org.example.mcpserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.mcpserver.dto.authorDtos.AuthorIdsRequestDto;
import org.example.mcpserver.dto.bookDtos.CreateBookRequestDto;
import org.example.mcpserver.dto.bookDtos.UpdateBookRequestDto;
import org.example.mcpserver.exception.BookNotFoundException;
import org.example.mcpserver.model.Author;
import org.example.mcpserver.model.Book;
import org.example.mcpserver.repository.BookRepository;
import org.example.mcpserver.service.AuthorService;
import org.example.mcpserver.service.BookService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;

    @Override
    @Tool(description = "Get book by id")
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    @Override
    @Transactional
    public Book createBook(CreateBookRequestDto bookRequestDto) {
        List<Author> authors = authorService.findAllByIds(bookRequestDto.getAuthorIds());
        Book book = new Book();
        book.setTitle(bookRequestDto.getTitle());
        book.getAuthors().addAll(authors);
        addBookToAuthors(authors, book);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book addAuthorsToBook(Long id, AuthorIdsRequestDto request) {
        Book book = getBookById(id);
        List<Author> authors = authorService.findAllByIds(request.getAuthorIds());
        if(!authors.isEmpty()){
            book.getAuthors().addAll(authors);
            addBookToAuthors(authors, book);
        }
        return book;
    }

    @Override
    @Transactional
    public Book updateBook(Long id, UpdateBookRequestDto bookRequestDto) {
        Book book = getBookById(id);
        book.setTitle(bookRequestDto.getTitle());
        return book;
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        Set<Author> authors = book.getAuthors();
        authors.forEach(author -> author.getBooks().remove(book));
        book.getAuthors().removeAll(authors);
        bookRepository.delete(book);
    }

    @Override
    public List<Book> findAllByIds(List<Long> ids) {
        return bookRepository.findAllById(ids);
    }


    private void addBookToAuthors(List<Author> authors, Book book) {
        authors.forEach(author -> author.getBooks().add(book));
    }
}

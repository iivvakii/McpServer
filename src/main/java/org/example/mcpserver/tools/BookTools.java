package org.example.mcpserver.tools;

import lombok.RequiredArgsConstructor;
import org.example.mcpserver.dto.authorDtos.AuthorIdsRequestDto;
import org.example.mcpserver.dto.bookDtos.CreateBookRequestDto;
import org.example.mcpserver.dto.bookDtos.UpdateBookRequestDto;
import org.example.mcpserver.model.Book;
import org.example.mcpserver.service.BookService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookTools {
    private final BookService bookService;

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

}

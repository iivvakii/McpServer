package org.example.mcpserver.tools;

import lombok.RequiredArgsConstructor;
import org.example.mcpserver.dto.authorDtos.CreateAuthorRequestDto;
import org.example.mcpserver.dto.authorDtos.UpdateAuthorRequestDto;
import org.example.mcpserver.dto.bookDtos.BooksIdsRequestDto;
import org.example.mcpserver.model.Author;
import org.example.mcpserver.service.AuthorService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorTools {

    private final AuthorService authorService;

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

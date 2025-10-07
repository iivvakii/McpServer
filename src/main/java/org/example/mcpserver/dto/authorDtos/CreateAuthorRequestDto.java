package org.example.mcpserver.dto.authorDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.mcpserver.dto.bookDtos.BooksIdsRequestDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthorRequestDto extends BooksIdsRequestDto {
    private String name;

    public CreateAuthorRequestDto(List<Long> bookIds, String name) {
        super(bookIds);
        this.name = name;
    }
}

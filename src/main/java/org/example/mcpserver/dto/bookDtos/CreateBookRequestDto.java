package org.example.mcpserver.dto.bookDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.mcpserver.dto.authorDtos.AuthorIdsRequestDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequestDto extends AuthorIdsRequestDto {
    private String title;

    public CreateBookRequestDto(List<Long> authorIds, String title) {
        super(authorIds);
        this.title = title;
    }

}

package org.example.mcpserver.dto.bookDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BooksIdsRequestDto {
    private List<Long> bookIds;
}

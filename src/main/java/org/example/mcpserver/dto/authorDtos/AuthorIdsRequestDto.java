package org.example.mcpserver.dto.authorDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorIdsRequestDto {
    private List<Long> authorIds;
}

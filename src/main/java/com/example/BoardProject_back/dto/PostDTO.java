package com.example.BoardProject_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {

    @NotNull
    private String title;

    @NotNull
    private String content;
}

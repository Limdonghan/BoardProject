package com.example.BoardProject_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {

    @NotNull
    private String title;

    @NotNull
    private int category;

    @NotNull
    private String context;

    private List<String> imageUrl;
}

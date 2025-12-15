package com.example.BoardProject_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostUpdateDTO {
    @NotNull
    private String title;

    @NotNull
    private int category;

    @NotNull
    private String context;

    private List<String> imageUrl;

}

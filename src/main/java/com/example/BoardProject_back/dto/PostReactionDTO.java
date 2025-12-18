package com.example.BoardProject_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReactionDTO {
    private String reactionType;  /// LIKE or DISLIKE
}

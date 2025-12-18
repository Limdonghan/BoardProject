package com.example.BoardProject_back.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageDeleteRequest {
    private List<String> imageUrls;

    public ImageDeleteRequest(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

}

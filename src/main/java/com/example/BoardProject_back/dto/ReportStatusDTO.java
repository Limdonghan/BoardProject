package com.example.BoardProject_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportStatusDTO {

    @NotNull
    private String status;

}

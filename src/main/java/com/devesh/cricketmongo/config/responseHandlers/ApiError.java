package com.devesh.cricketmongo.config.responseHandlers;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private String message;

    @Builder.Default
    private List<String> suberrors = new ArrayList<>();
}
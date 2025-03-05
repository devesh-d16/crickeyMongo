package com.devesh.cricketmongo.config.responseHandlers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;


    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
    @JsonPropertyOrder({ "meta", "data", "apiError", "timeStamp" }) // Ensures "meta" appears first
    public class ApiResponse<T> {
        private Meta meta;
        private T data;
        private ApiError errors;

        @JsonFormat(pattern = "hh:mm:ss dd-MM-yyyy")
        private LocalDateTime timestamp;

        public ApiResponse() {
            this.timestamp = LocalDateTime.now();
        }

        public ApiResponse(T data) {
            this();
            this.data = data;
            this.meta = new Meta("success");
        }

        public ApiResponse(ApiError errors) {
            this();
            this.errors = errors;
            this.meta = new Meta("failed");
        }

        @Getter
        @JsonInclude(JsonInclude.Include.NON_NULL) // Hide if null
        public static class Meta {
            private final String status;

            public Meta(String status) {
                this.status = status;
            }
        }
    }




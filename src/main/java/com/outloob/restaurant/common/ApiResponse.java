package com.outloob.restaurant.common;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp
) {
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, Instant.now());
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operation successful", data, Instant.now());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, Instant.now());
    }

    public record PageMetadata(
            int pageNumber,
            int pageSize,
            long totalElements,
            int totalPages,
            boolean isLast
    ) {}

    public record PagedData<E>(
            List<E> content,
            PageMetadata pagination
    ) {
        public static <E> PagedData<E> from(Page<E> page) {
            PageMetadata meta = new PageMetadata(
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages(),
                    page.isLast()
            );
            return new PagedData<>(page.getContent(), meta);
        }
    }
}
